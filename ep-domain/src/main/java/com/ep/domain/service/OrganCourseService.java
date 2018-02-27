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
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.jooq.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
    private OrganClassCatalogRepository organClassCatalogRepository;
    @Autowired
    private ConstantTagRepository constantTagRepository;
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
        //获取最低价格start
        BigDecimal[] priceArr=new BigDecimal[organClassBos.size()];
        for(int i=0;i<priceArr.length;i++){
            priceArr[i]=organClassBos.get(i).getClassPrize();
        }
        int index = 0;
        for (int j = index + 1; j < priceArr.length; j++) {
            if (priceArr[j].compareTo(priceArr[index])==-1 ) {
                BigDecimal temp  = priceArr[j];
                priceArr[j] = priceArr[index];
                priceArr[index] = temp;
            }
        }
        //获取最低价格end
        //机构课程表插入数据
        organCoursePo.setPrizeMin(priceArr[index]);
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
            List<EpOrganClassCatalogPo> organClassCatelogPos = organClassBo.getOrganClassCatelogPos();
            for (int i = 0; i < organClassCatelogPos.size(); i++) {
                organClassCatelogPos.get(i).setClassId(insertOrganClassId);
//                organClassCatelogPos.get(i).setCatelogIndex(i + 1);
            }
            //班次课程内容目录表插入数据
            organClassCatalogRepository.insert(organClassCatelogPos);
        });

        List<EpOrganCourseTagPo> insertOrganCourseTagPos = Lists.newArrayList();
        constantTagPos.forEach(constantTagPo -> {
            EpOrganCourseTagPo insertOrganCourseTagPo = new EpOrganCourseTagPo();
            insertOrganCourseTagPo.setTagId(constantTagPo.getId());
            insertOrganCourseTagPo.setCourseId(insertOrganCourseId);
            insertOrganCourseTagPos.add(insertOrganCourseTagPo);
        });
        //课程标签表插入数据
        organCourseTagRepository.insert(insertOrganCourseTagPos);
    }

    /**
     * 商户后台更新课程
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateOrganCourseByMerchant(EpOrganCoursePo organCoursePo, List<OrganClassBo> organClassBos, List<EpConstantTagPo> constantTagPos) {
        //获取最低价格start
        BigDecimal[] priceArr=new BigDecimal[organClassBos.size()];
        for(int i=0;i<priceArr.length;i++){
            priceArr[i]=organClassBos.get(i).getClassPrize();
        }
        int index = 0;
        for (int j = index + 1; j < priceArr.length; j++) {
            if (priceArr[j].compareTo(priceArr[index])==-1 ) {
                BigDecimal temp  = priceArr[j];
                priceArr[j] = priceArr[index];
                priceArr[index] = temp;
            }
        }
        //获取最低价格end
        organCoursePo.setPrizeMin(priceArr[index]);
        //机构课程表更新数据
        organCourseRepository.updateByIdLock(organCoursePo);
        //课程id
        Long organCourseId = organCoursePo.getId();
        List<Long> classIds=organClassRepository.findClassIdsByCourseId(organCourseId);
        //物理删除班次目录
        organClassCatalogRepository.deletePhysicByClassIds(classIds);
        //物理删除班次
        organClassRepository.deletePhysicByCourseId(organCourseId);
        //物理删除课程标签
        organCourseTagRepository.deletePhysicByCourseId(organCourseId);

        organClassBos.forEach(organClassBo -> {
            EpOrganClassPo organClassPo = new EpOrganClassPo();
            BeanTools.copyPropertiesIgnoreNull(organClassBo, organClassPo);
            organClassPo.setId(null);
            organClassPo.setOgnId(organCoursePo.getOgnId());
            organClassPo.setCourseId(organCourseId);
            //机构课程班次表插入数据
            EpOrganClassPo insertOrganClassPo = organClassRepository.insertNew(organClassPo);
            Long insertOrganClassId = insertOrganClassPo.getId();
            List<EpOrganClassCatalogPo> organClassCatelogPos = organClassBo.getOrganClassCatelogPos();
            for (int i = 0; i < organClassCatelogPos.size(); i++) {
                organClassCatelogPos.get(i).setClassId(insertOrganClassId);
                organClassCatelogPos.get(i).setId(null);
//                organClassCatelogPos.get(i).setCatelogIndex(i + 1);
            }
            //班次课程内容目录表插入数据
            organClassCatalogRepository.insert(organClassCatelogPos);
        });

        List<EpOrganCourseTagPo> insertOrganCourseTagPos = Lists.newArrayList();
        constantTagPos.forEach(constantTagPo -> {
            EpOrganCourseTagPo insertOrganCourseTagPo = new EpOrganCourseTagPo();
            insertOrganCourseTagPo.setTagId(constantTagPo.getId());
            insertOrganCourseTagPo.setCourseId(organCourseId);
            insertOrganCourseTagPos.add(insertOrganCourseTagPo);
        });
        //课程标签表插入数据
        organCourseTagRepository.insert(insertOrganCourseTagPos);

    }

    /**
     * 商户后台更新课程时，比较两个EpOrganCoursePo对象是否相同
     * 课程名称，课程类型，所属目录，课程简介，课程内容，课程须知，最低价格，上课地址，上线时间，报名开始时间，报名结束时间
     *
     * @param newPo
     * @param oldPo
     * @return
     */
    private Boolean isCourseEq4MerchUpdateCourse(EpOrganCoursePo newPo, EpOrganCoursePo oldPo) {
        if (!newPo.getCourseName().equals(oldPo.getCourseName())) {
            return false;
        }
        if (!newPo.getCourseType().equals(oldPo.getCourseType())) {
            return false;
        }
        if (!newPo.getCourseCatalogId().equals(oldPo.getCourseCatalogId())) {
            return false;
        }
        if (!newPo.getCourseIntroduce().equals(oldPo.getCourseIntroduce())) {
            return false;
        }
        if (!newPo.getCourseContent().equals(oldPo.getCourseContent())) {
            return false;
        }
        if (!newPo.getCourseNote().equals(oldPo.getCourseNote())) {
            return false;
        }
        if (0 != newPo.getPrizeMin().compareTo(oldPo.getPrizeMin())) {
            return false;
        }
        if (!newPo.getCourseAddress().equals(oldPo.getCourseAddress())) {
            return false;
        }
        if (!newPo.getOnlineTime().equals(oldPo.getOnlineTime())) {
            return false;
        }
        if (!newPo.getEnterTimeStart().equals(oldPo.getEnterTimeStart())) {
            return false;
        }
        if (!newPo.getEnterTimeEnd().equals(oldPo.getEnterTimeEnd())) {
            return false;
        }
        return true;
    }

    /**
     * 商户后台更新课程时，比较两个EpOrganClassPo对象是否相同
     * 班次名称，负责人，价格，折扣，是否限制报名人数，要求报名人数，总计课时
     *
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
//        if (newPo.getEnterRequireNum().intValue()==oldPo.getEnterRequireNum().intValue()) {
//            return false;
//        }
        if (newPo.getCourseNum().intValue()==oldPo.getCourseNum().intValue()) {
            return false;
        }
        return true;
    }

    /**
     * 商户后台更新课程时，比较两个EpOrganClassCatelogPo对象是否相同
     * 班次名称，负责人，价格，折扣，是否限制报名人数，要求报名人数，总计课时
     *
     * @param newPo
     * @param oldPo
     * @return
     */
//    private Boolean isClassCatelogEq4MerchUpdateCourse(EpOrganClassCatalogPo newPo, EpOrganClassCatalogPo oldPo) {
//        if (!newPo.getCatelogTitle().equals(oldPo.getCatelogTitle())) {
//            return false;
//        }
//        if (!newPo.getCatelogIndex().equals(oldPo.getCatelogIndex())) {
//            return false;
//        }
//        if (!newPo.getCatelogDesc().equals(oldPo.getCatelogDesc())) {
//            return false;
//        }
//        if (!newPo.getStartTime().equals(oldPo.getStartTime())) {
//            return false;
//        }
//        return true;
//    }
}
