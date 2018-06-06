package com.ep.domain.service;

import com.ep.domain.pojo.po.EpSystemDictPo;
import com.ep.domain.repository.SystemDictRepository;
import org.jooq.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * @Description: 字典服务
 * @Author: CC.F
 * @Date: 20:18 2018/6/5
 */
@Service
public class SystemDictService {
    @Autowired
    private SystemDictRepository systemDictRepository;

    public Page<EpSystemDictPo> findbyPageAndCondition(Pageable pageable, Collection<? extends Condition> condition) {
        return systemDictRepository.findByPageable(pageable, condition);
    }
}
