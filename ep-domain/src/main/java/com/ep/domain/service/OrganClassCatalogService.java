package com.ep.domain.service;

import com.ep.common.tool.CollectionsTools;
import com.ep.domain.constant.BizConstant;
import com.ep.domain.constant.MessageCode;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.*;
import com.ep.domain.pojo.dto.OrganClassCatalogCommentDto;
import com.ep.domain.pojo.dto.OrganClassCatalogDetailDto;
import com.ep.domain.pojo.po.*;
import com.ep.domain.repository.*;
import com.ep.domain.repository.domain.enums.EpOrganCourseCourseStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
    private OrganCourseRepository organCourseRepository;
    @Autowired
    private OrganAccountRepository organAccountRepository;
    @Autowired
    private MemberChildTagRepository memberChildTagRepository;

    /**
     * 课时评价初始化
     *
     * @param mobile
     * @param classCatalogId
     * @return
     */
    public ResultDo<OrganClassCatalogCommentDto> getClassCatalogCommentView(Long mobile, Long classCatalogId) {
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
            return resultDo.setError(MessageCode.ERROR_CLASS_NOT_EXISTS);
        }
        EpOrganCoursePo coursePo = organCourseRepository.getById(classPo.getCourseId());
        if (coursePo == null || coursePo.getDelFlag()) {
            log.error("课程不存在, courseId={}", classPo.getCourseId());
            return resultDo.setError(MessageCode.ERROR_COURSE_NOT_EXISTS);
        }
        if (!coursePo.getCourseStatus().equals(EpOrganCourseCourseStatus.opening)) {
            log.error("课程状态不是进行中, courseId={}, status={}", classPo.getCourseId(), coursePo.getCourseStatus());
            return resultDo.setError(MessageCode.ERROR_COURSE_NOT_OPENING);
        }
        // 孩子信息
        List<MemberChildBo> childList = memberChildRepository.queryAllByClassId(classCatalogPo.getClassId());
        if (CollectionsTools.isNotEmpty(childList)) {
            for (MemberChildBo childBo : childList) {
                Optional<EpFilePo> optional = fileRepository.getOneByBizTypeAndSourceId(BizConstant.FILE_BIZ_TYPE_CODE_CHILD_AVATAR, childBo.getId());
                String avatar = optional.isPresent() ? optional.get().getFileUrl() : null;
                childBo.setAvatar(avatar);
            }
        }
        // 校验班次负责人
        List<EpOrganAccountPo> accountList = organAccountRepository.getByMobile(mobile);
        if (CollectionsTools.isEmpty(accountList)) {
            return resultDo.setError(MessageCode.ERROR_ORGAN_ACCOUNT_NOT_EXISTS);
        }
        // 校验班次负责人
        Optional<EpOrganAccountPo> optional = accountList.stream().filter(item -> item.getId().equals(classPo.getOgnAccountId())).findFirst();
        if (!optional.isPresent()) {
            log.error("当前用户不是班次负责人, mobile={}", mobile);
            return resultDo.setError(MessageCode.ERROR_CLASS_ACCOUNT_NOT_MATCH);
        }
        // 课程标签
        List<OrganCourseTagBo> courseTagList = organCourseTagRepository.findBosByCourseId(classPo.getCourseId());
        // 孩子评论信息
        List<MemberChildTagAndCommentBo> childTagAndCommentList = organClassCatalogRepository.findChildComments(classPo.getId(), classCatalogId);
        for (MemberChildTagAndCommentBo bo : childTagAndCommentList) {
            // 加载标签
            List<EpMemberChildTagPo> tags = memberChildTagRepository.findByChildIdAndClassCatalogId(bo.getChildId(), classCatalogId);
            bo.setTags(tags);
        }
        OrganClassCatalogCommentDto commentDto = new OrganClassCatalogCommentDto(classCatalogPo, childList, courseTagList, childTagAndCommentList);
        return resultDo.setResult(commentDto);
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

}
