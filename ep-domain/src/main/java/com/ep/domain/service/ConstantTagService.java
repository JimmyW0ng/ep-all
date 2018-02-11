package com.ep.domain.service;

import com.ep.domain.pojo.po.EpConstantTagPo;
import com.ep.domain.repository.ConstantTagPepository;
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
    private ConstantTagPepository constantTagPepository;

    /**
     * 根据课程类目id和机构id获取标签
     * @param catalogId
     * @param ognId
     * @return
     */
    public List<EpConstantTagPo> findByCatalogIdAndOgnId(Long catalogId, Long ognId) {
        return constantTagPepository.findByCatalogIdAndOgnId(catalogId, ognId);
    }
}
