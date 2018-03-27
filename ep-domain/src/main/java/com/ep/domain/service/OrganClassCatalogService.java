package com.ep.domain.service;

import com.ep.common.tool.CollectionsTools;
import com.ep.common.tool.StringTools;
import com.ep.domain.constant.BizConstant;
import com.ep.domain.constant.MessageCode;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.*;
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

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @Description: 课程目录接口服务类
 * @Author: J.W
 * @Date: 下午5:54 2018/2/12
 */
@Slf4j
@Service
public class OrganClassCatalogService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrganClassCatalogRepository organClassCatalogRepository;
    @Autowired
    private MemberChildRepository memberChildRepository;
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private OrganCourseTagRepository organCourseTagRepository;
    @Autowired
    private OrganClassRepository organClassRepository;
    @Autowired
    private OrganClassChildRepository organClassChildRepository;
    @Autowired
    private OrganAccountRepository organAccountRepository;
    @Autowired
    private MemberChildTagRepository memberChildTagRepository;
    @Autowired
    private MemberChildCommentRepository memberChildCommentRepository;

    /**
     * 课时评价初始化
     *
     * @param organAccountPo
     * @param classCatalogId
     * @return
     */
    public ResultDo<OrganClassCatalogCommentDto> getClassCatalogCommentView(EpOrganAccountPo organAccountPo, Long classCatalogId) {
        log.info("课时评价初始化, accountId={}, classCatalogId={}", organAccountPo.getId(), classCatalogId);
        ResultDo<OrganClassCatalogCommentDto> resultDo = ResultDo.build();
        // 课时信息
        EpOrganClassCatalogPo classCatalogPo = organClassCatalogRepository.getById(classCatalogId);
        if (classCatalogPo == null || classCatalogPo.getDelFlag()) {
            log.error("课时信息不存在, classCatalogId={}", classCatalogId);
            return resultDo.setError(MessageCode.ERROR_CLASS_CATALOG_NOT_EXISTS);
        }
        // 校验课程
        EpOrganClassPo classPo = organClassRepository.getById(classCatalogPo.getClassId());
        if (classPo == null || classPo.getDelFlag()) {
            log.error("班次不存在, classId={}", classCatalogPo.getClassId());
            return resultDo.setError(MessageCode.ERROR_CLASS_NOT_EXIST);
        }
        if (!classPo.getStatus().equals(EpOrganClassStatus.opening) && !classPo.getStatus().equals(EpOrganClassStatus.end)) {
            log.error("班次不是进行中状态, classId={}, status={}", classCatalogPo.getClassId(), classPo.getStatus().getName());
            return resultDo.setError(MessageCode.ERROR_CLASS_NOT_START);
        }
        // 校验班次负责人
        if (!organAccountPo.getId().equals(classPo.getOgnAccountId())) {
            log.error("当前机构账户不是班次负责人, accountId={}, classId={}", organAccountPo.getId(), classPo.getId());
            return resultDo.setError(MessageCode.ERROR_ORGAN_ACCOUNT_NOT_MATCH_CLASS);
        }
        // 课程标签
        List<OrganCourseTagBo> courseTagList = organCourseTagRepository.findBosByCourseId(classPo.getCourseId());
        // 孩子评论信息
        List<OrganClassCatalogCommentBo> childTagAndCommentList = organClassCatalogRepository.findChildComments(classPo.getId(), classCatalogId);
        for (OrganClassCatalogCommentBo bo : childTagAndCommentList) {
            // 头像
            Optional<EpFilePo> optional = fileRepository.getOneByBizTypeAndSourceId(BizConstant.FILE_BIZ_TYPE_CODE_CHILD_AVATAR, bo.getChildId());
            String avatar = optional.isPresent() ? optional.get().getFileUrl() : null;
            bo.setAvatar(avatar);
            // 加载标签
            List<MemberChildTagBo> tags = memberChildTagRepository.findDetailByChildIdAndClassCatalogId(bo.getChildId(), classCatalogId);
            bo.setTags(tags);
        }
        OrganClassCatalogCommentDto commentDto = new OrganClassCatalogCommentDto(classCatalogPo, childTagAndCommentList, courseTagList);
        return resultDo.setResult(commentDto);
    }

    /**
     * 课时评价
     *
     * @param organAccountPo
     * @param classCatalogId
     * @param childId
     * @param tagIds
     * @param comment
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResultDo doClassCatalogComment(EpOrganAccountPo organAccountPo, Long classCatalogId, Long childId, List<Long> tagIds, String comment) {
        log.info("老师课时评价开始, accountId={}, classCatalogId={}, childId={}, tagIds={}, comment={}", organAccountPo.getId(), classCatalogId, childId, tagIds, childId);
        if (CollectionsTools.isEmpty(tagIds) && StringTools.isBlank(comment)) {
            return ResultDo.build(MessageCode.ERROR_SYSTEM_PARAM_FORMAT);
        }
        // 课时信息
        EpOrganClassCatalogPo classCatalogPo = organClassCatalogRepository.getById(classCatalogId);
        if (classCatalogPo == null || classCatalogPo.getDelFlag()) {
            log.error("课时信息不存在, classCatalogId={}", classCatalogId);
            return ResultDo.build(MessageCode.ERROR_CLASS_CATALOG_NOT_EXISTS);
        }
        // 校验课程
        EpOrganClassPo classPo = organClassRepository.getById(classCatalogPo.getClassId());
        if (classPo == null || classPo.getDelFlag()) {
            log.error("班次不存在, classId={}", classCatalogPo.getClassId());
            return ResultDo.build(MessageCode.ERROR_CLASS_NOT_EXIST);
        }
        if (!classPo.getStatus().equals(EpOrganClassStatus.opening)) {
            log.error("班次不是进行中状态, classId={}, status={}", classCatalogPo.getClassId(), classPo.getStatus().getName());
            return ResultDo.build(MessageCode.ERROR_CLASS_NOT_OPENING);
        }
        // 孩子信息
        Optional<EpOrganClassChildPo> existChild = organClassChildRepository.getByClassIdAndChildId(classCatalogPo.getClassId(), childId);
        if (!existChild.isPresent()) {
            return ResultDo.build(MessageCode.ERROR_CHILD_NOT_EXISTS);
        }
        // 校验班次负责人
        if (!organAccountPo.getId().equals(classPo.getOgnAccountId())) {
            log.error("当前机构账户不是班次负责人, accountId={}, classId={}", organAccountPo.getId(), classPo.getId());
            return ResultDo.build(MessageCode.ERROR_ORGAN_ACCOUNT_NOT_MATCH_CLASS);
        }
        // 加锁
        EpOrganAccountPo accountPo = organAccountRepository.getByIdForLock(organAccountPo.getId());
        if (accountPo == null || accountPo.getDelFlag()) {
            log.error("加锁失败，当前用户无机构账户数据, accountId={}", organAccountPo.getId());
            return ResultDo.build(MessageCode.ERROR_ORGAN_ACCOUNT_NOT_EXISTS);
        }
        // 检查是否已经存在评价标签
        boolean existTags = memberChildTagRepository.existByChildIdAndClassCatalogId(childId, classCatalogId);
        if (existTags) {
            log.error("当前课时评价（标签）已存在, childId={}, classCatalogId={}", childId, classCatalogId);
            return ResultDo.build(MessageCode.ERROR_CLASS_CATALOG_COMMENT_IS_EXIST);
        }
        // 检查是否已经存在评价内容
        boolean existCatalog = memberChildCommentRepository.existByChildIdAndClassCatalogId(childId, classCatalogId);
        if (existCatalog) {
            log.error("当前课时评价（内容）已存在, childId={}, classCatalogId={}", childId, classCatalogId);
            return ResultDo.build(MessageCode.ERROR_CLASS_CATALOG_COMMENT_IS_EXIST);
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
                tagPo.setClassCatalogId(classCatalogId);
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
            commentPo.setClassCatalogId(classCatalogId);
            commentPo.setType(EpMemberChildCommentType.launch);
            commentPo.setContent(comment);
            commentPo.setOgnAccountId(accountPo.getId());
            memberChildCommentRepository.insert(commentPo);
        }
        // 孩子班次评价数+1
        organClassChildRepository.addScheduleCommentNum(existChild.get().getOrderId());
        // 课时评价数+1
        organClassCatalogRepository.addChildEvaluatedNum(classCatalogId);
        return ResultDo.build();
    }

    /**
     * 根据班次获取
     *
     * @param classId
     * @return
     */
    public List<EpOrganClassCatalogPo> findByClassId(Long classId){
        return organClassCatalogRepository.findByClassId(classId);
    }

    /**
     * 根据班次获取
     *
     * @param classId
     * @return
     */
    public List<RectifyOrganClassCatalogBo> findRectifyBoByClassId(Long classId) {
        return organClassCatalogRepository.findRectifyBoByClassId(classId);
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
        if (orderPo == null || orderPo.getDelFlag()) {
            return resultDo.setError(MessageCode.ERROR_ORDER_NOT_EXISTS);
        }
        EpMemberChildPo childPo = memberChildRepository.getById(orderPo.getChildId());
        if (childPo == null || !childPo.getMemberId().equals(memberId)) {
            return resultDo.setError(MessageCode.ERROR_CHILD_NOT_EXISTS);
        }
        // 标签汇总
        List<MemberChildTagBo> tags = memberChildTagRepository.findTagsByChildIdAndClassId(orderPo.getChildId(), orderPo.getClassId());
        // 课时明细
        List<OrganClassCatalogBo> classCatalogs = organClassCatalogRepository.findDetailByClassId(orderPo.getClassId());
        OrganClassCatalogDetailDto detailDto = new OrganClassCatalogDetailDto(tags, classCatalogs);
        return resultDo.setResult(detailDto);
    }

    /**
     * 查看班次全部课时
     *
     * @param classId
     * @param organAccountPo
     * @return
     */
    public ResultDo<List<OrganAccountClassBo>> getClassAllCatalog(Long classId, EpOrganAccountPo organAccountPo) {
        ResultDo<List<OrganAccountClassBo>> resultDo = ResultDo.build();
        // 校验课程
        EpOrganClassPo classPo = organClassRepository.getById(classId);
        if (classPo == null || classPo.getDelFlag()) {
            log.error("班次不存在, classId={}", classId);
            return resultDo.setError(MessageCode.ERROR_CLASS_NOT_EXIST);
        }
        if (classPo.getStatus().equals(EpOrganClassStatus.save)) {
            log.error("课程未上线, classId={}, status={}", classId, classPo.getStatus().getName());
            return resultDo.setError(MessageCode.ERROR_COURSE_NOT_ONLINE);
        }
        // 校验班次负责人
        if (!organAccountPo.getId().equals(classPo.getOgnAccountId())) {
            log.error("当前机构账户不是班次负责人, accountId={}, classId={}", organAccountPo.getId(), classId);
            return resultDo.setError(MessageCode.ERROR_ORGAN_ACCOUNT_NOT_MATCH_CLASS);
        }
        List<OrganAccountClassBo> classCatalogs = organClassCatalogRepository.findByClassIdForOgnAccount(classId);
        return resultDo.setResult(classCatalogs);
    }
}
