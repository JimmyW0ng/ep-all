package com.ep.domain.service;

import com.ep.domain.pojo.po.EpSystemUserPo;
import com.ep.domain.repository.SystemUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.jooq.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * @Description: 用户服务类
 * @Author: CC.F
 * @Date: 10:33 2018/1/24
 */
@Slf4j
@Service
public class SystemUserService {
    @Autowired
    private SystemUserRepository systemUserRepository;

//    public EpSystemMenuPo getById(Long id) {
//        return systemMenuRepository.findById(id);
//
//    }

    public Page<EpSystemUserPo> findbyPageAndCondition(Pageable pageable, Collection<? extends Condition> condition) {
        return systemUserRepository.findByPageable(pageable, condition);
    }

}
