package com.ep.domain.service;

import com.ep.common.tool.CollectionsTools;
import com.ep.common.tool.DateTools;
import com.ep.common.tool.StringTools;
import com.ep.domain.constant.BizConstant;
import com.ep.domain.constant.MessageCode;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.OrganAccountBo;
import com.ep.domain.pojo.bo.OrganAccountClassBo;
import com.ep.domain.pojo.po.EpFilePo;
import com.ep.domain.pojo.po.EpOrganAccountPo;
import com.ep.domain.pojo.po.EpOrganPo;
import com.ep.domain.repository.FileRepository;
import com.ep.domain.repository.OrganAccountRepository;
import com.ep.domain.repository.OrganClassRepository;
import com.ep.domain.repository.OrganRepository;
import lombok.extern.slf4j.Slf4j;
import org.jooq.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
    private FileService fileService;

    /**
     * 根据手机号获取详情
     *
     * @param mobile
     * @return
     */
    public ResultDo<OrganAccountBo> getOrganAccountInfo(Long mobile) {
        ResultDo<OrganAccountBo> resultDo = ResultDo.build();
        List<EpOrganAccountPo> accountList = organAccountRepository.getByMobile(mobile);
        if (CollectionsTools.isEmpty(accountList)) {
            return resultDo.setError(MessageCode.ERROR_ORGAN_ACCOUNT_NOT_EXISTS);
        }
        EpOrganAccountPo accountPo = accountList.get(BizConstant.DB_NUM_ZERO);
        OrganAccountBo accountBo = new OrganAccountBo();
        // 认证编号
        accountBo.setId(accountPo.getId());
        // 昵称
        accountBo.setNickName(accountPo.getNickName());
        // 机构名
        EpOrganPo organPo = organRepository.getById(accountPo.getOgnId());
        accountBo.setOgnName(organPo.getOgnName());
        // 头像
        Optional<EpFilePo> optAvatar = fileRepository.getOneByBizTypeAndSourceId(BizConstant.FILE_BIZ_TYPE_CODE_TEACHER_AVATAR, accountBo.getId());
        String avatar = optAvatar.isPresent() ? optAvatar.get().getFileUrl() : null;
        accountBo.setAvatar(avatar);
        return resultDo.setResult(accountBo);
    }

    public EpOrganAccountPo getById(Long id) {
        return organAccountRepository.getById(id);

    }

    public Page<OrganAccountBo> merchantFindbyPageAndCondition(Pageable pageable, Collection<? extends Condition> condition) {
        return organAccountRepository.merchantFindbyPageAndCondition(pageable, condition);
    }

    public Page<OrganAccountBo> platformFindbyPageAndCondition(Pageable pageable, Collection<? extends Condition> condition) {
        return organAccountRepository.platformFindbyPageAndCondition(pageable, condition);
    }

    /**
     * 新增机构账户关联信息
     *
     * @param bo
     * @return
     */
    public ResultDo createOgnAccount(OrganAccountBo bo) {
        log.info("[教师]新增教师开始。教师对象={}。", bo);
        if (StringTools.isBlank(bo.getAccountName()) || StringTools.isBlank(bo.getNickName())
                || null == bo.getOgnId() || null == bo.getStatus()
                || null == bo.getReferMobile() || StringTools.isBlank(bo.getAvatar())) {
            log.error("[教师]新增教师失败，请求参数异常。");
            ResultDo.build(MessageCode.ERROR_OPERATE_FAIL);
        }
        EpOrganAccountPo po = new EpOrganAccountPo();
        copyBoPropertyToPo(bo, po);
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
    public ResultDo deleteOgnAccount(Long id) {
        log.info("[教师]删除教师开始，教师id={}。", id);
        if (organAccountRepository.deleteLogical(id) == BizConstant.DB_NUM_ONE) {
            log.info("[教师]删除教师成功，教师id={}。", id);
            return ResultDo.build();
        } else {
            log.error("[教师]删除教师失败，教师id={}。", id);
            return ResultDo.build(MessageCode.ERROR_OPERATE_FAIL);
        }
    }

    public List<EpOrganAccountPo> findByOgnId(Long ognId) {
        return organAccountRepository.findByOgnId(ognId);
    }

    /**
     * 根据账户id获取今日课程
     *
     * @param mobile
     * @return
     */
    public ResultDo<List<OrganAccountClassBo>> findTodayClassByOrganAccount(Long mobile) {
        ResultDo<List<OrganAccountClassBo>> resultDo = ResultDo.build();
        Timestamp now = DateTools.getCurrentDateTime();
        Timestamp startTime = DateTools.zerolizedTime(now);
        Timestamp endTime = DateTools.getEndTime(now);
        List<EpOrganAccountPo> accountList = organAccountRepository.getByMobile(mobile);
        if (CollectionsTools.isEmpty(accountList)) {
            log.error("机构账户不存在, mobile={}", mobile);
            return resultDo.setError(MessageCode.ERROR_ORGAN_ACCOUNT_NOT_EXISTS);
        }
        EpOrganAccountPo accountPo = accountList.get(BizConstant.DB_NUM_ZERO);
        List<OrganAccountClassBo> todayClasses = organClassRepository.findClassByOgnAccountId(accountPo.getId(), startTime, endTime);
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
     * 根据账户id获取全部课程
     *
     * @param mobile
     * @return
     */
    public ResultDo<Page<OrganAccountClassBo>> findAllClassByOrganAccountForPage(Pageable pageable, Long mobile) {
        ResultDo<Page<OrganAccountClassBo>> resultDo = ResultDo.build();
        List<EpOrganAccountPo> accountList = organAccountRepository.getByMobile(mobile);
        if (CollectionsTools.isEmpty(accountList)) {
            log.error("机构账户不存在, mobile={}", mobile);
            return resultDo.setError(MessageCode.ERROR_ORGAN_ACCOUNT_NOT_EXISTS);
        }
        EpOrganAccountPo accountPo = accountList.get(BizConstant.DB_NUM_ZERO);
        Page<OrganAccountClassBo> page = organClassRepository.findAllClassByOrganAccountForPage(pageable, accountPo.getId());
        List<OrganAccountClassBo> data = page.getContent();
        if (CollectionsTools.isEmpty(data)) {
            return resultDo.setResult(page);
        }
        for (OrganAccountClassBo classBo : data) {
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
}
