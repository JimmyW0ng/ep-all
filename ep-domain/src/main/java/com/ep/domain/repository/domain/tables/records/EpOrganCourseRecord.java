/*
 * This file is generated by jOOQ.
*/
package com.ep.domain.repository.domain.tables.records;


import com.ep.domain.repository.domain.enums.EpOrganCourseCourseStatus;
import com.ep.domain.repository.domain.enums.EpOrganCourseCourseType;
import com.ep.domain.repository.domain.tables.EpOrganCourse;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record20;
import org.jooq.Row20;
import org.jooq.impl.UpdatableRecordImpl;

import javax.annotation.Generated;
import java.math.BigDecimal;
import java.sql.Timestamp;


/**
 * 机构课程表
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.9.6"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class EpOrganCourseRecord extends UpdatableRecordImpl<EpOrganCourseRecord> implements Record20<Long, Long, EpOrganCourseCourseType, Long, String, String, String, BigDecimal, Boolean, Boolean, EpOrganCourseCourseStatus, Timestamp, Timestamp, Timestamp, Integer, String, Timestamp, Timestamp, Boolean, Long> {

    private static final long serialVersionUID = 1403072823;

    /**
     * Setter for <code>ep.ep_organ_course.id</code>. 主键
     */
    public EpOrganCourseRecord setId(Long value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_course.id</code>. 主键
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>ep.ep_organ_course.ogn_id</code>. 机构ID
     */
    public EpOrganCourseRecord setOgnId(Long value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_course.ogn_id</code>. 机构ID
     */
    public Long getOgnId() {
        return (Long) get(1);
    }

    /**
     * Setter for <code>ep.ep_organ_course.course_type</code>. 课程类型：课程；活动；
     */
    public EpOrganCourseRecord setCourseType(EpOrganCourseCourseType value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_course.course_type</code>. 课程类型：课程；活动；
     */
    public EpOrganCourseCourseType getCourseType() {
        return (EpOrganCourseCourseType) get(2);
    }

    /**
     * Setter for <code>ep.ep_organ_course.course_catalog_id</code>. 课程目录ID
     */
    public EpOrganCourseRecord setCourseCatalogId(Long value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_course.course_catalog_id</code>. 课程目录ID
     */
    public Long getCourseCatalogId() {
        return (Long) get(3);
    }

    /**
     * Setter for <code>ep.ep_organ_course.course_name</code>. 课名
     */
    public EpOrganCourseRecord setCourseName(String value) {
        set(4, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_course.course_name</code>. 课名
     */
    public String getCourseName() {
        return (String) get(4);
    }

    /**
     * Setter for <code>ep.ep_organ_course.course_introduce</code>. 课程简介
     */
    public EpOrganCourseRecord setCourseIntroduce(String value) {
        set(5, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_course.course_introduce</code>. 课程简介
     */
    public String getCourseIntroduce() {
        return (String) get(5);
    }

    /**
     * Setter for <code>ep.ep_organ_course.course_content</code>. 课程内容
     */
    public EpOrganCourseRecord setCourseContent(String value) {
        set(6, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_course.course_content</code>. 课程内容
     */
    public String getCourseContent() {
        return (String) get(6);
    }

    /**
     * Setter for <code>ep.ep_organ_course.prize_min</code>. 最低价格
     */
    public EpOrganCourseRecord setPrizeMin(BigDecimal value) {
        set(7, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_course.prize_min</code>. 最低价格
     */
    public BigDecimal getPrizeMin() {
        return (BigDecimal) get(7);
    }

    /**
     * Setter for <code>ep.ep_organ_course.vip_flag</code>. 是否会员才能报名
     */
    public EpOrganCourseRecord setVipFlag(Boolean value) {
        set(8, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_course.vip_flag</code>. 是否会员才能报名
     */
    public Boolean getVipFlag() {
        return (Boolean) get(8);
    }

    /**
     * Setter for <code>ep.ep_organ_course.wechat_pay_flag</code>. 是否通过微信支付
     */
    public EpOrganCourseRecord setWechatPayFlag(Boolean value) {
        set(9, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_course.wechat_pay_flag</code>. 是否通过微信支付
     */
    public Boolean getWechatPayFlag() {
        return (Boolean) get(9);
    }

    /**
     * Setter for <code>ep.ep_organ_course.course_status</code>. 课状态：已保存；已上线；已下线；
     */
    public EpOrganCourseRecord setCourseStatus(EpOrganCourseCourseStatus value) {
        set(10, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_course.course_status</code>. 课状态：已保存；已上线；已下线；
     */
    public EpOrganCourseCourseStatus getCourseStatus() {
        return (EpOrganCourseCourseStatus) get(10);
    }

    /**
     * Setter for <code>ep.ep_organ_course.online_time</code>. 上线时间
     */
    public EpOrganCourseRecord setOnlineTime(Timestamp value) {
        set(11, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_course.online_time</code>. 上线时间
     */
    public Timestamp getOnlineTime() {
        return (Timestamp) get(11);
    }

    /**
     * Setter for <code>ep.ep_organ_course.enter_time_start</code>. 报名开始时间
     */
    public EpOrganCourseRecord setEnterTimeStart(Timestamp value) {
        set(12, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_course.enter_time_start</code>. 报名开始时间
     */
    public Timestamp getEnterTimeStart() {
        return (Timestamp) get(12);
    }

    /**
     * Setter for <code>ep.ep_organ_course.enter_time_end</code>. 报名结束时间
     */
    public EpOrganCourseRecord setEnterTimeEnd(Timestamp value) {
        set(13, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_course.enter_time_end</code>. 报名结束时间
     */
    public Timestamp getEnterTimeEnd() {
        return (Timestamp) get(13);
    }

    /**
     * Setter for <code>ep.ep_organ_course.total_participate</code>. 总参与人数
     */
    public EpOrganCourseRecord setTotalParticipate(Integer value) {
        set(14, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_course.total_participate</code>. 总参与人数
     */
    public Integer getTotalParticipate() {
        return (Integer) get(14);
    }

    /**
     * Setter for <code>ep.ep_organ_course.remark</code>. 备注信息
     */
    public EpOrganCourseRecord setRemark(String value) {
        set(15, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_course.remark</code>. 备注信息
     */
    public String getRemark() {
        return (String) get(15);
    }

    /**
     * Setter for <code>ep.ep_organ_course.create_at</code>. 创建时间
     */
    public EpOrganCourseRecord setCreateAt(Timestamp value) {
        set(16, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_course.create_at</code>. 创建时间
     */
    public Timestamp getCreateAt() {
        return (Timestamp) get(16);
    }

    /**
     * Setter for <code>ep.ep_organ_course.update_at</code>. 更新时间
     */
    public EpOrganCourseRecord setUpdateAt(Timestamp value) {
        set(17, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_course.update_at</code>. 更新时间
     */
    public Timestamp getUpdateAt() {
        return (Timestamp) get(17);
    }

    /**
     * Setter for <code>ep.ep_organ_course.del_flag</code>. 删除标志
     */
    public EpOrganCourseRecord setDelFlag(Boolean value) {
        set(18, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_course.del_flag</code>. 删除标志
     */
    public Boolean getDelFlag() {
        return (Boolean) get(18);
    }

    /**
     * Setter for <code>ep.ep_organ_course.version</code>.
     */
    public EpOrganCourseRecord setVersion(Long value) {
        set(19, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_course.version</code>.
     */
    public Long getVersion() {
        return (Long) get(19);
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
    // Record20 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row20<Long, Long, EpOrganCourseCourseType, Long, String, String, String, BigDecimal, Boolean, Boolean, EpOrganCourseCourseStatus, Timestamp, Timestamp, Timestamp, Integer, String, Timestamp, Timestamp, Boolean, Long> fieldsRow() {
        return (Row20) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row20<Long, Long, EpOrganCourseCourseType, Long, String, String, String, BigDecimal, Boolean, Boolean, EpOrganCourseCourseStatus, Timestamp, Timestamp, Timestamp, Integer, String, Timestamp, Timestamp, Boolean, Long> valuesRow() {
        return (Row20) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field1() {
        return EpOrganCourse.EP_ORGAN_COURSE.ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field2() {
        return EpOrganCourse.EP_ORGAN_COURSE.OGN_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<EpOrganCourseCourseType> field3() {
        return EpOrganCourse.EP_ORGAN_COURSE.COURSE_TYPE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field4() {
        return EpOrganCourse.EP_ORGAN_COURSE.COURSE_CATALOG_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field5() {
        return EpOrganCourse.EP_ORGAN_COURSE.COURSE_NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field6() {
        return EpOrganCourse.EP_ORGAN_COURSE.COURSE_INTRODUCE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field7() {
        return EpOrganCourse.EP_ORGAN_COURSE.COURSE_CONTENT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<BigDecimal> field8() {
        return EpOrganCourse.EP_ORGAN_COURSE.PRIZE_MIN;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Boolean> field9() {
        return EpOrganCourse.EP_ORGAN_COURSE.VIP_FLAG;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Boolean> field10() {
        return EpOrganCourse.EP_ORGAN_COURSE.WECHAT_PAY_FLAG;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<EpOrganCourseCourseStatus> field11() {
        return EpOrganCourse.EP_ORGAN_COURSE.COURSE_STATUS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field12() {
        return EpOrganCourse.EP_ORGAN_COURSE.ONLINE_TIME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field13() {
        return EpOrganCourse.EP_ORGAN_COURSE.ENTER_TIME_START;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field14() {
        return EpOrganCourse.EP_ORGAN_COURSE.ENTER_TIME_END;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field15() {
        return EpOrganCourse.EP_ORGAN_COURSE.TOTAL_PARTICIPATE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field16() {
        return EpOrganCourse.EP_ORGAN_COURSE.REMARK;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field17() {
        return EpOrganCourse.EP_ORGAN_COURSE.CREATE_AT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field18() {
        return EpOrganCourse.EP_ORGAN_COURSE.UPDATE_AT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Boolean> field19() {
        return EpOrganCourse.EP_ORGAN_COURSE.DEL_FLAG;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field20() {
        return EpOrganCourse.EP_ORGAN_COURSE.VERSION;
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
    public EpOrganCourseCourseType value3() {
        return getCourseType();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value4() {
        return getCourseCatalogId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value5() {
        return getCourseName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value6() {
        return getCourseIntroduce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value7() {
        return getCourseContent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal value8() {
        return getPrizeMin();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean value9() {
        return getVipFlag();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean value10() {
        return getWechatPayFlag();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganCourseCourseStatus value11() {
        return getCourseStatus();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp value12() {
        return getOnlineTime();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp value13() {
        return getEnterTimeStart();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp value14() {
        return getEnterTimeEnd();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value15() {
        return getTotalParticipate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value16() {
        return getRemark();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp value17() {
        return getCreateAt();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp value18() {
        return getUpdateAt();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean value19() {
        return getDelFlag();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value20() {
        return getVersion();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganCourseRecord value1(Long value) {
        setId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganCourseRecord value2(Long value) {
        setOgnId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganCourseRecord value3(EpOrganCourseCourseType value) {
        setCourseType(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganCourseRecord value4(Long value) {
        setCourseCatalogId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganCourseRecord value5(String value) {
        setCourseName(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganCourseRecord value6(String value) {
        setCourseIntroduce(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganCourseRecord value7(String value) {
        setCourseContent(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganCourseRecord value8(BigDecimal value) {
        setPrizeMin(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganCourseRecord value9(Boolean value) {
        setVipFlag(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganCourseRecord value10(Boolean value) {
        setWechatPayFlag(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganCourseRecord value11(EpOrganCourseCourseStatus value) {
        setCourseStatus(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganCourseRecord value12(Timestamp value) {
        setOnlineTime(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganCourseRecord value13(Timestamp value) {
        setEnterTimeStart(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganCourseRecord value14(Timestamp value) {
        setEnterTimeEnd(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganCourseRecord value15(Integer value) {
        setTotalParticipate(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganCourseRecord value16(String value) {
        setRemark(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganCourseRecord value17(Timestamp value) {
        setCreateAt(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganCourseRecord value18(Timestamp value) {
        setUpdateAt(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganCourseRecord value19(Boolean value) {
        setDelFlag(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganCourseRecord value20(Long value) {
        setVersion(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganCourseRecord values(Long value1, Long value2, EpOrganCourseCourseType value3, Long value4, String value5, String value6, String value7, BigDecimal value8, Boolean value9, Boolean value10, EpOrganCourseCourseStatus value11, Timestamp value12, Timestamp value13, Timestamp value14, Integer value15, String value16, Timestamp value17, Timestamp value18, Boolean value19, Long value20) {
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
        value19(value19);
        value20(value20);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached EpOrganCourseRecord
     */
    public EpOrganCourseRecord() {
        super(EpOrganCourse.EP_ORGAN_COURSE);
    }

    /**
     * Create a detached, initialised EpOrganCourseRecord
     */
    public EpOrganCourseRecord(Long id, Long ognId, EpOrganCourseCourseType courseType, Long courseCatalogId, String courseName, String courseIntroduce, String courseContent, BigDecimal prizeMin, Boolean vipFlag, Boolean wechatPayFlag, EpOrganCourseCourseStatus courseStatus, Timestamp onlineTime, Timestamp enterTimeStart, Timestamp enterTimeEnd, Integer totalParticipate, String remark, Timestamp createAt, Timestamp updateAt, Boolean delFlag, Long version) {
        super(EpOrganCourse.EP_ORGAN_COURSE);

        set(0, id);
        set(1, ognId);
        set(2, courseType);
        set(3, courseCatalogId);
        set(4, courseName);
        set(5, courseIntroduce);
        set(6, courseContent);
        set(7, prizeMin);
        set(8, vipFlag);
        set(9, wechatPayFlag);
        set(10, courseStatus);
        set(11, onlineTime);
        set(12, enterTimeStart);
        set(13, enterTimeEnd);
        set(14, totalParticipate);
        set(15, remark);
        set(16, createAt);
        set(17, updateAt);
        set(18, delFlag);
        set(19, version);
    }
}
