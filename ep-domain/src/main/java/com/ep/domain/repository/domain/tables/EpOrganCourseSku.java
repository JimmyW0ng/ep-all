/*
 * This file is generated by jOOQ.
*/
package com.ep.domain.repository.domain.tables;


import com.ep.domain.repository.domain.Ep;
import com.ep.domain.repository.domain.Keys;
import com.ep.domain.repository.domain.tables.records.EpOrganCourseSkuRecord;
import org.jooq.*;
import org.jooq.impl.TableImpl;

import javax.annotation.Generated;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;


/**
 * 机构课程sku表
 */
@Generated(
        value = {
                "http://www.jooq.org",
                "jOOQ version:3.9.6"
        },
        comments = "This class is generated by jOOQ"
)
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class EpOrganCourseSku extends TableImpl<EpOrganCourseSkuRecord> {

    private static final long serialVersionUID = -1232445985;

    /**
     * The reference instance of <code>ep.ep_organ_course_sku</code>
     */
    public static final EpOrganCourseSku EP_ORGAN_COURSE_SKU = new EpOrganCourseSku();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<EpOrganCourseSkuRecord> getRecordType() {
        return EpOrganCourseSkuRecord.class;
    }

    /**
     * The column <code>ep.ep_organ_course_sku.id</code>. 主键
     */
    public final TableField<EpOrganCourseSkuRecord, Long> ID = createField("id", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "主键");

    /**
     * The column <code>ep.ep_organ_course_sku.ogn_id</code>. 机构ID
     */
    public final TableField<EpOrganCourseSkuRecord, Long> OGN_ID = createField("ogn_id", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "机构ID");

    /**
     * The column <code>ep.ep_organ_course_sku.course_id</code>. 课程ID
     */
    public final TableField<EpOrganCourseSkuRecord, Long> COURSE_ID = createField("course_id", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "课程ID");

    /**
     * The column <code>ep.ep_organ_course_sku.sku_name</code>. sku名称
     */
    public final TableField<EpOrganCourseSkuRecord, String> SKU_NAME = createField("sku_name", org.jooq.impl.SQLDataType.VARCHAR.length(255).nullable(false), this, "sku名称");

    /**
     * The column <code>ep.ep_organ_course_sku.sku_prize</code>. 价格
     */
    public final TableField<EpOrganCourseSkuRecord, BigDecimal> SKU_PRIZE = createField("sku_prize", org.jooq.impl.SQLDataType.DECIMAL.precision(12, 2).nullable(false), this, "价格");

    /**
     * The column <code>ep.ep_organ_course_sku.discount_amount</code>. 折扣优惠
     */
    public final TableField<EpOrganCourseSkuRecord, BigDecimal> DISCOUNT_AMOUNT = createField("discount_amount", org.jooq.impl.SQLDataType.DECIMAL.precision(12, 2), this, "折扣优惠");

    /**
     * The column <code>ep.ep_organ_course_sku.enter_limit_flag</code>. 是否限制报名人数
     */
    public final TableField<EpOrganCourseSkuRecord, Boolean> ENTER_LIMIT_FLAG = createField("enter_limit_flag", org.jooq.impl.SQLDataType.BIT.nullable(false).defaultValue(org.jooq.impl.DSL.inline("b'0'", org.jooq.impl.SQLDataType.BIT)), this, "是否限制报名人数");

    /**
     * The column <code>ep.ep_organ_course_sku.enter_require_num</code>. 要求报名人数
     */
    public final TableField<EpOrganCourseSkuRecord, Integer> ENTER_REQUIRE_NUM = createField("enter_require_num", org.jooq.impl.SQLDataType.INTEGER, this, "要求报名人数");

    /**
     * The column <code>ep.ep_organ_course_sku.entered_num</code>. 已报名人数
     */
    public final TableField<EpOrganCourseSkuRecord, Integer> ENTERED_NUM = createField("entered_num", org.jooq.impl.SQLDataType.INTEGER, this, "已报名人数");

    /**
     * The column <code>ep.ep_organ_course_sku.course_num</code>. 总计课时
     */
    public final TableField<EpOrganCourseSkuRecord, Integer> COURSE_NUM = createField("course_num", org.jooq.impl.SQLDataType.INTEGER.nullable(false).defaultValue(org.jooq.impl.DSL.inline("1", org.jooq.impl.SQLDataType.INTEGER)), this, "总计课时");

    /**
     * The column <code>ep.ep_organ_course_sku.remark</code>. 备注信息
     */
    public final TableField<EpOrganCourseSkuRecord, String> REMARK = createField("remark", org.jooq.impl.SQLDataType.VARCHAR.length(255), this, "备注信息");

    /**
     * The column <code>ep.ep_organ_course_sku.create_at</code>. 创建时间
     */
    public final TableField<EpOrganCourseSkuRecord, Timestamp> CREATE_AT = createField("create_at", org.jooq.impl.SQLDataType.TIMESTAMP.nullable(false).defaultValue(org.jooq.impl.DSL.inline("CURRENT_TIMESTAMP", org.jooq.impl.SQLDataType.TIMESTAMP)), this, "创建时间");

    /**
     * The column <code>ep.ep_organ_course_sku.update_at</code>. 更新时间
     */
    public final TableField<EpOrganCourseSkuRecord, Timestamp> UPDATE_AT = createField("update_at", org.jooq.impl.SQLDataType.TIMESTAMP, this, "更新时间");

    /**
     * The column <code>ep.ep_organ_course_sku.del_flag</code>. 删除标志
     */
    public final TableField<EpOrganCourseSkuRecord, Boolean> DEL_FLAG = createField("del_flag", org.jooq.impl.SQLDataType.BIT.nullable(false).defaultValue(org.jooq.impl.DSL.inline("b'0'", org.jooq.impl.SQLDataType.BIT)), this, "删除标志");

    /**
     * The column <code>ep.ep_organ_course_sku.version</code>.
     */
    public final TableField<EpOrganCourseSkuRecord, Long> VERSION = createField("version", org.jooq.impl.SQLDataType.BIGINT.nullable(false).defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.BIGINT)), this, "");

    /**
     * Create a <code>ep.ep_organ_course_sku</code> table reference
     */
    public EpOrganCourseSku() {
        this("ep_organ_course_sku", null);
    }

    /**
     * Create an aliased <code>ep.ep_organ_course_sku</code> table reference
     */
    public EpOrganCourseSku(String alias) {
        this(alias, EP_ORGAN_COURSE_SKU);
    }

    private EpOrganCourseSku(String alias, Table<EpOrganCourseSkuRecord> aliased) {
        this(alias, aliased, null);
    }

    private EpOrganCourseSku(String alias, Table<EpOrganCourseSkuRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, "机构课程sku表");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Schema getSchema() {
        return Ep.EP;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Identity<EpOrganCourseSkuRecord, Long> getIdentity() {
        return Keys.IDENTITY_EP_ORGAN_COURSE_SKU;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<EpOrganCourseSkuRecord> getPrimaryKey() {
        return Keys.KEY_EP_ORGAN_COURSE_SKU_PRIMARY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<EpOrganCourseSkuRecord>> getKeys() {
        return Arrays.<UniqueKey<EpOrganCourseSkuRecord>>asList(Keys.KEY_EP_ORGAN_COURSE_SKU_PRIMARY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TableField<EpOrganCourseSkuRecord, Long> getRecordVersion() {
        return VERSION;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganCourseSku as(String alias) {
        return new EpOrganCourseSku(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public EpOrganCourseSku rename(String name) {
        return new EpOrganCourseSku(name, null);
    }
}
