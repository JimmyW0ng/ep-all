package com.ep.domain.service;

import com.ep.common.tool.BeanTools;
import com.ep.common.tool.CollectionsTools;
import com.ep.domain.constant.BizConstant;
import com.ep.domain.constant.MessageCode;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.OrganAccountBo;
import com.ep.domain.pojo.bo.OrganClassBo;
import com.ep.domain.pojo.bo.OrganClassCommentBo;
import com.ep.domain.pojo.bo.OrganCourseBo;
import com.ep.domain.pojo.dto.OrganCourseDto;
import com.ep.domain.pojo.po.*;
import com.ep.domain.repository.*;
import com.ep.domain.repository.domain.enums.EpOrganClassStatus;
import com.ep.domain.repository.domain.enums.EpOrganCourseCourseStatus;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.jooq.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
    private OrganCourseTagRepository organCourseTagRepository;
    @Autowired
    private OrganCourseTeamRepository organCourseTeamRepository;
    @Autowired
    private OrganCatalogRepository organCatalogRepository;
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
        // 总评论数
        Long totalCommentNum = organClassCommentRepository.countByCourseId(courseId);
        // 封装返回dto
        OrganCourseDto courseDto = new OrganCourseDto(ognCourseBo, classes, team, comments, totalCommentNum);
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
        Long ognId = organCoursePo.getOgnId();
        //课程负责人账户id集合
        Set<Long> ognAccountIds = Sets.newHashSet();
        organClassBos.forEach(organClassBo -> {
            ognAccountIds.add(organClassBo.getOgnAccountId());
            EpOrganClassPo organClassPo = new EpOrganClassPo();
            BeanTools.copyPropertiesIgnoreNull(organClassBo, organClassPo);
            organClassPo.setOgnId(organCoursePo.getOgnId());
            organClassPo.setCourseId(insertOrganCourseId);
            organClassPo.setStatus(EpOrganClassStatus.save);
            //机构课程班次表插入数据
            EpOrganClassPo insertOrganClassPo = organClassRepository.insertNew(organClassPo);
            Long insertOrganClassId = insertOrganClassPo.getId();
            List<EpOrganClassCatalogPo> organClassCatalogPos = organClassBo.getOrganClassCatalogPos();
            for (int i = 0; i < organClassCatalogPos.size(); i++) {
                organClassCatalogPos.get(i).setClassId(insertOrganClassId);
//                organClassCatalogPos.get(i).setCatalogIndex(i + 1);
            }
            //班次课程内容目录表 插入数据
            organClassCatalogRepository.insert(organClassCatalogPos);
        });
        //机构类目表 插入数据
        organCatalogRepository.insert(new EpOrganCatalogPo(null,ognId,organCoursePo.getCourseCatalogId(),null,null,null,null,null,null));

        List<EpOrganCourseTeamPo> organCourseTeamPos = Lists.newArrayList();
        ognAccountIds.forEach(ognAccountId->{
            organCourseTeamPos.add(new EpOrganCourseTeamPo(null,insertOrganCourseId,ognAccountId,null,null,null,null,null,null));
        });
        //机构课程团队信息表插入数据
        organCourseTeamRepository.insert(organCourseTeamPos);

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
        Long ognId = organCoursePo.getOgnId();
        List<Long> classIds=organClassRepository.findClassIdsByCourseId(organCourseId);
        //物理删除班次目录
        organClassCatalogRepository.deletePhysicByClassIds(classIds);
        //物理删除班次
        organClassRepository.deletePhysicByCourseId(organCourseId);
        //物理删除课程标签
        organCourseTagRepository.deletePhysicByCourseId(organCourseId);
        //物理删除 机构类目表 记录
        organCatalogRepository.deletePhysicByOgnId(ognId);
        //物理删除 机构课程团队信息表 记录
        organCourseTeamRepository.deletePhysicByCourseId(organCourseId);
        //课程负责人账户id集合
        Set<Long> ognAccountIds = Sets.newHashSet();

        organClassBos.forEach(organClassBo -> {
            ognAccountIds.add(organClassBo.getOgnAccountId());
            EpOrganClassPo organClassPo = new EpOrganClassPo();
            BeanTools.copyPropertiesIgnoreNull(organClassBo, organClassPo);
            organClassPo.setId(null);
            organClassPo.setOgnId(organCoursePo.getOgnId());
            organClassPo.setCourseId(organCourseId);
            organClassPo.setStatus(EpOrganClassStatus.save);
            //机构课程班次表插入数据
            EpOrganClassPo insertOrganClassPo = organClassRepository.insertNew(organClassPo);
            Long insertOrganClassId = insertOrganClassPo.getId();
            List<EpOrganClassCatalogPo> organClassCatalogPos = organClassBo.getOrganClassCatalogPos();
            for (int i = 0; i < organClassCatalogPos.size(); i++) {
                organClassCatalogPos.get(i).setClassId(insertOrganClassId);
                organClassCatalogPos.get(i).setId(null);
//                organClassCatalogPos.get(i).setCatalogIndex(i + 1);
            }
            //班次课程内容目录表插入数据
            organClassCatalogRepository.insert(organClassCatalogPos);
        });

        //机构类目表 插入数据start
        List<Long> courseCatalogIds = organCourseRepository.findCourseCatalogIdByOgnId(ognId);
        Set<Long> courseCatalogIdsSet = Sets.newHashSet(courseCatalogIds);
        courseCatalogIdsSet.add(organCoursePo.getCourseCatalogId());
        List<EpOrganCatalogPo> organCatalogPos = Lists.newArrayList();
        courseCatalogIdsSet.forEach(courseCatalogId->{
            organCatalogPos.add(new EpOrganCatalogPo(null,ognId,courseCatalogId,null,null,null,null,null,null));
        });
        organCatalogRepository.insert(organCatalogPos);
        //机构类目表 插入数据end

        //机构课程团队信息表插入数据start
        List<EpOrganCourseTeamPo> organCourseTeamPos = Lists.newArrayList();
        ognAccountIds.forEach(ognAccountId->{
            organCourseTeamPos.add(new EpOrganCourseTeamPo(null,organCourseId,ognAccountId,null,null,null,null,null,null));
        });
        organCourseTeamRepository.insert(organCourseTeamPos);
        //机构课程团队信息表插入数据end

        //课程标签表插入数据start
        List<EpOrganCourseTagPo> insertOrganCourseTagPos = Lists.newArrayList();
        constantTagPos.forEach(constantTagPo -> {
            EpOrganCourseTagPo insertOrganCourseTagPo = new EpOrganCourseTagPo();
            insertOrganCourseTagPo.setTagId(constantTagPo.getId());
            insertOrganCourseTagPo.setCourseId(organCourseId);
            insertOrganCourseTagPos.add(insertOrganCourseTagPo);
        });
        organCourseTagRepository.insert(insertOrganCourseTagPos);
        //课程标签表插入数据end
    }

    /**
     * 根据课程id删除课程
     * @param courseId
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteCourseByCourseId(Long courseId,Long ognId){
//        //机构类目表 待插入数据start
//        List<Long> courseCatalogIds = organCourseRepository.findCourseCatalogIdByOgnId(ognId);
//        Set<Long> courseCatalogIdsSet = Sets.newHashSet(courseCatalogIds);
//        courseCatalogIdsSet.add(organCoursePo.getCourseCatalogId());
//        List<EpOrganCatalogPo> organCatalogPos = Lists.newArrayList();
//        courseCatalogIdsSet.forEach(courseCatalogId->{
//            organCatalogPos.add(new EpOrganCatalogPo(null,ognId,courseCatalogId,null,null,null,null,null,null));
//        });
//        organCatalogRepository.insert(organCatalogPos);
//        //机构类目表 待插入数据end
        //课程表 逻辑删除
        organCourseRepository.deleteLogicById(courseId);
        //班次表 逻辑删除
        List<Long> classIds = organClassRepository.deleteLogicByCourseId(courseId);
        //班次目录表 逻辑删除
        organClassCatalogRepository.deleteByClassIds(classIds);
        //课程标签 逻辑删除
        organCourseTagRepository.deleteLogicByCourseId(courseId);
        //机构类目 逻辑删除
//        organCatalogRepository.deleteLogicByOgnId(ognId);
//        //机构类目表 插入数据start
//        List<Long> courseCatalogIds = organCourseRepository.findCourseCatalogIdByOgnId(ognId);
//        Set<Long> courseCatalogIdsSet = Sets.newHashSet(courseCatalogIds);
//        List<EpOrganCatalogPo> organCatalogPos = Lists.newArrayList();
//        courseCatalogIdsSet.forEach(courseCatalogId->{
//            organCatalogPos.add(new EpOrganCatalogPo(null,ognId,courseCatalogId,null,null,null,null,null,null));
//        });
//        organCatalogRepository.insert(organCatalogPos);
//        //机构类目表 插入数据end
    }

    /**
     * 根据id课程上线
     * @param id
     */
    @Transactional(rollbackFor = Exception.class)
    public void onlineById(Long id){
        organCourseRepository.onlineById(id);
        organClassRepository.onlineByCourseId(id);
    }
}
