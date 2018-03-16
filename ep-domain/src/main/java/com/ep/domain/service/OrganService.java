package com.ep.domain.service;

import com.ep.common.tool.CollectionsTools;
import com.ep.common.tool.DateTools;
import com.ep.common.tool.StringTools;
import com.ep.domain.constant.BizConstant;
import com.ep.domain.constant.MessageCode;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.OrganBo;
import com.ep.domain.pojo.bo.SystemOrganBo;
import com.ep.domain.pojo.dto.OrganInfoDto;
import com.ep.domain.pojo.po.EpFilePo;
import com.ep.domain.pojo.po.EpOrganPo;
import com.ep.domain.repository.*;
import com.ep.domain.repository.domain.enums.EpOrganCourseCourseStatus;
import com.ep.domain.repository.domain.enums.EpOrganStatus;
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
 * @Description: 机构服务类
 * @Author: J.W
 * @Date: 上午9:30 2018/1/14
 */
@Slf4j
@Service
public class OrganService {

    @Autowired
    private OrganRepository organRepository;
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrganCourseRepository organCourseRepository;
    @Autowired
    private OrganClassRepository organClassRepository;
    @Autowired
    private SystemUserRepository systemUserRepository;

    /**
     * 机构详情(基本信息＋banner列表＋课程列表)
     *
     * @param id
     * @return
     */
    public ResultDo<OrganInfoDto> getOgnDetail(Long id) {
        ResultDo<OrganInfoDto> resultDo = ResultDo.build();
        // 机构详情
        Optional<EpOrganPo> ognInfoPojo = this.getById(id);
        if (!ognInfoPojo.isPresent() || !EpOrganStatus.online.equals(ognInfoPojo.get().getStatus())) {
            return ResultDo.build(MessageCode.ERROR_DATA_MISS);
        }
        // 机构主图
        Optional<EpFilePo> mainPicOpt = fileRepository.getOneByBizTypeAndSourceId(BizConstant.FILE_BIZ_TYPE_CODE_ORGAN_MAIN_PIC, id);
        String mainPic = mainPicOpt.isPresent() ? mainPicOpt.get().getFileUrl() : null;
        // 机构Logo
        Optional<EpFilePo> logoOpt = fileRepository.getOneByBizTypeAndSourceId(BizConstant.FILE_BIZ_TYPE_CODE_ORGAN_LOGO, id);
        String logo = logoOpt.isPresent() ? logoOpt.get().getFileUrl() : null;
        // 总评论数
        Long totalCommentNum = orderRepository.countByOgnId(id);
        OrganInfoDto ognInfoDto = new OrganInfoDto(ognInfoPojo.get(), logo, mainPic, totalCommentNum);
        return resultDo.setResult(ognInfoDto);
    }

    /**
     * 根据主键获取机构信息
     *
     * @param id
     * @return
     */
    public Optional<EpOrganPo> getById(Long id) {
        return Optional.ofNullable(organRepository.getById(id));
    }

    /**
     * 分页查询机构列表
     *
     * @param pageable
     * @return
     */
    public ResultDo<Page<OrganBo>> queryOrganForPage(Pageable pageable) {
        Page<OrganBo> page = organRepository.queryOrganForPage(pageable);
        List<OrganBo> data = page.getContent();
        if (CollectionsTools.isNotEmpty(data)) {
            for (OrganBo organ : data) {
                Optional<EpFilePo> organMainPic = fileRepository.getOneByBizTypeAndSourceId(BizConstant.FILE_BIZ_TYPE_CODE_ORGAN_MAIN_PIC, organ.getId());
                String mainPic = organMainPic.isPresent() ? organMainPic.get().getFileUrl() : null;
                organ.setFileUrl(mainPic);
            }
        }
        ResultDo<Page<OrganBo>> resultDo = ResultDo.build();
        resultDo.setResult(page);
        return resultDo;
    }


    public Page<EpOrganPo> findByPageAndCondition(Pageable pageable, Collection<? extends Condition> condition) {
        return organRepository.findByPageable(pageable, condition);
    }

    /**
     * 系统后台新增机构
     *
     * @param bo
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResultDo createSystemOrgan(SystemOrganBo bo) {
        log.info("[机构]新增机构开始。机构对象={}。", bo);
        if (bo.getVipFlag() == null) {
            bo.setVipFlag(false);
            bo.setVipName(null);
        }
        if (StringTools.isBlank(bo.getOgnName()) || StringTools.isBlank(bo.getOgnAddress())
                || null == bo.getOgnRegion() || null == bo.getMarketWeight()) {
            log.error("[机构]新增机构失败。请求参数异常。");
            return ResultDo.build(MessageCode.ERROR_SYSTEM_PARAM_FORMAT);
        }
        if (organRepository.findByName(bo.getOgnName()).isPresent()) {
            log.error("[机构]新增机构失败。机构名称已存在。");
            return ResultDo.build(MessageCode.ERROR_ORGAN_NAME_EXISTS);
        }
        EpOrganPo po = new EpOrganPo();
        po.setOgnName(bo.getOgnName());
        po.setOgnAddress(bo.getOgnAddress());
        po.setOgnRegion(bo.getOgnRegion());
        po.setOgnLat(StringTools.getNullIfBlank(bo.getOgnLat()));
        po.setOgnLng(StringTools.getNullIfBlank(bo.getOgnLng()));
        po.setOgnShortIntroduce(StringTools.getNullIfBlank(bo.getOgnShortIntroduce()));
        po.setOgnPhone(StringTools.getNullIfBlank(bo.getOgnPhone()));
        po.setOgnEmail(StringTools.getNullIfBlank(bo.getOgnEmail()));
        po.setOgnUrl(StringTools.getNullIfBlank(bo.getOgnUrl()));
        po.setOgnIntroduce(StringTools.getNullIfBlank(bo.getOgnIntroduce()));
        po.setVipFlag(bo.getVipFlag());
        po.setVipName(bo.getVipName());
        po.setMarketWeight(bo.getMarketWeight());
        po.setRemark(StringTools.getNullIfBlank(bo.getRemark()));
        po.setStatus(EpOrganStatus.save);
        po.setOgnCreateDate(DateTools.stringToTimestamp(bo.getOgnCreateDateStr(), "yyyy-MM-dd"));

        //先插入机构，后插入主图、logo
        organRepository.insert(po);
        //机构主图
        if (StringTools.isNotBlank(bo.getMainpicUrlPreCode())) {
            fileRepository.updateSourceIdByPreCode(bo.getMainpicUrlPreCode(), po.getId());
        }
        //机构logo
        if (StringTools.isNotBlank(bo.getLogoUrlPreCode())) {
            fileRepository.updateSourceIdByPreCode(bo.getLogoUrlPreCode(), po.getId());
        }
        log.info("[机构]新增机构成功，id={}。", po.getId());
        return ResultDo.build();
    }

    /**
     * 系统后台修改机构
     *
     * @param bo
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResultDo updateSystemOrgan(SystemOrganBo bo) {
        log.info("[机构]修改机构开始。机构对象={}。", bo);
        if (bo.getVipFlag() == null) {
            bo.setVipFlag(false);
            bo.setVipName(null);
        }
        if (null == bo.getId() || StringTools.isBlank(bo.getOgnName())
                || StringTools.isBlank(bo.getOgnAddress()) || null == bo.getOgnRegion()) {
            log.error("[机构]修改机构失败。请求参数异常。");
            return ResultDo.build(MessageCode.ERROR_SYSTEM_PARAM_FORMAT);
        }
        Optional<EpOrganPo> optional = organRepository.findByName(bo.getOgnName());
        if (optional.isPresent() && !(optional.get().getId().equals(bo.getId()))) {
            log.error("[机构]修改机构失败。机构名称已存在。");
            return ResultDo.build(MessageCode.ERROR_ORGAN_NAME_EXISTS);
        }

        EpOrganPo poLock = organRepository.findByIdLock(bo.getId());
        if (null == poLock) {
            log.error("[机构]修改机构失败。该机构不存在。");
            return ResultDo.build(MessageCode.ERROR_ORGAN_NOT_EXISTS);
        }
        EpOrganPo po = new EpOrganPo();
        po.setId(bo.getId());
        po.setOgnName(bo.getOgnName());
        po.setOgnAddress(bo.getOgnAddress());
        po.setOgnRegion(bo.getOgnRegion());
        po.setOgnLat(StringTools.getNullIfBlank(bo.getOgnLat()));
        po.setOgnLng(StringTools.getNullIfBlank(bo.getOgnLng()));
        po.setOgnShortIntroduce(StringTools.getNullIfBlank(bo.getOgnShortIntroduce()));
        po.setOgnPhone(StringTools.getNullIfBlank(bo.getOgnPhone()));
        po.setOgnEmail(StringTools.getNullIfBlank(bo.getOgnEmail()));
        po.setOgnUrl(StringTools.getNullIfBlank(bo.getOgnUrl()));
        po.setOgnIntroduce(StringTools.getNullIfBlank(bo.getOgnIntroduce()));
        po.setVipFlag(bo.getVipFlag());
        po.setVipName(bo.getVipName());
        po.setMarketWeight(bo.getMarketWeight());
        po.setRemark(StringTools.getNullIfBlank(bo.getRemark()));
        po.setOgnCreateDate(DateTools.stringToTimestamp(bo.getOgnCreateDateStr(), "yyyy-MM-dd"));
        System.out.println(po);
        //主图
        if (StringTools.isNotBlank(bo.getMainpicUrlPreCode())) {
            fileRepository.deleteLogicByBizTypeAndSourceId(BizConstant.FILE_BIZ_TYPE_CODE_ORGAN_MAIN_PIC, po.getId());
            fileRepository.updateSourceIdByPreCode(bo.getMainpicUrlPreCode(), po.getId());
        }
        //logo
        if (StringTools.isNotBlank(bo.getLogoUrlPreCode())) {
            fileRepository.deleteLogicByBizTypeAndSourceId(BizConstant.FILE_BIZ_TYPE_CODE_ORGAN_LOGO, po.getId());
            fileRepository.updateSourceIdByPreCode(bo.getLogoUrlPreCode(), po.getId());
        }
        if (organRepository.updateSystemOrgan(po) == BizConstant.DB_NUM_ONE) {
            log.info("[机构]更新机构成功，id={}。", po.getId());
            return ResultDo.build();
        } else {
            log.error("[机构]更新机构失败，id={}。", po.getId());
            return ResultDo.build(MessageCode.ERROR_OPERATE_FAIL);

        }

    }

    /**
     * 删除机构
     *
     * @param id
     */
    public void delete(Long id) {
        organRepository.deleteLogical(id);
    }

    /**
     * 根据id下线机构
     *
     * @param id
     */
    @Transactional(rollbackFor = Exception.class)
    public ResultDo offlineById(Long id) {
        EpOrganPo po = organRepository.findById(id);
        if (null == po) {
            log.error("[机构]，下线失败，机构不存在。");
            return ResultDo.build(MessageCode.ERROR_ORGAN_NOT_EXISTS);
        }
        if (po.getStatus().equals(EpOrganStatus.offline)) {
            log.info("[机构]，机构已下线。id={}。", id);
            return ResultDo.build();
        }
        if (!po.getStatus().equals(EpOrganStatus.online)) {
            log.error("[机构]，机构下线失败。状态为{},只有online状态能下线。", po.getStatus().getLiteral());
            return ResultDo.build(MessageCode.ERROR_OFFLINE_CURRSTATUS_NOT_ONLINE);
        }
        //机构下没有online的课程的机构才能下线
        if (CollectionsTools.isNotEmpty(organCourseRepository.findByOgnIdAndStatus(id, EpOrganCourseCourseStatus.online))) {
            log.error("[机构]，机构下线失败。机构下存在上线的课程。");
            return ResultDo.build(MessageCode.ERROR_OFFLINE_EXISTS_ONLINE_COURSE);
        }

        if (organRepository.offlineById(id) == BizConstant.DB_NUM_ONE) {
            //机构下线，该机构下的课程下线
            organCourseRepository.updateCourseByOfflineOgn(id);
            //机构下线，该机构下的班次结束
            organClassRepository.updateClassByOfflineOgn(id);
            //机构下线，该机构对应平台账号注销
            systemUserRepository.cancelByOfflineOgn(id);
            log.info("[机构]，下线成功，id={}。", id);
            return ResultDo.build();
        } else {
            log.error("[机构]，下线失败，id={}。", id);
            return ResultDo.build(MessageCode.ERROR_OPERATE_FAIL);
        }

    }

    /**
     * 根据id上线机构
     *
     * @param id
     */
    public ResultDo onlineById(Long id) {
        EpOrganPo po = organRepository.findById(id);
        if (null == po) {
            log.error("[机构]，上线失败，机构不存在。");
            return ResultDo.build(MessageCode.ERROR_ORGAN_NOT_EXISTS);
        }
        if (po.getStatus().equals(EpOrganStatus.online)) {
            log.info("[机构]，机构已上线。id={}。", id);
            return ResultDo.build();
        }
        //机构主图和logo存在才能上线
        Optional<EpFilePo> optionalMainpic = fileRepository.getOneByBizTypeAndSourceId(BizConstant.FILE_BIZ_TYPE_CODE_ORGAN_MAIN_PIC, id);
        if (!optionalMainpic.isPresent()) {
            log.error("[机构]，上线失败，机构主图不存在。");
            return ResultDo.build(MessageCode.ERROR_ORGAN_MAINPIC_NOT_EXISTS);
        }
        Optional<EpFilePo> optionalLogo = fileRepository.getOneByBizTypeAndSourceId(BizConstant.FILE_BIZ_TYPE_CODE_ORGAN_LOGO, id);
        if (!optionalLogo.isPresent()) {
            log.error("[机构]，上线失败，机构logo不存在。");
            return ResultDo.build(MessageCode.ERROR_ORGAN_LOGO_NOT_EXISTS);
        }
        if (organRepository.onlineById(id) == BizConstant.DB_NUM_ONE) {
            log.info("[机构]，上线成功，id={}。", id);
            return ResultDo.build();
        } else {
            log.error("[机构]，上线失败，id={}。", id);
            return ResultDo.build(MessageCode.ERROR_OPERATE_FAIL);
        }

    }

    /**
     * 根据sourceId获取机构主图
     *
     * @param sourceId
     * @return
     */
    public Optional<EpFilePo> getOgnMainpic(Long sourceId) {
        return fileRepository.getOneByBizTypeAndSourceId(BizConstant.FILE_BIZ_TYPE_CODE_ORGAN_MAIN_PIC, sourceId);
    }

    /**
     * 根据sourceId获取机构logo
     *
     * @param sourceId
     * @return
     */
    public Optional<EpFilePo> getOgnLogo(Long sourceId) {
        return fileRepository.getOneByBizTypeAndSourceId(BizConstant.FILE_BIZ_TYPE_CODE_ORGAN_LOGO, sourceId);
    }

    /**
     * 冻结机构
     *
     * @param id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResultDo freezeById(Long id) {
        EpOrganPo po = organRepository.findById(id);
        if (null == po) {
            log.error("[机构]，冻结失败，机构不存在。");
            return ResultDo.build(MessageCode.ERROR_ORGAN_NOT_EXISTS);
        }
        if (organRepository.freezeById(id) == BizConstant.DB_NUM_ONE) {
            //冻结该机构对应的用户账号
            systemUserRepository.freezeByOgnId(id);
            log.info("[机构]，冻结成功。id={}", id);
            return ResultDo.build();
        } else {
            log.error("[机构]，冻结失败。id={}", id);
            return ResultDo.build(MessageCode.ERROR_OPERATE_FAIL);
        }
    }

    /**
     * 解冻机构
     *
     * @param id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResultDo unfreezeById(Long id) {
        EpOrganPo po = organRepository.findById(id);
        if (null == po) {
            log.error("[机构]，解冻失败，机构不存在。");
            return ResultDo.build(MessageCode.ERROR_ORGAN_NOT_EXISTS);
        }
        if (organRepository.unfreezeById(id) == BizConstant.DB_NUM_ONE) {
            //解冻该机构对应的用户账号
            systemUserRepository.unfreezeByOgnId(id);
            log.info("[机构]，解冻成功。id={}", id);
            return ResultDo.build();
        } else {
            log.error("[机构]，解冻失败。id={}", id);
            return ResultDo.build(MessageCode.ERROR_OPERATE_FAIL);
        }
    }
}
