package com.ep.domain.service;

import com.ep.domain.constant.BizConstant;
import com.ep.domain.constant.MessageCode;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.po.EpSystemRolePo;
import com.ep.domain.pojo.po.EpSystemUserPo;
import com.ep.domain.pojo.po.EpSystemUserRolePo;
import com.ep.domain.repository.SystemUserRepository;
import com.ep.domain.repository.SystemUserRoleRepository;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
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

    public Optional<EpSystemUserPo> findById(Long id) {
        return systemUserRepository.findById(id);

    }

    public Page<EpSystemUserPo> findbyPageAndCondition(Pageable pageable, Collection<? extends Condition> condition) {
        return systemUserRepository.findByPageable(pageable, condition);
    }

    /**
     * 新增用户
     *
     * @param po
     * @param list
     */
    @Transactional(rollbackFor = Exception.class)
    public EpSystemUserPo createUser(EpSystemUserPo po, List<EpSystemRolePo> list) {
        EpSystemUserPo insertPo = systemUserRepository.insertNew(po);
        if (null != insertPo) {
            list.forEach(p -> {
                EpSystemUserRolePo systemUserRolePo = new EpSystemUserRolePo();
                systemUserRolePo.setUserId(insertPo.getId());
                systemUserRolePo.setRoleId(p.getId());
                systemUserRoleRepository.insert(systemUserRolePo);
            });
        }
        return insertPo;
    }

    /**
     * 修改用户
     *
     * @param po
     * @param list
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(EpSystemUserPo po, List<EpSystemRolePo> list) {
        int count = systemUserRepository.updateReturnEffRowsCount(po);
        if (1 == count) {
            List<Long> roleOldList = systemUserRoleRepository.getRoleIdsByUserId(po.getId());
            Set<Long> roleOldSet = new HashSet<>(roleOldList);
            Set<Long> roleNewSet = new HashSet<>();
            list.forEach(p -> {
                roleNewSet.add(p.getId());
            });
            //差集，roleNewSet有, roleOldSet无
            Set<Long> diffAdd = Sets.difference(roleNewSet, roleOldSet);
            //差集，roleOldSet有, roleNewSet无
            Set<Long> diffDel = Sets.difference(roleOldSet, roleNewSet);

            //删除 roleOldSet有, roleNewSet无
            diffDel.forEach(p -> {
                systemUserRoleRepository.deleteByUserIdAndRoleId(po.getId(), p);
            });

            Map map = Maps.newHashMap();
            list.forEach(p -> {
                map.put(p.getId(), p);
            });

            List<EpSystemUserRolePo> systemUserRolePoNew = Lists.newArrayList();
            diffAdd.forEach(p -> {
                EpSystemUserRolePo systemUserRolePoAdd = (EpSystemUserRolePo) map.get(p);
                systemUserRolePoAdd.setUserId(po.getId());
                systemUserRolePoNew.add(systemUserRolePoAdd);
            });
            //插入  roleNewSet有， roleOldSet无
            systemUserRoleRepository.insert(systemUserRolePoNew);
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
        systemUserRoleRepository.deleteByUserId(userId);
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
}
