package com.ep.domain.service;

import com.ep.common.tool.CryptTools;
import com.ep.common.tool.StringTools;
import com.ep.domain.constant.BizConstant;
import com.ep.domain.constant.MessageCode;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.SystemUserBo;
import com.ep.domain.pojo.po.EpOrganPo;
import com.ep.domain.pojo.po.EpSystemRolePo;
import com.ep.domain.pojo.po.EpSystemUserPo;
import com.ep.domain.pojo.po.EpSystemUserRolePo;
import com.ep.domain.repository.OrganRepository;
import com.ep.domain.repository.SystemUserRepository;
import com.ep.domain.repository.SystemUserRoleRepository;
import com.ep.domain.repository.domain.enums.EpOrganStatus;
import com.ep.domain.repository.domain.enums.EpSystemUserStatus;
import com.ep.domain.repository.domain.enums.EpSystemUserType;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.jooq.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.GeneralSecurityException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @Description: 用户服务类
 * @Author: CC.F
 * @Date: 10:33 2018/1/24
 */
@Slf4j
@Service
public class SystemUserService {
    @Autowired
    private SystemUserRepository systemUserRepository;
    @Autowired
    private SystemUserRoleRepository systemUserRoleRepository;
    @Autowired
    private OrganRepository organRepository;
    @Value("${password.salt.key}")
    private String passwordSaltKey;

    public Optional<EpSystemUserPo> findById(Long id) {
        return systemUserRepository.findById(id);

    }

    public Page<EpSystemUserPo> findbyPageAndCondition(Pageable pageable, Collection<? extends Condition> condition) {
        return systemUserRepository.findByPageable(pageable, condition);
    }

    /**
     * 新增用户
     *
     * @param bo
     */
    @Transactional(rollbackFor = Exception.class)
    public ResultDo createUser(SystemUserBo bo) throws Exception {
        log.info("[用户]新增用户开始。用户对象={}。", bo);
        if (null == bo.getMobile() || StringTools.isBlank(bo.getUserName())
                || StringTools.isBlank(bo.getPassword()) || null == bo.getType()) {
            log.error("[用户]新增用户失败，请求参数异常。");
            return ResultDo.build(MessageCode.ERROR_SYSTEM_PARAM_FORMAT);
        }

        if (bo.getType().equals(EpSystemUserType.platform) && null != bo.getOgnId()) {
            log.error("[用户]新增用户失败，请求参数异常。");
            return ResultDo.build(MessageCode.ERROR_SYSTEM_PARAM_FORMAT);
        }
//        systemUserRepository.findByMobile()
        if (bo.getType().equals(EpSystemUserType.merchant)) {
            if (null == bo.getOgnId()) {
                log.error("[用户]新增用户失败，请求参数异常。");
                return ResultDo.build(MessageCode.ERROR_SYSTEM_PARAM_FORMAT);
            } else {
                EpOrganPo organPo = organRepository.findById(bo.getOgnId());
                //用户类型为商户必须要求机构为上线状态
                if (organPo == null) {
                    log.error("[用户]新增用户失败，机构id不存在。");
                    return ResultDo.build(MessageCode.ERROR_ORGAN_NOT_EXISTS);
                }
                if (!organPo.getStatus().equals(EpOrganStatus.online)) {
                    log.error("[用户]新增用户失败，机构未上线。");
                    return ResultDo.build(MessageCode.ERROR_SYSTEM_USER_ORGAN_NOT_ONLINE);
                }
            }

        }
        EpSystemUserPo po = new EpSystemUserPo();
        List<EpSystemRolePo> systemRolePos = bo.getSystemRolePos();

        this.copyBoPropertyToPo(bo, po);
        //盐
        po.setSalt(StringTools.generateShortUrl(po.getMobile(), passwordSaltKey, BizConstant.PASSWORD_SALT_MINLENGTH));
        po.setPassword(CryptTools.aesEncrypt(po.getPassword(), po.getSalt()));
        po.setStatus(EpSystemUserStatus.normal);
        systemUserRepository.insert(po);
        //插入角色
        systemRolePos.forEach(p -> {
            EpSystemUserRolePo systemUserRolePo = new EpSystemUserRolePo();
            systemUserRolePo.setUserId(po.getId());
            systemUserRolePo.setRoleId(p.getId());
            systemUserRoleRepository.insert(systemUserRolePo);
        });
        log.info("[用户]，新增用户成功。id={}。", po.getId());
        return ResultDo.build();
    }

    /**
     * 修改用户
     */
    @Transactional(rollbackFor = Exception.class)
    public ResultDo updateUser(SystemUserBo bo) throws Exception {
        log.info("[用户]修改用户开始。用户对象={}。", bo);
        if (null == bo.getId() || null == bo.getMobile()
                || StringTools.isBlank(bo.getUserName()) || StringTools.isBlank(bo.getPassword())
                || null == bo.getType()) {
            log.error("[用户]修改用户失败，请求参数异常。");
            return ResultDo.build(MessageCode.ERROR_SYSTEM_PARAM_FORMAT);
        }

        if (bo.getType().equals(EpSystemUserType.platform) && null != bo.getOgnId()) {
            log.error("[用户]修改用户失败，请求参数异常。");
            return ResultDo.build(MessageCode.ERROR_SYSTEM_PARAM_FORMAT);
        }
        if (bo.getType().equals(EpSystemUserType.merchant)) {
            if (null == bo.getOgnId()) {
                log.error("[用户]修改用户失败，请求参数异常。");
                return ResultDo.build(MessageCode.ERROR_SYSTEM_PARAM_FORMAT);
            } else {
                EpOrganPo organPo = organRepository.findById(bo.getOgnId());
                //用户类型为商户必须要求机构为上线状态
                if (organPo == null) {
                    log.error("[用户]修改用户失败，机构id不存在。");
                    return ResultDo.build(MessageCode.ERROR_ORGAN_NOT_EXISTS);
                }
                if (!organPo.getStatus().equals(EpOrganStatus.online)) {
                    log.error("[用户]修改用户失败，机构未上线。");
                    return ResultDo.build(MessageCode.ERROR_SYSTEM_USER_ORGAN_NOT_ONLINE);
                }
            }

        }
        if (!systemUserRepository.findById(bo.getId()).isPresent()) {
            log.error("[用户]修改用户失败，该用户不存在。id={}。", bo.getId());
            return ResultDo.build(MessageCode.ERROR_SYSTEM_USER_NOT_EXISTS);
        }

        EpSystemUserPo po = new EpSystemUserPo();
        this.copyBoPropertyToPo(bo, po);
        //盐
        po.setSalt(StringTools.generateShortUrl(po.getMobile(), passwordSaltKey, BizConstant.PASSWORD_SALT_MINLENGTH));
        po.setPassword(CryptTools.aesEncrypt(po.getPassword(), po.getSalt()));
        if (systemUserRepository.updatePo(po) == BizConstant.DB_NUM_ONE) {
            //删除用户角色表记录
            systemUserRoleRepository.deleteLogicByUserId(po.getId());
            List<EpSystemRolePo> newSystemRolePos = bo.getSystemRolePos();
            List<EpSystemUserRolePo> newSystemUserRolePos = Lists.newArrayList();
            newSystemRolePos.forEach(p -> {
                EpSystemUserRolePo systemUserRolePo = new EpSystemUserRolePo();
                systemUserRolePo.setUserId(po.getId());
                systemUserRolePo.setRoleId(p.getId());
                newSystemUserRolePos.add(systemUserRolePo);
            });
            //重新插入用户角色表记录
            systemUserRoleRepository.insert(newSystemUserRolePos);

            log.info("[用户]，修改用户成功。id={}。", po.getId());
            return ResultDo.build();
        } else {
            log.error("[用户]，修改用户失败。id={}。", po.getId());
            return ResultDo.build(MessageCode.ERROR_OPERATE_FAIL);
        }
    }

    /**
     * 删除用户
     *
     * @param userId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public int deleteUser(Long userId) {
        systemUserRoleRepository.deleteLogicByUserId(userId);
        return systemUserRepository.deleteLogical(userId);
    }

    /**
     * 根据id冻结系统用户
     *
     * @param id
     * @return
     */
    public ResultDo freezeById(Long id) {
        if (!systemUserRepository.findById(id).isPresent()) {
            log.error("[用户]冻结失败，该用户不存在。id={}。", id);
            return ResultDo.build(MessageCode.ERROR_SYSTEM_USER_NOT_EXISTS);
        }
        if (systemUserRepository.freezeById(id) == BizConstant.DB_NUM_ONE) {
            log.info("[用户]冻结成功。id={}。", id);
            return ResultDo.build();
        } else {
            log.error("[用户]冻结失败。id={}。", id);
            return ResultDo.build(MessageCode.ERROR_OPERATE_FAIL);
        }
    }

    /**
     * 根据id解冻系统用户
     *
     * @param id
     * @return
     */
    public ResultDo unfreezeById(Long id) {
        if (!systemUserRepository.findById(id).isPresent()) {
            log.error("[用户]解冻失败，该用户不存在。id={}。", id);
            return ResultDo.build(MessageCode.ERROR_SYSTEM_USER_NOT_EXISTS);
        }
        if (systemUserRepository.unfreezeById(id) == BizConstant.DB_NUM_ONE) {
            log.info("[用户]解冻成功。id={}。", id);
            return ResultDo.build();
        } else {
            log.error("[用户]解冻失败。id={}。", id);
            return ResultDo.build(MessageCode.ERROR_OPERATE_FAIL);
        }
    }

    /**
     * 根据id注销系统用户
     *
     * @param id
     * @return
     */
    public ResultDo cancelById(Long id) {
        if (!systemUserRepository.findById(id).isPresent()) {
            log.error("[用户]注销失败，该用户不存在。id={}。", id);
            return ResultDo.build(MessageCode.ERROR_SYSTEM_USER_NOT_EXISTS);
        }
        if (systemUserRepository.cancelById(id) == BizConstant.DB_NUM_ONE) {
            log.info("[用户]注销成功。id={}。", id);
            return ResultDo.build();
        } else {
            log.error("[用户]注销失败。id={}。", id);
            return ResultDo.build(MessageCode.ERROR_OPERATE_FAIL);
        }
    }



    /**
     * 个人设置修改密码
     *
     * @param userId
     * @param oldPsd
     * @param password
     * @return
     * @throws GeneralSecurityException
     */
    public ResultDo updatePassword(Long userId, String oldPsd, String password) throws GeneralSecurityException {
        log.info("[用户]修改用户密码开始。用户id={},原密码={}，新密码={}。", userId, oldPsd, password);
        Optional<EpSystemUserPo> systemUserPoOptional = systemUserRepository.findById(userId);
        if (!systemUserPoOptional.isPresent()) {
            log.error("[用户]修改用户密码失败。用户id={}。", userId);
            return ResultDo.build(MessageCode.ERROR_SYSTEM_USER_NOT_EXISTS);
        }
        EpSystemUserPo systemUserPo = systemUserPoOptional.get();
        String encryptOldPsd = CryptTools.aesEncrypt(oldPsd, systemUserPo.getSalt());
        if (!encryptOldPsd.equals(systemUserPo.getPassword())) {
            log.error("[用户]修改用户密码失败。原密码不正确。");
            return ResultDo.build(MessageCode.ERROR_SYSTEM_USER_OLDPSD_WRONG);
        }
        String encryptPassword = CryptTools.aesEncrypt(password, systemUserPo.getSalt());
        if (systemUserRepository.updatePsdById(userId, encryptPassword) == BizConstant.DB_NUM_ONE) {
            log.info("[用户]修改用户密码成功。用户id={}。", userId);
            return ResultDo.build();
        } else {
            log.error("[用户]修改用户密码失败。用户id={}。", userId);
            return ResultDo.build(MessageCode.ERROR_OPERATE_FAIL);
        }
    }

    private void copyBoPropertyToPo(SystemUserBo bo, EpSystemUserPo po) {
        if (bo.getId() != null) {
            po.setId(bo.getId());
        }
        po.setMobile(bo.getMobile());
        po.setUserName(StringTools.getNullIfBlank(bo.getUserName()));
        po.setSalt(StringTools.getNullIfBlank(bo.getSalt()));
        po.setPassword(StringTools.getNullIfBlank(bo.getPassword()));
        po.setEmail(StringTools.getNullIfBlank(bo.getEmail()));
        po.setType(bo.getType());
        po.setOgnId(bo.getOgnId());
        po.setRemark(StringTools.getNullIfBlank(bo.getRemark()));
    }
}
