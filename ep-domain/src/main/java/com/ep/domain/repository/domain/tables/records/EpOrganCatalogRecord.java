/*
 * This file is generated by jOOQ.
*/
package com.ep.domain.repository.domain.tables.records;


import com.ep.domain.repository.domain.tables.EpOrganCatalog;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record8;
import org.jooq.Row8;
import org.jooq.impl.UpdatableRecordImpl;

import javax.annotation.Generated;
import java.sql.Timestamp;


/**
 * 机构类目表
 */
@Generated(
        value = {
                "http://www.jooq.org",
                "jOOQ version:3.9.6"
        },
        comments = "This class is generated by jOOQ"
)
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class EpOrganCatalogRecord extends UpdatableRecordImpl<EpOrganCatalogRecord> implements Record8<Long, Long, Long, Timestamp, Timestamp, String, Boolean, Long> {

    private static final long serialVersionUID = -1570567201;

    /**
     * Setter for <code>ep.ep_organ_catalog.id</code>. 主键
     */
    public EpOrganCatalogRecord setId(Long value) {
        set(0, value);
        return this;
    }

    /**
     * Create a detached EpOrganCatalogRecord
     */
    public EpOrganCatalogRecord() {
        super(EpOrganCatalog.EP_ORGAN_CATALOG);
    }

    /**
     * Setter for <code>ep.ep_organ_catalog.ogn_id</code>. 机构id
     */
    public EpOrganCatalogRecord setOgnId(Long value) {
        set(1, value);
        return this;
    }

    /**
     * Create a detached, initialised EpOrganCatalogRecord
     */
    public EpOrganCatalogRecord(Long id, Long ognId, Long courseCatalogId, Timestamp createAt, Timestamp updateAt, String remark, Boolean delFlag, Long version) {
        super(EpOrganCatalog.EP_ORGAN_CATALOG);

        set(0, id);
        set(1, ognId);
        set(2, courseCatalogId);
        set(3, createAt);
        set(4, updateAt);
        set(5, remark);
        set(6, delFlag);
        set(7, version);
    }

    /**
     * Setter for <code>ep.ep_organ_catalog.course_catalog_id</code>. 课程类目id
     */
    public EpOrganCatalogRecord setCourseCatalogId(Long value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_catalog.id</code>. 主键
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>ep.ep_organ_catalog.create_at</code>. 创建时间
     */
    public EpOrganCatalogRecord setCreateAt(Timestamp value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_catalog.ogn_id</code>. 机构id
     */
    public Long getOgnId() {
        return (Long) get(1);
    }

    /**
     * Setter for <code>ep.ep_organ_catalog.update_at</code>. 更新时间
     */
    public EpOrganCatalogRecord setUpdateAt(Timestamp value) {
        set(4, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_catalog.course_catalog_id</code>. 课程类目id
     */
    public Long getCourseCatalogId() {
        return (Long) get(2);
    }

    /**
     * Setter for <code>ep.ep_organ_catalog.remark</code>. 备注
     */
    public EpOrganCatalogRecord setRemark(String value) {
        set(5, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_catalog.remark</code>. 备注
     */
    public String getRemark() {
        return (String) get(5);
    }

    /**
     * Getter for <code>ep.ep_organ_catalog.create_at</code>. 创建时间
     */
    public Timestamp getCreateAt() {
        return (Timestamp) get(3);
    }

    /**
     * Getter for <code>ep.ep_organ_catalog.del_flag</code>. 删除标记
     */
    public Boolean getDelFlag() {
        return (Boolean) get(6);
    }

    /**
     * Getter for <code>ep.ep_organ_catalog.update_at</code>. 更新时间
     */
    public Timestamp getUpdateAt() {
        return (Timestamp) get(4);
    }

    /**
     * Getter for <code>ep.ep_organ_catalog.version</code>.
     */
    public Long getVersion() {
        return (Long) get(7);
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
    // Record8 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row8<Long, Long, Long, Timestamp, Timestamp, String, Boolean, Long> fieldsRow() {
        return (Row8) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row8<Long, Long, Long, Timestamp, Timestamp, String, Boolean, Long> valuesRow() {
        return (Row8) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field1() {
        return EpOrganCatalog.EP_ORGAN_CATALOG.ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field2() {
        return EpOrganCatalog.EP_ORGAN_CATALOG.OGN_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field3() {
        return EpOrganCatalog.EP_ORGAN_CATALOG.COURSE_CATALOG_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field4() {
        return EpOrganCatalog.EP_ORGAN_CATALOG.CREATE_AT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field5() {
        return EpOrganCatalog.EP_ORGAN_CATALOG.UPDATE_AT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field6() {
        return EpOrganCatalog.EP_ORGAN_CATALOG.REMARK;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Boolean> field7() {
        return EpOrganCatalog.EP_ORGAN_CATALOG.DEL_FLAG;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field8() {
        return EpOrganCatalog.EP_ORGAN_CATALOG.VERSION;
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
        return getCourseCatalogId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp value4() {
        return getCreateAt();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp value5() {
        return getUpdateAt();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value6() {
        return getRemark();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean value7() {
        return getDelFlag();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value8() {
        return getVersion();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganCatalogRecord value1(Long value) {
        setId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganCatalogRecord value2(Long value) {
        setOgnId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganCatalogRecord value3(Long value) {
        setCourseCatalogId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganCatalogRecord value4(Timestamp value) {
        setCreateAt(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganCatalogRecord value5(Timestamp value) {
        setUpdateAt(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganCatalogRecord value6(String value) {
        setRemark(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganCatalogRecord value7(Boolean value) {
        setDelFlag(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganCatalogRecord value8(Long value) {
        setVersion(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganCatalogRecord values(Long value1, Long value2, Long value3, Timestamp value4, Timestamp value5, String value6, Boolean value7, Long value8) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        value8(value8);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Setter for <code>ep.ep_organ_catalog.del_flag</code>. 删除标记
     */
    public EpOrganCatalogRecord setDelFlag(Boolean value) {
        set(6, value);
        return this;
    }

    /**
     * Setter for <code>ep.ep_organ_catalog.version</code>.
     */
    public EpOrganCatalogRecord setVersion(Long value) {
        set(7, value);
        return this;
    }
}
