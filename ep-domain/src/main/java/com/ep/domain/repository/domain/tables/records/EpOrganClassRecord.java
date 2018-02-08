/*
 * This file is generated by jOOQ.
*/
package com.ep.domain.repository.domain.tables.records;


import com.ep.domain.repository.domain.tables.EpOrganClass;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record18;
import org.jooq.Row18;
import org.jooq.impl.UpdatableRecordImpl;

import javax.annotation.Generated;
import java.math.BigDecimal;
import java.sql.Timestamp;


/**
 * 机构课程班次表
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.9.6"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class EpOrganClassRecord extends UpdatableRecordImpl<EpOrganClassRecord> implements Record18<Long, Long, Long, String, Long, BigDecimal, BigDecimal, Boolean, Integer, Integer, Integer, Integer, Long, String, Timestamp, Timestamp, Boolean, Long> {

    private static final long serialVersionUID = 1324530611;

    /**
     * Setter for <code>ep.ep_organ_class.id</code>. 主键
     */
    public EpOrganClassRecord setId(Long value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_class.id</code>. 主键
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>ep.ep_organ_class.ogn_id</code>. 机构ID
     */
    public EpOrganClassRecord setOgnId(Long value) {
        set(1, value);
        return this;
    }

    /**
     * Create a detached EpOrganClassRecord
     */
    public EpOrganClassRecord() {
        super(EpOrganClass.EP_ORGAN_CLASS);
    }

    /**
     * Setter for <code>ep.ep_organ_class.course_id</code>. 课程ID
     */
    public EpOrganClassRecord setCourseId(Long value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_class.course_id</code>. 课程ID
     */
    public Long getCourseId() {
        return (Long) get(2);
    }

    /**
     * Setter for <code>ep.ep_organ_class.class_name</code>. 班次名称
     */
    public EpOrganClassRecord setClassName(String value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_class.class_name</code>. 班次名称
     */
    public String getClassName() {
        return (String) get(3);
    }

    /**
     * Setter for <code>ep.ep_organ_class.ogn_account_id</code>. 课程负责人账户id
     */
    public EpOrganClassRecord setOgnAccountId(Long value) {
        set(4, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_class.ogn_account_id</code>. 课程负责人账户id
     */
    public Long getOgnAccountId() {
        return (Long) get(4);
    }

    /**
     * Setter for <code>ep.ep_organ_class.class_prize</code>. 价格
     */
    public EpOrganClassRecord setClassPrize(BigDecimal value) {
        set(5, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_class.class_prize</code>. 价格
     */
    public BigDecimal getClassPrize() {
        return (BigDecimal) get(5);
    }

    /**
     * Setter for <code>ep.ep_organ_class.discount_amount</code>. 折扣优惠
     */
    public EpOrganClassRecord setDiscountAmount(BigDecimal value) {
        set(6, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_class.discount_amount</code>. 折扣优惠
     */
    public BigDecimal getDiscountAmount() {
        return (BigDecimal) get(6);
    }

    /**
     * Setter for <code>ep.ep_organ_class.enter_limit_flag</code>. 是否限制报名人数
     */
    public EpOrganClassRecord setEnterLimitFlag(Boolean value) {
        set(7, value);
        return this;
    }

    /**
     * Create a detached, initialised EpOrganClassRecord
     */
    public EpOrganClassRecord(Long id, Long ognId, Long courseId, String className, Long ognAccountId, BigDecimal classPrize, BigDecimal discountAmount, Boolean enterLimitFlag, Integer enterRequireNum, Integer orderedNum, Integer enteredNum, Integer courseNum, Long sort, String remark, Timestamp createAt, Timestamp updateAt, Boolean delFlag, Long version) {
        super(EpOrganClass.EP_ORGAN_CLASS);

        set(0, id);
        set(1, ognId);
        set(2, courseId);
        set(3, className);
        set(4, ognAccountId);
        set(5, classPrize);
        set(6, discountAmount);
        set(7, enterLimitFlag);
        set(8, enterRequireNum);
        set(9, orderedNum);
        set(10, enteredNum);
        set(11, courseNum);
        set(12, sort);
        set(13, remark);
        set(14, createAt);
        set(15, updateAt);
        set(16, delFlag);
        set(17, version);
    }

    /**
     * Getter for <code>ep.ep_organ_class.ogn_id</code>. 机构ID
     */
    public Long getOgnId() {
        return (Long) get(1);
    }

    /**
     * Getter for <code>ep.ep_organ_class.enter_require_num</code>. 要求报名人数
     */
    public Integer getEnterRequireNum() {
        return (Integer) get(8);
    }

    /**
     * Getter for <code>ep.ep_organ_class.enter_limit_flag</code>. 是否限制报名人数
     */
    public Boolean getEnterLimitFlag() {
        return (Boolean) get(7);
    }

    /**
     * Setter for <code>ep.ep_organ_class.enter_require_num</code>. 要求报名人数
     */
    public EpOrganClassRecord setEnterRequireNum(Integer value) {
        set(8, value);
        return this;
    }

    /**
     * Setter for <code>ep.ep_organ_class.entered_num</code>. 已报名成功人数
     */
    public EpOrganClassRecord setEnteredNum(Integer value) {
        set(10, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_class.ordered_num</code>. 下单人数
     */
    public Integer getOrderedNum() {
        return (Integer) get(9);
    }

    /**
     * Setter for <code>ep.ep_organ_class.course_num</code>. 总计课时
     */
    public EpOrganClassRecord setCourseNum(Integer value) {
        set(11, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_class.course_num</code>. 总计课时
     */
    public Integer getCourseNum() {
        return (Integer) get(11);
    }

    /**
     * Setter for <code>ep.ep_organ_class.sort</code>. 排序
     */
    public EpOrganClassRecord setSort(Long value) {
        set(12, value);
        return this;
    }

    /**
     * Setter for <code>ep.ep_organ_class.ordered_num</code>. 下单人数
     */
    public EpOrganClassRecord setOrderedNum(Integer value) {
        set(9, value);
        return this;
    }

    /**
     * Setter for <code>ep.ep_organ_class.remark</code>. 备注信息
     */
    public EpOrganClassRecord setRemark(String value) {
        set(13, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_class.entered_num</code>. 已报名成功人数
     */
    public Integer getEnteredNum() {
        return (Integer) get(10);
    }

    /**
     * Setter for <code>ep.ep_organ_class.create_at</code>. 创建时间
     */
    public EpOrganClassRecord setCreateAt(Timestamp value) {
        set(14, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_class.sort</code>. 排序
     */
    public Long getSort() {
        return (Long) get(12);
    }

    /**
     * Setter for <code>ep.ep_organ_class.update_at</code>. 更新时间
     */
    public EpOrganClassRecord setUpdateAt(Timestamp value) {
        set(15, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_class.remark</code>. 备注信息
     */
    public String getRemark() {
        return (String) get(13);
    }

    /**
     * Setter for <code>ep.ep_organ_class.del_flag</code>. 删除标志
     */
    public EpOrganClassRecord setDelFlag(Boolean value) {
        set(16, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_class.del_flag</code>. 删除标志
     */
    public Boolean getDelFlag() {
        return (Boolean) get(16);
    }

    /**
     * Getter for <code>ep.ep_organ_class.create_at</code>. 创建时间
     */
    public Timestamp getCreateAt() {
        return (Timestamp) get(14);
    }

    /**
     * Getter for <code>ep.ep_organ_class.update_at</code>. 更新时间
     */
    public Timestamp getUpdateAt() {
        return (Timestamp) get(15);
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
    // Record18 type implementation
    // -------------------------------------------------------------------------

    /**
     * Getter for <code>ep.ep_organ_class.version</code>.
     */
    public Long getVersion() {
        return (Long) get(17);
    }

    /**
     * Setter for <code>ep.ep_organ_class.version</code>.
     */
    public EpOrganClassRecord setVersion(Long value) {
        set(17, value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field1() {
        return EpOrganClass.EP_ORGAN_CLASS.ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field2() {
        return EpOrganClass.EP_ORGAN_CLASS.OGN_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field3() {
        return EpOrganClass.EP_ORGAN_CLASS.COURSE_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field4() {
        return EpOrganClass.EP_ORGAN_CLASS.CLASS_NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field5() {
        return EpOrganClass.EP_ORGAN_CLASS.OGN_ACCOUNT_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<BigDecimal> field6() {
        return EpOrganClass.EP_ORGAN_CLASS.CLASS_PRIZE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<BigDecimal> field7() {
        return EpOrganClass.EP_ORGAN_CLASS.DISCOUNT_AMOUNT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Boolean> field8() {
        return EpOrganClass.EP_ORGAN_CLASS.ENTER_LIMIT_FLAG;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field9() {
        return EpOrganClass.EP_ORGAN_CLASS.ENTER_REQUIRE_NUM;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field10() {
        return EpOrganClass.EP_ORGAN_CLASS.ORDERED_NUM;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field11() {
        return EpOrganClass.EP_ORGAN_CLASS.ENTERED_NUM;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field12() {
        return EpOrganClass.EP_ORGAN_CLASS.COURSE_NUM;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field13() {
        return EpOrganClass.EP_ORGAN_CLASS.SORT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field14() {
        return EpOrganClass.EP_ORGAN_CLASS.REMARK;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field15() {
        return EpOrganClass.EP_ORGAN_CLASS.CREATE_AT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field16() {
        return EpOrganClass.EP_ORGAN_CLASS.UPDATE_AT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row18<Long, Long, Long, String, Long, BigDecimal, BigDecimal, Boolean, Integer, Integer, Integer, Integer, Long, String, Timestamp, Timestamp, Boolean, Long> fieldsRow() {
        return (Row18) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row18<Long, Long, Long, String, Long, BigDecimal, BigDecimal, Boolean, Integer, Integer, Integer, Integer, Long, String, Timestamp, Timestamp, Boolean, Long> valuesRow() {
        return (Row18) super.valuesRow();
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
        return getCourseId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value4() {
        return getClassName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value5() {
        return getOgnAccountId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal value6() {
        return getClassPrize();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal value7() {
        return getDiscountAmount();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean value8() {
        return getEnterLimitFlag();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value9() {
        return getEnterRequireNum();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value10() {
        return getOrderedNum();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value11() {
        return getEnteredNum();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value12() {
        return getCourseNum();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value13() {
        return getSort();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value14() {
        return getRemark();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp value15() {
        return getCreateAt();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp value16() {
        return getUpdateAt();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Boolean> field17() {
        return EpOrganClass.EP_ORGAN_CLASS.DEL_FLAG;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field18() {
        return EpOrganClass.EP_ORGAN_CLASS.VERSION;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganClassRecord value1(Long value) {
        setId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganClassRecord value2(Long value) {
        setOgnId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganClassRecord value3(Long value) {
        setCourseId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganClassRecord value4(String value) {
        setClassName(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganClassRecord value5(Long value) {
        setOgnAccountId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganClassRecord value6(BigDecimal value) {
        setClassPrize(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganClassRecord value7(BigDecimal value) {
        setDiscountAmount(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganClassRecord value8(Boolean value) {
        setEnterLimitFlag(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganClassRecord value9(Integer value) {
        setEnterRequireNum(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganClassRecord value10(Integer value) {
        setOrderedNum(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganClassRecord value11(Integer value) {
        setEnteredNum(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganClassRecord value12(Integer value) {
        setCourseNum(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganClassRecord value13(Long value) {
        setSort(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganClassRecord value14(String value) {
        setRemark(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganClassRecord value15(Timestamp value) {
        setCreateAt(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganClassRecord value16(Timestamp value) {
        setUpdateAt(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganClassRecord value17(Boolean value) {
        setDelFlag(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganClassRecord value18(Long value) {
        setVersion(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganClassRecord values(Long value1, Long value2, Long value3, String value4, Long value5, BigDecimal value6, BigDecimal value7, Boolean value8, Integer value9, Integer value10, Integer value11, Integer value12, Long value13, String value14, Timestamp value15, Timestamp value16, Boolean value17, Long value18) {
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
        value15(value15);
        value16(value16);
        value17(value17);
        value18(value18);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean value17() {
        return getDelFlag();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value18() {
        return getVersion();
    }
}
