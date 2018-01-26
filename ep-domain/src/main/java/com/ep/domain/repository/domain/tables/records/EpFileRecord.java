/*
 * This file is generated by jOOQ.
*/
package com.ep.domain.repository.domain.tables.records;


import com.ep.domain.repository.domain.tables.EpFile;

import java.sql.Timestamp;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record11;
import org.jooq.Row11;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * 文件表
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.9.6"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class EpFileRecord extends UpdatableRecordImpl<EpFileRecord> implements Record11<Long, String, String, Short, Long, Integer, Timestamp, Timestamp, String, Boolean, Long> {

    private static final long serialVersionUID = -1218887556;

    /**
     * Setter for <code>ep.ep_file.id</code>. 主键
     */
    public EpFileRecord setId(Long value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_file.id</code>. 主键
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>ep.ep_file.file_name</code>. 文件名字
     */
    public EpFileRecord setFileName(String value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_file.file_name</code>. 文件名字
     */
    public String getFileName() {
        return (String) get(1);
    }

    /**
     * Setter for <code>ep.ep_file.file_url</code>. 文件url
     */
    public EpFileRecord setFileUrl(String value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_file.file_url</code>. 文件url
     */
    public String getFileUrl() {
        return (String) get(2);
    }

    /**
     * Setter for <code>ep.ep_file.biz_type_code</code>. 业务类型编码（FILE_BIZ_TYPE）
     */
    public EpFileRecord setBizTypeCode(Short value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_file.biz_type_code</code>. 业务类型编码（FILE_BIZ_TYPE）
     */
    public Short getBizTypeCode() {
        return (Short) get(3);
    }

    /**
     * Setter for <code>ep.ep_file.source_id</code>. 业务ID
     */
    public EpFileRecord setSourceId(Long value) {
        set(4, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_file.source_id</code>. 业务ID
     */
    public Long getSourceId() {
        return (Long) get(4);
    }

    /**
     * Setter for <code>ep.ep_file.sort</code>. 排序
     */
    public EpFileRecord setSort(Integer value) {
        set(5, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_file.sort</code>. 排序
     */
    public Integer getSort() {
        return (Integer) get(5);
    }

    /**
     * Setter for <code>ep.ep_file.create_at</code>. 创建时间
     */
    public EpFileRecord setCreateAt(Timestamp value) {
        set(6, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_file.create_at</code>. 创建时间
     */
    public Timestamp getCreateAt() {
        return (Timestamp) get(6);
    }

    /**
     * Setter for <code>ep.ep_file.update_at</code>. 更新时间
     */
    public EpFileRecord setUpdateAt(Timestamp value) {
        set(7, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_file.update_at</code>. 更新时间
     */
    public Timestamp getUpdateAt() {
        return (Timestamp) get(7);
    }

    /**
     * Setter for <code>ep.ep_file.remark</code>. 备注信息
     */
    public EpFileRecord setRemark(String value) {
        set(8, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_file.remark</code>. 备注信息
     */
    public String getRemark() {
        return (String) get(8);
    }

    /**
     * Setter for <code>ep.ep_file.del_flag</code>. 删除标志
     */
    public EpFileRecord setDelFlag(Boolean value) {
        set(9, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_file.del_flag</code>. 删除标志
     */
    public Boolean getDelFlag() {
        return (Boolean) get(9);
    }

    /**
     * Setter for <code>ep.ep_file.version</code>.
     */
    public EpFileRecord setVersion(Long value) {
        set(10, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_file.version</code>.
     */
    public Long getVersion() {
        return (Long) get(10);
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
    // Record11 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row11<Long, String, String, Short, Long, Integer, Timestamp, Timestamp, String, Boolean, Long> fieldsRow() {
        return (Row11) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row11<Long, String, String, Short, Long, Integer, Timestamp, Timestamp, String, Boolean, Long> valuesRow() {
        return (Row11) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field1() {
        return EpFile.EP_FILE.ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field2() {
        return EpFile.EP_FILE.FILE_NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field3() {
        return EpFile.EP_FILE.FILE_URL;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Short> field4() {
        return EpFile.EP_FILE.BIZ_TYPE_CODE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field5() {
        return EpFile.EP_FILE.SOURCE_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field6() {
        return EpFile.EP_FILE.SORT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field7() {
        return EpFile.EP_FILE.CREATE_AT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field8() {
        return EpFile.EP_FILE.UPDATE_AT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field9() {
        return EpFile.EP_FILE.REMARK;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Boolean> field10() {
        return EpFile.EP_FILE.DEL_FLAG;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field11() {
        return EpFile.EP_FILE.VERSION;
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
        return getFileName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value3() {
        return getFileUrl();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Short value4() {
        return getBizTypeCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value5() {
        return getSourceId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value6() {
        return getSort();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp value7() {
        return getCreateAt();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp value8() {
        return getUpdateAt();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value9() {
        return getRemark();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean value10() {
        return getDelFlag();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value11() {
        return getVersion();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpFileRecord value1(Long value) {
        setId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpFileRecord value2(String value) {
        setFileName(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpFileRecord value3(String value) {
        setFileUrl(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpFileRecord value4(Short value) {
        setBizTypeCode(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpFileRecord value5(Long value) {
        setSourceId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpFileRecord value6(Integer value) {
        setSort(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpFileRecord value7(Timestamp value) {
        setCreateAt(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpFileRecord value8(Timestamp value) {
        setUpdateAt(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpFileRecord value9(String value) {
        setRemark(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpFileRecord value10(Boolean value) {
        setDelFlag(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpFileRecord value11(Long value) {
        setVersion(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpFileRecord values(Long value1, String value2, String value3, Short value4, Long value5, Integer value6, Timestamp value7, Timestamp value8, String value9, Boolean value10, Long value11) {
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
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached EpFileRecord
     */
    public EpFileRecord() {
        super(EpFile.EP_FILE);
    }

    /**
     * Create a detached, initialised EpFileRecord
     */
    public EpFileRecord(Long id, String fileName, String fileUrl, Short bizTypeCode, Long sourceId, Integer sort, Timestamp createAt, Timestamp updateAt, String remark, Boolean delFlag, Long version) {
        super(EpFile.EP_FILE);

        set(0, id);
        set(1, fileName);
        set(2, fileUrl);
        set(3, bizTypeCode);
        set(4, sourceId);
        set(5, sort);
        set(6, createAt);
        set(7, updateAt);
        set(8, remark);
        set(9, delFlag);
        set(10, version);
    }
}
