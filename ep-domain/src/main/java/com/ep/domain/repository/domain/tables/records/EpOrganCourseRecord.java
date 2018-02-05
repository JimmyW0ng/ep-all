/*
 * This file is generated by jOOQ.
*/
package com.ep.domain.repository.domain.tables.records;


import com.ep.domain.repository.domain.enums.EpOrganCourseCourseStatus;
import com.ep.domain.repository.domain.enums.EpOrganCourseCourseType;
import com.ep.domain.repository.domain.tables.EpOrganCourse;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record22;
import org.jooq.Row22;
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
public class EpOrganCourseRecord extends UpdatableRecordImpl<EpOrganCourseRecord> implements Record22<Long, Long, EpOrganCourseCourseType, Long, String, String, String, String, BigDecimal, String, String, String, EpOrganCourseCourseStatus, Timestamp, Timestamp, Timestamp, Integer, String, Timestamp, Timestamp, Boolean, Long> {

    private static final long serialVersionUID = 684149922;

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
     * Setter for <code>ep.ep_organ_course.course_type</code>. 课程类型：培训课；活动；
     */
    public EpOrganCourseRecord setCourseType(EpOrganCourseCourseType value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_course.course_type</code>. 课程类型：培训课；活动；
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
     * Setter for <code>ep.ep_organ_course.course_note</code>. 课程须知
     */
    public EpOrganCourseRecord setCourseNote(String value) {
        set(7, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_course.course_note</code>. 课程须知
     */
    public String getCourseNote() {
        return (String) get(7);
    }

    /**
     * Setter for <code>ep.ep_organ_course.prize_min</code>. 最低价格
     */
    public EpOrganCourseRecord setPrizeMin(BigDecimal value) {
        set(8, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_course.prize_min</code>. 最低价格
     */
    public BigDecimal getPrizeMin() {
        return (BigDecimal) get(8);
    }

    /**
     * Setter for <code>ep.ep_organ_course.course_address</code>. 上课地址
     */
    public EpOrganCourseRecord setCourseAddress(String value) {
        set(9, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_course.course_address</code>. 上课地址
     */
    public String getCourseAddress() {
        return (String) get(9);
    }

    /**
     * Setter for <code>ep.ep_organ_course.address_lng</code>. 上课地址经度
     */
    public EpOrganCourseRecord setAddressLng(String value) {
        set(10, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_course.address_lng</code>. 上课地址经度
     */
    public String getAddressLng() {
        return (String) get(10);
    }

    /**
     * Setter for <code>ep.ep_organ_course.address_lat</code>. 上课地址纬度
     */
    public EpOrganCourseRecord setAddressLat(String value) {
        set(11, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_course.address_lat</code>. 上课地址纬度
     */
    public String getAddressLat() {
        return (String) get(11);
    }

    /**
     * Create a detached EpOrganCourseRecord
     */
    public EpOrganCourseRecord() {
        super(EpOrganCourse.EP_ORGAN_COURSE);
    }

    /**
     * Create a detached, initialised EpOrganCourseRecord
     */
    public EpOrganCourseRecord(Long id, Long ognId, EpOrganCourseCourseType courseType, Long courseCatalogId, String courseName, String courseIntroduce, String courseContent, String courseNote, BigDecimal prizeMin, String courseAddress, String addressLng, String addressLat, EpOrganCourseCourseStatus courseStatus, Timestamp onlineTime, Timestamp enterTimeStart, Timestamp enterTimeEnd, Integer totalParticipate, String remark, Timestamp createAt, Timestamp updateAt, Boolean delFlag, Long version) {
        super(EpOrganCourse.EP_ORGAN_COURSE);

        set(0, id);
        set(1, ognId);
        set(2, courseType);
        set(3, courseCatalogId);
        set(4, courseName);
        set(5, courseIntroduce);
        set(6, courseContent);
        set(7, courseNote);
        set(8, prizeMin);
        set(9, courseAddress);
        set(10, addressLng);
        set(11, addressLat);
        set(12, courseStatus);
        set(13, onlineTime);
        set(14, enterTimeStart);
        set(15, enterTimeEnd);
        set(16, totalParticipate);
        set(17, remark);
        set(18, createAt);
        set(19, updateAt);
        set(20, delFlag);
        set(21, version);
    }

    /**
     * Setter for <code>ep.ep_organ_course.online_time</code>. 上线时间
     */
    public EpOrganCourseRecord setOnlineTime(Timestamp value) {
        set(13, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_course.online_time</code>. 上线时间
     */
    public Timestamp getOnlineTime() {
        return (Timestamp) get(13);
    }

    /**
     * Setter for <code>ep.ep_organ_course.enter_time_start</code>. 报名开始时间
     */
    public EpOrganCourseRecord setEnterTimeStart(Timestamp value) {
        set(14, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_course.enter_time_start</code>. 报名开始时间
     */
    public Timestamp getEnterTimeStart() {
        return (Timestamp) get(14);
    }

    /**
     * Setter for <code>ep.ep_organ_course.enter_time_end</code>. 报名结束时间
     */
    public EpOrganCourseRecord setEnterTimeEnd(Timestamp value) {
        set(15, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_course.enter_time_end</code>. 报名结束时间
     */
    public Timestamp getEnterTimeEnd() {
        return (Timestamp) get(15);
    }

    /**
     * Getter for <code>ep.ep_organ_course.course_status</code>. 课状态：已保存；已上线；进行中；已下线；
     */
    public EpOrganCourseCourseStatus getCourseStatus() {
        return (EpOrganCourseCourseStatus) get(12);
    }

    /**
     * Getter for <code>ep.ep_organ_course.total_participate</code>. 总参与人数
     */
    public Integer getTotalParticipate() {
        return (Integer) get(16);
    }

    /**
     * Setter for <code>ep.ep_organ_course.course_status</code>. 课状态：已保存；已上线；进行中；已下线；
     */
    public EpOrganCourseRecord setCourseStatus(EpOrganCourseCourseStatus value) {
        set(12, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_course.remark</code>. 备注信息
     */
    public String getRemark() {
        return (String) get(17);
    }

    /**
     * Setter for <code>ep.ep_organ_course.total_participate</code>. 总参与人数
     */
    public EpOrganCourseRecord setTotalParticipate(Integer value) {
        set(16, value);
        return this;
    }

    /**
     * Setter for <code>ep.ep_organ_course.remark</code>. 备注信息
     */
    public EpOrganCourseRecord setRemark(String value) {
        set(17, value);
        return this;
    }

    /**
     * Setter for <code>ep.ep_organ_course.update_at</code>. 更新时间
     */
    public EpOrganCourseRecord setUpdateAt(Timestamp value) {
        set(19, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_course.update_at</code>. 更新时间
     */
    public Timestamp getUpdateAt() {
        return (Timestamp) get(19);
    }

    /**
     * Setter for <code>ep.ep_organ_course.del_flag</code>. 删除标志
     */
    public EpOrganCourseRecord setDelFlag(Boolean value) {
        set(20, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_course.create_at</code>. 创建时间
     */
    public Timestamp getCreateAt() {
        return (Timestamp) get(18);
    }

    /**
     * Setter for <code>ep.ep_organ_course.create_at</code>. 创建时间
     */
    public EpOrganCourseRecord setCreateAt(Timestamp value) {
        set(18, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_course.del_flag</code>. 删除标志
     */
    public Boolean getDelFlag() {
        return (Boolean) get(20);
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
    // Record22 type implementation
    // -------------------------------------------------------------------------

    /**
     * Getter for <code>ep.ep_organ_course.version</code>.
     */
    public Long getVersion() {
        return (Long) get(21);
    }

    /**
     * Setter for <code>ep.ep_organ_course.version</code>.
     */
    public EpOrganCourseRecord setVersion(Long value) {
        set(21, value);
        return this;
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
    public Field<String> field8() {
        return EpOrganCourse.EP_ORGAN_COURSE.COURSE_NOTE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<BigDecimal> field9() {
        return EpOrganCourse.EP_ORGAN_COURSE.PRIZE_MIN;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field10() {
        return EpOrganCourse.EP_ORGAN_COURSE.COURSE_ADDRESS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field11() {
        return EpOrganCourse.EP_ORGAN_COURSE.ADDRESS_LNG;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field12() {
        return EpOrganCourse.EP_ORGAN_COURSE.ADDRESS_LAT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<EpOrganCourseCourseStatus> field13() {
        return EpOrganCourse.EP_ORGAN_COURSE.COURSE_STATUS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field14() {
        return EpOrganCourse.EP_ORGAN_COURSE.ONLINE_TIME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field15() {
        return EpOrganCourse.EP_ORGAN_COURSE.ENTER_TIME_START;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field16() {
        return EpOrganCourse.EP_ORGAN_COURSE.ENTER_TIME_END;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field17() {
        return EpOrganCourse.EP_ORGAN_COURSE.TOTAL_PARTICIPATE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field18() {
        return EpOrganCourse.EP_ORGAN_COURSE.REMARK;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field19() {
        return EpOrganCourse.EP_ORGAN_COURSE.CREATE_AT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field20() {
        return EpOrganCourse.EP_ORGAN_COURSE.UPDATE_AT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row22<Long, Long, EpOrganCourseCourseType, Long, String, String, String, String, BigDecimal, String, String, String, EpOrganCourseCourseStatus, Timestamp, Timestamp, Timestamp, Integer, String, Timestamp, Timestamp, Boolean, Long> fieldsRow() {
        return (Row22) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row22<Long, Long, EpOrganCourseCourseType, Long, String, String, String, String, BigDecimal, String, String, String, EpOrganCourseCourseStatus, Timestamp, Timestamp, Timestamp, Integer, String, Timestamp, Timestamp, Boolean, Long> valuesRow() {
        return (Row22) super.valuesRow();
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
    public String value8() {
        return getCourseNote();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal value9() {
        return getPrizeMin();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value10() {
        return getCourseAddress();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value11() {
        return getAddressLng();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value12() {
        return getAddressLat();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganCourseCourseStatus value13() {
        return getCourseStatus();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp value14() {
        return getOnlineTime();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp value15() {
        return getEnterTimeStart();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp value16() {
        return getEnterTimeEnd();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value17() {
        return getTotalParticipate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value18() {
        return getRemark();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp value19() {
        return getCreateAt();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp value20() {
        return getUpdateAt();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Boolean> field21() {
        return EpOrganCourse.EP_ORGAN_COURSE.DEL_FLAG;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field22() {
        return EpOrganCourse.EP_ORGAN_COURSE.VERSION;
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
    public EpOrganCourseRecord value8(String value) {
        setCourseNote(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganCourseRecord value9(BigDecimal value) {
        setPrizeMin(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganCourseRecord value10(String value) {
        setCourseAddress(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganCourseRecord value11(String value) {
        setAddressLng(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganCourseRecord value12(String value) {
        setAddressLat(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganCourseRecord value13(EpOrganCourseCourseStatus value) {
        setCourseStatus(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganCourseRecord value14(Timestamp value) {
        setOnlineTime(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganCourseRecord value15(Timestamp value) {
        setEnterTimeStart(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganCourseRecord value16(Timestamp value) {
        setEnterTimeEnd(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganCourseRecord value17(Integer value) {
        setTotalParticipate(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganCourseRecord value18(String value) {
        setRemark(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganCourseRecord value19(Timestamp value) {
        setCreateAt(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganCourseRecord value20(Timestamp value) {
        setUpdateAt(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganCourseRecord value21(Boolean value) {
        setDelFlag(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganCourseRecord value22(Long value) {
        setVersion(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganCourseRecord values(Long value1, Long value2, EpOrganCourseCourseType value3, Long value4, String value5, String value6, String value7, String value8, BigDecimal value9, String value10, String value11, String value12, EpOrganCourseCourseStatus value13, Timestamp value14, Timestamp value15, Timestamp value16, Integer value17, String value18, Timestamp value19, Timestamp value20, Boolean value21, Long value22) {
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
        value21(value21);
        value22(value22);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean value21() {
        return getDelFlag();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value22() {
        return getVersion();
    }
}
