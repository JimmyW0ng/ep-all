/*
 * This file is generated by jOOQ.
*/
package com.ep.domain.repository.domain.tables.records;


import com.ep.domain.repository.domain.tables.EpWechatPayBillDetail;
import org.jooq.Record1;
import org.jooq.impl.UpdatableRecordImpl;

import javax.annotation.Generated;
import java.math.BigDecimal;
import java.sql.Timestamp;


/**
 * 微信支付对账明细表
 */
@Generated(
        value = {
                "http://www.jooq.org",
                "jOOQ version:3.9.6"
        },
        comments = "This class is generated by jOOQ"
)
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class EpWechatPayBillDetailRecord extends UpdatableRecordImpl<EpWechatPayBillDetailRecord> {

    private static final long serialVersionUID = -2061825450;

    /**
     * Setter for <code>ep.ep_wechat_pay_bill_detail.id</code>. 主键
     */
    public EpWechatPayBillDetailRecord setId(Long value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_wechat_pay_bill_detail.id</code>. 主键
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>ep.ep_wechat_pay_bill_detail.bill_id</code>. 对账id
     */
    public EpWechatPayBillDetailRecord setBillId(Long value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_wechat_pay_bill_detail.bill_id</code>. 对账id
     */
    public Long getBillId() {
        return (Long) get(1);
    }

    /**
     * Setter for <code>ep.ep_wechat_pay_bill_detail.ogn_id</code>. 机构id
     */
    public EpWechatPayBillDetailRecord setOgnId(Long value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_wechat_pay_bill_detail.ogn_id</code>. 机构id
     */
    public Long getOgnId() {
        return (Long) get(2);
    }

    /**
     * Setter for <code>ep.ep_wechat_pay_bill_detail.course_id</code>. 课程id
     */
    public EpWechatPayBillDetailRecord setCourseId(Long value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_wechat_pay_bill_detail.course_id</code>. 课程id
     */
    public Long getCourseId() {
        return (Long) get(3);
    }

    /**
     * Setter for <code>ep.ep_wechat_pay_bill_detail.class_id</code>. 班次id
     */
    public EpWechatPayBillDetailRecord setClassId(Long value) {
        set(4, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_wechat_pay_bill_detail.class_id</code>. 班次id
     */
    public Long getClassId() {
        return (Long) get(4);
    }

    /**
     * Setter for <code>ep.ep_wechat_pay_bill_detail.order_id</code>. 订单id
     */
    public EpWechatPayBillDetailRecord setOrderId(Long value) {
        set(5, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_wechat_pay_bill_detail.order_id</code>. 订单id
     */
    public Long getOrderId() {
        return (Long) get(5);
    }

    /**
     * Setter for <code>ep.ep_wechat_pay_bill_detail.transaction_time</code>. 交易时间
     */
    public EpWechatPayBillDetailRecord setTransactionTime(String value) {
        set(6, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_wechat_pay_bill_detail.transaction_time</code>. 交易时间
     */
    public String getTransactionTime() {
        return (String) get(6);
    }

    /**
     * Setter for <code>ep.ep_wechat_pay_bill_detail.appid</code>. appid
     */
    public EpWechatPayBillDetailRecord setAppid(String value) {
        set(7, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_wechat_pay_bill_detail.appid</code>. appid
     */
    public String getAppid() {
        return (String) get(7);
    }

    /**
     * Setter for <code>ep.ep_wechat_pay_bill_detail.mch_id</code>. 商户号
     */
    public EpWechatPayBillDetailRecord setMchId(String value) {
        set(8, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_wechat_pay_bill_detail.mch_id</code>. 商户号
     */
    public String getMchId() {
        return (String) get(8);
    }

    /**
     * Setter for <code>ep.ep_wechat_pay_bill_detail.sub_mch_id</code>. 子商户号
     */
    public EpWechatPayBillDetailRecord setSubMchId(String value) {
        set(9, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_wechat_pay_bill_detail.sub_mch_id</code>. 子商户号
     */
    public String getSubMchId() {
        return (String) get(9);
    }

    /**
     * Setter for <code>ep.ep_wechat_pay_bill_detail.device_no</code>. 设备号
     */
    public EpWechatPayBillDetailRecord setDeviceNo(String value) {
        set(10, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_wechat_pay_bill_detail.device_no</code>. 设备号
     */
    public String getDeviceNo() {
        return (String) get(10);
    }

    /**
     * Setter for <code>ep.ep_wechat_pay_bill_detail.transaction_id</code>. 微信支付订单号
     */
    public EpWechatPayBillDetailRecord setTransactionId(String value) {
        set(11, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_wechat_pay_bill_detail.transaction_id</code>. 微信支付订单号
     */
    public String getTransactionId() {
        return (String) get(11);
    }

    /**
     * Setter for <code>ep.ep_wechat_pay_bill_detail.out_trade_no</code>. 商户订单号
     */
    public EpWechatPayBillDetailRecord setOutTradeNo(String value) {
        set(12, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_wechat_pay_bill_detail.out_trade_no</code>. 商户订单号
     */
    public String getOutTradeNo() {
        return (String) get(12);
    }

    /**
     * Setter for <code>ep.ep_wechat_pay_bill_detail.openid</code>. 用户标识
     */
    public EpWechatPayBillDetailRecord setOpenid(String value) {
        set(13, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_wechat_pay_bill_detail.openid</code>. 用户标识
     */
    public String getOpenid() {
        return (String) get(13);
    }

    /**
     * Setter for <code>ep.ep_wechat_pay_bill_detail.trade_type</code>. 交易类型
     */
    public EpWechatPayBillDetailRecord setTradeType(String value) {
        set(14, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_wechat_pay_bill_detail.trade_type</code>. 交易类型
     */
    public String getTradeType() {
        return (String) get(14);
    }

    /**
     * Setter for <code>ep.ep_wechat_pay_bill_detail.trade_state</code>. 交易状态
     */
    public EpWechatPayBillDetailRecord setTradeState(String value) {
        set(15, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_wechat_pay_bill_detail.trade_state</code>. 交易状态
     */
    public String getTradeState() {
        return (String) get(15);
    }

    /**
     * Setter for <code>ep.ep_wechat_pay_bill_detail.bank_type</code>. 付款银行
     */
    public EpWechatPayBillDetailRecord setBankType(String value) {
        set(16, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_wechat_pay_bill_detail.bank_type</code>. 付款银行
     */
    public String getBankType() {
        return (String) get(16);
    }

    /**
     * Setter for <code>ep.ep_wechat_pay_bill_detail.fee_type</code>. 货币种类
     */
    public EpWechatPayBillDetailRecord setFeeType(String value) {
        set(17, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_wechat_pay_bill_detail.fee_type</code>. 货币种类
     */
    public String getFeeType() {
        return (String) get(17);
    }

    /**
     * Setter for <code>ep.ep_wechat_pay_bill_detail.total_fee</code>. 总金额
     */
    public EpWechatPayBillDetailRecord setTotalFee(BigDecimal value) {
        set(18, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_wechat_pay_bill_detail.total_fee</code>. 总金额
     */
    public BigDecimal getTotalFee() {
        return (BigDecimal) get(18);
    }

    /**
     * Setter for <code>ep.ep_wechat_pay_bill_detail.coupon_fee</code>. 企业红包金额
     */
    public EpWechatPayBillDetailRecord setCouponFee(BigDecimal value) {
        set(19, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_wechat_pay_bill_detail.coupon_fee</code>. 企业红包金额
     */
    public BigDecimal getCouponFee() {
        return (BigDecimal) get(19);
    }

    /**
     * Setter for <code>ep.ep_wechat_pay_bill_detail.refund_id</code>. 退款单号
     */
    public EpWechatPayBillDetailRecord setRefundId(String value) {
        set(20, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_wechat_pay_bill_detail.refund_id</code>. 退款单号
     */
    public String getRefundId() {
        return (String) get(20);
    }

    /**
     * Setter for <code>ep.ep_wechat_pay_bill_detail.out_refund_no</code>. 商户退款单号
     */
    public EpWechatPayBillDetailRecord setOutRefundNo(String value) {
        set(21, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_wechat_pay_bill_detail.out_refund_no</code>. 商户退款单号
     */
    public String getOutRefundNo() {
        return (String) get(21);
    }

    /**
     * Setter for <code>ep.ep_wechat_pay_bill_detail.refund_fee</code>. 退款金额
     */
    public EpWechatPayBillDetailRecord setRefundFee(BigDecimal value) {
        set(22, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_wechat_pay_bill_detail.refund_fee</code>. 退款金额
     */
    public BigDecimal getRefundFee() {
        return (BigDecimal) get(22);
    }

    /**
     * Setter for <code>ep.ep_wechat_pay_bill_detail.refund_coupon_fee</code>. 企业红包退款金额
     */
    public EpWechatPayBillDetailRecord setRefundCouponFee(BigDecimal value) {
        set(23, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_wechat_pay_bill_detail.refund_coupon_fee</code>. 企业红包退款金额
     */
    public BigDecimal getRefundCouponFee() {
        return (BigDecimal) get(23);
    }

    /**
     * Setter for <code>ep.ep_wechat_pay_bill_detail.refund_type</code>. 退款类型
     */
    public EpWechatPayBillDetailRecord setRefundType(String value) {
        set(24, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_wechat_pay_bill_detail.refund_type</code>. 退款类型
     */
    public String getRefundType() {
        return (String) get(24);
    }

    /**
     * Setter for <code>ep.ep_wechat_pay_bill_detail.refund_status</code>. 退款状态
     */
    public EpWechatPayBillDetailRecord setRefundStatus(String value) {
        set(25, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_wechat_pay_bill_detail.refund_status</code>. 退款状态
     */
    public String getRefundStatus() {
        return (String) get(25);
    }

    /**
     * Setter for <code>ep.ep_wechat_pay_bill_detail.body</code>. 商品描述
     */
    public EpWechatPayBillDetailRecord setBody(String value) {
        set(26, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_wechat_pay_bill_detail.body</code>. 商品描述
     */
    public String getBody() {
        return (String) get(26);
    }

    /**
     * Setter for <code>ep.ep_wechat_pay_bill_detail.attach</code>. 商户数据包
     */
    public EpWechatPayBillDetailRecord setAttach(String value) {
        set(27, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_wechat_pay_bill_detail.attach</code>. 商户数据包
     */
    public String getAttach() {
        return (String) get(27);
    }

    /**
     * Setter for <code>ep.ep_wechat_pay_bill_detail.poundage</code>. 手续费
     */
    public EpWechatPayBillDetailRecord setPoundage(BigDecimal value) {
        set(28, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_wechat_pay_bill_detail.poundage</code>. 手续费
     */
    public BigDecimal getPoundage() {
        return (BigDecimal) get(28);
    }

    /**
     * Setter for <code>ep.ep_wechat_pay_bill_detail.poundage_rate</code>. 费率
     */
    public EpWechatPayBillDetailRecord setPoundageRate(String value) {
        set(29, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_wechat_pay_bill_detail.poundage_rate</code>. 费率
     */
    public String getPoundageRate() {
        return (String) get(29);
    }

    /**
     * Setter for <code>ep.ep_wechat_pay_bill_detail.create_at</code>. 创建时间
     */
    public EpWechatPayBillDetailRecord setCreateAt(Timestamp value) {
        set(30, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_wechat_pay_bill_detail.create_at</code>. 创建时间
     */
    public Timestamp getCreateAt() {
        return (Timestamp) get(30);
    }

    /**
     * Setter for <code>ep.ep_wechat_pay_bill_detail.update_at</code>. 更新时间
     */
    public EpWechatPayBillDetailRecord setUpdateAt(Timestamp value) {
        set(31, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_wechat_pay_bill_detail.update_at</code>. 更新时间
     */
    public Timestamp getUpdateAt() {
        return (Timestamp) get(31);
    }

    /**
     * Setter for <code>ep.ep_wechat_pay_bill_detail.remark</code>. 备注
     */
    public EpWechatPayBillDetailRecord setRemark(String value) {
        set(32, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_wechat_pay_bill_detail.remark</code>. 备注
     */
    public String getRemark() {
        return (String) get(32);
    }

    /**
     * Setter for <code>ep.ep_wechat_pay_bill_detail.del_flag</code>. 删除标记
     */
    public EpWechatPayBillDetailRecord setDelFlag(Boolean value) {
        set(33, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_wechat_pay_bill_detail.del_flag</code>. 删除标记
     */
    public Boolean getDelFlag() {
        return (Boolean) get(33);
    }

    /**
     * Setter for <code>ep.ep_wechat_pay_bill_detail.version</code>.
     */
    public EpWechatPayBillDetailRecord setVersion(Long value) {
        set(34, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_wechat_pay_bill_detail.version</code>.
     */
    public Long getVersion() {
        return (Long) get(34);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached EpWechatPayBillDetailRecord
     */
    public EpWechatPayBillDetailRecord() {
        super(EpWechatPayBillDetail.EP_WECHAT_PAY_BILL_DETAIL);
    }

    /**
     * Create a detached, initialised EpWechatPayBillDetailRecord
     */
    public EpWechatPayBillDetailRecord(Long id, Long billId, Long ognId, Long courseId, Long classId, Long orderId, String transactionTime, String appid, String mchId, String subMchId, String deviceNo, String transactionId, String outTradeNo, String openid, String tradeType, String tradeState, String bankType, String feeType, BigDecimal totalFee, BigDecimal couponFee, String refundId, String outRefundNo, BigDecimal refundFee, BigDecimal refundCouponFee, String refundType, String refundStatus, String body, String attach, BigDecimal poundage, String poundageRate, Timestamp createAt, Timestamp updateAt, String remark, Boolean delFlag, Long version) {
        super(EpWechatPayBillDetail.EP_WECHAT_PAY_BILL_DETAIL);

        set(0, id);
        set(1, billId);
        set(2, ognId);
        set(3, courseId);
        set(4, classId);
        set(5, orderId);
        set(6, transactionTime);
        set(7, appid);
        set(8, mchId);
        set(9, subMchId);
        set(10, deviceNo);
        set(11, transactionId);
        set(12, outTradeNo);
        set(13, openid);
        set(14, tradeType);
        set(15, tradeState);
        set(16, bankType);
        set(17, feeType);
        set(18, totalFee);
        set(19, couponFee);
        set(20, refundId);
        set(21, outRefundNo);
        set(22, refundFee);
        set(23, refundCouponFee);
        set(24, refundType);
        set(25, refundStatus);
        set(26, body);
        set(27, attach);
        set(28, poundage);
        set(29, poundageRate);
        set(30, createAt);
        set(31, updateAt);
        set(32, remark);
        set(33, delFlag);
        set(34, version);
    }
}
