package com.ep.domain.service;

import com.ep.domain.repository.WechatPayBillDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @Author: CC.F
 * @Date: 15:09 2018/5/21/021
 */
@Service
public class WechatPayBillDetailService {

    @Autowired
    private WechatPayBillDetailRepository wechatPayBillDetailRepository;


//    public SumWechatBillDetailDto sumDtoByPayStatus(String tradeState,String refundStatus){
//        return
//    }
}
