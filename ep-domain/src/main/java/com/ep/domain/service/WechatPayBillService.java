package com.ep.domain.service;

import com.ep.domain.pojo.po.EpWechatPayBillPo;
import com.ep.domain.repository.WechatPayBillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @Author: CC.F
 * @Date: 15:45 2018/5/18/018
 */
@Service
public class WechatPayBillService {
    @Autowired
    private WechatPayBillRepository wechatPayBillRepository;

    public EpWechatPayBillPo getLastPayBill() {
        return wechatPayBillRepository.getLastPayBill();
    }
}
