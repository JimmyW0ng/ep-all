package com.ep.domain.service;

import com.ep.domain.pojo.po.EpSystemRoleAuthorityPo;
import com.ep.domain.pojo.po.EpSystemRolePo;
import com.ep.domain.repository.SystemRoleAuthorityRepository;
import com.ep.domain.repository.SystemRoleRepository;
import com.ep.domain.repository.SystemUserRoleRepository;
import com.ep.domain.repository.domain.enums.EpSystemRoleTarget;
import com.ep.domain.repository.domain.enums.EpSystemUserType;
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
 * @Description: 系统角色服务类
 * @Author: CC.F
 * @Date: 17:10 2018/1/24
 */
@Slf4j
@Service
public class SystemRoleService {
    @Autowired
    private SystemRoleRepository systemRoleRepository;
    @Autowired
    private SystemRoleAuthorityRepository systemRoleAuthorityRepository;
    @Autowired
    private SystemUserRoleRepository systemUserRoleRepository;

    public EpSystemRolePo getById(Long id){
        return systemRoleRepository.getById(id);
    }

    public Page<EpSystemRolePo> findbyPageAndCondition(Pageable pageable, Collection<? extends Condition> condition) {
        return systemRoleRepository.findByPageable(pageable, condition);
    }

    public List<EpSystemRolePo> getAllRoleByUserType(EpSystemUserType type){
        if(type.equals(EpSystemUserType.merchant)){
            return systemRoleRepository.getAllRoleByTarget(EpSystemRoleTarget.backend);
        }else if(type.equals(EpSystemUserType.platform)){
            return systemRoleRepository.getAllRoleByTarget(EpSystemRoleTarget.admin);

        }else{
            return systemRoleRepository.getAllRoleByTarget(null);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public EpSystemRolePo createSystemRole(EpSystemRolePo po, List<EpSystemRoleAuthorityPo> list) {
        EpSystemRolePo insertPo=systemRoleRepository.insertNew(po);
        list.forEach(p->{
            p.setRoleId(insertPo.getId());
        });
        systemRoleAuthorityRepository.insert(list);
        return insertPo;
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateSystemRole(EpSystemRolePo po, List<EpSystemRoleAuthorityPo> list) {
        int count=systemRoleRepository.updateReturnEffRowsCount(po);
        List<Long> menuOldList=systemRoleAuthorityRepository.getMenuIdByRole(po.getId());
        Set<Long> menuOldSet= new HashSet<>(menuOldList);
        Set<Long> menuNewSet= new HashSet<>();
        list.forEach(p->{
            menuNewSet.add(p.getMenuId());
        });
        Set<Long> diffAdd = Sets.difference(menuNewSet, menuOldSet);//差集，menuNewSet有, menuOldSet无
        Set<Long> diffDel = Sets.difference(menuOldSet, menuNewSet);//差集，menuOldSet有, menuNewSet无

        //删除 menuOldSet有, menuNewSet无
        diffDel.forEach(p->{
            systemRoleAuthorityRepository.deleteByRoleIdAndMenuId(po.getId(),p);
        });

        Map map= Maps.newHashMap();
        list.forEach(p->{
            map.put(p.getMenuId(),p);
        });

        List<EpSystemRoleAuthorityPo> systemRoleAuthorityPoNew= Lists.newArrayList();
        diffAdd.forEach(p->{
            EpSystemRoleAuthorityPo systemRoleAuthorityPoAdd=(EpSystemRoleAuthorityPo)map.get(p);
            systemRoleAuthorityPoAdd.setRoleId(po.getId());
            systemRoleAuthorityPoNew.add(systemRoleAuthorityPoAdd);
        });
        //插入  menuNewSet有， menuOldSet无
        systemRoleAuthorityRepository.insert(systemRoleAuthorityPoNew);
    }

    @Transactional(rollbackFor = Exception.class)
    public int delete(Long id) {
        systemRoleAuthorityRepository.deleteByRoleId(id);
        systemUserRoleRepository.deleteByRoleId(id);
        return systemRoleRepository.deleteLogical(id);
    }

}
