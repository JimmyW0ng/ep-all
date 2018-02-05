package com.ep.domain.service;

import com.ep.domain.pojo.po.EpConstantCatalogPo;
import com.ep.domain.repository.ConstantCatalogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description:课程类目服务
 * @Author: CC.F
 * @Date: 21:59 2018/2/5
 */
@Service
public class ConstantCatalogService {
    @Autowired
    private ConstantCatalogRepository constantCatalogRepository;

    public List<EpConstantCatalogPo> getAll(){
        return constantCatalogRepository.getAll();
    }

    public EpConstantCatalogPo insert(EpConstantCatalogPo po) {
        return constantCatalogRepository.create(po);
    }

    public int update(EpConstantCatalogPo po) {
        return constantCatalogRepository.updateConstantCatalogPo(po);
    }

    public EpConstantCatalogPo getById(Long id){
        return constantCatalogRepository.getById(id);
    }

    public void delete(Long id){
        constantCatalogRepository.deleteById(id);
    }
}
