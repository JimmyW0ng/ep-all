package com.ep.domain.service;

import com.ep.common.tool.DateTools;
import com.ep.domain.pojo.bo.SystemMenuBo;
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

    public List<EpSystemMenuPo> getAll() {
        return systemMenuRepository.getAll();
    }

    public List<SystemMenuBo> getLeftMenu() {
        List<SystemMenuBo> list = systemMenuRepository.getAllMenu(1L);
        list.forEach(p -> {
            p.setChildList(systemMenuRepository.getAllMenu(p.getId()));
        });
        return list;
    }

    public EpSystemMenuPo insert(EpSystemMenuPo po) {
        return systemMenuRepository.create(po);
    }

    public void update(EpSystemMenuPo po) {
        po.setUpdateAt(DateTools.getCurrentDateTime());
        systemMenuRepository.update(po);
    }
}
