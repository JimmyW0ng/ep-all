package com.ep.domain.service;

import com.ep.common.tool.DateTools;
import com.ep.domain.constant.BizConstant;
import com.ep.domain.constant.MessageCode;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.WechatPayWithdrawBo;
import com.ep.domain.pojo.po.EpWechatPayWithdrawPo;
import com.ep.domain.repository.OrderRepository;
import com.ep.domain.repository.WechatPayBillDetailRepository;
import com.ep.domain.repository.WechatPayBillRepository;
import com.ep.domain.repository.WechatPayWithdrawRepository;
import com.ep.domain.repository.domain.enums.EpWechatPayWithdrawStatus;
import lombok.extern.slf4j.Slf4j;
import org.jooq.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private WechatPayBillDetailRepository wechatPayBillDetailRepository;
    @Autowired
    private WechatPayBillRepository wechatPayBillRepository;

    public Optional<EpWechatPayWithdrawPo> findById(Long id) {
        return wechatPayWithdrawRepository.findById(id);
    }

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
     * @param classId
     * @param courseId
     * @return
     */
    public ResultDo applyPayWithdrawByClassId(Long classId, Long courseId, String withdrawDeadline, String accountName, String accountNumber) {
        log.info("[微信订单费提现]订单微信支付订单费提现申请开始，classId={},courseId={}。", classId, courseId);
        //该班次上一次提现截止时间
        EpWechatPayWithdrawPo wechatPayWithdrawPo = new EpWechatPayWithdrawPo();
        wechatPayWithdrawPo.setClassId(classId);
        wechatPayWithdrawPo.setCourseId(courseId);
        Timestamp orderDeadline = DateTools.addDate(DateTools.stringToTimestamp(withdrawDeadline, DateTools.DATE_FMT_3), BizConstant.DB_NUM_ONE);
        wechatPayWithdrawPo.setOrderDeadline(orderDeadline);
        wechatPayWithdrawPo.setStatus(EpWechatPayWithdrawStatus.wait);
        //微信支付订单数(即待提现订单数)
        wechatPayWithdrawPo.setWechatPayNum(orderRepository.countWaitWithdrawOrderByClassId(classId, orderDeadline));
        //提现总金额
        wechatPayWithdrawPo.setTotalAmount(orderRepository.sumWaitWithdrawOrderByClassId(classId, orderDeadline));
        //微信支付手续费
        wechatPayWithdrawPo.setWechatPayFee(orderRepository.sumWaitWithdrawPoundageByClassId(classId, orderDeadline));
        wechatPayWithdrawPo.setAccountName(accountName);
        wechatPayWithdrawPo.setAccountNumber(accountNumber);
        wechatPayWithdrawRepository.insert(wechatPayWithdrawPo);
        log.info("[微信订单费提现]订单微信支付订单费提现申请，ep_wechat_pay_withdraw表插入数据。{}。", wechatPayWithdrawPo);
        log.info("[微信订单费提现]订单微信支付订单费提现申请成功，classId={},courseId={}。", classId, courseId);
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
    public ResultDo finishPayWithdrawById(Long id) {
        log.info("[微信订单费提现]提现完成开始，id={}。", id);
        Optional<EpWechatPayWithdrawPo> wechatPayWithdrawOptional = wechatPayWithdrawRepository.findById(id);
        Long classId = wechatPayWithdrawOptional.get().getClassId();
        EpWechatPayWithdrawPo lastFinishWithdrawPo = wechatPayWithdrawRepository.getLastFinishWithdrawByClassId(classId);

        if (wechatPayWithdrawRepository.finishPayWithdrawById(id) == BizConstant.DB_NUM_ONE) {
            List<Long> orderIds = wechatPayWithdrawRepository.findWaitWithdrawOrderIds(classId,
                    lastFinishWithdrawPo != null ? lastFinishWithdrawPo.getOrderDeadline() : null, wechatPayWithdrawOptional.get().getOrderDeadline());
            orderRepository.finishPayWithdrawByOrderIds(orderIds);
            log.info("[微信订单费提现]提现完成成功，wechatPayWithdrawId={},orderIds={}。", id, orderIds.toString());
            return ResultDo.build();
        }
        log.error("[微信订单费提现]提现完成失败，id={}。", id);
        return ResultDo.build(MessageCode.ERROR_OPERATE_FAIL);
    }


    /**
     * 提现完成
     *
     * @param id
     * @return
     */
    public ResultDo refusePayWithdrawById(Long id, String remark) {
        log.info("[微信订单费提现]提现申请拒绝开始，id={}。", id);
        if (wechatPayWithdrawRepository.refusePayWithdrawById(id, remark) == BizConstant.DB_NUM_ONE) {
            log.info("[微信订单费提现]提现申请拒绝成功，id={}。", id);
            return ResultDo.build();
        }
        log.error("[微信订单费提现]提现申请拒绝失败，id={}。", id);
        return ResultDo.build(MessageCode.ERROR_OPERATE_FAIL);
    }

    public List<EpWechatPayWithdrawPo> findByClassId(Long classId) {
        return wechatPayWithdrawRepository.findByClassId(classId);
    }

}
