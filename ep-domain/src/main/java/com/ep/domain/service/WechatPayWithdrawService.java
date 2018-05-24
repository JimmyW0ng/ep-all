package com.ep.domain.service;

import com.ep.common.tool.CollectionsTools;
import com.ep.common.tool.DateTools;
import com.ep.domain.constant.BizConstant;
import com.ep.domain.constant.MessageCode;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.WechatPayWithdrawBo;
import com.ep.domain.pojo.po.EpWechatPayBillDetailPo;
import com.ep.domain.pojo.po.EpWechatPayWithdrawDetailPo;
import com.ep.domain.pojo.po.EpWechatPayWithdrawPo;
import com.ep.domain.repository.OrderRepository;
import com.ep.domain.repository.WechatPayWithdrawDetailRepository;
import com.ep.domain.repository.WechatPayWithdrawRepository;
import com.ep.domain.repository.domain.enums.EpWechatPayWithdrawStatus;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.jooq.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

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
    private WechatPayWithdrawDetailRepository wechatPayWithdrawDetailRepository;

    /**
     * 微信支付提现平台分页
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
     * 提现申请
     *
     * @param classId
     * @param courseId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResultDo applyPayWithdrawByClassId(Long classId, Long courseId, String withdrawDeadlineTime, String accountName, String accountNumber) {
        log.info("[微信订单费提现]微信支付订单费提现申请开始，classId={},courseId={}，withdrawDeadlineTime={}，accountName={}，accountNumber={}。"
                , classId, courseId, withdrawDeadlineTime, accountName, accountNumber);
        //该班次上一次提现截止时间
        Timestamp orderDeadline = DateTools.stringToTimestamp(withdrawDeadlineTime, DateTools.TIME_PATTERN);
        //校验是否有状态为wait,submit提现申请
        List<Long> unfinishIds = wechatPayWithdrawRepository.findIdsByClassIdAndStatus(classId, EpWechatPayWithdrawStatus.wait, EpWechatPayWithdrawStatus.submit);
        if (CollectionsTools.isNotEmpty(unfinishIds)) {
            log.error("[微信订单费提现]微信支付订单费提现申请失败，原因：classId={}存在未完成的{}笔提现,ids={}。", classId, unfinishIds.size(), unfinishIds);
            return ResultDo.build(MessageCode.ERROR_WECHAT_PAY_WITHDRAW_UNFINISH_WITHDRAW_EXIST);
        }
        //校验是否有提现中的订单
        List<Long> withdrawApplyIds = orderRepository.findIdsWithdrawApply(classId);
        if (CollectionsTools.isNotEmpty(withdrawApplyIds)) {
            log.error("[微信订单费提现]微信支付订单费提现申请失败，原因：classId={}存在申请中的{}笔订单,ids={}。", classId, unfinishIds.size(), withdrawApplyIds);
            return ResultDo.build(MessageCode.ERROR_WECHAT_PAY_WITHDRAW_WITHDRAW_APPLY_ORDER_EXIST);
        }
        //校验是否有重复支付的订单
        List<Long> duplicatePaidOrder = orderRepository.findDuplicatePaidOrder(classId, orderDeadline);
        if (CollectionsTools.isNotEmpty(duplicatePaidOrder)) {
            log.error("[微信订单费提现]微信支付订单费提现申请失败，原因：classId={}存在重复支付订单， duplicatePaidOrder={}。", classId, duplicatePaidOrder);
            return ResultDo.build(MessageCode.ERROR_WECHAT_PAY_WITHDRAW_DUPLICATEPAID_ORDER_EXIST);
        }
        List<EpWechatPayBillDetailPo> paidList = orderRepository.findWechatPaidOrderBillDetail(classId, orderDeadline);
        if (CollectionsTools.isEmpty(paidList)) {
            log.error("[微信订单费提现]微信支付订单费提现申请失败，原因：classId={}没有可提现订单。", classId);
            return ResultDo.build(MessageCode.ERROR_WECHAT_PAY_WITHDRAW_NO_PAID_ORDERS);
        }
        int wchatPayNum = BizConstant.DB_NUM_ZERO;
        BigDecimal totalAmount = BigDecimal.ZERO;
        BigDecimal wechatPayFee = BigDecimal.ZERO;
        List<EpWechatPayBillDetailPo> withdrawBills = Lists.newArrayList();
        for (EpWechatPayBillDetailPo paid : paidList) {
            if (orderRepository.withdrawApplyOrderById(paid.getOrderId()) == BizConstant.DB_NUM_ZERO) {
                continue;
            }
            wchatPayNum++;
            totalAmount = totalAmount.add(paid.getTotalFee());
            wechatPayFee = wechatPayFee.add(paid.getPoundage());
            withdrawBills.add(paid);
        }
        if (wchatPayNum == BizConstant.DB_NUM_ZERO) {
            log.error("[微信订单费提现]微信支付订单费提现申请失败，原因：classId={}没有可提现订单。", classId);
            return ResultDo.build(MessageCode.ERROR_WECHAT_PAY_WITHDRAW_NO_PAID_ORDERS);
        }
        EpWechatPayWithdrawPo wechatPayWithdrawPo = new EpWechatPayWithdrawPo();
        wechatPayWithdrawPo.setClassId(classId);
        wechatPayWithdrawPo.setCourseId(courseId);
        wechatPayWithdrawPo.setOrderDeadline(orderDeadline);
        wechatPayWithdrawPo.setStatus(EpWechatPayWithdrawStatus.wait);
        //微信支付订单数(即待提现订单数)
        wechatPayWithdrawPo.setWechatPayNum(wchatPayNum);
        //提现总金额
        wechatPayWithdrawPo.setTotalAmount(totalAmount);
        //微信支付手续费
        wechatPayWithdrawPo.setWechatPayFee(wechatPayFee);
        //收款账户名
        wechatPayWithdrawPo.setAccountName(accountName);
        //收款账户号
        wechatPayWithdrawPo.setAccountNumber(accountNumber);
        wechatPayWithdrawRepository.insert(wechatPayWithdrawPo);
        List<EpWechatPayWithdrawDetailPo> withdrawDetails = Lists.newArrayList();
        for (EpWechatPayBillDetailPo withdrawBill : withdrawBills) {
            EpWechatPayWithdrawDetailPo detailPo = new EpWechatPayWithdrawDetailPo();
            detailPo.setWithdrawId(wechatPayWithdrawPo.getId());
            detailPo.setOrderId(withdrawBill.getOrderId());
            detailPo.setOutTradeNo(withdrawBill.getOutTradeNo());
            withdrawDetails.add(detailPo);
        }
        wechatPayWithdrawDetailRepository.insert(withdrawDetails);
        log.info("[微信订单费提现]微信支付订单费提现申请，ep_wechat_pay_withdraw表插入数据。{}。", wechatPayWithdrawPo);
        log.info("[微信订单费提现]微信支付订单费提现申请成功，classId={},courseId={}。", classId, courseId);
        return ResultDo.build();
    }

    /**
     * 审核通过提现申请
     *
     * @param id
     * @return
     */
    public ResultDo submitPayWithdrawById(Long id) {
        log.info("[微信订单费提现]审核通过提现申请开始，id={}。", id);
        if (wechatPayWithdrawRepository.submitPayWithdrawById(id) == BizConstant.DB_NUM_ONE) {
            log.info("[微信订单费提现]审核通过提现申请成功，id={}。", id);
            return ResultDo.build();
        }
        log.error("[微信订单费提现]审核通过提现申请失败，id={}。", id);
        return ResultDo.build(MessageCode.ERROR_OPERATE_FAIL);
    }

    /**
     * 提现完成
     *
     * @param id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResultDo finishPayWithdrawById(Long id, String outWithdrawNo, String payId, BigDecimal withdrawFee, Timestamp paidTime) {
        log.info("[微信订单费提现]提现完成开始，id={}。", id);
        Optional<EpWechatPayWithdrawPo> wechatPayWithdrawOptional = wechatPayWithdrawRepository.findById(id);
        List<Long> orderIds = wechatPayWithdrawDetailRepository.findOrderIdsByWithdrawId(id);
        if (!wechatPayWithdrawOptional.isPresent() || CollectionsTools.isEmpty(orderIds)) {
            log.error("[微信订单费提现]提现完成失败, 数据缺失, id={}, orderIds={}", id, orderIds);
            return ResultDo.build(MessageCode.ERROR_DATA_MISS);
        }
        if (wechatPayWithdrawRepository.finishPayWithdrawById(id, outWithdrawNo, payId, withdrawFee, paidTime) == BizConstant.DB_NUM_ONE) {
            orderRepository.finishPayWithdrawByOrderIds(wechatPayWithdrawOptional.get().getClassId(), orderIds);
            log.info("[微信订单费提现]提现完成成功，wechatPayWithdrawId={},orderIds={}。", id, orderIds.toString());
            return ResultDo.build();
        }
        log.error("[微信订单费提现]提现完成失败，id={}。", id);
        return ResultDo.build(MessageCode.ERROR_OPERATE_FAIL);
    }


    /**
     * 提现拒绝
     *
     * @param id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResultDo refusePayWithdrawById(Long id, String remark) {
        log.info("[微信订单费提现]提现申请拒绝开始，id={}。", id);
        Optional<EpWechatPayWithdrawPo> wechatPayWithdrawOptional = wechatPayWithdrawRepository.findById(id);
        List<Long> orderIds = wechatPayWithdrawDetailRepository.findOrderIdsByWithdrawId(id);
        if (!wechatPayWithdrawOptional.isPresent() || CollectionsTools.isEmpty(orderIds)) {
            log.error("[微信订单费提现]提现完成失败, 数据缺失, id={}, orderIds={}", id, orderIds);
            return ResultDo.build(MessageCode.ERROR_DATA_MISS);
        }
        int num = wechatPayWithdrawRepository.refusePayWithdrawById(id, remark);
        if (num == BizConstant.DB_NUM_ONE) {
            Long classId = wechatPayWithdrawOptional.get().getClassId();
            int count = orderRepository.refuseWithdrawByClassId(classId, orderIds);
            log.info("[微信订单费提现]提现申请拒绝成功，id={},拒绝{}笔订单。", id, count);
            return ResultDo.build();
        }
        log.error("[微信订单费提现]提现申请拒绝失败，id={}。", id);
        return ResultDo.build(MessageCode.ERROR_OPERATE_FAIL);
    }

    public List<EpWechatPayWithdrawPo> findByClassId(Long classId) {
        return wechatPayWithdrawRepository.findByClassId(classId);
    }

}
