package com.ep.domain.service;

import com.ep.domain.pojo.bo.WechatPayWithdrawBo;
import com.ep.domain.repository.WechatPayWithdrawRepository;
import org.jooq.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * @Description:
 * @Author: CC.F
 * @Date: 23:24 2018/5/16
 */
@Service
public class WechatPayWithdrawService {
    @Autowired
    private WechatPayWithdrawRepository wechatPayWithdrawRepository;

    /**
     * 商户提现分页
     *
     * @param pageable
     * @param condition
     * @return
     */
    public Page<WechatPayWithdrawBo> findbyPageAndCondition(Pageable pageable, Collection<? extends Condition> condition) {
        return wechatPayWithdrawRepository.findbyPageAndCondition(pageable, condition);
    }
}
