package com.ep.domain.service;

import com.ep.common.tool.CollectionsTools;
import com.ep.common.tool.DateTools;
import com.ep.common.tool.StringTools;
import com.ep.domain.constant.BizConstant;
import com.ep.domain.constant.MessageCode;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.OrganAccountAllClassBo;
import com.ep.domain.pojo.bo.OrganAccountBo;
import com.ep.domain.pojo.bo.OrganAccountClassBo;
import com.ep.domain.pojo.po.EpFilePo;
import com.ep.domain.pojo.po.EpOrganAccountPo;
import com.ep.domain.pojo.po.EpOrganPo;
import com.ep.domain.repository.*;
import com.ep.domain.repository.domain.enums.EpOrganAccountStatus;
import com.ep.domain.repository.domain.enums.EpOrganClassStatus;
import lombok.extern.slf4j.Slf4j;
import org.jooq.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @Description: 机构账户关联信息服务
 * @Author: CC.F
 * @Date: 9:45 2018/2/6
 */
@Service
@Slf4j
public class OrganAccountService {

    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private OrganRepository organRepository;
    @Autowired
    private OrganAccountRepository organAccountRepository;
    @Autowired
    private OrganClassRepository organClassRepository;
    @Autowired
    private OrganClassScheduleRepository organClassScheduleRepository;


    /**
     * 获取机构账户详情
     *
     * @param organAccountPo
     * @return
     */
    public ResultDo<OrganAccountBo> getOrganAccountInfo(EpOrganAccountPo organAccountPo) {
        ResultDo<OrganAccountBo> resultDo = ResultDo.build();
        OrganAccountBo accountBo = new OrganAccountBo();
        // 认证编号
        accountBo.setId(organAccountPo.getId());
        // 昵称
        accountBo.setNickName(organAccountPo.getNickName());
        // 机构名
        EpOrganPo organPo = organRepository.getById(organAccountPo.getOgnId());
        accountBo.setOgnName(organPo.getOgnName());
        // 头像
        Optional<EpFilePo> optAvatar = fileRepository.getOneByBizTypeAndSourceId(BizConstant.FILE_BIZ_TYPE_CODE_TEACHER_AVATAR, accountBo.getId());
        String avatar = optAvatar.isPresent() ? optAvatar.get().getFileUrl() : null;
        accountBo.setAvatar(avatar);
        return resultDo.setResult(accountBo);
    }

    public Optional<EpOrganAccountPo> findById(Long id) {
        return organAccountRepository.findById(id);

    }

    /**
     * 商户获取教师分页
     *
     * @param pageable
     * @param condition
     * @return
     */
    public Page<OrganAccountBo> merchantFindbyPageAndCondition(Pageable pageable, Collection<? extends Condition> condition) {
        return organAccountRepository.merchantFindbyPageAndCondition(pageable, condition);
    }

    /**
     * 平台获取教师分页
     * @param pageable
     * @param condition
     * @return
     */
    public Page<OrganAccountBo> platformFindbyPageAndCondition(Pageable pageable, Collection<? extends Condition> condition) {
        return organAccountRepository.platformFindbyPageAndCondition(pageable, condition);
    }

    /**
     * 新增机构账户关联信息
     *
     * @param bo
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResultDo createOgnAccount(OrganAccountBo bo) {

        log.info("[教师]新增教师开始。教师对象={}。", bo);
        if (StringTools.isBlank(bo.getAccountName()) || StringTools.isBlank(bo.getNickName())
                || null == bo.getOgnId()
                || null == bo.getReferMobile() || StringTools.isBlank(bo.getAvatar())) {
            log.error("[教师]新增教师失败，请求参数异常。");
            ResultDo.build(MessageCode.ERROR_OPERATE_FAIL);
        }
        EpOrganAccountPo po = new EpOrganAccountPo();
        copyBoPropertyToPo(bo, po);
        po.setStatus(EpOrganAccountStatus.normal);
        organAccountRepository.insert(po);
        //教师头像
        if (StringTools.isNotBlank(bo.getAvatarUrlPreCode())) {
            log.info("[课程]文件表ep_file更新数据。biz_type_code={},source_id={}。", bo.getAvatarUrlPreCode(), po.getId());
            fileRepository.updateSourceIdByPreCode(bo.getAvatarUrlPreCode(), po.getId());
        }
        log.info("[教师]新增教师成功。id={}。", po.getId());
        return ResultDo.build();
    }

    /**
     * 修改机构账户关联信息
     *
     * @param bo
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResultDo updateOgnAccount(OrganAccountBo bo) {
        log.info("[教师]修改教师开始，教师对象={}。", bo);
        if (null == bo.getId() || StringTools.isBlank(bo.getAccountName()) || StringTools.isBlank(bo.getNickName())
                || null == bo.getStatus() || null == bo.getReferMobile()) {
            log.error("[教师]修改教师失败，请求参数异常。");
            ResultDo.build(MessageCode.ERROR_OPERATE_FAIL);
        }
        EpOrganAccountPo po = new EpOrganAccountPo();
        copyBoPropertyToPo(bo, po);
        if (organAccountRepository.updateById(po) == BizConstant.DB_NUM_ONE) {
            //主图
            if (StringTools.isNotBlank(bo.getAvatarUrlPreCode())) {
                fileRepository.deleteLogicByBizTypeAndSourceId(BizConstant.FILE_BIZ_TYPE_CODE_TEACHER_AVATAR, po.getId());
                log.info("[课程]文件表ep_file更新数据。biz_type_code={},source_id={}。", bo.getAvatarUrlPreCode(), po.getId());
                fileRepository.updateSourceIdByPreCode(bo.getAvatarUrlPreCode(), po.getId());
            }
            log.info("[教师]修改教师成功，id={}。", po.getId());
            return ResultDo.build();
        } else {
            log.error("[教师]修改教师失败，id={}。", po.getId());
            return ResultDo.build(MessageCode.ERROR_OPERATE_FAIL);
        }

    }

    /**
     * 删除机构账户关联信息
     *
     * @param id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResultDo deleteOgnAccount(Long id) {
        log.info("[教师]删除教师开始，教师id={}。", id);
        if (organAccountRepository.deleteLogical(id) == BizConstant.DB_NUM_ONE) {
            fileRepository.deleteLogicByBizTypeAndSourceId(BizConstant.FILE_BIZ_TYPE_CODE_TEACHER_AVATAR,id);
            log.info("[教师]删除教师成功，教师id={}。", id);
            return ResultDo.build();
        } else {
            log.error("[教师]删除教师失败，教师id={}。", id);
            return ResultDo.build(MessageCode.ERROR_OPERATE_FAIL);
        }
    }

    /**
     * 根据机构id和状态获取教师
     * @param ognId
     * @param status
     * @return
     */
    public List<EpOrganAccountPo> findByOgnIdAndStatus(Long ognId, EpOrganAccountStatus[] status) {
        return organAccountRepository.findByOgnIdAndStatus(ognId, status);
    }

    /**
     * 根据机构id和状态获取教师下拉框
     *
     * @param ognId
     * @param status
     * @return
     */
    public List<EpOrganAccountPo> findSelectModelByOgnIdAndStatus(Long ognId, EpOrganAccountStatus[] status) {
        return organAccountRepository.findSelectModelByOgnIdAndStatus(ognId, status);
    }

    /**
     * 根据账户id获取今日课程
     *
     * @param organAccountPo
     * @return
     */
    public ResultDo<List<OrganAccountClassBo>> findTodayClassByOrganAccount(EpOrganAccountPo organAccountPo) {
        ResultDo<List<OrganAccountClassBo>> resultDo = ResultDo.build();
        Timestamp now = DateTools.getCurrentDateTime();
        Timestamp startTime = DateTools.zerolizedTime(now);
        Timestamp endTime = DateTools.getEndTime(now);
        List<OrganAccountClassBo> todayClasses = organClassScheduleRepository.findClassByOgnAccountId(organAccountPo.getId(), startTime, endTime);
        if (CollectionsTools.isNotEmpty(todayClasses)) {
            for (OrganAccountClassBo classBo : todayClasses) {
                // 加载课程图片
                Optional<EpFilePo> optional = fileRepository.getOneByBizTypeAndSourceId(BizConstant.FILE_BIZ_TYPE_CODE_COURSE_MAIN_PIC, classBo.getCourseId());
                String mainPicUrl = optional.isPresent() ? optional.get().getFileUrl() : null;
                classBo.setMainPicUrl(mainPicUrl);
            }
        }
        return resultDo.setResult(todayClasses);
    }

    /**
     * 根据当前账户获取全部课程
     *
     * @param pageable
     * @param organAccountPo
     * @return
     */
    public ResultDo<Page<OrganAccountAllClassBo>> findAllClassByOrganAccountForPage(Pageable pageable, EpOrganAccountPo organAccountPo) {
        ResultDo<Page<OrganAccountAllClassBo>> resultDo = ResultDo.build();
        Page<OrganAccountAllClassBo> page = organClassRepository.findAllClassByOrganAccountForPage(pageable, organAccountPo.getId());
        List<OrganAccountAllClassBo> data = page.getContent();
        if (CollectionsTools.isEmpty(data)) {
            return resultDo.setResult(page);
        }
        for (OrganAccountAllClassBo classBo : data) {
            // 加载课程图片
            Optional<EpFilePo> optional = fileRepository.getOneByBizTypeAndSourceId(BizConstant.FILE_BIZ_TYPE_CODE_COURSE_MAIN_PIC, classBo.getCourseId());
            String mainPicUrl = optional.isPresent() ? optional.get().getFileUrl() : null;
            classBo.setMainPicUrl(mainPicUrl);
        }
        return resultDo.setResult(page);
    }

    /**
     * 根据sourceId获取教师头像
     *
     * @param sourceId
     * @return
     */
    public Optional<EpFilePo> getTeacherAvatar(Long sourceId) {
        return fileRepository.getOneByBizTypeAndSourceId(BizConstant.FILE_BIZ_TYPE_CODE_TEACHER_AVATAR, sourceId);
    }

    private void copyBoPropertyToPo(OrganAccountBo bo, EpOrganAccountPo po) {
        if (bo.getId() != null) {
            po.setId(bo.getId());
        }
        po.setAccountName(StringTools.getNullIfBlank(bo.getAccountName()));
        po.setNickName(StringTools.getNullIfBlank(bo.getNickName()));
        po.setIntroduce(StringTools.getNullIfBlank(bo.getIntroduce()));
        po.setOgnId(bo.getOgnId());
        po.setStatus(bo.getStatus());
        po.setReferMobile(bo.getReferMobile());
        po.setRemark(StringTools.getNullIfBlank(bo.getRemark()));
    }

    /**
     * 根据id注销
     *
     * @param id
     * @return
     */
    public ResultDo cancelOgnAccount(Long id) {
        log.info("[教师]注销教师开始，id={}。", id);
        EpOrganClassStatus[] classStatus = {EpOrganClassStatus.save, EpOrganClassStatus.online, EpOrganClassStatus.online};
        //该教师不存在 状态为 已保存；已上线；进行中；的班次，才能注销
        if (organClassRepository.countByOgnAccountIdAndClassStatus(id, classStatus) > BizConstant.DB_NUM_ZERO) {
            log.error("[教师]注销教师失败，该教师存在状态为已保存或已上线或进行中的班次，id={}。", id);
            return ResultDo.build(MessageCode.ERROR_ORGAN_ACCOUNT_CANCEL_EXIST_CLASS);
        }
        if (organAccountRepository.cancelById(id) == BizConstant.DB_NUM_ONE) {
            log.info("[教师]注销教师成功，id={}。", id);
            return ResultDo.build();
        } else {
            log.error("[教师]注销教师失败，id={}。", id);
            return ResultDo.build(MessageCode.ERROR_OPERATE_FAIL);
        }
    }

    /**
     * 根据id冻结
     *
     * @param id
     * @return
     */
    public ResultDo freezeOgnAccount(Long id) {
        log.info("[教师]冻结教师开始，id={}。", id);
        if (organAccountRepository.freezeById(id) == BizConstant.DB_NUM_ONE) {
            log.info("[教师]冻结教师成功，id={}。", id);
            return ResultDo.build();
        } else {
            log.error("[教师]冻结教师失败，id={}。", id);
            return ResultDo.build(MessageCode.ERROR_OPERATE_FAIL);
        }
    }

    /**
     * 根据id解冻
     *
     * @param id
     * @return
     */
    public ResultDo unfreezeOgnAccount(Long id) {
        log.info("[教师]解冻教师开始，id={}。", id);
        if (organAccountRepository.unfreezeById(id) == BizConstant.DB_NUM_ONE) {
            log.info("[教师]解冻教师成功，id={}。", id);
            return ResultDo.build();
        } else {
            log.error("[教师]解冻教师失败，id={}。", id);
            return ResultDo.build(MessageCode.ERROR_OPERATE_FAIL);
        }
    }

    /**
     * 根据机构id和关联手机号获取教师
     *
     * @param ognId
     * @param referMobile
     * @return
     */
    public Optional<EpOrganAccountPo> getByOgnIdAndReferMobile(Long ognId, Long referMobile) {
        return organAccountRepository.getByOgnIdAndReferMobile(ognId, referMobile);
    }

    /**
     * 根据手机号获取机构账户归属机构列表
     *
     * @param mobile
     * @return
     */
    public ResultDo<List<EpOrganPo>> getOrgansByRefferMobile(Long mobile) {
        ResultDo<List<EpOrganPo>> resultDo = ResultDo.build();
        List<EpOrganPo> data = organAccountRepository.getOrgansByRefferMobile(mobile);
        return resultDo.setResult(data);
    }
}
