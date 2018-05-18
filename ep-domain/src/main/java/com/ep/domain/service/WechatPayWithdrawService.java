package com.ep.domain.service;

import com.ep.common.tool.DateTools;
import com.ep.common.tool.SerialNumberTools;
import com.ep.domain.constant.BizConstant;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.WechatPayWithdrawBo;
import com.ep.domain.pojo.po.EpWechatPayWithdrawPo;
import com.ep.domain.repository.OrderRepository;
import com.ep.domain.repository.WechatPayBillDetailRepository;
import com.ep.domain.repository.WechatPayWithdrawRepository;
import com.ep.domain.repository.domain.enums.EpOrderPayStatus;
import com.ep.domain.repository.domain.enums.EpOrderPayType;
import com.ep.domain.repository.domain.enums.EpOrderStatus;
import com.ep.domain.repository.domain.enums.EpWechatPayWithdrawStatus;
import lombok.extern.slf4j.Slf4j;
import org.jooq.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Collection;

/**
 * @Description:
 * @Author: CC.F
 * @Date: 23:24 2018/5/16
 */
@Slf4j
@Service
public class WechatPayWithdrawService {
    @Autowired
    private WechatPayWithdrawRepository wechatPayWithdrawRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private WechatPayBillDetailRepository wechatPayBillDetailRepository;

    /**
     * 微信支付提现申请分页
     *
     * @param pageable
     * @param condition
     * @return
     */
    public Page<WechatPayWithdrawBo> findbyPageAndCondition(Pageable pageable, Collection<? extends Condition> condition) {
        return wechatPayWithdrawRepository.findbyPageAndCondition(pageable, condition);
    }

    /**
     * 按班次统计提现成功订单数
     *
     * @param classId
     * @return
     */
    public int countPayWithdrawByClassId(Long classId) {
        return wechatPayWithdrawRepository.countPayWithdrawByClassId(classId);
    }

    /**
     * 最近一次提现记录
     *
     * @param classId
     * @return
     */
    public EpWechatPayWithdrawPo getLastWithdrawByClassId(Long classId) {
        return wechatPayWithdrawRepository.getLastWithdrawByClassId(classId);
    }

    /**
     * @param classId
     * @param courseId
     * @return
     */
    public ResultDo applyPayWithdrawByClassId(Long classId, Long courseId, String withdrawDeadline) {
        log.info("[微信报名费提现]订单微信支付报名费提现申请开始，classId={},courseId={}。", classId, courseId);
        //该班次上一次提现记录
        EpWechatPayWithdrawPo lastWechatPayWithdrawPo = wechatPayWithdrawRepository.getLastWithdrawByClassId(classId);
        Timestamp startTime = null;
        if (null != lastWechatPayWithdrawPo) {
            startTime = lastWechatPayWithdrawPo.getOrderDeadline();
        }
        //该班次上一次提现截止时间
        EpWechatPayWithdrawPo wechatPayWithdrawPo = new EpWechatPayWithdrawPo();
        wechatPayWithdrawPo.setClassId(classId);
        wechatPayWithdrawPo.setCourseId(courseId);
        Timestamp orderDeadline = DateTools.addDate(DateTools.stringToTimestamp(withdrawDeadline, DateTools.DATE_FMT_3), BizConstant.DB_NUM_ONE);
        wechatPayWithdrawPo.setOrderDeadline(orderDeadline);
        wechatPayWithdrawPo.setStatus(EpWechatPayWithdrawStatus.wait);
        EpOrderStatus[] orderStatuses = new EpOrderStatus[]{EpOrderStatus.success, EpOrderStatus.opening, EpOrderStatus.end};
        //报名订单数（状态为success，opening，end）
        wechatPayWithdrawPo.setOrderNum(orderRepository.countByClassIdAndOrderStatus(courseId, orderStatuses));
        //线下支付订单数
        wechatPayWithdrawPo.setOfflinePayNum(orderRepository.countByClassIdAndPayTypeAndPayStatus(classId, EpOrderPayType.offline, EpOrderPayStatus.paid, startTime, orderDeadline));
        //微信支付订单数
        wechatPayWithdrawPo.setWechatPayNum(orderRepository.countByClassIdAndPayTypeAndPayStatus(classId, EpOrderPayType.wechat_pay, EpOrderPayStatus.paid, startTime, orderDeadline));
        //提现总金额
        wechatPayWithdrawPo.setTotalAmount(wechatPayBillDetailRepository.sumWithdrawFeeByClassId(classId, startTime, orderDeadline));
        //微信支付手续费
        wechatPayWithdrawPo.setWechatPayFee(wechatPayBillDetailRepository.sumWechatPayFeeByClassId(classId, startTime, orderDeadline));
        //提现订单号
        wechatPayWithdrawPo.setWithdrawNo(SerialNumberTools.generateWithdrawNo(classId));
        wechatPayWithdrawRepository.insert(wechatPayWithdrawPo);
        log.info("[微信报名费提现]订单微信支付报名费提现申请，ep_wechat_pay_withdraw表插入数据。{}。", wechatPayWithdrawPo);
        log.info("[微信报名费提现]订单微信支付报名费提现申请成功，classId={},courseId={}。", classId, courseId);
        return ResultDo.build();
    }
}
