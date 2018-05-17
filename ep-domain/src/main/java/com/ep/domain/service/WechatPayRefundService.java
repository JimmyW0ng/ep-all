package com.ep.domain.service;

import com.ep.domain.constant.BizConstant;
import com.ep.domain.pojo.po.EpWechatPayRefundPo;
import com.ep.domain.repository.WechatPayRefundRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description:
 * @Author: CC.F
 * @Date: 23:21 2018/5/15
 */
@Service
public class WechatPayRefundService {
    @Autowired
    private WechatPayRefundRepository wechatPayRefundRepository;

    /**
     * 商户根据商户订单号outTradeNo获取退款结果。若所有退款申请均未回调成功则显示所有记录；若退款回调成功则成功记录
     *
     * @param outTradeNo
     * @return
     */
    public List<EpWechatPayRefundPo> merchantFindByOutTradeNo(String outTradeNo) {
        List<EpWechatPayRefundPo> list = wechatPayRefundRepository.findSuccessPoByOutTradeNo(outTradeNo);
        if (list.size() == BizConstant.DB_NUM_ONE) {
            //若退款回调成功则成功记录
            return list;
        } else {
            //若所有退款申请均未回调成功则显示所有记录
            return wechatPayRefundRepository.findByOutTradeNo(outTradeNo);
        }
    }
}
