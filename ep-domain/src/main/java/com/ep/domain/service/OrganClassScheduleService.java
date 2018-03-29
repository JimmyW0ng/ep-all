package com.ep.domain.service;

import com.ep.common.tool.CollectionsTools;
import com.ep.common.tool.DateTools;
import com.ep.common.tool.StringTools;
import com.ep.domain.constant.BizConstant;
import com.ep.domain.constant.MessageCode;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.MemberChildTagBo;
import com.ep.domain.pojo.bo.OrganClassCatalogBo;
import com.ep.domain.pojo.bo.OrganClassCatalogCommentBo;
import com.ep.domain.pojo.bo.OrganCourseTagBo;
import com.ep.domain.pojo.dto.OrganClassCatalogCommentDto;
import com.ep.domain.pojo.dto.OrganClassCatalogDetailDto;
import com.ep.domain.pojo.po.*;
import com.ep.domain.repository.*;
import com.ep.domain.repository.domain.enums.EpMemberChildCommentType;
import com.ep.domain.repository.domain.enums.EpOrganClassStatus;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @Description: 班次行程服务
 * @Author: CC.F
 * @Date: 16:08 2018/3/28/028
 */
@Slf4j
@Service
public class OrganClassScheduleService {

    @Autowired
    private OrganClassCatalogRepository organClassCatalogRepository;
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private OrganCourseTagRepository organCourseTagRepository;
    @Autowired
    private OrganClassChildRepository organClassChildRepository;
    @Autowired
    private OrganAccountRepository organAccountRepository;
    @Autowired
    private MemberChildCommentRepository memberChildCommentRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrganClassScheduleRepository organClassScheduleRepository;
    @Autowired
    private OrganClassRepository organClassRepository;
    @Autowired
    private MemberChildTagRepository memberChildTagRepository;

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
        if (orderPo == null || orderPo.getDelFlag()) {
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
     * @param childId
     * @param tagIds
     * @param comment
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResultDo doClassCatalogComment(EpOrganAccountPo organAccountPo, Long classScheduleId, Long childId, List<Long> tagIds, String comment) {
        log.info("老师课时评价开始, accountId={}, classScheduleId={}, childId={}, tagIds={}, comment={}", organAccountPo.getId(), classScheduleId, childId, tagIds, childId);
        if (CollectionsTools.isEmpty(tagIds) && StringTools.isBlank(comment)) {
            return ResultDo.build(MessageCode.ERROR_SYSTEM_PARAM_FORMAT);
        }
        // 课时信息
        EpOrganClassSchedulePo schedulePo = organClassScheduleRepository.getById(classScheduleId);
        if (schedulePo == null || schedulePo.getDelFlag()) {
            log.error("班次行程信息不存在, classScheduleId={}", classScheduleId);
            return ResultDo.build(MessageCode.ERROR_CLASS_SCHEDULE_NOT_EXIST);
        }
        // 不允许在课时未开始的
        if (DateTools.getCurrentDateTime().before(schedulePo.getStartTime())) {
            log.error("班次行程未开始, classScheduleId={}", classScheduleId);
            return ResultDo.build(MessageCode.ERROR_CLASS_SCHEDULE_NOT_START);
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
        // 孩子信息
        Optional<EpOrganClassChildPo> existChild = organClassChildRepository.getByClassIdAndChildId(schedulePo.getClassId(), childId);
        if (!existChild.isPresent()) {
            return ResultDo.build(MessageCode.ERROR_CHILD_NOT_EXISTS);
        }
        // 校验班次负责人
        if (!organAccountPo.getId().equals(classPo.getOgnAccountId())) {
            log.error("当前机构账户不是班次负责人, accountId={}, classId={}", organAccountPo.getId(), classPo.getId());
            return ResultDo.build(MessageCode.ERROR_ORGAN_ACCOUNT_NOT_MATCH_CLASS);
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
                tagPo.setChildId(childId);
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
            commentPo.setChildId(childId);
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
        organClassChildRepository.addScheduleCommentNum(existChild.get().getOrderId());
        return ResultDo.build();
    }

}
