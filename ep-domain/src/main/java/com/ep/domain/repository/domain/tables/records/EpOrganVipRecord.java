/*
 * This file is generated by jOOQ.
*/
package com.ep.domain.repository.domain.tables.records;


import com.ep.domain.repository.domain.enums.EpOrganVipStatus;
import com.ep.domain.repository.domain.tables.EpOrganVip;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record14;
import org.jooq.Row14;
import org.jooq.impl.UpdatableRecordImpl;

import javax.annotation.Generated;
import java.math.BigDecimal;
import java.sql.Timestamp;


/**
 * 机构会员信息表
 */
@Generated(
        value = {
                "http://www.jooq.org",
                "jOOQ version:3.9.6"
        },
        comments = "This class is generated by jOOQ"
)
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class EpOrganVipRecord extends UpdatableRecordImpl<EpOrganVipRecord> implements Record14<Long, Long, Long, Long, Timestamp, Timestamp, Byte, BigDecimal, EpOrganVipStatus, String, Timestamp, Timestamp, Boolean, Long> {

    private static final long serialVersionUID = -744544601;

    /**
     * Create a detached EpOrganVipRecord
     */
    public EpOrganVipRecord() {
        super(EpOrganVip.EP_ORGAN_VIP);
    }

    /**
     * Create a detached, initialised EpOrganVipRecord
     */
    public EpOrganVipRecord(Long id, Long ognId, Long memberId, Long childId, Timestamp startTime, Timestamp endTime, Byte level, BigDecimal prize, EpOrganVipStatus status, String remark, Timestamp createAt, Timestamp updateAt, Boolean delFlag, Long version) {
        super(EpOrganVip.EP_ORGAN_VIP);

        set(0, id);
        set(1, ognId);
        set(2, memberId);
        set(3, childId);
        set(4, startTime);
        set(5, endTime);
        set(6, level);
        set(7, prize);
        set(8, status);
        set(9, remark);
        set(10, createAt);
        set(11, updateAt);
        set(12, delFlag);
        set(13, version);
    }

    /**
     * Getter for <code>ep.ep_organ_vip.id</code>.
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>ep.ep_organ_vip.id</code>.
     */
    public EpOrganVipRecord setId(Long value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_vip.ogn_id</code>. 机构id
     */
    public Long getOgnId() {
        return (Long) get(1);
    }

    /**
     * Setter for <code>ep.ep_organ_vip.ogn_id</code>. 机构id
     */
    public EpOrganVipRecord setOgnId(Long value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_vip.member_id</code>. 会员id
     */
    public Long getMemberId() {
        return (Long) get(2);
    }

    /**
     * Setter for <code>ep.ep_organ_vip.member_id</code>. 会员id
     */
    public EpOrganVipRecord setMemberId(Long value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_vip.child_id</code>. 孩子id
     */
    public Long getChildId() {
        return (Long) get(3);
    }

    /**
     * Setter for <code>ep.ep_organ_vip.child_id</code>. 孩子id
     */
    public EpOrganVipRecord setChildId(Long value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_vip.start_time</code>. 会员开始时间
     */
    public Timestamp getStartTime() {
        return (Timestamp) get(4);
    }

    /**
     * Setter for <code>ep.ep_organ_vip.start_time</code>. 会员开始时间
     */
    public EpOrganVipRecord setStartTime(Timestamp value) {
        set(4, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_vip.end_time</code>. 会员结束时间
     */
    public Timestamp getEndTime() {
        return (Timestamp) get(5);
    }

    /**
     * Setter for <code>ep.ep_organ_vip.end_time</code>. 会员结束时间
     */
    public EpOrganVipRecord setEndTime(Timestamp value) {
        set(5, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_vip.level</code>. 会员等级
     */
    public Byte getLevel() {
        return (Byte) get(6);
    }

    /**
     * Setter for <code>ep.ep_organ_vip.level</code>. 会员等级
     */
    public EpOrganVipRecord setLevel(Byte value) {
        set(6, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_vip.prize</code>. 入会费用
     */
    public BigDecimal getPrize() {
        return (BigDecimal) get(7);
    }

    /**
     * Setter for <code>ep.ep_organ_vip.prize</code>. 入会费用
     */
    public EpOrganVipRecord setPrize(BigDecimal value) {
        set(7, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_vip.status</code>. 状态：已保存；成功；拒绝；取消；
     */
    public EpOrganVipStatus getStatus() {
        return (EpOrganVipStatus) get(8);
    }

    /**
     * Setter for <code>ep.ep_organ_vip.status</code>. 状态：已保存；成功；拒绝；取消；
     */
    public EpOrganVipRecord setStatus(EpOrganVipStatus value) {
        set(8, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_vip.remark</code>. 备注信息
     */
    public String getRemark() {
        return (String) get(9);
    }

    /**
     * Setter for <code>ep.ep_organ_vip.remark</code>. 备注信息
     */
    public EpOrganVipRecord setRemark(String value) {
        set(9, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_vip.create_at</code>. 创建时间
     */
    public Timestamp getCreateAt() {
        return (Timestamp) get(10);
    }

    /**
     * Setter for <code>ep.ep_organ_vip.create_at</code>. 创建时间
     */
    public EpOrganVipRecord setCreateAt(Timestamp value) {
        set(10, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_vip.update_at</code>. 更新时间
     */
    public Timestamp getUpdateAt() {
        return (Timestamp) get(11);
    }

    /**
     * Setter for <code>ep.ep_organ_vip.update_at</code>. 更新时间
     */
    public EpOrganVipRecord setUpdateAt(Timestamp value) {
        set(11, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_vip.del_flag</code>. 删除标志
     */
    public Boolean getDelFlag() {
        return (Boolean) get(12);
    }

    /**
     * Setter for <code>ep.ep_organ_vip.del_flag</code>. 删除标志
     */
    public EpOrganVipRecord setDelFlag(Boolean value) {
        set(12, value);
        return this;
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    /**
     * Getter for <code>ep.ep_organ_vip.version</code>.
     */
    public Long getVersion() {
        return (Long) get(13);
    }

    // -------------------------------------------------------------------------
    // Record14 type implementation
    // -------------------------------------------------------------------------

    /**
     * Setter for <code>ep.ep_organ_vip.version</code>.
     */
    public EpOrganVipRecord setVersion(Long value) {
        set(13, value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row14<Long, Long, Long, Long, Timestamp, Timestamp, Byte, BigDecimal, EpOrganVipStatus, String, Timestamp, Timestamp, Boolean, Long> fieldsRow() {
        return (Row14) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row14<Long, Long, Long, Long, Timestamp, Timestamp, Byte, BigDecimal, EpOrganVipStatus, String, Timestamp, Timestamp, Boolean, Long> valuesRow() {
        return (Row14) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field1() {
        return EpOrganVip.EP_ORGAN_VIP.ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field2() {
        return EpOrganVip.EP_ORGAN_VIP.OGN_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field3() {
        return EpOrganVip.EP_ORGAN_VIP.MEMBER_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field4() {
        return EpOrganVip.EP_ORGAN_VIP.CHILD_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field5() {
        return EpOrganVip.EP_ORGAN_VIP.START_TIME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field6() {
        return EpOrganVip.EP_ORGAN_VIP.END_TIME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Byte> field7() {
        return EpOrganVip.EP_ORGAN_VIP.LEVEL;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<BigDecimal> field8() {
        return EpOrganVip.EP_ORGAN_VIP.PRIZE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<EpOrganVipStatus> field9() {
        return EpOrganVip.EP_ORGAN_VIP.STATUS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field10() {
        return EpOrganVip.EP_ORGAN_VIP.REMARK;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field11() {
        return EpOrganVip.EP_ORGAN_VIP.CREATE_AT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field12() {
        return EpOrganVip.EP_ORGAN_VIP.UPDATE_AT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Boolean> field13() {
        return EpOrganVip.EP_ORGAN_VIP.DEL_FLAG;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field14() {
        return EpOrganVip.EP_ORGAN_VIP.VERSION;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value1() {
        return getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value2() {
        return getOgnId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value3() {
        return getMemberId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value4() {
        return getChildId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp value5() {
        return getStartTime();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp value6() {
        return getEndTime();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Byte value7() {
        return getLevel();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal value8() {
        return getPrize();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganVipStatus value9() {
        return getStatus();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value10() {
        return getRemark();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp value11() {
        return getCreateAt();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp value12() {
        return getUpdateAt();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean value13() {
        return getDelFlag();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value14() {
        return getVersion();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganVipRecord value1(Long value) {
        setId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganVipRecord value2(Long value) {
        setOgnId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganVipRecord value3(Long value) {
        setMemberId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganVipRecord value4(Long value) {
        setChildId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganVipRecord value5(Timestamp value) {
        setStartTime(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganVipRecord value6(Timestamp value) {
        setEndTime(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganVipRecord value7(Byte value) {
        setLevel(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganVipRecord value8(BigDecimal value) {
        setPrize(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganVipRecord value9(EpOrganVipStatus value) {
        setStatus(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganVipRecord value10(String value) {
        setRemark(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganVipRecord value11(Timestamp value) {
        setCreateAt(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganVipRecord value12(Timestamp value) {
        setUpdateAt(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganVipRecord value13(Boolean value) {
        setDelFlag(value);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganVipRecord value14(Long value) {
        setVersion(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganVipRecord values(Long value1, Long value2, Long value3, Long value4, Timestamp value5, Timestamp value6, Byte value7, BigDecimal value8, EpOrganVipStatus value9, String value10, Timestamp value11, Timestamp value12, Boolean value13, Long value14) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        value8(value8);
        value9(value9);
        value10(value10);
        value11(value11);
        value12(value12);
        value13(value13);
        value14(value14);
        return this;
    }
}
