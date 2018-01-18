package com.ep.domain.service;

import com.ep.domain.pojo.dto.SystemMenuDto;
import com.ep.domain.pojo.po.EpSystemMenuPo;
import com.ep.domain.repository.SystemMenuRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 菜单服务类
 * @Author: J.W
 * @Date: 上午9:30 2018/1/14
 */
@Slf4j
@Service
public class SystemMenuService {
    @Autowired
    private SystemMenuRepository systemMenuRepository;

    public EpSystemMenuPo getById(Long id) {
        return systemMenuRepository.findById(id);

    }


    public List<SystemMenuDto> getAllMenu() {
        List<SystemMenuDto> list = systemMenuRepository.getAllMenu(0L);
        list.forEach(p -> {
            p.setChildList(systemMenuRepository.getAllMenu(p.getId()));
        });
        return list;
    }

}
