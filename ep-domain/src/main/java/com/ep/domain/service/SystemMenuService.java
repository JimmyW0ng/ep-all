package com.ep.domain.service;

import com.ep.domain.pojo.dto.SystemMenuDto;
import com.ep.domain.pojo.po.EpSystemMenuPo;
import com.ep.domain.repository.SystemMenuRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by fcc on 2018/1/17.
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
