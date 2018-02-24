package com.ep.domain.service;

import com.ep.domain.pojo.po.EpConstantTagPo;
import com.ep.domain.repository.ConstantTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 标签服务类
 * @Author: CC.F
 * @Date: 11:22 2018/2/9
 */
@Service
public class ConstantTagService {
    @Autowired
    private ConstantTagRepository constantTagRepository;

    /**
     * 根据课程类目id和机构id获取标签
     * @param catalogId
     * @param ognId
     * @return
     */
    public List<EpConstantTagPo> findByCatalogIdAndOgnId(Long catalogId, Long ognId) {
        return constantTagRepository.findByCatalogIdAndOgnId(catalogId, ognId);
    }

    /**
     * 根据机构id获取标签
     * @param ognId
     * @return
     */
    public List<EpConstantTagPo> findByOgnId(Long ognId) {
        return constantTagRepository.findByOgnId(ognId);
    }

    /**
     * 新增EpConstantTagPo
     * @param po
     */
    public EpConstantTagPo createPo(EpConstantTagPo po){
        return constantTagRepository.insertNew(po);
    }

    /**
     * 根据id逻辑删除
     * @param id
     */
    public void deleteById(Long id){
        constantTagRepository.deleteById(id);
    }
}
