package com.ep.domain.service;

import com.ep.domain.repository.SystemUserRoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 系统用户-角色服务类
 * @Author: CC.F
 * @Date: 16:49 2018/1/26
 */
@Service
@Slf4j
public class SystemUserRoleService {
    @Autowired
    private SystemUserRoleRepository systemUserRoleRepository;

    /**
     * 根据用户id获得角色id的集合
     * @param userId
     * @return
     */
    public List<Long> getRoleIdsByUserId(Long userId){
        return systemUserRoleRepository.getRoleIdsByUserId(userId);
    }
}
