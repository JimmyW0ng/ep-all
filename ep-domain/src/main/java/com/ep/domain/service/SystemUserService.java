package com.ep.domain.service;

import com.ep.domain.pojo.po.EpSystemRolePo;
import com.ep.domain.pojo.po.EpSystemUserPo;
import com.ep.domain.pojo.po.EpSystemUserRolePo;
import com.ep.domain.repository.SystemUserRepository;
import com.ep.domain.repository.SystemUserRoleRepository;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.jooq.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public EpSystemUserPo getById(Long id) {
        return systemUserRepository.getById(id);

    }

    public Page<EpSystemUserPo> findbyPageAndCondition(Pageable pageable, Collection<? extends Condition> condition) {
        return systemUserRepository.findByPageable(pageable, condition);
    }

    /**
     * 新增用户
     * @param po
     * @param list
     */
    @Transactional(rollbackFor = Exception.class)
    public void createUser(EpSystemUserPo po, List<EpSystemRolePo> list) throws Exception {
        EpSystemUserPo insertPo = systemUserRepository.insertNew(po);
        if(null!=insertPo){
            list.forEach(p->{
                EpSystemUserRolePo systemUserRolePo = new EpSystemUserRolePo();
                systemUserRolePo.setUserId(insertPo.getId());
                systemUserRolePo.setRoleId(p.getId());
                systemUserRolePo.setRoleCode(p.getRoleCode());
                systemUserRoleRepository.insert(systemUserRolePo);
            });
        }
    }

    /**
     * 修改用户
     * @param po
     * @param list
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(EpSystemUserPo po, List<EpSystemRolePo> list) throws Exception {
        int count = systemUserRepository.updateReturnEffRowsCount(po);
        if(1==count){
            List<Long> roleOldList=systemUserRoleRepository.getRoleIdsByUserId(po.getId());
            Set<Long> roleOldSet= new HashSet<>(roleOldList);
            Set<Long> roleNewSet= new HashSet<>();
            list.forEach(p->{
                roleNewSet.add(p.getId());
            });
            Set<Long> diffAdd = Sets.difference(roleNewSet, roleOldSet);//差集，roleNewSet有, roleOldSet无
            Set<Long> diffDel = Sets.difference(roleOldSet, roleNewSet);//差集，roleOldSet有, roleNewSet无

            //删除 roleOldSet有, roleNewSet无
            diffDel.forEach(p->{
                systemUserRoleRepository.deleteByUserIdAndRoleId(po.getId(),p);
            });
//            list.forEach(p->{
//                EpSystemUserRolePo systemUserRolePo = new EpSystemUserRolePo();
//                systemUserRolePo.setUserId(insertPo.getId());
//                systemUserRolePo.setRoleId(p.getId());
//                systemUserRolePo.setRoleCode(p.getRoleCode());
//                systemUserRoleRepository.insert(systemUserRolePo);
//            });

        }
    }

}
