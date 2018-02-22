package com.ep.domain.service;

import com.ep.common.tool.BeanTools;
import com.ep.common.tool.CollectionsTools;
import com.ep.domain.constant.BizConstant;
import com.ep.domain.constant.MessageCode;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.*;
import com.ep.domain.pojo.dto.OrganCourseDto;
import com.ep.domain.pojo.po.*;
import com.ep.domain.repository.*;
import com.ep.domain.repository.domain.enums.EpOrganCourseCourseStatus;
import com.ep.domain.repository.domain.tables.EpOrganCourse;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.jooq.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
     *
     * @param id
     * @return
     */
    public EpOrganCoursePo getById(Long id) {
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
    public void createOrganCourseByMerchant(EpOrganCoursePo organCoursePo, List<OrganClassBo> organClassBos, List<EpConstantTagPo> constantTagPos) {
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
            for (int i = 0; i < organClassCatelogPos.size(); i++) {
                organClassCatelogPos.get(i).setClassId(insertOrganClassId);
                organClassCatelogPos.get(i).setCatelogIndex(i + 1);
            }
            //班次课程内容目录表插入数据
            organClassCatelogRepository.insert(organClassCatelogPos);
        });

        List<EpOrganCourseTagPo> insertOrganCourseTagPos = Lists.newArrayList();
        constantTagPos.forEach(constantTagPo -> {
            if (constantTagPo.getId() == null) {
                constantTagPo.setOgnFlag(true);
                constantTagPo.setOgnId(organCoursePo.getOgnId());
                constantTagPo.setCatalogId(insertOrganCoursePo.getCourseCatalogId());
                EpConstantTagPo insertPo = constantTagPepository.insertNew(constantTagPo);
                insertOrganCourseTagPos.add(new EpOrganCourseTagPo(null, insertPo.getId(), insertOrganCourseId, null, null, null, null, null, null));
            } else {
                insertOrganCourseTagPos.add(new EpOrganCourseTagPo(null, constantTagPo.getId(), insertOrganCourseId, null, null, null, null, null, null));
            }
        });
        //课程标签表插入数据
        organCourseTagRepository.insert(insertOrganCourseTagPos);
    }

    /**
     * 商户后台更新课程
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateOrganCourseByMerchant(EpOrganCoursePo organCoursePo, List<OrganClassBo> organClassBos, List<EpConstantTagPo> constantTagPos) {
        //机构课程表更新数据
//        organCourseRepository.updateById(organCoursePo);
        Long organCourseId = organCoursePo.getId();
        organClassBos.forEach(organClassBo -> {
            EpOrganClassPo newOrganClassPo = new EpOrganClassPo();
            BeanTools.copyPropertiesIgnoreNull(organClassBo, newOrganClassPo);
            EpOrganClassPo oldOrganClassPo = organClassRepository.getById(newOrganClassPo.getId());
            if(!this.isClassEq4MerchUpdateCourse(newOrganClassPo,oldOrganClassPo)){
                organClassRepository.updateOrganClassPo(newOrganClassPo);
            }
            List<EpOrganClassCatelogPo> newOrganClassCatelogPos=organClassBo.getOrganClassCatelogPos();
            List<EpOrganClassCatelogPo> oldOrganClassCatelogPos=organClassCatelogRepository.findByClassId(organClassBo.getId());

            Set<Long> classCatelogNewSet= new HashSet<>();
            Set<Long> classCatelogOldSet= new HashSet<>();
            newOrganClassCatelogPos.forEach(p->{
                classCatelogNewSet.add(p.getId());
            });
            oldOrganClassCatelogPos.forEach(p->{
                classCatelogOldSet.add(p.getId());
            });
            Set<Long> classCatelogdiffAdd = Sets.difference(classCatelogNewSet, classCatelogOldSet);//差集，classCatelogNewSet有, classCatelogOldSet无
            Set<Long> classCatelogdiffDel = Sets.difference(classCatelogOldSet, classCatelogNewSet);//差集，classCatelogOldSet有, classCatelogNewSet无
            Set<Long> classCatelogInter= Sets.intersection(classCatelogNewSet, classCatelogOldSet);//交集
            //批量删除 constantTagOldSet有, constantTagNewSet无
            organClassCatelogRepository.deleteByIds(new ArrayList<>(classCatelogdiffDel));
// organClassPo.setOgnId(organCoursePo.getOgnId());
//            organClassPo.setCourseId(insertOrganCourseId);
            //机构课程班次表更新数据
//            EpOrganClassPo insertOrganClassPo = organClassRepository.insertNew(organClassPo);
//            Long insertOrganClassId = insertOrganClassPo.getId();
//            List<EpOrganClassCatelogPo> organClassCatelogPos = organClassBo.getOrganClassCatelogPos();
//            for(int i=0;i<organClassCatelogPos.size();i++){
//                organClassCatelogPos.get(i).setClassId(insertOrganClassId);
//                organClassCatelogPos.get(i).setCatelogIndex(i+1);
//            }
//            //班次课程内容目录表插入数据
//            organClassCatelogRepository.insert(organClassCatelogPos);
        });
//
//        List<EpOrganCourseTagPo> insertOrganCourseTagPos = Lists.newArrayList();

        //标签start
        List<EpOrganCourseTagPo> insertOrganCourseTagPos= Lists.newArrayList();
        Set<Long> constantTagNewSet= new HashSet<>();
        Set<Long> constantTagOldSet= new HashSet<>();
        constantTagPos.forEach(constantTagPo->{
            if(constantTagPo.getId()==null){
                constantTagPo.setOgnFlag(true);
                constantTagPo.setOgnId(organCoursePo.getOgnId());
                constantTagPo.setCatalogId(organCoursePo.getCourseCatalogId());
                EpConstantTagPo insertConstantTagPo = constantTagPepository.insertNew(constantTagPo);
                insertOrganCourseTagPos.add(new EpOrganCourseTagPo(null,insertConstantTagPo.getId(),organCourseId,null,null,null,null,null,null));
            }else{
                constantTagNewSet.add(constantTagPo.getId());
            }
        });
        List<OrganCourseTagBo> oldTagBos = organCourseTagRepository.findBosByCourseId(organCoursePo.getId());

        oldTagBos.forEach(oldTagBo->{
            constantTagOldSet.add(oldTagBo.getTagId());
        });

        Set<Long> diffAdd = Sets.difference(constantTagNewSet, constantTagOldSet);//差集，constantTagNewSet有, constantTagOldSet无
        Set<Long> diffDel = Sets.difference(constantTagOldSet, constantTagNewSet);//差集，constantTagOldSet有, constantTagNewSet无
        //批量删除 constantTagOldSet有, constantTagNewSet无
        organCourseTagRepository.deleteByTagIdsAndCourseId(new ArrayList<>(diffDel),organCourseId);
        constantTagPepository.deleteOgnTagByIds(new ArrayList<>(diffDel));
        //批量插入
        diffAdd.forEach(p->{
            insertOrganCourseTagPos.add(new EpOrganCourseTagPo(null,p,organCourseId,null,null,null,null,null,null));
        });
        organCourseTagRepository.insert(insertOrganCourseTagPos);
        //标签end

        constantTagPos.forEach(constantTagPo -> {
//            if (constantTagPo.getId() == null) {
//                constantTagPo.setOgnFlag(true);
//                constantTagPo.setOgnId(organCoursePo.getOgnId());
//                constantTagPo.setCatalogId(insertOrganCoursePo.getCourseCatalogId());
//                EpConstantTagPo insertPo = constantTagPepository.insertNew(constantTagPo);
//                insertOrganCourseTagPos.add(new EpOrganCourseTagPo(null,insertPo.getId(),insertOrganCourseId,null,null,null,null,null,null));
//            }else{
//                insertOrganCourseTagPos.add(new EpOrganCourseTagPo(null,constantTagPo.getId(),insertOrganCourseId,null,null,null,null,null,null));
//            }
        });
//        //课程标签表插入数据
//        organCourseTagRepository.insert(insertOrganCourseTagPos);
    }

    /**
     * 商户后台更新课程时，比较两个EpOrganClassPo对象是否相同
     * 班次名称，负责人，价格，折扣，是否限制报名人数，要求报名人数，总计课时
     * @param newPo
     * @param oldPo
     * @return
     */
    private Boolean isClassEq4MerchUpdateCourse(EpOrganClassPo newPo, EpOrganClassPo oldPo) {
        if (!newPo.getClassName().equals(oldPo.getClassName())) {
            return false;
        }
        if (!newPo.getOgnAccountId().equals(oldPo.getOgnAccountId())) {
            return false;
        }
        if (0 != newPo.getClassPrize().compareTo(oldPo.getClassPrize())) {
            return false;
        }
        if (0 != newPo.getDiscountAmount().compareTo(oldPo.getDiscountAmount())) {
            return false;
        }
        if (newPo.getEnterLimitFlag().booleanValue() != oldPo.getEnterLimitFlag().booleanValue()) {
            return false;
        }
        if (newPo.getEnterRequireNum().equals(oldPo.getEnterRequireNum())) {
            return false;
        }
        if (newPo.getCourseNum().equals(oldPo.getCourseNum())) {
            return false;
        }
        return true;
    }

    /**
     * 商户后台更新课程时，比较两个EpOrganClassCatelogPo对象是否相同
     * 班次名称，负责人，价格，折扣，是否限制报名人数，要求报名人数，总计课时
     * @param newPo
     * @param oldPo
     * @return
     */
    private Boolean isClassCatelogEq4MerchUpdateCourse(EpOrganClassCatelogPo newPo, EpOrganClassCatelogPo oldPo){
        if (!newPo.getCatelogTitle().equals(oldPo.getCatelogTitle())) {
            return false;
        }
        if (!newPo.getCatelogDesc().equals(oldPo.getCatelogDesc())) {
            return false;
        }
        if (!newPo.getStartTime().equals(oldPo.getStartTime())) {
            return false;
        }
        return true;
    }
}
