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
import com.ep.domain.repository.domain.enums.EpOrganClassType;
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
 * @Description: 机构产品服务类
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
    private OrganClassScheduleRepository organClassScheduleRepository;
    @Autowired
    private OrganCatalogRepository organCatalogRepository;
    @Autowired
    private OrganConfigRepository organConfigRepository;


    /**
     * 根据id获取机构产品
     *
     * @param id
     * @return
     */
    public Optional<EpOrganCoursePo> findById(Long id) {
        return organCourseRepository.findById(id);
    }

    /**
     * 前提－产品明细
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
        ognCourseBo.setVipName(organPo.getVipName());
        // 获取班次信息
        List<OrganClassEnterBo> classes = organClassRepository.getClassEnterInfoByCourseId(courseId);
        Boolean canEnterFlag = false;
        for (OrganClassEnterBo bo : classes) {
            if (bo.getCanEnterFlag()) {
                canEnterFlag = true;
                break;
            }
        }
        ognCourseBo.setCanEnterFlag(canEnterFlag);
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
     * 查询机构产品列表-分页
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
     * 后台机构产品分页列表
     *
     * @param pageable
     * @param condition
     * @return
     */
    public Page<OrganCourseBo> findbyPageAndCondition(Pageable pageable, Collection<? extends Condition> condition) {
        return organCourseRepository.findbyPageAndCondition(pageable, condition);
    }

    /**
     * 商户后台创建产品
     * 涉及ep_organ_course，ep_organ_class，ep_organ_class_catalog，ep_organ_course_team，ep_organ_course_tag
     */
    @Transactional(rollbackFor = Exception.class)
    public ResultDo createOrganCourseByMerchant(CreateOrganCourseDto dto, Long ognId) {
        log.info("[产品]创建产品开始。产品dto={}。", dto);
        EpOrganCoursePo organCoursePo = dto.getOrganCoursePo();
        if (null == organCoursePo) {
            log.error("[产品]新增产品失败。organCoursePo=null。");
            return ResultDo.build(MessageCode.ERROR_SYSTEM_PARAM_FORMAT);
        }
        organCoursePo.setOgnId(ognId);
        if (!checkPoParams(organCoursePo)) {
            log.error("[产品]新增产品失败。请求参数异常。");
            return ResultDo.build(MessageCode.ERROR_SYSTEM_PARAM_FORMAT);
        }
        List<OrganClassBo> organClassBos = dto.getOrganClassBos();
        if (CollectionsTools.isEmpty(organClassBos)) {
            log.error("[产品]新增产品失败,请求参数异常,organClassBos为空。");
            return ResultDo.build(MessageCode.ERROR_SYSTEM_PARAM_FORMAT);
        }
        List<EpConstantTagPo> constantTagPos = dto.getConstantTagPos();

        //获取最低价格start
        BigDecimal priceMin = getCoursePriceMin(organClassBos);
        //获取最低价格end
        //机构产品表插入数据
        organCoursePo.setPrizeMin(priceMin);
        organCoursePo.setCourseStatus(EpOrganCourseCourseStatus.save);
        //内容图片preCode
        List<String> courseDescPicPreCodes = dto.getCourseDescPicPreCodes();
        log.info("[产品]机构产品表ep_organ_course插入数据。{}。", organCoursePo);
        organCourseRepository.insert(organCoursePo);
        Long insertOrganCourseId = organCoursePo.getId();
        //产品负责人账户id集合
        Set<Long> ognAccountIds = Sets.newHashSet();
        organClassBos.forEach(organClassBo -> {
            ognAccountIds.add(organClassBo.getOgnAccountId());
            EpOrganClassPo organClassPo = new EpOrganClassPo();
            BeanTools.copyPropertiesIgnoreNull(organClassBo, organClassPo);
            organClassPo.setOgnId(ognId);
            organClassPo.setCourseId(insertOrganCourseId);
            organClassPo.setStatus(EpOrganClassStatus.save);
            organClassPo.setEnteredNum(BizConstant.ORGAN_CLASS_INIT_ENTERED_NUM);
            organClassPo.setOrderedNum(BizConstant.ORGAN_CLASS_INIT_ORDERED_NUM);
            //机构产品班次表插入数据
            log.info("[产品]机构产品班次表ep_organ_class插入数据。{}。", organClassPo);
            organClassRepository.insert(organClassPo);
            Long insertOrganClassId = organClassPo.getId();
            List<EpOrganClassCatalogPo> organClassCatalogPos = organClassBo.getOrganClassCatalogPos();
            if (organClassBo.getType().equals(EpOrganClassType.normal) && CollectionsTools.isNotEmpty(organClassCatalogPos)) {
                for (int i = 0; i < organClassCatalogPos.size(); i++) {
                    organClassCatalogPos.get(i).setClassId(insertOrganClassId);
                }
                //班次产品班次目录表 插入数据
                log.info("[产品]班次产品内容目录表ep_organ_class_catalog插入数据。{}。", organClassCatalogPos);
                organClassCatalogRepository.insert(organClassCatalogPos);
            }
        });
        //机构产品团队信息list
        List<EpOrganCourseTeamPo> organCourseTeamPos = Lists.newArrayList();
        if (CollectionsTools.isNotEmpty(ognAccountIds)) {
            ognAccountIds.forEach(ognAccountId -> {
                organCourseTeamPos.add(new EpOrganCourseTeamPo(null, insertOrganCourseId, ognAccountId, null, null, null, null, null, null));
            });
        }
        //机构产品团队信息表插入数据
        log.info("[产品]机构产品团队信息表ep_organ_course_team插入数据。{}。", organCourseTeamPos);
        organCourseTeamRepository.insert(organCourseTeamPos);
        //产品标签list
        List<EpOrganCourseTagPo> insertOrganCourseTagPos = Lists.newArrayList();
        if (CollectionsTools.isNotEmpty(constantTagPos)) {
            constantTagPos.forEach(constantTagPo -> {
                EpOrganCourseTagPo insertOrganCourseTagPo = new EpOrganCourseTagPo();
                insertOrganCourseTagPo.setTagId(constantTagPo.getId());
                insertOrganCourseTagPo.setCourseId(insertOrganCourseId);
                insertOrganCourseTagPos.add(insertOrganCourseTagPo);
            });
            //产品标签表插入数据
            log.info("[产品]产品标签表ep_organ_course_tag插入数据。{}。", insertOrganCourseTagPos);
            organCourseTagRepository.insert(insertOrganCourseTagPos);
        }
        //产品主图
        if (StringTools.isNotBlank(dto.getMainpicUrlPreCode())) {
            log.info("[产品]文件表ep_file更新数据。biz_type_code={},source_id={}。", dto.getMainpicUrlPreCode(), insertOrganCourseTagPos);
            fileRepository.updateSourceIdByPreCode(dto.getMainpicUrlPreCode(), insertOrganCourseId);
        }
        //产品内容图片
        if (CollectionsTools.isNotEmpty(courseDescPicPreCodes)) {
            courseDescPicPreCodes.forEach(proCode -> {
                fileRepository.updateSourceIdByPreCode(proCode, insertOrganCourseId);
            });
            log.info("[产品]文件表ep_file更新数据。内容图片courseDescPicPreCodes={}。", courseDescPicPreCodes);
        }
        log.info("[产品]创建产品成功。产品id={}。", insertOrganCourseId);
        return ResultDo.build();
    }

    /**
     * 商户后台更新产品
     * 涉及ep_organ_course，ep_organ_class，ep_organ_class_catalog，ep_organ_course_team，ep_organ_course_tag
     */
    @Transactional(rollbackFor = Exception.class)
    public ResultDo updateOrganCourseByMerchant(CreateOrganCourseDto dto, Long ognId) {
        log.info("[产品]修改产品开始。产品dto={}。", dto);
        //产品对象
        EpOrganCoursePo organCoursePo = dto.getOrganCoursePo();
        if (null == organCoursePo) {
            log.error("[产品]修改产品失败。organCoursePo=null。");
            return ResultDo.build(MessageCode.ERROR_SYSTEM_PARAM_FORMAT);
        }
        organCoursePo.setOgnId(ognId);
        if (null == organCoursePo.getId() || !checkPoParams(organCoursePo)) {
            log.error("[产品]修改产品失败。接受参数异常。");
            return ResultDo.build(MessageCode.ERROR_SYSTEM_PARAM_FORMAT);
        }
        Optional<EpOrganCoursePo> optional = organCourseRepository.findById(organCoursePo.getId());
        if (!optional.isPresent()) {
            log.error("[产品]修改产品失败。该产品不存在。");
            return ResultDo.build(MessageCode.ERROR_COURSE_NOT_EXIST);
        }
        //班次
        List<OrganClassBo> organClassBos = dto.getOrganClassBos();
        if (CollectionsTools.isEmpty(organClassBos)) {
            log.error("[产品]修改产品失败,请求参数异常,organClassBos为空。");
            return ResultDo.build(MessageCode.ERROR_SYSTEM_PARAM_FORMAT);
        }
        //标签
        List<EpConstantTagPo> constantTagPos = dto.getConstantTagPos();
        //内容图片preCode
        List<String> courseDescPicPreCodes = dto.getCourseDescPicPreCodes();
        //获取最低价格start
        BigDecimal priceMin = getCoursePriceMin(organClassBos);
        //获取最低价格end
        organCoursePo.setPrizeMin(priceMin);
        //机构产品表更新数据
        log.info("[产品]机构产品表ep_organ_course修改数据。{}。", organCoursePo);
        //加行级锁
        EpOrganCourseCourseStatus courseStatus = organCourseRepository.getByIdWithLock(organCoursePo.getId()).getCourseStatus();
        //更新产品po
        if (courseStatus.equals(EpOrganCourseCourseStatus.save)) {
            organCourseRepository.updateById(organCoursePo);
        } else {
            log.error("[产品]紧急修改产品失败。该产品状态不为{}。", EpOrganCourseCourseStatus.save);
            return ResultDo.build(MessageCode.ERROR_SYSTEM_PARAM_FORMAT);
        }
        //产品id
        Long organCourseId = organCoursePo.getId();
        //该产品下班次id
        List<Long> classIds = organClassRepository.findClassIdsByCourseId(organCourseId);
        //物理删除班次目录
        organClassCatalogRepository.deletePhysicByClassIds(classIds);
        //物理删除班次
        organClassRepository.deletePhysicByCourseId(organCourseId);
        //物理删除产品标签
        organCourseTagRepository.deletePhysicByCourseId(organCourseId);
        //物理删除 机构产品团队信息表 记录
        organCourseTeamRepository.deletePhysicByCourseId(organCourseId);
        //产品负责人账户id集合
        Set<Long> ognAccountIds = Sets.newHashSet();
        organClassBos.forEach(organClassBo -> {
            ognAccountIds.add(organClassBo.getOgnAccountId());
            EpOrganClassPo organClassPo = new EpOrganClassPo();
            BeanTools.copyPropertiesIgnoreNull(organClassBo, organClassPo);
            organClassPo.setId(null);
            organClassPo.setOgnId(ognId);
            organClassPo.setCourseId(organCourseId);
            organClassPo.setStatus(EpOrganClassStatus.save);
            organClassPo.setEnteredNum(BizConstant.ORGAN_CLASS_INIT_ENTERED_NUM);
            organClassPo.setOrderedNum(BizConstant.ORGAN_CLASS_INIT_ORDERED_NUM);
            //机构产品班次表插入数据
            log.info("[产品]机构产品班次表ep_organ_class插入数据。{}。", organClassPo);
            organClassRepository.insert(organClassPo);
            Long insertOrganClassId = organClassPo.getId();
            List<EpOrganClassCatalogPo> organClassCatalogPos = organClassBo.getOrganClassCatalogPos();
            //正常类型的班次有班次目录
            if (organClassBo.getType().equals(EpOrganClassType.normal) && CollectionsTools.isNotEmpty(organClassCatalogPos)) {
                for (int i = 0; i < organClassCatalogPos.size(); i++) {
                    organClassCatalogPos.get(i).setClassId(insertOrganClassId);
                    organClassCatalogPos.get(i).setId(null);
                }
                //班次产品班次目录表插入数据
                log.info("[产品]班次产品内容目录表ep_organ_class_catalog插入数据。{}。", organClassCatalogPos);
                organClassCatalogRepository.insert(organClassCatalogPos);
            }
        });
        //机构产品团队信息表插入数据start
        List<EpOrganCourseTeamPo> organCourseTeamPos = Lists.newArrayList();
        if (CollectionsTools.isNotEmpty(ognAccountIds)) {
            ognAccountIds.forEach(ognAccountId -> {
                organCourseTeamPos.add(new EpOrganCourseTeamPo(null, organCourseId, ognAccountId, null, null, null, null, null, null));
            });
        }
        log.info("[产品]机构产品团队信息表ep_organ_course_team插入数据。{}。", organCourseTeamPos);
        organCourseTeamRepository.insert(organCourseTeamPos);
        //机构产品团队信息表插入数据end
        //产品标签表插入数据start
        List<EpOrganCourseTagPo> insertOrganCourseTagPos = Lists.newArrayList();
        if (CollectionsTools.isNotEmpty(constantTagPos)) {
            constantTagPos.forEach(constantTagPo -> {
                EpOrganCourseTagPo insertOrganCourseTagPo = new EpOrganCourseTagPo();
                insertOrganCourseTagPo.setTagId(constantTagPo.getId());
                insertOrganCourseTagPo.setCourseId(organCourseId);
                insertOrganCourseTagPos.add(insertOrganCourseTagPo);
            });
            log.info("[产品]产品标签表ep_organ_course_tag插入数据。{}。", insertOrganCourseTagPos);
            organCourseTagRepository.insert(insertOrganCourseTagPos);
            //产品标签表插入数据end
        }
        //主图
        if (StringTools.isNotBlank(dto.getMainpicUrlPreCode())) {
            fileRepository.deleteLogicByBizTypeAndSourceId(BizConstant.FILE_BIZ_TYPE_CODE_COURSE_MAIN_PIC, organCourseId);
            log.info("[产品]文件表ep_file更新数据。biz_type_code={},source_id={}。", dto.getMainpicUrlPreCode(), insertOrganCourseTagPos);
            fileRepository.updateSourceIdByPreCode(dto.getMainpicUrlPreCode(), organCourseId);
        }
        //产品内容图片
        if (CollectionsTools.isNotEmpty(courseDescPicPreCodes)) {
            courseDescPicPreCodes.forEach(proCode -> {
                fileRepository.updateSourceIdByPreCode(proCode, organCourseId);
            });
            List<String> oldProCodes = fileRepository.getPreCodeByBizTypeAndSourceId(BizConstant.FILE_BIZ_TYPE_CODE_COURSE_DESC_PIC, organCourseId);
            //待删除，差集oldProCodes里有，courseDescPicPreCodes里没有
            Set<String> diffDel = Sets.difference(new HashSet<String>(oldProCodes), new HashSet<String>(courseDescPicPreCodes));
            fileRepository.deleteLogicByPreCodes(new ArrayList<String>(diffDel));
            log.info("[产品]文件表ep_file更新数据。原内容图片oldProCodes={},新内容图片courseDescPicPreCodes={}。", oldProCodes, courseDescPicPreCodes);
        }
        log.info("[产品]修改产品成功。产品id={}。", organCourseId);
        return ResultDo.build();
    }

    /**
     * 商户后台紧急修改产品
     */
    @Transactional(rollbackFor = Exception.class)
    public ResultDo rectifyOrganCourseByMerchant(RectifyOrganCourseDto dto) {
        log.info("[产品]紧急修改产品开始。产品dto={}。", dto);
        EpOrganCoursePo organCoursePo = dto.getOrganCoursePo();
        Long ognId = organCoursePo.getOgnId();
        if (null == organCoursePo) {
            log.error("[产品]紧急修改产品失败。该产品不存在。");
            return ResultDo.build(MessageCode.ERROR_COURSE_NOT_EXIST);
        }
        if (null == organCoursePo.getId()) {
            log.error("[产品]紧急修改产品失败。该产品不存在。");
            return ResultDo.build(MessageCode.ERROR_COURSE_NOT_EXIST);
        }
        Optional<EpOrganCoursePo> optional = organCourseRepository.findById(organCoursePo.getId());
        if (!optional.isPresent()) {
            log.error("[产品]紧急修改产品失败。该产品不存在。");
            return ResultDo.build(MessageCode.ERROR_COURSE_NOT_EXIST);
        }
        //标签
        List<EpConstantTagPo> constantTagPos = dto.getConstantTagPos();
        //内容图片preCode
        List<String> courseDescPicPreCodes = dto.getCourseDescPicPreCodes();
        //机构产品表更新数据
        log.info("[产品紧急修改]机构产品表ep_organ_course修改数据。{}。", organCoursePo);
        //加行级锁
        EpOrganCourseCourseStatus courseStatus = organCourseRepository.getByIdWithLock(organCoursePo.getId()).getCourseStatus();
        //更新产品po
        if (courseStatus.equals(EpOrganCourseCourseStatus.online)) {
            organCourseRepository.rectifyById(organCoursePo);
        } else {
            log.error("[产品]紧急修改产品失败。该产品状态不为{}。", EpOrganCourseCourseStatus.online);
            return ResultDo.build(MessageCode.ERROR_SYSTEM_PARAM_FORMAT);
        }
        //班次更新班次负责人ognAccountId,联系电话phone
        List<EpOrganClassPo> organClassPos = dto.getOrganClassPos();
        if (CollectionsTools.isNotEmpty(organClassPos)) {
            organClassPos.forEach(organClassPo -> {
                organClassPo.setOgnId(ognId);
                organClassRepository.rectifyOrganClass(organClassPo);
            });
        }
        // 根据classCatelogId更新班次目录
        List<RectifyOrganClassCatalogBo> rectifyOrganClassCatalogBos = dto.getRectifyOrganClassCatalogBos();
        if (CollectionsTools.isNotEmpty(rectifyOrganClassCatalogBos)) {
            rectifyOrganClassCatalogBos.forEach(bo -> {
                if (bo.getRectifyFlag()) {
                    organClassCatalogRepository.rectifyByRectifyCourse(bo);
                    //同步ep_organ_class_schedule表里数据
                    organClassScheduleRepository.rectifyByRectifyCatalog(bo);
                }
            });
        }
        //产品id
        Long organCourseId = organCoursePo.getId();
        //物理删除产品标签
        organCourseTagRepository.deletePhysicByCourseId(organCourseId);
        //产品标签表插入数据start
        List<EpOrganCourseTagPo> insertOrganCourseTagPos = Lists.newArrayList();
        if (CollectionsTools.isNotEmpty(constantTagPos)) {
            constantTagPos.forEach(constantTagPo -> {
                EpOrganCourseTagPo insertOrganCourseTagPo = new EpOrganCourseTagPo();
                insertOrganCourseTagPo.setTagId(constantTagPo.getId());
                insertOrganCourseTagPo.setCourseId(organCourseId);
                insertOrganCourseTagPos.add(insertOrganCourseTagPo);
            });
        }
        log.info("[产品]产品标签表ep_organ_course_tag插入数据。{}。", insertOrganCourseTagPos);
        organCourseTagRepository.insert(insertOrganCourseTagPos);
        //产品标签表插入数据end
        //主图
        if (StringTools.isNotBlank(dto.getMainpicUrlPreCode())) {
            fileRepository.deleteLogicByBizTypeAndSourceId(BizConstant.FILE_BIZ_TYPE_CODE_COURSE_MAIN_PIC, organCourseId);
            log.info("[产品]文件表ep_file更新数据。biz_type_code={},source_id={}。", dto.getMainpicUrlPreCode(), insertOrganCourseTagPos);
            fileRepository.updateSourceIdByPreCode(dto.getMainpicUrlPreCode(), organCourseId);
        }
        //产品内容图片
        if (CollectionsTools.isNotEmpty(courseDescPicPreCodes)) {
            courseDescPicPreCodes.forEach(proCode -> {
                fileRepository.updateSourceIdByPreCode(proCode, organCourseId);
            });
            List<String> oldProCodes = fileRepository.getPreCodeByBizTypeAndSourceId(BizConstant.FILE_BIZ_TYPE_CODE_COURSE_DESC_PIC, organCourseId);
            //待删除，差集oldProCodes里有，courseDescPicPreCodes里没有
            Set<String> diffDel = Sets.difference(new HashSet<String>(oldProCodes), new HashSet<String>(courseDescPicPreCodes));
            fileRepository.deleteLogicByPreCodes(new ArrayList<String>(diffDel));
            log.info("[产品]文件表ep_file更新数据。原内容图片oldProCodes={},新内容图片courseDescPicPreCodes={}。", oldProCodes, courseDescPicPreCodes);
        }
        log.info("[产品]紧急修改产品成功。产品id={}。", organCourseId);
        return ResultDo.build();
    }

    /**
     * 根据产品id删除产品
     * 涉及ep_organ_course，ep_organ_class，ep_organ_class_catalog，ep_organ_catalog，ep_organ_course_team，ep_organ_course_tag
     * ep_file
     *
     * @param courseId
     */
    @Transactional(rollbackFor = Exception.class)
    public ResultDo deleteCourseByCourseId(Long courseId, Long ognId) {
        log.info("[产品]删除产品开始。产品id={},机构id={}。", courseId, ognId);
        Optional<EpOrganCoursePo> optional = organCourseRepository.findById(courseId);
        if (!optional.isPresent()) {
            log.error("[产品]删除产品失败，该产品不存在。产品id={}。", courseId);
            ResultDo.build(MessageCode.ERROR_COURSE_NOT_EXIST);
        }
        //产品表 逻辑删除
        organCourseRepository.deleteLogicById(courseId);
        //班次表 逻辑删除
        List<Long> classIds = organClassRepository.deleteLogicByCourseId(courseId);
        //班次目录表 逻辑删除
        organClassCatalogRepository.deleteLogicByClassIds(classIds);
        //产品标签 逻辑删除
        organCourseTagRepository.deleteLogicByCourseId(courseId);
        //主图 逻辑删除
        fileRepository.deleteLogicByBizTypeAndSourceId(BizConstant.FILE_BIZ_TYPE_CODE_COURSE_MAIN_PIC, courseId);
        //内容图片 逻辑删除
        fileRepository.deleteLogicByBizTypeAndSourceId(BizConstant.FILE_BIZ_TYPE_CODE_COURSE_DESC_PIC, courseId);
        organCourseTeamRepository.deletePhysicByCourseId(courseId);
        log.info("[产品]删除产品成功。产品id={}。", courseId);
        return ResultDo.build();
    }

    /**
     * 产品上线
     *
     * @param currentUser
     * @param id
     */
    @Transactional(rollbackFor = Exception.class)
    public ResultDo onlineById(EpSystemUserPo currentUser, Long id) {
        log.info("机构上线产品, sysUserId={}, courseId={}", currentUser.getId(), id);
        EpOrganCoursePo coursePo = organCourseRepository.getById(id);
        if (coursePo == null || coursePo.getDelFlag()) {
            log.error("产品不存在, courseId={}", id);
            return ResultDo.build(MessageCode.ERROR_COURSE_NOT_EXIST);
        }
        if (!coursePo.getCourseStatus().equals(EpOrganCourseCourseStatus.save)) {
            log.error("产品不是已保存状态, courseId={}, status={}", id, coursePo.getCourseStatus());
            return ResultDo.build(MessageCode.ERROR_COURSE_NOT_SAVE);
        }
        if (!coursePo.getOgnId().equals(currentUser.getOgnId())) {
            log.error("产品与当前操作机构不匹配, courseId={}, courseOgnId={}, userOgnId={}", id, coursePo.getOgnId(), currentUser.getOgnId());
            return ResultDo.build(MessageCode.ERROR_COURSE_OGN_NOT_MATCH);
        }
        // 上线
        int num = organCourseRepository.onlineById(id);
        if (num == BizConstant.DB_NUM_ONE) {
            // 上线班次
            organClassRepository.onlineByCourseId(id);
            // 机构类目刷新
            if (!organCatalogRepository.existOgnAndCatalog(coursePo.getOgnId(), coursePo.getCourseCatalogId())) {
                EpOrganCatalogPo organCatalogPo = new EpOrganCatalogPo();
                organCatalogPo.setOgnId(coursePo.getOgnId());
                organCatalogPo.setCourseCatalogId(coursePo.getCourseCatalogId());
                organCatalogRepository.insert(organCatalogPo);
            }
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
     * 机构下线，该机构下的产品下线
     *
     * @param ognId
     */
    public void updateCourseByOfflineOgn(Long ognId) {
        organCourseRepository.updateCourseByOfflineOgn(ognId);
    }

    /**
     * 根据sourceId获取产品主图
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
    private BigDecimal getCoursePriceMin(List<OrganClassBo> organClassBos) throws RuntimeException {
        BigDecimal priceMin = organClassBos.get(BizConstant.DB_NUM_ZERO).getClassPrize();
        for (OrganClassBo classBo : organClassBos) {
            if (classBo.getClassPrize() == null) {
                throw new RuntimeException("班次价格不为空");
            }
            BigDecimal thisMin;
            if (classBo.getDiscountAmount() != null
                    && classBo.getDiscountAmount().compareTo(classBo.getClassPrize()) < BizConstant.DB_NUM_ZERO) {
                thisMin = classBo.getDiscountAmount();
            } else {
                thisMin = classBo.getClassPrize();
            }
            if (thisMin.compareTo(priceMin) < BizConstant.DB_NUM_ZERO) {
                priceMin = thisMin;
            }
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
        EpOrganPo organPo = organRepository.findById(po.getOgnId());
        if (organPo == null) {
            log.error("接受参数异常，organPo=null。");
            return false;
        }
        if (organPo.getVipFlag()) {
            if (null == po.getVipFlag()) {
                log.error("接受参数异常，vipFlag=null。");
                return false;
            }
        } else {
            po.setVipFlag(false);
        }
        Optional<EpOrganConfigPo> organConfigOptional = organConfigRepository.getByOgnId(po.getOgnId());
        if (!organConfigOptional.isPresent()) {
            log.error("接受参数异常，organConfig=null。");
            return false;
        }
        if (!organConfigOptional.get().getWechatPayFlag()) {
            po.setWechatPayFlag(false);
        } else {
            if (null == po.getWechatPayFlag()) {
                log.error("接受参数异常，wechatPayFlag=null。");
                return false;
            }
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
