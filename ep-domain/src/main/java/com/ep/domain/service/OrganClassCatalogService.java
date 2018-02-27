package com.ep.domain.service;

import com.ep.common.tool.CollectionsTools;
import com.ep.domain.constant.BizConstant;
import com.ep.domain.constant.MessageCode;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.MemberChildBo;
import com.ep.domain.pojo.bo.MemberChildTagAndCommentBo;
import com.ep.domain.pojo.bo.OrganCourseTagBo;
import com.ep.domain.pojo.dto.OrganClassCatelogCommentDto;
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
     * @param classCatelogId
     * @return
     */
    public ResultDo<OrganClassCatelogCommentDto> getClassCatelogCommentView(Long mobile, Long classCatelogId) {
        ResultDo<OrganClassCatelogCommentDto> resultDo = ResultDo.build();
        // 课时信息
        EpOrganClassCatalogPo classCatelogPo = organClassCatalogRepository.getById(classCatelogId);
        if (classCatelogPo == null || classCatelogPo.getDelFlag()) {
            log.error("课时信息不存在, classCatelogId={}", classCatelogId);
            return resultDo.setError(MessageCode.ERROR_CLASS_CATELOG_NOT_EXISTS);
        }
        // 校验课程
        EpOrganClassPo classPo = organClassRepository.getById(classCatelogPo.getClassId());
        if (classPo == null || classPo.getDelFlag()) {
            log.error("班次不存在, classId={}", classCatelogPo.getClassId());
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
        List<MemberChildBo> childList = memberChildRepository.queryAllByClassId(classCatelogPo.getClassId());
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
        List<MemberChildTagAndCommentBo> childTagAndCommentList = organClassCatalogRepository.findChildComments(classPo.getId(), classCatelogId);
        for (MemberChildTagAndCommentBo bo : childTagAndCommentList) {
            // 加载标签
            List<EpMemberChildTagPo> tags = memberChildTagRepository.findByChildIdAndClassCatelogId(bo.getChildId(), classCatelogId);
            bo.setTags(tags);
        }
        OrganClassCatelogCommentDto commentDto = new OrganClassCatelogCommentDto(classCatelogPo, childList, courseTagList, childTagAndCommentList);
        return resultDo.setResult(commentDto);
    }

    public List<EpOrganClassCatalogPo> findByCourseId(Long courseId){
        return organClassCatalogRepository.findByCourseId(courseId);
    }

    public List<EpOrganClassCatalogPo> findByClassId(Long classId){
        return organClassCatalogRepository.findByClassId(classId);
    }
}
