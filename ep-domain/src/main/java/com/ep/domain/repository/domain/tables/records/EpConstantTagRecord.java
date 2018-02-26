/*
 * This file is generated by jOOQ.
*/
package com.ep.domain.repository.domain.tables.records;


import com.ep.domain.repository.domain.tables.EpConstantTag;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record12;
import org.jooq.Row12;
import org.jooq.impl.UpdatableRecordImpl;

import javax.annotation.Generated;
import java.sql.Timestamp;


/**
 * 标签表
 */
@Generated(
        value = {
                "http://www.jooq.org",
                "jOOQ version:3.9.6"
        },
        comments = "This class is generated by jOOQ"
)
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class EpConstantTagRecord extends UpdatableRecordImpl<EpConstantTagRecord> implements Record12<Long, Long, String, Byte, Long, Boolean, Long, Timestamp, Timestamp, String, Boolean, Long> {

    private static final long serialVersionUID = 1354384936;

    /**
     * Setter for <code>ep.ep_constant_tag.id</code>.
     */
    public EpConstantTagRecord setId(Long value) {
        set(0, value);
        return this;
    }

    /**
     * Create a detached EpConstantTagRecord
     */
    public EpConstantTagRecord() {
        super(EpConstantTag.EP_CONSTANT_TAG);
    }

    /**
     * Setter for <code>ep.ep_constant_tag.catalog_id</code>. 课程类目id
     */
    public EpConstantTagRecord setCatalogId(Long value) {
        set(1, value);
        return this;
    }

    /**
     * Create a detached, initialised EpConstantTagRecord
     */
    public EpConstantTagRecord(Long id, Long catalogId, String tagName, Byte tagLevel, Long sort, Boolean ognFlag, Long ognId, Timestamp createAt, Timestamp updateAt, String remark, Boolean delFlag, Long version) {
        super(EpConstantTag.EP_CONSTANT_TAG);

        set(0, id);
        set(1, catalogId);
        set(2, tagName);
        set(3, tagLevel);
        set(4, sort);
        set(5, ognFlag);
        set(6, ognId);
        set(7, createAt);
        set(8, updateAt);
        set(9, remark);
        set(10, delFlag);
        set(11, version);
    }

    /**
     * Setter for <code>ep.ep_constant_tag.tag_name</code>. 标签名称
     */
    public EpConstantTagRecord setTagName(String value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_constant_tag.id</code>.
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>ep.ep_constant_tag.tag_level</code>. 标签等级
     */
    public EpConstantTagRecord setTagLevel(Byte value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_constant_tag.catalog_id</code>. 课程类目id
     */
    public Long getCatalogId() {
        return (Long) get(1);
    }

    /**
     * Setter for <code>ep.ep_constant_tag.sort</code>. 排序
     */
    public EpConstantTagRecord setSort(Long value) {
        set(4, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_constant_tag.tag_name</code>. 标签名称
     */
    public String getTagName() {
        return (String) get(2);
    }

    /**
     * Setter for <code>ep.ep_constant_tag.ogn_flag</code>. 是否机构私有标签
     */
    public EpConstantTagRecord setOgnFlag(Boolean value) {
        set(5, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_constant_tag.tag_level</code>. 标签等级
     */
    public Byte getTagLevel() {
        return (Byte) get(3);
    }

    /**
     * Setter for <code>ep.ep_constant_tag.ogn_id</code>. 机构id
     */
    public EpConstantTagRecord setOgnId(Long value) {
        set(6, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_constant_tag.sort</code>. 排序
     */
    public Long getSort() {
        return (Long) get(4);
    }

    /**
     * Setter for <code>ep.ep_constant_tag.create_at</code>. 创建时间
     */
    public EpConstantTagRecord setCreateAt(Timestamp value) {
        set(7, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_constant_tag.ogn_flag</code>. 是否机构私有标签
     */
    public Boolean getOgnFlag() {
        return (Boolean) get(5);
    }

    /**
     * Setter for <code>ep.ep_constant_tag.update_at</code>. 更新时间
     */
    public EpConstantTagRecord setUpdateAt(Timestamp value) {
        set(8, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_constant_tag.ogn_id</code>. 机构id
     */
    public Long getOgnId() {
        return (Long) get(6);
    }

    /**
     * Setter for <code>ep.ep_constant_tag.remark</code>. 备注
     */
    public EpConstantTagRecord setRemark(String value) {
        set(9, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_constant_tag.create_at</code>. 创建时间
     */
    public Timestamp getCreateAt() {
        return (Timestamp) get(7);
    }

    /**
     * Setter for <code>ep.ep_constant_tag.del_flag</code>. 删除标记
     */
    public EpConstantTagRecord setDelFlag(Boolean value) {
        set(10, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_constant_tag.update_at</code>. 更新时间
     */
    public Timestamp getUpdateAt() {
        return (Timestamp) get(8);
    }

    /**
     * Getter for <code>ep.ep_constant_tag.remark</code>. 备注
     */
    public String getRemark() {
        return (String) get(9);
    }

    /**
     * Getter for <code>ep.ep_constant_tag.version</code>.
     */
    public Long getVersion() {
        return (Long) get(11);
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
    // Record12 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row12<Long, Long, String, Byte, Long, Boolean, Long, Timestamp, Timestamp, String, Boolean, Long> fieldsRow() {
        return (Row12) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row12<Long, Long, String, Byte, Long, Boolean, Long, Timestamp, Timestamp, String, Boolean, Long> valuesRow() {
        return (Row12) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field1() {
        return EpConstantTag.EP_CONSTANT_TAG.ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field2() {
        return EpConstantTag.EP_CONSTANT_TAG.CATALOG_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field3() {
        return EpConstantTag.EP_CONSTANT_TAG.TAG_NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Byte> field4() {
        return EpConstantTag.EP_CONSTANT_TAG.TAG_LEVEL;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field5() {
        return EpConstantTag.EP_CONSTANT_TAG.SORT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Boolean> field6() {
        return EpConstantTag.EP_CONSTANT_TAG.OGN_FLAG;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field7() {
        return EpConstantTag.EP_CONSTANT_TAG.OGN_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field8() {
        return EpConstantTag.EP_CONSTANT_TAG.CREATE_AT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field9() {
        return EpConstantTag.EP_CONSTANT_TAG.UPDATE_AT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field10() {
        return EpConstantTag.EP_CONSTANT_TAG.REMARK;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Boolean> field11() {
        return EpConstantTag.EP_CONSTANT_TAG.DEL_FLAG;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field12() {
        return EpConstantTag.EP_CONSTANT_TAG.VERSION;
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
        return getCatalogId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value3() {
        return getTagName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Byte value4() {
        return getTagLevel();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value5() {
        return getSort();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean value6() {
        return getOgnFlag();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value7() {
        return getOgnId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp value8() {
        return getCreateAt();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp value9() {
        return getUpdateAt();
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
    public Boolean value11() {
        return getDelFlag();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value12() {
        return getVersion();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpConstantTagRecord value1(Long value) {
        setId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpConstantTagRecord value2(Long value) {
        setCatalogId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpConstantTagRecord value3(String value) {
        setTagName(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpConstantTagRecord value4(Byte value) {
        setTagLevel(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpConstantTagRecord value5(Long value) {
        setSort(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpConstantTagRecord value6(Boolean value) {
        setOgnFlag(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpConstantTagRecord value7(Long value) {
        setOgnId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpConstantTagRecord value8(Timestamp value) {
        setCreateAt(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpConstantTagRecord value9(Timestamp value) {
        setUpdateAt(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpConstantTagRecord value10(String value) {
        setRemark(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpConstantTagRecord value11(Boolean value) {
        setDelFlag(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpConstantTagRecord value12(Long value) {
        setVersion(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpConstantTagRecord values(Long value1, Long value2, String value3, Byte value4, Long value5, Boolean value6, Long value7, Timestamp value8, Timestamp value9, String value10, Boolean value11, Long value12) {
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
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Getter for <code>ep.ep_constant_tag.del_flag</code>. 删除标记
     */
    public Boolean getDelFlag() {
        return (Boolean) get(10);
    }

    /**
     * Setter for <code>ep.ep_constant_tag.version</code>.
     */
    public EpConstantTagRecord setVersion(Long value) {
        set(11, value);
        return this;
    }
}
