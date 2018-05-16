/*
 * This file is generated by jOOQ.
*/
package com.ep.domain.repository.domain.tables.records;


import com.ep.domain.repository.domain.enums.EpOrganStatus;
import com.ep.domain.repository.domain.tables.EpOrgan;
import org.jooq.Record1;
import org.jooq.impl.UpdatableRecordImpl;

import javax.annotation.Generated;
import java.sql.Timestamp;


/**
 * 机构信息表
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.9.6"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class EpOrganRecord extends UpdatableRecordImpl<EpOrganRecord> {

    private static final long serialVersionUID = 1409392105;

    /**
     * Setter for <code>ep.ep_organ.id</code>. 主键
     */
    public EpOrganRecord setId(Long value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ.id</code>. 主键
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>ep.ep_organ.ogn_name</code>. 机构名称
     */
    public EpOrganRecord setOgnName(String value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ.ogn_name</code>. 机构名称
     */
    public String getOgnName() {
        return (String) get(1);
    }

    /**
     * Setter for <code>ep.ep_organ.ogn_address</code>. 机构地址
     */
    public EpOrganRecord setOgnAddress(String value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ.ogn_address</code>. 机构地址
     */
    public String getOgnAddress() {
        return (String) get(2);
    }

    /**
     * Setter for <code>ep.ep_organ.ogn_region</code>. 机构地区
     */
    public EpOrganRecord setOgnRegion(Long value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ.ogn_region</code>. 机构地区
     */
    public Long getOgnRegion() {
        return (Long) get(3);
    }

    /**
     * Setter for <code>ep.ep_organ.ogn_lng</code>. 地区经度
     */
    public EpOrganRecord setOgnLng(String value) {
        set(4, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ.ogn_lng</code>. 地区经度
     */
    public String getOgnLng() {
        return (String) get(4);
    }

    /**
     * Setter for <code>ep.ep_organ.ogn_lat</code>. 地区纬度
     */
    public EpOrganRecord setOgnLat(String value) {
        set(5, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ.ogn_lat</code>. 地区纬度
     */
    public String getOgnLat() {
        return (String) get(5);
    }

    /**
     * Setter for <code>ep.ep_organ.ogn_short_introduce</code>. 机构简介
     */
    public EpOrganRecord setOgnShortIntroduce(String value) {
        set(6, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ.ogn_short_introduce</code>. 机构简介
     */
    public String getOgnShortIntroduce() {
        return (String) get(6);
    }

    /**
     * Setter for <code>ep.ep_organ.ogn_create_date</code>. 机构成立日期
     */
    public EpOrganRecord setOgnCreateDate(Timestamp value) {
        set(7, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ.ogn_create_date</code>. 机构成立日期
     */
    public Timestamp getOgnCreateDate() {
        return (Timestamp) get(7);
    }

    /**
     * Setter for <code>ep.ep_organ.ogn_phone</code>. 机构官方电话
     */
    public EpOrganRecord setOgnPhone(String value) {
        set(8, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ.ogn_phone</code>. 机构官方电话
     */
    public String getOgnPhone() {
        return (String) get(8);
    }

    /**
     * Setter for <code>ep.ep_organ.ogn_email</code>. 机构官方邮箱
     */
    public EpOrganRecord setOgnEmail(String value) {
        set(9, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ.ogn_email</code>. 机构官方邮箱
     */
    public String getOgnEmail() {
        return (String) get(9);
    }

    /**
     * Setter for <code>ep.ep_organ.ogn_url</code>. 机构官方网址
     */
    public EpOrganRecord setOgnUrl(String value) {
        set(10, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ.ogn_url</code>. 机构官方网址
     */
    public String getOgnUrl() {
        return (String) get(10);
    }

    /**
     * Setter for <code>ep.ep_organ.ogn_introduce</code>. 机构简介
     */
    public EpOrganRecord setOgnIntroduce(String value) {
        set(11, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ.ogn_introduce</code>. 机构简介
     */
    public String getOgnIntroduce() {
        return (String) get(11);
    }

    /**
     * Setter for <code>ep.ep_organ.vip_flag</code>. 是否开通会员制度
     */
    public EpOrganRecord setVipFlag(Boolean value) {
        set(12, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ.vip_flag</code>. 是否开通会员制度
     */
    public Boolean getVipFlag() {
        return (Boolean) get(12);
    }

    /**
     * Setter for <code>ep.ep_organ.vip_name</code>. 会员称呼
     */
    public EpOrganRecord setVipName(String value) {
        set(13, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ.vip_name</code>. 会员称呼
     */
    public String getVipName() {
        return (String) get(13);
    }

    /**
     * Setter for <code>ep.ep_organ.market_weight</code>. 营销权重
     */
    public EpOrganRecord setMarketWeight(Byte value) {
        set(14, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ.market_weight</code>. 营销权重
     */
    public Byte getMarketWeight() {
        return (Byte) get(14);
    }

    /**
     * Setter for <code>ep.ep_organ.together_score</code>. 综合得分
     */
    public EpOrganRecord setTogetherScore(Byte value) {
        set(15, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ.together_score</code>. 综合得分
     */
    public Byte getTogetherScore() {
        return (Byte) get(15);
    }

    /**
     * Setter for <code>ep.ep_organ.total_participate</code>. 总参与人数
     */
    public EpOrganRecord setTotalParticipate(Integer value) {
        set(16, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ.total_participate</code>. 总参与人数
     */
    public Integer getTotalParticipate() {
        return (Integer) get(16);
    }

    /**
     * Setter for <code>ep.ep_organ.status</code>. 状态：已保存；已上线；已下线；已冻结
     */
    public EpOrganRecord setStatus(EpOrganStatus value) {
        set(17, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ.status</code>. 状态：已保存；已上线；已下线；已冻结
     */
    public EpOrganStatus getStatus() {
        return (EpOrganStatus) get(17);
    }

    /**
     * Setter for <code>ep.ep_organ.remark</code>. 备注信息
     */
    public EpOrganRecord setRemark(String value) {
        set(18, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ.remark</code>. 备注信息
     */
    public String getRemark() {
        return (String) get(18);
    }

    /**
     * Setter for <code>ep.ep_organ.create_at</code>. 创建时间
     */
    public EpOrganRecord setCreateAt(Timestamp value) {
        set(19, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ.create_at</code>. 创建时间
     */
    public Timestamp getCreateAt() {
        return (Timestamp) get(19);
    }

    /**
     * Setter for <code>ep.ep_organ.update_at</code>. 更新时间
     */
    public EpOrganRecord setUpdateAt(Timestamp value) {
        set(20, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ.update_at</code>. 更新时间
     */
    public Timestamp getUpdateAt() {
        return (Timestamp) get(20);
    }

    /**
     * Setter for <code>ep.ep_organ.del_flag</code>. 删除标志
     */
    public EpOrganRecord setDelFlag(Boolean value) {
        set(21, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ.del_flag</code>. 删除标志
     */
    public Boolean getDelFlag() {
        return (Boolean) get(21);
    }

    /**
     * Setter for <code>ep.ep_organ.version</code>.
     */
    public EpOrganRecord setVersion(Long value) {
        set(22, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ.version</code>.
     */
    public Long getVersion() {
        return (Long) get(22);
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
     * Create a detached EpOrganRecord
     */
    public EpOrganRecord() {
        super(EpOrgan.EP_ORGAN);
    }

    /**
     * Create a detached, initialised EpOrganRecord
     */
    public EpOrganRecord(Long id, String ognName, String ognAddress, Long ognRegion, String ognLng, String ognLat, String ognShortIntroduce, Timestamp ognCreateDate, String ognPhone, String ognEmail, String ognUrl, String ognIntroduce, Boolean vipFlag, String vipName, Byte marketWeight, Byte togetherScore, Integer totalParticipate, EpOrganStatus status, String remark, Timestamp createAt, Timestamp updateAt, Boolean delFlag, Long version) {
        super(EpOrgan.EP_ORGAN);

        set(0, id);
        set(1, ognName);
        set(2, ognAddress);
        set(3, ognRegion);
        set(4, ognLng);
        set(5, ognLat);
        set(6, ognShortIntroduce);
        set(7, ognCreateDate);
        set(8, ognPhone);
        set(9, ognEmail);
        set(10, ognUrl);
        set(11, ognIntroduce);
        set(12, vipFlag);
        set(13, vipName);
        set(14, marketWeight);
        set(15, togetherScore);
        set(16, totalParticipate);
        set(17, status);
        set(18, remark);
        set(19, createAt);
        set(20, updateAt);
        set(21, delFlag);
        set(22, version);
    }
}
