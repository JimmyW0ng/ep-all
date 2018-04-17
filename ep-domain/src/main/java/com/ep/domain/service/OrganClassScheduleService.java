package com.ep.domain.service;

import com.ep.common.tool.CollectionsTools;
import com.ep.common.tool.DateTools;
import com.ep.common.tool.ExcelUtil;
import com.ep.common.tool.StringTools;
import com.ep.domain.constant.BizConstant;
import com.ep.domain.constant.MessageCode;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.*;
import com.ep.domain.pojo.dto.OrganClassCatalogCommentDto;
import com.ep.domain.pojo.dto.OrganClassCatalogDetailDto;
import com.ep.domain.pojo.dto.OrganClassScheduleDto;
import com.ep.domain.pojo.po.*;
import com.ep.domain.repository.*;
import com.ep.domain.repository.domain.enums.EpMemberChildCommentType;
import com.ep.domain.repository.domain.enums.EpMemberMessageType;
import com.ep.domain.repository.domain.enums.EpOrganClassScheduleStatus;
import com.ep.domain.repository.domain.enums.EpOrganClassStatus;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.jooq.Condition;
import org.jooq.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.ep.domain.repository.domain.Tables.EP_MEMBER_CHILD;
import static com.ep.domain.repository.domain.Tables.EP_ORGAN_CLASS_SCHEDULE;

/**
 * @Description: 班次行程服务
 * @Author: CC.F
 * @Date: 16:08 2018/3/28/028
 */
@Slf4j
@Service
public class OrganClassScheduleService {

    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private OrganCourseTagRepository organCourseTagRepository;
    @Autowired
    private OrganClassChildRepository organClassChildRepository;
    @Autowired
    private MemberChildCommentRepository memberChildCommentRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrganClassScheduleRepository organClassScheduleRepository;
    @Autowired
    private MemberMessageRepository memberMessageRepository;
    @Autowired
    private OrganClassRepository organClassRepository;
    @Autowired
    private MemberChildTagRepository memberChildTagRepository;

    public Optional<EpOrganClassSchedulePo> findById(Long id) {
        return organClassScheduleRepository.findById(id);
    }

    /**
     * 商户后台获取分页
     *
     * @param pageable
     * @param conditions
     * @return
     */
    public Page<OrganClassScheduleDto> findbyPageAndCondition(Long courseId, Pageable pageable, Collection<Condition> conditions) {
        Page<OrganClassScheduleDto> page = organClassScheduleRepository.findbyPageAndCondition(pageable, conditions);
        page.getContent().forEach(p -> {
            if (p.getCourseId() == null) {
                p.setCourseId(courseId);
            }
        });
        return page;
    }

    /**
     * 课时明细
     *
     * @param memberId
     * @param orderId
     * @return
     */
    public ResultDo<OrganClassCatalogDetailDto> getCatalogDetail(Long memberId, Long orderId) {
        ResultDo<OrganClassCatalogDetailDto> resultDo = ResultDo.build();
        // 校验订单
        EpOrderPo orderPo = orderRepository.getById(orderId);
        if (orderPo == null || orderPo.getDelFlag() || !orderPo.getMemberId().equals(memberId)) {
            return resultDo.setError(MessageCode.ERROR_ORDER_NOT_EXISTS);
        }
        EpOrganClassPo classPo = organClassRepository.getById(orderPo.getClassId());
        if (classPo == null || classPo.getDelFlag()) {
            return resultDo.setError(MessageCode.ERROR_CLASS_NOT_EXIST);
        }
        // 课时明细
        List<OrganClassCatalogBo> classCatalogs = organClassScheduleRepository.findDetailByOrderId(orderId);
        if (CollectionsTools.isEmpty(classCatalogs)) {
            return resultDo;
        }
        // 标签汇总
        List<MemberChildTagBo> tags = memberChildTagRepository.findTagsByChildIdAndClassId(orderPo.getChildId(), orderPo.getClassId());
        OrganClassCatalogDetailDto detailDto = new OrganClassCatalogDetailDto(tags, classCatalogs);
        return resultDo.setResult(detailDto);
    }

    /**
     * 课时评价初始化
     *
     * @param organAccountPo
     * @param classId
     * @param startTimeStamp
     * @return
     */
    public ResultDo<OrganClassCatalogCommentDto> getClassCatalogCommentView(EpOrganAccountPo organAccountPo, Long classId, Long startTimeStamp) {
        log.info("课时评价初始化, accountId={}, classId={}, startTimeStamp={}", organAccountPo.getId(), classId, startTimeStamp);
        ResultDo<OrganClassCatalogCommentDto> resultDo = ResultDo.build();
        // 校验课程
        EpOrganClassPo classPo = organClassRepository.getById(classId);
        if (classPo == null || classPo.getDelFlag()) {
            log.error("班次不存在, classId={}", classId);
            return resultDo.setError(MessageCode.ERROR_CLASS_NOT_EXIST);
        }
        if (classPo.getStatus().equals(EpOrganClassStatus.save)) {
            log.error("班次未上线, classId={}, status={}", classId, classPo.getStatus().getName());
            return resultDo.setError(MessageCode.ERROR_CLASS_NOT_START);
        }
        // 校验班次负责人
        if (!organAccountPo.getId().equals(classPo.getOgnAccountId())) {
            log.error("当前机构账户不是班次负责人, accountId={}, classId={}", organAccountPo.getId(), classPo.getId());
            return resultDo.setError(MessageCode.ERROR_ORGAN_ACCOUNT_NOT_MATCH_CLASS);
        }
        // 班次行程信息
        Timestamp startTime = new Timestamp(startTimeStamp);
        List<OrganClassCatalogCommentBo> schedulePoList = organClassScheduleRepository.findbyClassIdAndStartTime(classId, startTime);
        if (CollectionsTools.isEmpty(schedulePoList)) {
            log.error("班次行程信息不存在, classId={}, startTimeStamp={}", organAccountPo.getId(), classId, startTimeStamp);
            return resultDo.setError(MessageCode.ERROR_CLASS_SCHEDULE_NOT_EXIST);
        }
        for (OrganClassCatalogCommentBo bo : schedulePoList) {
            // 头像
            Optional<EpFilePo> optional = fileRepository.getOneByBizTypeAndSourceId(BizConstant.FILE_BIZ_TYPE_CODE_CHILD_AVATAR, bo.getChildId());
            String avatar = optional.isPresent() ? optional.get().getFileUrl() : null;
            bo.setAvatar(avatar);
            // 加载标签
            List<MemberChildTagBo> tags = memberChildTagRepository.findDetailByChildIdAndClassScheduleId(bo.getChildId(), bo.getClassScheduleId());
            bo.setTags(tags);
        }
        // 课程标签
        List<OrganCourseTagBo> courseTagList = organCourseTagRepository.findBosByCourseId(classPo.getCourseId());
        OrganClassCatalogCommentDto commentDto = new OrganClassCatalogCommentDto(schedulePoList, courseTagList);
        return resultDo.setResult(commentDto);
    }

    /**
     * 课时评价
     *
     * @param organAccountPo
     * @param classScheduleId
     * @param tagIds
     * @param comment
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResultDo doClassCatalogComment(EpOrganAccountPo organAccountPo, Long classScheduleId, List<Long> tagIds, String comment) {
        log.info("新增随堂评价开始, accountId={}, classScheduleId={}, tagIds={}, comment={}", organAccountPo.getId(), classScheduleId, tagIds, comment);
        if (CollectionsTools.isEmpty(tagIds) && StringTools.isBlank(comment)) {
            return ResultDo.build(MessageCode.ERROR_SYSTEM_PARAM_FORMAT);
        }
        // 行程信息
        EpOrganClassSchedulePo schedulePo = organClassScheduleRepository.getById(classScheduleId);
        if (schedulePo == null
                || schedulePo.getDelFlag()
                || schedulePo.getStatus().equals(EpOrganClassScheduleStatus.absent)
                || schedulePo.getStatus().equals(EpOrganClassScheduleStatus.leave)
                || schedulePo.getStatus().equals(EpOrganClassScheduleStatus.close)) {
            log.error("班次行程信息不存在, classScheduleId={}", classScheduleId);
            return ResultDo.build(MessageCode.ERROR_CLASS_SCHEDULE_NOT_EXIST);
        }
        // 不允许在课时未开始的
        if (DateTools.getCurrentDateTime().before(schedulePo.getStartTime())) {
            log.error("班次行程未开始, classScheduleId={}", classScheduleId);
            return ResultDo.build(MessageCode.ERROR_CLASS_SCHEDULE_NOT_START);
        }
        if (schedulePo.getEvaluateFlag()) {
            log.error("随堂信息不存在, classScheduleId={}", classScheduleId);
            return ResultDo.build(MessageCode.ERROR_CLASS_CATALOG_COMMENT_IS_EXIST);
        }
        // 校验课程
        EpOrganClassPo classPo = organClassRepository.getById(schedulePo.getClassId());
        if (classPo == null || classPo.getDelFlag()) {
            log.error("班次不存在, classId={}", schedulePo.getClassId());
            return ResultDo.build(MessageCode.ERROR_CLASS_NOT_EXIST);
        }
        if (classPo.getStatus().equals(EpOrganClassStatus.save)) {
            log.error("班次未上线, classId={}, status={}", schedulePo.getClassId(), classPo.getStatus().getName());
            return ResultDo.build(MessageCode.ERROR_CLASS_NOT_START);
        }
        // 校验班次负责人
        if (!organAccountPo.getId().equals(classPo.getOgnAccountId())) {
            log.error("当前机构账户不是班次负责人, accountId={}, classId={}", organAccountPo.getId(), classPo.getId());
            return ResultDo.build(MessageCode.ERROR_ORGAN_ACCOUNT_NOT_MATCH_CLASS);
        }
        int num = organClassScheduleRepository.evaluate(classScheduleId);
        if (num == BizConstant.DB_NUM_ZERO) {
            return ResultDo.build(MessageCode.ERROR_OPERATE_FAIL);
        }
        Set<Long> tagSet;
        if (CollectionsTools.isNotEmpty(tagIds)) {
            tagSet = Sets.newHashSet(tagIds);
            // 检测标签是否都是课程设置里的
            boolean existOtherTag = organCourseTagRepository.existOtherTag(classPo.getCourseId(), tagSet);
            if (existOtherTag) {
                log.error("存在课程设置外的标签");
                return ResultDo.build(MessageCode.ERROR_CLASS_CATALOG_COMMENT_OTHER_TAG_EXIST);
            }
            // 插入标签
            for (Long tagId : tagSet) {
                EpMemberChildTagPo tagPo = new EpMemberChildTagPo();
                tagPo.setChildId(schedulePo.getChildId());
                tagPo.setOgnId(classPo.getOgnId());
                tagPo.setCourseId(classPo.getCourseId());
                tagPo.setClassId(classPo.getId());
                tagPo.setClassScheduleId(classScheduleId);
                tagPo.setTagId(tagId);
                memberChildTagRepository.insert(tagPo);
            }
        }
        // 插入评论内容
        if (StringTools.isNotBlank(comment)) {
            EpMemberChildCommentPo commentPo = new EpMemberChildCommentPo();
            commentPo.setChildId(schedulePo.getChildId());
            commentPo.setOgnId(classPo.getOgnId());
            commentPo.setCourseId(classPo.getCourseId());
            commentPo.setClassId(classPo.getId());
            commentPo.setClassScheduleId(classScheduleId);
            commentPo.setType(EpMemberChildCommentType.launch);
            commentPo.setContent(comment);
            commentPo.setOgnAccountId(organAccountPo.getId());
            memberChildCommentRepository.insert(commentPo);
        }
        // 孩子班次评价数+1
        organClassChildRepository.addScheduleCommentNum(schedulePo.getOrderId());
        return ResultDo.build();
    }

    /**
     * 撤销随堂评价
     *
     * @param organAccountId
     * @param classScheduleId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResultDo cancelClassCatalogComment(Long organAccountId, Long classScheduleId) {
        log.info("撤销随堂评价开始, accountId={}, classScheduleId={}", organAccountId, classScheduleId);
        // 行程信息
        EpOrganClassSchedulePo schedulePo = organClassScheduleRepository.getById(classScheduleId);
        if (schedulePo == null || schedulePo.getDelFlag()) {
            log.error("班次行程信息不存在, classScheduleId={}", classScheduleId);
            return ResultDo.build(MessageCode.ERROR_CLASS_SCHEDULE_NOT_EXIST);
        }
        if (!schedulePo.getEvaluateFlag()) {
            log.error("随堂信息不存在, classScheduleId={}", classScheduleId);
            return ResultDo.build(MessageCode.ERROR_CLASS_CATALOG_COMMENT_NOT_EXIST);
        }
        // 校验班次负责人
        EpOrganClassPo classPo = organClassRepository.getById(schedulePo.getClassId());
        if (classPo == null || classPo.getDelFlag()) {
            log.error("班次不存在, classId={}", schedulePo.getClassId());
            return ResultDo.build(MessageCode.ERROR_CLASS_NOT_EXIST);
        }
        if (!organAccountId.equals(classPo.getOgnAccountId())) {
            log.error("当前机构账户不是班次负责人, accountId={}, classId={}", organAccountId, classPo.getId());
            return ResultDo.build(MessageCode.ERROR_ORGAN_ACCOUNT_NOT_MATCH_CLASS);
        }
        int num = organClassScheduleRepository.cancelEvaluate(classScheduleId);
        if (num == BizConstant.DB_NUM_ZERO) {
            return ResultDo.build(MessageCode.ERROR_OPERATE_FAIL);
        }
        // 物理删除评论
        memberChildCommentRepository.physicalDeleteByScheduleId(classScheduleId);
        // 物理删除标签
        memberChildTagRepository.physicalDeleteByScheduleId(classScheduleId);
        // 孩子班次评价数-1
        organClassChildRepository.subtractScheduleCommentNum(schedulePo.getOrderId());
        // 删除消息
        memberMessageRepository.physicalDeleteBySourceIdAndType(classScheduleId, EpMemberMessageType.class_schedule_comment);
        return ResultDo.build();
    }

    /**
     * 创建行程
     *
     * @param po
     * @return
     */
    public ResultDo createSchedule(EpOrganClassSchedulePo po) {
        EpOrganClassSchedulePo insertClassSchedulePo = new EpOrganClassSchedulePo();
        insertClassSchedulePo.setChildId(po.getChildId());
        insertClassSchedulePo.setClassId(po.getClassId());
        insertClassSchedulePo.setOrderId(po.getOrderId());
        insertClassSchedulePo.setStartTime(po.getStartTime());
        insertClassSchedulePo.setDuration(po.getDuration());
        insertClassSchedulePo.setCatalogTitle(po.getCatalogTitle());
        insertClassSchedulePo.setCatalogDesc(po.getCatalogDesc());
        insertClassSchedulePo.setCatalogIndex(po.getCatalogIndex());
        log.info("[预约行程]新增预约行程开始，insertClassSchedulePo={}。", insertClassSchedulePo);
        if (!checkPoParams(insertClassSchedulePo)) {
            log.error("[预约行程]新增预约行程失败，请求参数异常。");
            return ResultDo.build(MessageCode.ERROR_SYSTEM_PARAM_FORMAT);
        }
        boolean flag = (DateTools.getTwoTimeDiffSecond(insertClassSchedulePo.getStartTime(), DateTools.getCurrentDateTime()) / BizConstant.TIME_UNIT) >=
                BizConstant.RECTIFY_SCHEDULE_STARTTIME_TONOW_LT30;
        if (!flag) {
            log.error("[预约行程]新增预约行程失败，预约行程开始时间距离当前时间不得小于30分钟。");
            return ResultDo.build(MessageCode.RECTIFY_SCHEDULE_STARTTIME_TONOW_LT30);
        }
        insertClassSchedulePo.setStatus(EpOrganClassScheduleStatus.normal);
        organClassScheduleRepository.insert(insertClassSchedulePo);
        log.info("[预约行程]新增预约行程成功，id={}。", insertClassSchedulePo.getId());
        return ResultDo.build().setResult(insertClassSchedulePo);
    }

    /**
     * 更新行程
     *
     * @param po
     * @return
     */
    public ResultDo updateSchedule(EpOrganClassSchedulePo po) {
        EpOrganClassSchedulePo insertClassSchedulePo = new EpOrganClassSchedulePo();
        insertClassSchedulePo.setId(po.getId());
        insertClassSchedulePo.setChildId(po.getChildId());
        insertClassSchedulePo.setClassId(po.getClassId());
        insertClassSchedulePo.setOrderId(po.getOrderId());
        insertClassSchedulePo.setStartTime(po.getStartTime());
        insertClassSchedulePo.setDuration(po.getDuration());
        insertClassSchedulePo.setCatalogTitle(po.getCatalogTitle());
        insertClassSchedulePo.setCatalogDesc(po.getCatalogDesc());
        insertClassSchedulePo.setCatalogIndex(po.getCatalogIndex());
        log.info("[预约行程]修改预约行程开始，insertClassSchedulePo={}。", insertClassSchedulePo);

        if (!checkPoParams(insertClassSchedulePo) || insertClassSchedulePo.getId() == null) {
            log.error("[预约行程]修改预约行程失败，请求参数异常。");
            return ResultDo.build(MessageCode.ERROR_SYSTEM_PARAM_FORMAT);
        }
        boolean flag = (DateTools.getTwoTimeDiffSecond(insertClassSchedulePo.getStartTime(), DateTools.getCurrentDateTime()) / BizConstant.TIME_UNIT) >=
                BizConstant.RECTIFY_SCHEDULE_STARTTIME_TONOW_LT30;
        if (!flag) {
            log.error("[预约行程]新增预约行程失败，预约行程开始时间距离当前时间不得小于30分钟。");
            return ResultDo.build(MessageCode.RECTIFY_SCHEDULE_STARTTIME_TONOW_LT30);
        }
        if (organClassScheduleRepository.updateClassSchedule(insertClassSchedulePo) == BizConstant.DB_NUM_ONE) {
            log.info("[预约行程]修改预约行程成功，id={}。", insertClassSchedulePo.getId());
            return ResultDo.build().setResult(insertClassSchedulePo);
        } else {
            log.error("[预约行程]修改预约行程失败，id={}。", insertClassSchedulePo.getId());
            return ResultDo.build(MessageCode.ERROR_OPERATE_FAIL);
        }
    }

    /**
     * 根据订单id获取订单行程Bo
     *
     * @param orderId
     * @return
     */
    public List<OrganClassScheduleBo> findBoByOrderId(Long orderId) {
        return organClassScheduleRepository.findBoByOrderId(orderId);
    }

    /**
     * 根据订单id获取订单行程修改Bo
     *
     * @param orderId
     * @return
     */
    public List<OrganClassBespeakScheduleBo> findBespeakScheduleBoByOrderId(Long orderId) {
        return organClassScheduleRepository.findBespeakScheduleBoByOrderId(orderId);
    }

    /**
     * 变更考勤
     *
     * @param id
     * @param status
     * @return
     */
    public ResultDo changeClassScheduleStatus(Long id, EpOrganClassScheduleStatus status) {
        log.info("[行程]行程考勤变更开始，id={},status={}。", id, status);
        if (organClassScheduleRepository.updateClassScheduleStatusById(id, status) == BizConstant.DB_NUM_ONE) {
            log.info("[行程]行程考勤变更成功，id={},status={}。", id, status);
            return ResultDo.build();
        } else {
            log.error("[行程]行程考勤变更失败，id={},status={}。", id, status);
            return ResultDo.build(MessageCode.ERROR_OPERATE_FAIL);
        }
    }

    /**
     * 根据搜索条件导出excel
     *
     * @param pageable
     * @param condition
     * @return
     */
    public void indexExportExcel(HttpServletRequest request, HttpServletResponse response, String fileName, Pageable pageable, Collection<? extends Condition> condition) {
        log.info("[随堂管理]导出excel开始。");
        List<Field<?>> fieldList = Lists.newArrayList();
        fieldList.add(EP_MEMBER_CHILD.CHILD_NICK_NAME);
        fieldList.add(EP_MEMBER_CHILD.CHILD_TRUE_NAME);
        fieldList.add(EP_ORGAN_CLASS_SCHEDULE.STATUS);
        fieldList.add(EP_ORGAN_CLASS_SCHEDULE.START_TIME);
        fieldList.add(EP_ORGAN_CLASS_SCHEDULE.CATALOG_INDEX);
        fieldList.add(EP_ORGAN_CLASS_SCHEDULE.CREATE_AT);
        List<ClassScheduleExcelBo> list = organClassScheduleRepository.indexExportExcel(fieldList, pageable, condition);
        List<String> fieldNameStrs = Lists.newArrayList();
        fieldNameStrs.add("childNickName");
        fieldNameStrs.add("childTrueName");
        fieldNameStrs.add("statusText");
        fieldNameStrs.add("fmtStartTime");
        fieldNameStrs.add("catalogIndex");
        fieldNameStrs.add("fmtCreateAt");
        String[] titles = {"昵称", "姓名", "考勤", "开始时间", "目录索引", "创建时间"};
        try {
            ExcelUtil.exportExcel(request, response, fileName, fieldList.size(), list, fieldNameStrs, titles);
        } catch (Exception e) {
            log.error("[随堂管理]导出excel失败", e);
        }
        log.info("[随堂管理]导出excel成功。");
    }


    /**
     * 校验po对象入参
     *
     * @param po
     * @return
     */
    private boolean checkPoParams(EpOrganClassSchedulePo po) {
        if (po.getChildId() == null) {
            return false;
        }
        if (po.getClassId() == null) {
            return false;
        }
        if (po.getOrderId() == null) {
            return false;
        }
        if (po.getStartTime() == null) {
            return false;
        }
        if (po.getDuration() == null) {
            return false;
        }
        if (po.getCatalogTitle() == null) {
            return false;
        }
        if (po.getCatalogIndex() == null) {
            return false;
        }
        return true;
    }

}
