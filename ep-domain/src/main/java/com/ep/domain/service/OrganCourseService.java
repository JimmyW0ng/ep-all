package com.ep.domain.service;

import com.ep.common.tool.BeanTools;
import com.ep.common.tool.CollectionsTools;
import com.ep.common.tool.StringTools;
import com.ep.domain.constant.BizConstant;
import com.ep.domain.constant.MessageCode;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.*;
import com.ep.domain.pojo.dto.CreateOrganCourseDto;
import com.ep.domain.pojo.dto.OrganCourseDto;
import com.ep.domain.pojo.dto.RectifyOrganCourseDto;
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
    public Optional<EpOrganCoursePo> findById(Long id) {
        return organCourseRepository.findById(id);
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
                // 孩子头像
                Optional<EpFilePo> optAvatar = fileRepository.getOneByBizTypeAndSourceId(BizConstant.FILE_BIZ_TYPE_CODE_CHILD_AVATAR, classCommentBo.getChildId());
                String avatar = optAvatar.isPresent() ? optAvatar.get().getFileUrl() : null;
                classCommentBo.setChildAvatar(avatar);
                // 评论图片
                List<String> pics = fileRepository.getUrlByBizTypeAndSourceId(BizConstant.FILE_BIZ_TYPE_CODE_COURSE_CLASS_COMMENT_PIC, classCommentBo.getId());
                classCommentBo.setPics(pics);
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
     * 涉及ep_organ_course，ep_organ_class，ep_organ_class_catalog，ep_organ_catalog，ep_organ_course_team，ep_organ_course_tag
     */
    @Transactional(rollbackFor = Exception.class)
    public ResultDo createOrganCourseByMerchant(CreateOrganCourseDto dto, Long ognId) {
        log.info("[课程]创建课程开始。课程dto={}。", dto);
        EpOrganCoursePo organCoursePo = dto.getOrganCoursePo();
        if (null == organCoursePo) {
            log.error("[课程]新增课程失败。organCoursePo=null。");
            return ResultDo.build(MessageCode.ERROR_SYSTEM_PARAM_FORMAT);
        }
        organCoursePo.setOgnId(ognId);
        if (!checkPoParams(organCoursePo)) {
            log.error("[课程]新增课程失败。请求参数异常。");
            return ResultDo.build(MessageCode.ERROR_SYSTEM_PARAM_FORMAT);
        }
        List<OrganClassBo> organClassBos = dto.getOrganClassBos();
        if (CollectionsTools.isEmpty(organClassBos)) {
            log.error("[课程]新增课程失败,请求参数异常,organClassBos为空。");
            return ResultDo.build(MessageCode.ERROR_SYSTEM_PARAM_FORMAT);
        }
        List<EpConstantTagPo> constantTagPos = dto.getConstantTagPos();
        if (CollectionsTools.isEmpty(constantTagPos)) {
            log.error("[课程]新增课程失败,请求参数异常,constantTagPos为空。");
            return ResultDo.build(MessageCode.ERROR_SYSTEM_PARAM_FORMAT);
        }
        //获取最低价格start
        BigDecimal priceMin = getCoursePriceMin(organClassBos);
        //获取最低价格end
        //机构课程表插入数据
        organCoursePo.setPrizeMin(priceMin);
        organCoursePo.setCourseStatus(EpOrganCourseCourseStatus.save);
        //内容图片preCode
        List<String> courseDescPicPreCodes = dto.getCourseDescPicPreCodes();
        log.info("[课程]机构课程表ep_organ_course插入数据。{}。", organCoursePo);
        organCourseRepository.insert(organCoursePo);
        Long insertOrganCourseId = organCoursePo.getId();
        //课程负责人账户id集合
        Set<Long> ognAccountIds = Sets.newHashSet();
        organClassBos.forEach(organClassBo -> {
            ognAccountIds.add(organClassBo.getOgnAccountId());
            EpOrganClassPo organClassPo = new EpOrganClassPo();
            BeanTools.copyPropertiesIgnoreNull(organClassBo, organClassPo);
            organClassPo.setOgnId(ognId);
            organClassPo.setCourseId(insertOrganCourseId);
            organClassPo.setStatus(EpOrganClassStatus.save);
            organClassPo.setEnteredNum(0);
            organClassPo.setOrderedNum(0);
            //机构课程班次表插入数据
            log.info("[课程]机构课程班次表ep_organ_class插入数据。{}。", organClassPo);
            organClassRepository.insert(organClassPo);
            Long insertOrganClassId = organClassPo.getId();
            List<EpOrganClassCatalogPo> organClassCatalogPos = organClassBo.getOrganClassCatalogPos();
            if (CollectionsTools.isNotEmpty(organClassCatalogPos)) {
                for (int i = 0; i < organClassCatalogPos.size(); i++) {
                    organClassCatalogPos.get(i).setClassId(insertOrganClassId);
                }
                //班次课程内容目录表 插入数据
                log.info("[课程]班次课程内容目录表ep_organ_class_catalog插入数据。{}。", organClassCatalogPos);
                organClassCatalogRepository.insert(organClassCatalogPos);
            }
        });

        //机构类目表 插入数据
        //是否存在机构id和课程类目id组合的重复,否那么插入
        if (!organCatalogRepository.existOgnAndCatalog(ognId, organCoursePo.getCourseCatalogId())) {
            log.info("[课程]班次课程内容目录表ep_organ_catalog插入数据。ognId={},courseCatalogId={}。", organCoursePo.getCourseCatalogId());
            organCatalogRepository.insert(new EpOrganCatalogPo(null, ognId, organCoursePo.getCourseCatalogId(), null, null, null, null, null, null));
        }

        List<EpOrganCourseTeamPo> organCourseTeamPos = Lists.newArrayList();
        if (CollectionsTools.isNotEmpty(ognAccountIds)) {
            ognAccountIds.forEach(ognAccountId -> {
                organCourseTeamPos.add(new EpOrganCourseTeamPo(null, insertOrganCourseId, ognAccountId, null, null, null, null, null, null));
            });
        }
        //机构课程团队信息表插入数据
        log.info("[课程]机构课程团队信息表ep_organ_course_team插入数据。{}。", organCourseTeamPos);
        organCourseTeamRepository.insert(organCourseTeamPos);

        List<EpOrganCourseTagPo> insertOrganCourseTagPos = Lists.newArrayList();
        constantTagPos.forEach(constantTagPo -> {
            EpOrganCourseTagPo insertOrganCourseTagPo = new EpOrganCourseTagPo();
            insertOrganCourseTagPo.setTagId(constantTagPo.getId());
            insertOrganCourseTagPo.setCourseId(insertOrganCourseId);
            insertOrganCourseTagPos.add(insertOrganCourseTagPo);
        });

        //课程标签表插入数据
        log.info("[课程]课程标签表ep_organ_course_tag插入数据。{}。", insertOrganCourseTagPos);
        organCourseTagRepository.insert(insertOrganCourseTagPos);
        //课程主图
        if (StringTools.isNotBlank(dto.getMainpicUrlPreCode())) {
            log.info("[课程]文件表ep_file更新数据。biz_type_code={},source_id={}。", dto.getMainpicUrlPreCode(), insertOrganCourseTagPos);
            fileRepository.updateSourceIdByPreCode(dto.getMainpicUrlPreCode(), insertOrganCourseId);
        }
        //课程内容图片
        if (CollectionsTools.isNotEmpty(courseDescPicPreCodes)) {
            courseDescPicPreCodes.forEach(proCode -> {
                fileRepository.updateSourceIdByPreCode(proCode, insertOrganCourseId);
            });
            log.info("[课程]文件表ep_file更新数据。内容图片courseDescPicPreCodes={}。", courseDescPicPreCodes);
        }
        log.info("[课程]创建课程成功。课程id={}。", insertOrganCourseId);
        return ResultDo.build();
    }

    /**
     * 商户后台更新课程
     */
    @Transactional(rollbackFor = Exception.class)
    public ResultDo updateOrganCourseByMerchant(CreateOrganCourseDto dto, Long ognId) {
        log.info("[课程]修改课程开始。课程dto={}。", dto);
        //课程对象
        EpOrganCoursePo organCoursePo = dto.getOrganCoursePo();
        if (null == organCoursePo) {
            log.error("[课程]修改课程失败。organCoursePo=null。");
            return ResultDo.build(MessageCode.ERROR_SYSTEM_PARAM_FORMAT);
        }
        organCoursePo.setOgnId(ognId);
        if (null == organCoursePo.getId() || !checkPoParams(organCoursePo)) {
            log.error("[课程]修改课程失败。接受参数异常。");
            return ResultDo.build(MessageCode.ERROR_SYSTEM_PARAM_FORMAT);
        }
        Optional<EpOrganCoursePo> optional = organCourseRepository.findById(organCoursePo.getId());
        if (!optional.isPresent()) {
            log.error("[课程]修改课程失败。该课程不存在。");
            return ResultDo.build(MessageCode.ERROR_COURSE_NOT_EXIST);
        }
        //班次
        List<OrganClassBo> organClassBos = dto.getOrganClassBos();
        if (CollectionsTools.isEmpty(organClassBos)) {
            log.error("[课程]新增课程失败,请求参数异常,organClassBos为空。");
            return ResultDo.build(MessageCode.ERROR_SYSTEM_PARAM_FORMAT);
        }
        //标签
        List<EpConstantTagPo> constantTagPos = dto.getConstantTagPos();
        if (CollectionsTools.isEmpty(constantTagPos)) {
            log.error("[课程]新增课程失败,请求参数异常,constantTagPos为空。");
            return ResultDo.build(MessageCode.ERROR_SYSTEM_PARAM_FORMAT);
        }
        //内容图片preCode
        List<String> courseDescPicPreCodes = dto.getCourseDescPicPreCodes();
        //获取最低价格start
        BigDecimal priceMin = getCoursePriceMin(organClassBos);

        //获取最低价格end
        organCoursePo.setPrizeMin(priceMin);
        //机构课程表更新数据
        log.info("[课程]机构课程表ep_organ_course修改数据。{}。", organCoursePo);
        organCourseRepository.updateByIdLock(organCoursePo);
        //课程id
        Long organCourseId = organCoursePo.getId();

        List<Long> classIds = organClassRepository.findClassIdsByCourseId(organCourseId);
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
            organClassPo.setOgnId(ognId);
            organClassPo.setCourseId(organCourseId);
            organClassPo.setStatus(EpOrganClassStatus.save);
            organClassPo.setEnteredNum(0);
            organClassPo.setOrderedNum(0);
            //机构课程班次表插入数据
            log.info("[课程]机构课程班次表ep_organ_class插入数据。{}。", organClassPo);
            organClassRepository.insert(organClassPo);
            Long insertOrganClassId = organClassPo.getId();
            List<EpOrganClassCatalogPo> organClassCatalogPos = organClassBo.getOrganClassCatalogPos();
            for (int i = 0; i < organClassCatalogPos.size(); i++) {
                organClassCatalogPos.get(i).setClassId(insertOrganClassId);
                organClassCatalogPos.get(i).setId(null);
            }
            //班次课程内容目录表插入数据
            log.info("[课程]班次课程内容目录表ep_organ_class_catalog插入数据。{}。", organClassCatalogPos);
            organClassCatalogRepository.insert(organClassCatalogPos);
        });

        //机构类目表 插入数据start
        List<Long> courseCatalogIds = organCourseRepository.findCourseCatalogIdByOgnId(ognId);
        //课程类目去重
        Set<Long> courseCatalogIdsSet = Sets.newHashSet(courseCatalogIds);
        courseCatalogIdsSet.add(organCoursePo.getCourseCatalogId());
        List<EpOrganCatalogPo> organCatalogPos = Lists.newArrayList();
        courseCatalogIdsSet.forEach(courseCatalogId -> {
            organCatalogPos.add(new EpOrganCatalogPo(null, ognId, courseCatalogId, null, null, null, null, null, null));
        });
        log.info("[课程]班次课程内容目录表ep_organ_catalog插入数据。{}。", organCatalogPos);
        organCatalogRepository.insert(organCatalogPos);
        //机构类目表 插入数据end

        //机构课程团队信息表插入数据start
        List<EpOrganCourseTeamPo> organCourseTeamPos = Lists.newArrayList();
        if (CollectionsTools.isNotEmpty(ognAccountIds)) {
            ognAccountIds.forEach(ognAccountId -> {
                organCourseTeamPos.add(new EpOrganCourseTeamPo(null, organCourseId, ognAccountId, null, null, null, null, null, null));
            });
        }
        log.info("[课程]机构课程团队信息表ep_organ_course_team插入数据。{}。", organCourseTeamPos);
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
        log.info("[课程]课程标签表ep_organ_course_tag插入数据。{}。", insertOrganCourseTagPos);
        organCourseTagRepository.insert(insertOrganCourseTagPos);
        //课程标签表插入数据end

        //主图
        if (StringTools.isNotBlank(dto.getMainpicUrlPreCode())) {
            fileRepository.deleteLogicByBizTypeAndSourceId(BizConstant.FILE_BIZ_TYPE_CODE_COURSE_MAIN_PIC, organCourseId);
            log.info("[课程]文件表ep_file更新数据。biz_type_code={},source_id={}。", dto.getMainpicUrlPreCode(), insertOrganCourseTagPos);
            fileRepository.updateSourceIdByPreCode(dto.getMainpicUrlPreCode(), organCourseId);
        }
        //课程内容图片
        if (CollectionsTools.isNotEmpty(courseDescPicPreCodes)) {
            courseDescPicPreCodes.forEach(proCode -> {
                fileRepository.updateSourceIdByPreCode(proCode, organCourseId);
            });
            List<String> oldProCodes = fileRepository.getPreCodeByBizTypeAndSourceId(BizConstant.FILE_BIZ_TYPE_CODE_COURSE_DESC_PIC, organCourseId);
            //待删除，差集oldProCodes里有，courseDescPicPreCodes里没有
            Set<String> diffDel = Sets.difference(new HashSet<String>(oldProCodes), new HashSet<String>(courseDescPicPreCodes));
            fileRepository.deleteLogicByPreCodes(new ArrayList<String>(diffDel));
            log.info("[课程]文件表ep_file更新数据。原内容图片oldProCodes={},新内容图片courseDescPicPreCodes={}。", oldProCodes, courseDescPicPreCodes);
        }

        log.info("[课程]修改课程成功。课程id={}。", organCourseId);
        return ResultDo.build();
    }

    /**
     * 商户后台紧急修改课程
     */
    @Transactional(rollbackFor = Exception.class)
    public ResultDo rectifyOrganCourseByMerchant(RectifyOrganCourseDto dto) {
        log.info("[课程]紧急修改课程开始。课程dto={}。", dto);
        EpOrganCoursePo organCoursePo = dto.getOrganCoursePo();
        if (null == organCoursePo) {
            log.error("[课程]紧急修改课程失败。该课程不存在。");
            return ResultDo.build(MessageCode.ERROR_COURSE_NOT_EXIST);
        }
        if (null == organCoursePo.getId()) {
            log.error("[课程]紧急修改课程失败。该课程不存在。");
            return ResultDo.build(MessageCode.ERROR_COURSE_NOT_EXIST);
        }
        Optional<EpOrganCoursePo> optional = organCourseRepository.findById(organCoursePo.getId());
        if (!optional.isPresent()) {
            log.error("[课程]紧急修改课程失败。该课程不存在。");
            return ResultDo.build(MessageCode.ERROR_COURSE_NOT_EXIST);
        }


        //标签
        List<EpConstantTagPo> constantTagPos = dto.getConstantTagPos();
        //内容图片preCode
        List<String> courseDescPicPreCodes = dto.getCourseDescPicPreCodes();

        //机构课程表更新数据
        log.info("[课程紧急修改]机构课程表ep_organ_course修改数据。{}。", organCoursePo);
        organCourseRepository.rectifyByIdLock(organCoursePo);
        //班次,仅根据classCatelogId更新班次目录
        List<RectifyOrganClassCatalogBo> rectifyOrganClassCatalogBos = dto.getRectifyOrganClassCatalogBos();
        if (CollectionsTools.isNotEmpty(rectifyOrganClassCatalogBos)) {
            rectifyOrganClassCatalogBos.forEach(bo -> {
                if (bo.getRectifyFlag()) {
                    organClassCatalogRepository.rectifyByRectifyCourse(bo);
                }

            });
        }

        //课程id
        Long organCourseId = organCoursePo.getId();

        //物理删除课程标签
        organCourseTagRepository.deletePhysicByCourseId(organCourseId);
        //课程标签表插入数据start
        List<EpOrganCourseTagPo> insertOrganCourseTagPos = Lists.newArrayList();
        if (CollectionsTools.isNotEmpty(constantTagPos)) {
            constantTagPos.forEach(constantTagPo -> {
                EpOrganCourseTagPo insertOrganCourseTagPo = new EpOrganCourseTagPo();
                insertOrganCourseTagPo.setTagId(constantTagPo.getId());
                insertOrganCourseTagPo.setCourseId(organCourseId);
                insertOrganCourseTagPos.add(insertOrganCourseTagPo);
            });
        }
        log.info("[课程]课程标签表ep_organ_course_tag插入数据。{}。", insertOrganCourseTagPos);
        organCourseTagRepository.insert(insertOrganCourseTagPos);
        //课程标签表插入数据end

        //主图
        if (StringTools.isNotBlank(dto.getMainpicUrlPreCode())) {
            fileRepository.deleteLogicByBizTypeAndSourceId(BizConstant.FILE_BIZ_TYPE_CODE_COURSE_MAIN_PIC, organCourseId);
            log.info("[课程]文件表ep_file更新数据。biz_type_code={},source_id={}。", dto.getMainpicUrlPreCode(), insertOrganCourseTagPos);
            fileRepository.updateSourceIdByPreCode(dto.getMainpicUrlPreCode(), organCourseId);
        }
        //课程内容图片
        if (CollectionsTools.isNotEmpty(courseDescPicPreCodes)) {
            courseDescPicPreCodes.forEach(proCode -> {
                fileRepository.updateSourceIdByPreCode(proCode, organCourseId);
            });
            List<String> oldProCodes = fileRepository.getPreCodeByBizTypeAndSourceId(BizConstant.FILE_BIZ_TYPE_CODE_COURSE_DESC_PIC, organCourseId);
            //待删除，差集oldProCodes里有，courseDescPicPreCodes里没有
            Set<String> diffDel = Sets.difference(new HashSet<String>(oldProCodes), new HashSet<String>(courseDescPicPreCodes));
            fileRepository.deleteLogicByPreCodes(new ArrayList<String>(diffDel));
            log.info("[课程]文件表ep_file更新数据。原内容图片oldProCodes={},新内容图片courseDescPicPreCodes={}。", oldProCodes, courseDescPicPreCodes);
        }

        log.info("[课程]紧急修改课程成功。课程id={}。", organCourseId);
        return ResultDo.build();
    }

    /**
     * 根据课程id删除课程
     * 涉及ep_organ_course，ep_organ_class，ep_organ_class_catalog，ep_organ_catalog，ep_organ_course_team，ep_organ_course_tag
     * ep_file
     *
     * @param courseId
     */
    @Transactional(rollbackFor = Exception.class)
    public ResultDo deleteCourseByCourseId(Long courseId, Long ognId) {
        log.info("[课程]删除课程开始。课程id={},机构id={}。", courseId, ognId);
        Optional<EpOrganCoursePo> optional = organCourseRepository.findById(courseId);
        if (!optional.isPresent()) {
            log.error("[课程]删除课程失败，该课程不存在。课程id={}。", courseId);
            ResultDo.build(MessageCode.ERROR_COURSE_NOT_EXIST);
        }
        //课程表 逻辑删除
        organCourseRepository.deleteLogicById(courseId);
        //班次表 逻辑删除
        List<Long> classIds = organClassRepository.deleteLogicByCourseId(courseId);
        //班次目录表 逻辑删除
        organClassCatalogRepository.deleteByClassIds(classIds);
        //课程标签 逻辑删除
        organCourseTagRepository.deletePhysicByCourseId(courseId);
        //机构类目 逻辑删除
        Long courseCatalogId = optional.get().getCourseCatalogId();
        long count = organCourseRepository.countByCourseCatalogIdAndOgnId(optional.get().getCourseCatalogId(), ognId);
        if (count == BizConstant.DB_NUM_ZERO) {
            organCatalogRepository.deletePhysicByOgnIdAndCatalogId(ognId, courseCatalogId);
        }
        //主图 逻辑删除
        fileRepository.deleteLogicByBizTypeAndSourceId(BizConstant.FILE_BIZ_TYPE_CODE_COURSE_MAIN_PIC, courseId);
        //内容图片 逻辑删除
        fileRepository.deleteLogicByBizTypeAndSourceId(BizConstant.FILE_BIZ_TYPE_CODE_COURSE_DESC_PIC, courseId);
        organCourseTeamRepository.deletePhysicByCourseId(courseId);
        log.info("[课程]删除课程成功。课程id={}。", courseId);
        return ResultDo.build();
    }

    /**
     * 课程上线
     *
     * @param currentUser
     * @param id
     */
    @Transactional(rollbackFor = Exception.class)
    public ResultDo onlineById(EpSystemUserPo currentUser, Long id) {
        log.info("机构上线课程, sysUserId={}, courseId={}", currentUser.getId(), id);
        EpOrganCoursePo coursePo = organCourseRepository.getById(id);
        if (coursePo == null || coursePo.getDelFlag()) {
            log.error("课程不存在, courseId={}", id);
            return ResultDo.build(MessageCode.ERROR_COURSE_NOT_EXIST);
        }
        if (!coursePo.getCourseStatus().equals(EpOrganCourseCourseStatus.save)) {
            log.error("课程不是已保存状态, courseId={}, status={}", id, coursePo.getCourseStatus());
            return ResultDo.build(MessageCode.ERROR_COURSE_NOT_SAVE);
        }
        if (!coursePo.getOgnId().equals(currentUser.getOgnId())) {
            log.error("课程与当前操作机构不匹配, courseId={}, courseOgnId={}, userOgnId={}", id, coursePo.getOgnId(), currentUser.getOgnId());
            return ResultDo.build(MessageCode.ERROR_COURSE_OGN_NOT_MATCH);
        }
        // 上线
        int num = organCourseRepository.onlineById(id);
        if (num == BizConstant.DB_NUM_ONE) {
            // 上线班次
            organClassRepository.onlineByCourseId(id);
        }
        return ResultDo.build();
    }

    /**
     * 根据机构id获取记录
     *
     * @param ognId
     * @return
     */
    public List<EpOrganCoursePo> findByOgnId(Long ognId) {
        return organCourseRepository.findByOgnId(ognId);
    }

    /**
     * 根据机构id和状态获取记录
     *
     * @param ognId
     * @return
     */
    public List<EpOrganCoursePo> findByOgnIdAndStatus(Long ognId, EpOrganCourseCourseStatus status) {
        return organCourseRepository.findByOgnIdAndStatus(ognId, status);
    }

    /**
     * 机构下线，该机构下的课程下线
     *
     * @param ognId
     */
    public void updateCourseByOfflineOgn(Long ognId) {
        organCourseRepository.updateCourseByOfflineOgn(ognId);
    }

    /**
     * 根据sourceId获取课程主图
     *
     * @param sourceId
     * @return
     */
    public Optional<EpFilePo> getCourseMainpic(Long sourceId) {
        return fileRepository.getOneByBizTypeAndSourceId(BizConstant.FILE_BIZ_TYPE_CODE_COURSE_MAIN_PIC, sourceId);
    }

    /**
     * 获取班次最低价格
     *
     * @param organClassBos
     * @return
     */
    private BigDecimal getCoursePriceMin(List<OrganClassBo> organClassBos) {
        if (organClassBos.size() == BizConstant.DB_NUM_ONE) {
            return organClassBos.get(0).getClassPrize();
        }
        BigDecimal priceMin;
        if (CollectionsTools.isNotEmpty(organClassBos)) {
            BigDecimal[] priceArr = new BigDecimal[organClassBos.size()];
            for (int i = 0; i < priceArr.length; i++) {
                priceArr[i] = organClassBos.get(i).getClassPrize();
            }
            int index = BizConstant.DB_NUM_ZERO;
            for (int j = index + 1; j < priceArr.length; j++) {
                if (priceArr[j].compareTo(priceArr[index]) == -1) {
                    BigDecimal temp = priceArr[j];
                    priceArr[j] = priceArr[index];
                    priceArr[index] = temp;
                }
            }
            priceMin = priceArr[index];
        } else {
            priceMin = BigDecimal.ZERO;
        }
        return priceMin;
    }

    /**
     * 校验po对象属性
     *
     * @param po
     * @return
     */
    private boolean checkPoParams(EpOrganCoursePo po) {
        if (null == po.getOgnId()) {
            log.error("接受参数异常，ognId=null。");
            return false;
        }
        if (null == po.getCourseType()) {
            log.error("接受参数异常，courseType=null。");
            return false;
        }
        if (null == po.getCourseCatalogId()) {
            log.error("接受参数异常，courseCatalogId=null。");
            return false;
        }
        if (null == po.getCourseName()) {
            log.error("接受参数异常，courseName=null。");
            return false;
        }
        if (null == po.getCourseIntroduce()) {
            log.error("接受参数异常，courseIntroduce=null。");
            return false;
        }
        if (null == po.getCourseContent()) {
            log.error("接受参数异常，courseContent=null。");
            return false;
        }

        if (null == po.getCourseAddress()) {
            log.error("接受参数异常，courseAddress=null。");
            return false;
        }
        if (null == po.getOnlineTime()) {
            log.error("接受参数异常，onlineTime=null。");
            return false;
        }
        if (null == po.getEnterTimeStart()) {
            log.error("接受参数异常，enterTimeStart=null。");
            return false;
        }
        return true;
    }
}
