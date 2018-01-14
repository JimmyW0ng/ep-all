/*
 * This file is generated by jOOQ.
*/
package com.ep.domain.repository.domain.tables.records;


import com.ep.domain.repository.domain.enums.EpMessageCaptchaCaptchaScene;
import com.ep.domain.repository.domain.enums.EpMessageCaptchaCaptchaType;
import com.ep.domain.repository.domain.tables.EpMessageCaptcha;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record13;
import org.jooq.Row13;
import org.jooq.impl.UpdatableRecordImpl;

import javax.annotation.Generated;
import java.sql.Timestamp;


/**
 * 验证码表
 */
@Generated(
        value = {
                "http://www.jooq.org",
                "jOOQ version:3.9.6"
        },
        comments = "This class is generated by jOOQ"
)
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class EpMessageCaptchaRecord extends UpdatableRecordImpl<EpMessageCaptchaRecord> implements Record13<Long, EpMessageCaptchaCaptchaType, Long, String, String, EpMessageCaptchaCaptchaScene, Timestamp, String, Timestamp, Timestamp, String, Boolean, Long> {

    private static final long serialVersionUID = -1906792210;

    /**
     * Setter for <code>ep.ep_message_captcha.id</code>. 主键
     */
    public EpMessageCaptchaRecord setId(Long value) {
        set(0, value);
        return this;
    }

    /**
     * Create a detached EpMessageCaptchaRecord
     */
    public EpMessageCaptchaRecord() {
        super(EpMessageCaptcha.EP_MESSAGE_CAPTCHA);
    }

    /**
     * Setter for <code>ep.ep_message_captcha.captcha_type</code>. 类型：短信
     */
    public EpMessageCaptchaRecord setCaptchaType(EpMessageCaptchaCaptchaType value) {
        set(1, value);
        return this;
    }

    /**
     * Create a detached, initialised EpMessageCaptchaRecord
     */
    public EpMessageCaptchaRecord(Long id, EpMessageCaptchaCaptchaType captchaType, Long sourceId, String captchaCode, String captchaContent, EpMessageCaptchaCaptchaScene captchaScene, Timestamp expireTime, String ip, Timestamp createAt, Timestamp updateAt, String remark, Boolean delFlag, Long version) {
        super(EpMessageCaptcha.EP_MESSAGE_CAPTCHA);

        set(0, id);
        set(1, captchaType);
        set(2, sourceId);
        set(3, captchaCode);
        set(4, captchaContent);
        set(5, captchaScene);
        set(6, expireTime);
        set(7, ip);
        set(8, createAt);
        set(9, updateAt);
        set(10, remark);
        set(11, delFlag);
        set(12, version);
    }

    /**
     * Setter for <code>ep.ep_message_captcha.source_id</code>. 业务id
     */
    public EpMessageCaptchaRecord setSourceId(Long value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_message_captcha.id</code>. 主键
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>ep.ep_message_captcha.captcha_code</code>. 业务编码
     */
    public EpMessageCaptchaRecord setCaptchaCode(String value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_message_captcha.captcha_type</code>. 类型：短信
     */
    public EpMessageCaptchaCaptchaType getCaptchaType() {
        return (EpMessageCaptchaCaptchaType) get(1);
    }

    /**
     * Setter for <code>ep.ep_message_captcha.captcha_content</code>. 验证码内容
     */
    public EpMessageCaptchaRecord setCaptchaContent(String value) {
        set(4, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_message_captcha.source_id</code>. 业务id
     */
    public Long getSourceId() {
        return (Long) get(2);
    }

    /**
     * Setter for <code>ep.ep_message_captcha.captcha_scene</code>. 验证场景：登录
     */
    public EpMessageCaptchaRecord setCaptchaScene(EpMessageCaptchaCaptchaScene value) {
        set(5, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_message_captcha.captcha_code</code>. 业务编码
     */
    public String getCaptchaCode() {
        return (String) get(3);
    }

    /**
     * Setter for <code>ep.ep_message_captcha.expire_time</code>. 过期时间
     */
    public EpMessageCaptchaRecord setExpireTime(Timestamp value) {
        set(6, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_message_captcha.captcha_content</code>. 验证码内容
     */
    public String getCaptchaContent() {
        return (String) get(4);
    }

    /**
     * Setter for <code>ep.ep_message_captcha.ip</code>. 访问ip
     */
    public EpMessageCaptchaRecord setIp(String value) {
        set(7, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_message_captcha.captcha_scene</code>. 验证场景：登录
     */
    public EpMessageCaptchaCaptchaScene getCaptchaScene() {
        return (EpMessageCaptchaCaptchaScene) get(5);
    }

    /**
     * Setter for <code>ep.ep_message_captcha.create_at</code>. 创建时间
     */
    public EpMessageCaptchaRecord setCreateAt(Timestamp value) {
        set(8, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_message_captcha.expire_time</code>. 过期时间
     */
    public Timestamp getExpireTime() {
        return (Timestamp) get(6);
    }

    /**
     * Setter for <code>ep.ep_message_captcha.update_at</code>. 更新时间
     */
    public EpMessageCaptchaRecord setUpdateAt(Timestamp value) {
        set(9, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_message_captcha.ip</code>. 访问ip
     */
    public String getIp() {
        return (String) get(7);
    }

    /**
     * Setter for <code>ep.ep_message_captcha.remark</code>. 备注
     */
    public EpMessageCaptchaRecord setRemark(String value) {
        set(10, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_message_captcha.remark</code>. 备注
     */
    public String getRemark() {
        return (String) get(10);
    }

    /**
     * Getter for <code>ep.ep_message_captcha.create_at</code>. 创建时间
     */
    public Timestamp getCreateAt() {
        return (Timestamp) get(8);
    }

    /**
     * Getter for <code>ep.ep_message_captcha.update_at</code>. 更新时间
     */
    public Timestamp getUpdateAt() {
        return (Timestamp) get(9);
    }

    /**
     * Getter for <code>ep.ep_message_captcha.del_flag</code>. 删除标记
     */
    public Boolean getDelFlag() {
        return (Boolean) get(11);
    }

    /**
     * Getter for <code>ep.ep_message_captcha.version</code>.
     */
    public Long getVersion() {
        return (Long) get(12);
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
    // Record13 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row13<Long, EpMessageCaptchaCaptchaType, Long, String, String, EpMessageCaptchaCaptchaScene, Timestamp, String, Timestamp, Timestamp, String, Boolean, Long> fieldsRow() {
        return (Row13) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row13<Long, EpMessageCaptchaCaptchaType, Long, String, String, EpMessageCaptchaCaptchaScene, Timestamp, String, Timestamp, Timestamp, String, Boolean, Long> valuesRow() {
        return (Row13) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field1() {
        return EpMessageCaptcha.EP_MESSAGE_CAPTCHA.ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<EpMessageCaptchaCaptchaType> field2() {
        return EpMessageCaptcha.EP_MESSAGE_CAPTCHA.CAPTCHA_TYPE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field3() {
        return EpMessageCaptcha.EP_MESSAGE_CAPTCHA.SOURCE_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field4() {
        return EpMessageCaptcha.EP_MESSAGE_CAPTCHA.CAPTCHA_CODE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field5() {
        return EpMessageCaptcha.EP_MESSAGE_CAPTCHA.CAPTCHA_CONTENT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<EpMessageCaptchaCaptchaScene> field6() {
        return EpMessageCaptcha.EP_MESSAGE_CAPTCHA.CAPTCHA_SCENE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field7() {
        return EpMessageCaptcha.EP_MESSAGE_CAPTCHA.EXPIRE_TIME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field8() {
        return EpMessageCaptcha.EP_MESSAGE_CAPTCHA.IP;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field9() {
        return EpMessageCaptcha.EP_MESSAGE_CAPTCHA.CREATE_AT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field10() {
        return EpMessageCaptcha.EP_MESSAGE_CAPTCHA.UPDATE_AT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field11() {
        return EpMessageCaptcha.EP_MESSAGE_CAPTCHA.REMARK;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Boolean> field12() {
        return EpMessageCaptcha.EP_MESSAGE_CAPTCHA.DEL_FLAG;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field13() {
        return EpMessageCaptcha.EP_MESSAGE_CAPTCHA.VERSION;
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
    public EpMessageCaptchaCaptchaType value2() {
        return getCaptchaType();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value3() {
        return getSourceId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value4() {
        return getCaptchaCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value5() {
        return getCaptchaContent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpMessageCaptchaCaptchaScene value6() {
        return getCaptchaScene();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp value7() {
        return getExpireTime();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value8() {
        return getIp();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp value9() {
        return getCreateAt();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp value10() {
        return getUpdateAt();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value11() {
        return getRemark();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean value12() {
        return getDelFlag();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value13() {
        return getVersion();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpMessageCaptchaRecord value1(Long value) {
        setId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpMessageCaptchaRecord value2(EpMessageCaptchaCaptchaType value) {
        setCaptchaType(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpMessageCaptchaRecord value3(Long value) {
        setSourceId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpMessageCaptchaRecord value4(String value) {
        setCaptchaCode(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpMessageCaptchaRecord value5(String value) {
        setCaptchaContent(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpMessageCaptchaRecord value6(EpMessageCaptchaCaptchaScene value) {
        setCaptchaScene(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpMessageCaptchaRecord value7(Timestamp value) {
        setExpireTime(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpMessageCaptchaRecord value8(String value) {
        setIp(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpMessageCaptchaRecord value9(Timestamp value) {
        setCreateAt(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpMessageCaptchaRecord value10(Timestamp value) {
        setUpdateAt(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpMessageCaptchaRecord value11(String value) {
        setRemark(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpMessageCaptchaRecord value12(Boolean value) {
        setDelFlag(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpMessageCaptchaRecord value13(Long value) {
        setVersion(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpMessageCaptchaRecord values(Long value1, EpMessageCaptchaCaptchaType value2, Long value3, String value4, String value5, EpMessageCaptchaCaptchaScene value6, Timestamp value7, String value8, Timestamp value9, Timestamp value10, String value11, Boolean value12, Long value13) {
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
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Setter for <code>ep.ep_message_captcha.del_flag</code>. 删除标记
     */
    public EpMessageCaptchaRecord setDelFlag(Boolean value) {
        set(11, value);
        return this;
    }

    /**
     * Setter for <code>ep.ep_message_captcha.version</code>.
     */
    public EpMessageCaptchaRecord setVersion(Long value) {
        set(12, value);
        return this;
    }
}
