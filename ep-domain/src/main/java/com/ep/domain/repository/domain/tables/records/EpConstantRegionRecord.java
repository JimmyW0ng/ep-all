/*
 * This file is generated by jOOQ.
*/
package com.ep.domain.repository.domain.tables.records;


import com.ep.domain.repository.domain.enums.EpConstantRegionRegionType;
import com.ep.domain.repository.domain.tables.EpConstantRegion;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record16;
import org.jooq.Row16;
import org.jooq.impl.UpdatableRecordImpl;

import javax.annotation.Generated;
import java.sql.Timestamp;


/**
 * 地区表
 */
@Generated(
        value = {
                "http://www.jooq.org",
                "jOOQ version:3.9.6"
        },
        comments = "This class is generated by jOOQ"
)
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class EpConstantRegionRecord extends UpdatableRecordImpl<EpConstantRegionRecord> implements Record16<Long, String, Long, String, EpConstantRegionRegionType, String, String, String, String, String, String, Timestamp, Timestamp, String, Boolean, Long> {

    private static final long serialVersionUID = 625364668;

    /**
     * Setter for <code>ep.ep_constant_region.id</code>. 主键
     */
    public EpConstantRegionRecord setId(Long value) {
        set(0, value);
        return this;
    }

    /**
     * Create a detached EpConstantRegionRecord
     */
    public EpConstantRegionRecord() {
        super(EpConstantRegion.EP_CONSTANT_REGION);
    }

    /**
     * Setter for <code>ep.ep_constant_region.region_name</code>. 地区名称
     */
    public EpConstantRegionRecord setRegionName(String value) {
        set(1, value);
        return this;
    }

    /**
     * Create a detached, initialised EpConstantRegionRecord
     */
    public EpConstantRegionRecord(Long id, String regionName, Long parentId, String shortName, EpConstantRegionRegionType regionType, String cityCode, String zipCode, String mergerName, String regionLng, String regionLat, String pinYin, Timestamp createAt, Timestamp updateAt, String remark, Boolean delFlag, Long version) {
        super(EpConstantRegion.EP_CONSTANT_REGION);

        set(0, id);
        set(1, regionName);
        set(2, parentId);
        set(3, shortName);
        set(4, regionType);
        set(5, cityCode);
        set(6, zipCode);
        set(7, mergerName);
        set(8, regionLng);
        set(9, regionLat);
        set(10, pinYin);
        set(11, createAt);
        set(12, updateAt);
        set(13, remark);
        set(14, delFlag);
        set(15, version);
    }

    /**
     * Setter for <code>ep.ep_constant_region.parent_id</code>. 父级id
     */
    public EpConstantRegionRecord setParentId(Long value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_constant_region.id</code>. 主键
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>ep.ep_constant_region.short_name</code>. 简称
     */
    public EpConstantRegionRecord setShortName(String value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_constant_region.region_name</code>. 地区名称
     */
    public String getRegionName() {
        return (String) get(1);
    }

    /**
     * Setter for <code>ep.ep_constant_region.region_type</code>. 类型：国；省；市；区
     */
    public EpConstantRegionRecord setRegionType(EpConstantRegionRegionType value) {
        set(4, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_constant_region.parent_id</code>. 父级id
     */
    public Long getParentId() {
        return (Long) get(2);
    }

    /**
     * Setter for <code>ep.ep_constant_region.city_code</code>. 地区编码
     */
    public EpConstantRegionRecord setCityCode(String value) {
        set(5, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_constant_region.short_name</code>. 简称
     */
    public String getShortName() {
        return (String) get(3);
    }

    /**
     * Setter for <code>ep.ep_constant_region.zip_code</code>. 邮政编码
     */
    public EpConstantRegionRecord setZipCode(String value) {
        set(6, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_constant_region.region_type</code>. 类型：国；省；市；区
     */
    public EpConstantRegionRegionType getRegionType() {
        return (EpConstantRegionRegionType) get(4);
    }

    /**
     * Setter for <code>ep.ep_constant_region.merger_name</code>. 全称
     */
    public EpConstantRegionRecord setMergerName(String value) {
        set(7, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_constant_region.city_code</code>. 地区编码
     */
    public String getCityCode() {
        return (String) get(5);
    }

    /**
     * Setter for <code>ep.ep_constant_region.region_lng</code>. 经度
     */
    public EpConstantRegionRecord setRegionLng(String value) {
        set(8, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_constant_region.zip_code</code>. 邮政编码
     */
    public String getZipCode() {
        return (String) get(6);
    }

    /**
     * Setter for <code>ep.ep_constant_region.region_lat</code>. 维度
     */
    public EpConstantRegionRecord setRegionLat(String value) {
        set(9, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_constant_region.merger_name</code>. 全称
     */
    public String getMergerName() {
        return (String) get(7);
    }

    /**
     * Setter for <code>ep.ep_constant_region.pin_yin</code>. 拼音
     */
    public EpConstantRegionRecord setPinYin(String value) {
        set(10, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_constant_region.region_lng</code>. 经度
     */
    public String getRegionLng() {
        return (String) get(8);
    }

    /**
     * Setter for <code>ep.ep_constant_region.create_at</code>. 创建时间
     */
    public EpConstantRegionRecord setCreateAt(Timestamp value) {
        set(11, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_constant_region.region_lat</code>. 维度
     */
    public String getRegionLat() {
        return (String) get(9);
    }

    /**
     * Setter for <code>ep.ep_constant_region.update_at</code>. 更新时间
     */
    public EpConstantRegionRecord setUpdateAt(Timestamp value) {
        set(12, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_constant_region.pin_yin</code>. 拼音
     */
    public String getPinYin() {
        return (String) get(10);
    }

    /**
     * Setter for <code>ep.ep_constant_region.remark</code>. 备注
     */
    public EpConstantRegionRecord setRemark(String value) {
        set(13, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_constant_region.remark</code>. 备注
     */
    public String getRemark() {
        return (String) get(13);
    }

    /**
     * Getter for <code>ep.ep_constant_region.create_at</code>. 创建时间
     */
    public Timestamp getCreateAt() {
        return (Timestamp) get(11);
    }

    /**
     * Getter for <code>ep.ep_constant_region.del_flag</code>. 删除标记
     */
    public Boolean getDelFlag() {
        return (Boolean) get(14);
    }

    /**
     * Getter for <code>ep.ep_constant_region.update_at</code>. 更新时间
     */
    public Timestamp getUpdateAt() {
        return (Timestamp) get(12);
    }

    /**
     * Getter for <code>ep.ep_constant_region.version</code>.
     */
    public Long getVersion() {
        return (Long) get(15);
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
    // Record16 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row16<Long, String, Long, String, EpConstantRegionRegionType, String, String, String, String, String, String, Timestamp, Timestamp, String, Boolean, Long> fieldsRow() {
        return (Row16) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row16<Long, String, Long, String, EpConstantRegionRegionType, String, String, String, String, String, String, Timestamp, Timestamp, String, Boolean, Long> valuesRow() {
        return (Row16) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field1() {
        return EpConstantRegion.EP_CONSTANT_REGION.ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field2() {
        return EpConstantRegion.EP_CONSTANT_REGION.REGION_NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field3() {
        return EpConstantRegion.EP_CONSTANT_REGION.PARENT_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field4() {
        return EpConstantRegion.EP_CONSTANT_REGION.SHORT_NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<EpConstantRegionRegionType> field5() {
        return EpConstantRegion.EP_CONSTANT_REGION.REGION_TYPE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field6() {
        return EpConstantRegion.EP_CONSTANT_REGION.CITY_CODE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field7() {
        return EpConstantRegion.EP_CONSTANT_REGION.ZIP_CODE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field8() {
        return EpConstantRegion.EP_CONSTANT_REGION.MERGER_NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field9() {
        return EpConstantRegion.EP_CONSTANT_REGION.REGION_LNG;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field10() {
        return EpConstantRegion.EP_CONSTANT_REGION.REGION_LAT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field11() {
        return EpConstantRegion.EP_CONSTANT_REGION.PIN_YIN;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field12() {
        return EpConstantRegion.EP_CONSTANT_REGION.CREATE_AT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field13() {
        return EpConstantRegion.EP_CONSTANT_REGION.UPDATE_AT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field14() {
        return EpConstantRegion.EP_CONSTANT_REGION.REMARK;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Boolean> field15() {
        return EpConstantRegion.EP_CONSTANT_REGION.DEL_FLAG;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field16() {
        return EpConstantRegion.EP_CONSTANT_REGION.VERSION;
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
    public String value2() {
        return getRegionName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value3() {
        return getParentId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value4() {
        return getShortName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpConstantRegionRegionType value5() {
        return getRegionType();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value6() {
        return getCityCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value7() {
        return getZipCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value8() {
        return getMergerName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value9() {
        return getRegionLng();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value10() {
        return getRegionLat();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value11() {
        return getPinYin();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp value12() {
        return getCreateAt();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp value13() {
        return getUpdateAt();
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
    public Boolean value15() {
        return getDelFlag();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value16() {
        return getVersion();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpConstantRegionRecord value1(Long value) {
        setId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpConstantRegionRecord value2(String value) {
        setRegionName(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpConstantRegionRecord value3(Long value) {
        setParentId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpConstantRegionRecord value4(String value) {
        setShortName(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpConstantRegionRecord value5(EpConstantRegionRegionType value) {
        setRegionType(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpConstantRegionRecord value6(String value) {
        setCityCode(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpConstantRegionRecord value7(String value) {
        setZipCode(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpConstantRegionRecord value8(String value) {
        setMergerName(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpConstantRegionRecord value9(String value) {
        setRegionLng(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpConstantRegionRecord value10(String value) {
        setRegionLat(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpConstantRegionRecord value11(String value) {
        setPinYin(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpConstantRegionRecord value12(Timestamp value) {
        setCreateAt(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpConstantRegionRecord value13(Timestamp value) {
        setUpdateAt(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpConstantRegionRecord value14(String value) {
        setRemark(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpConstantRegionRecord value15(Boolean value) {
        setDelFlag(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpConstantRegionRecord value16(Long value) {
        setVersion(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpConstantRegionRecord values(Long value1, String value2, Long value3, String value4, EpConstantRegionRegionType value5, String value6, String value7, String value8, String value9, String value10, String value11, Timestamp value12, Timestamp value13, String value14, Boolean value15, Long value16) {
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
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Setter for <code>ep.ep_constant_region.del_flag</code>. 删除标记
     */
    public EpConstantRegionRecord setDelFlag(Boolean value) {
        set(14, value);
        return this;
    }

    /**
     * Setter for <code>ep.ep_constant_region.version</code>.
     */
    public EpConstantRegionRecord setVersion(Long value) {
        set(15, value);
        return this;
    }
}
