package com.ep.domain.service;

import com.ep.domain.pojo.po.EpSystemRoleAuthorityPo;
import com.ep.domain.pojo.po.EpSystemRolePo;
import com.ep.domain.repository.SystemRoleAuthorityRepository;
import com.ep.domain.repository.SystemRoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.jooq.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

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

    public Page<EpSystemRolePo> findbyPageAndCondition(Pageable pageable, Collection<? extends Condition> condition) {
        return systemRoleRepository.findByPageable(pageable, condition);
    }

    @Transactional(rollbackFor = Exception.class)
    public void createSystemRole(EpSystemRolePo po, List<EpSystemRoleAuthorityPo> list) throws Exception {
        systemRoleRepository.insert(po);
        systemRoleAuthorityRepository.insert(list);
    }
}
