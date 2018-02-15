package com.ep.domain.service;

import com.ep.common.tool.BeanTools;
import com.ep.common.tool.CollectionsTools;
import com.ep.domain.constant.BizConstant;
import com.ep.domain.constant.MessageCode;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.EpOrganClassBo;
import com.ep.domain.pojo.bo.OrganAccountBo;
import com.ep.domain.pojo.bo.OrganClassCommentBo;
import com.ep.domain.pojo.bo.OrganCourseBo;
import com.ep.domain.pojo.dto.OrganCourseDto;
import com.ep.domain.pojo.po.*;
import com.ep.domain.repository.*;
import com.ep.domain.repository.domain.enums.EpOrganCourseCourseStatus;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.jooq.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @Description: 机构课程服务类
 * @Author: J.W
 * @Date: 上午9:30 2018/1/14
 */
@Slf4j
@Service
public class OrganCourseService {

    @Autowired
    private OrganCourseRepository organCourseRepository;
    @Autowired
    private OrganRepository organRepository;
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private OrganClassRepository organClassRepository;
    @Autowired
    private OrganAccountRepository organAccountRepository;
    @Autowired
    private OrganClassCommentRepository organClassCommentRepository;
    @Autowired
    private OrganClassCatelogRepository organClassCatelogRepository;
    @Autowired
    private ConstantTagPepository constantTagPepository;
    @Autowired
    private OrganCourseTagRepository organCourseTagRepository;

    /**
     * 根据id获取机构课程
     * @param id
     * @return
     */
    public EpOrganCoursePo getById(Long id){
        return organCourseRepository.getById(id);
    }

    /**
     * 前提－课程明细
     *
     * @param courseId
     * @return
     */
    public ResultDo<OrganCourseDto> getCourseDetail(Long courseId) {
        OrganCourseBo ognCourseBo = organCourseRepository.getDetailById(courseId);
        if (ognCourseBo == null) {
            return ResultDo.build(MessageCode.ERROR_DATA_MISS);
        }
        // 获取主图
        Optional<EpFilePo> optional = fileRepository.getOneByBizTypeAndSourceId(BizConstant.FILE_BIZ_TYPE_CODE_COURSE_MAIN_PIC, courseId);
        if (optional.isPresent()) {
            ognCourseBo.setMainPicUrl(optional.get().getFileUrl());
        }
        // 获取机构信息
        EpOrganPo organPo = organRepository.getById(ognCourseBo.getOgnId());
        ognCourseBo.setOgnName(organPo.getOgnName());
        ognCourseBo.setOgnPhone(organPo.getOgnPhone());
        // 获取班次信息
        List<EpOrganClassPo> classes = organClassRepository.getByCourseId(courseId);
        // 老师介绍
        List<OrganAccountBo> team = organAccountRepository.getByCourseId(courseId);
        if (CollectionsTools.isNotEmpty(team)) {
            for (OrganAccountBo accountBo : team) {
                Optional<EpFilePo> optAvatar = fileRepository.getOneByBizTypeAndSourceId(BizConstant.FILE_BIZ_TYPE_CODE_TEACHER_AVATAR, accountBo.getId());
                String avatar = optAvatar.isPresent() ? optAvatar.get().getFileUrl() : null;
                accountBo.setAvatar(avatar);
            }
        }
        // 评论
        List<OrganClassCommentBo> comments = organClassCommentRepository.getChosenByCourseId(courseId);
        if (CollectionsTools.isNotEmpty(comments)) {
            for (OrganClassCommentBo classCommentBo : comments) {
                Optional<EpFilePo> optAvatar = fileRepository.getOneByBizTypeAndSourceId(BizConstant.FILE_BIZ_TYPE_CODE_CHILD_AVATAR, classCommentBo.getChildId());
                String avatar = optAvatar.isPresent() ? optAvatar.get().getFileUrl() : null;
                classCommentBo.setChildAvatar(avatar);
            }
        }
        // 封装返回dto
        OrganCourseDto courseDto = new OrganCourseDto(ognCourseBo, classes, team, comments);
        ResultDo<OrganCourseDto> resultDo = ResultDo.build();
        resultDo.setResult(courseDto);
        return resultDo;
    }

    /**
     * 查询机构课程列表-分页
     *
     * @param pageable
     * @param ognId
     * @return
     */
    public Page<OrganCourseBo> findCourseByOgnIdForPage(Pageable pageable, Long ognId) {
        Page<OrganCourseBo> ognCourses = organCourseRepository.findCourseByOgnIdForPage(pageable, ognId);
        if (CollectionsTools.isNotEmpty(ognCourses.getContent())) {
            for (OrganCourseBo courseBo : ognCourses.getContent()) {
                Optional<EpFilePo> optional = fileRepository.getOneByBizTypeAndSourceId(BizConstant.FILE_BIZ_TYPE_CODE_COURSE_MAIN_PIC, courseBo.getId());
                if (optional.isPresent()) {
                    courseBo.setMainPicUrl(optional.get().getFileUrl());
                }
            }
        }
        return ognCourses;
    }

    /**
     * 后台机构课程分页列表
     *
     * @param pageable
     * @param condition
     * @return
     */
    public Page<OrganCourseBo> findbyPageAndCondition(Pageable pageable, Collection<? extends Condition> condition) {
        return organCourseRepository.findbyPageAndCondition(pageable, condition);
    }

    /**
     * 商户后台创建课程
     */
    @Transactional(rollbackFor = Exception.class)
    public void createOrganCourseByMerchant(EpOrganCoursePo organCoursePo, List<EpOrganClassBo> organClassBos, List<EpConstantTagPo> constantTagPos) {
        //机构课程表插入数据
        organCoursePo.setCourseStatus(EpOrganCourseCourseStatus.save);
        EpOrganCoursePo insertOrganCoursePo = organCourseRepository.insertNew(organCoursePo);
        Long insertOrganCourseId = insertOrganCoursePo.getId();
        organClassBos.forEach(organClassBo -> {
            EpOrganClassPo organClassPo = new EpOrganClassPo();
            BeanTools.copyPropertiesIgnoreNull(organClassBo, organClassPo);
            organClassPo.setOgnId(organCoursePo.getOgnId());
            organClassPo.setCourseId(insertOrganCourseId);
            //机构课程班次表插入数据
            EpOrganClassPo insertOrganClassPo = organClassRepository.insertNew(organClassPo);
            Long insertOrganClassId = insertOrganClassPo.getId();
            List<EpOrganClassCatelogPo> organClassCatelogPos = organClassBo.getOrganClassCatelogPos();
            for(int i=0;i<organClassCatelogPos.size();i++){
                organClassCatelogPos.get(i).setClassId(insertOrganClassId);
                organClassCatelogPos.get(i).setCatelogIndex(i+1);
            }
            //班次课程内容目录表插入数据
            organClassCatelogRepository.insert(organClassCatelogPos);
        });

        List<EpOrganCourseTagPo> insertOrganCourseTagPos = Lists.newArrayList();
        constantTagPos.forEach(constantTagPo -> {
            if (constantTagPo.getId() == null) {
                constantTagPo.setOgnFlag(true);
                constantTagPo.setCatalogId(insertOrganCoursePo.getCourseCatalogId());
                EpConstantTagPo insertPo = constantTagPepository.insertNew(constantTagPo);
                insertOrganCourseTagPos.add(new EpOrganCourseTagPo(null,insertPo.getId(),insertOrganCourseId,null,null,null,null,null,null));
            }else{
                insertOrganCourseTagPos.add(new EpOrganCourseTagPo(null,constantTagPo.getId(),insertOrganCourseId,null,null,null,null,null,null));
            }
        });
        //课程标签表插入数据
        organCourseTagRepository.insert(insertOrganCourseTagPos);
    }
}
