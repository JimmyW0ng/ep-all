package com.ep.domain.component;

import com.ep.domain.repository.SystemRoleAuthorityRepository;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

/**
 * @Description: 鉴权组件
 * @Author: J.W
 * @Date: 上午9:37 2018/1/19
 */
@Component
public class SecurityAuth {

    @Autowired
    private SystemRoleAuthorityRepository systemRoleAuthorityRepository;

    /**
     * 加载用户的权限
     *
     * @param roleIds
     * @return
     */
    public Collection<GrantedAuthority> loadCurrentUserGrantedAuthorities(Long... roleIds) {
        List<String> auths = systemRoleAuthorityRepository.getAuthoritesByRoleIds(roleIds);
        Collection<GrantedAuthority> authorities = Lists.newArrayList();
        auths.forEach(item -> authorities.add(new SimpleGrantedAuthority(item)));
        return authorities;
    }

}
