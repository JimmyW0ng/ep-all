package com.ep.domain.service;

import com.ep.domain.pojo.po.EpSystemRoleAuthorityPo;
import com.ep.domain.repository.SystemRoleAuthorityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 角色权限服务类
 * @Author: CC.F
 * @Date: 23:35 2018/1/24
 */
@Slf4j
@Service
public class SystemRoleAuthorityService {
    @Autowired
    private SystemRoleAuthorityRepository systemRoleAuthorityRepository;

    public void insertPos(List<EpSystemRoleAuthorityPo> pos) {
        systemRoleAuthorityRepository.insert(pos);
    }

    public List<Long> getMenuIdByRole(Long roleId){
        return systemRoleAuthorityRepository.getMenuIdByRole(roleId);
    }
}
