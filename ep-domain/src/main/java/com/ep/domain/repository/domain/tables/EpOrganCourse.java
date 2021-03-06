/*
 * This file is generated by jOOQ.
*/
package com.ep.domain.repository.domain.tables;


import com.ep.domain.repository.domain.Ep;
import com.ep.domain.repository.domain.Keys;
import com.ep.domain.repository.domain.enums.EpOrganCourseCourseStatus;
import com.ep.domain.repository.domain.enums.EpOrganCourseCourseType;
import com.ep.domain.repository.domain.tables.records.EpOrganCourseRecord;
import org.jooq.*;
import org.jooq.impl.TableImpl;

import javax.annotation.Generated;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;


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
public class EpOrganCourse extends TableImpl<EpOrganCourseRecord> {

    private static final long serialVersionUID = 1206997043;

    /**
     * The reference instance of <code>ep.ep_organ_course</code>
     */
    public static final EpOrganCourse EP_ORGAN_COURSE = new EpOrganCourse();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<EpOrganCourseRecord> getRecordType() {
        return EpOrganCourseRecord.class;
    }

    /**
     * The column <code>ep.ep_organ_course.id</code>. 主键
     */
    public final TableField<EpOrganCourseRecord, Long> ID = createField("id", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "主键");

    /**
     * The column <code>ep.ep_organ_course.ogn_id</code>. 机构ID
     */
    public final TableField<EpOrganCourseRecord, Long> OGN_ID = createField("ogn_id", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "机构ID");

    /**
     * The column <code>ep.ep_organ_course.course_type</code>. 课程类型：课程；活动；
     */
    public final TableField<EpOrganCourseRecord, EpOrganCourseCourseType> COURSE_TYPE = createField("course_type", org.jooq.util.mysql.MySQLDataType.VARCHAR.asEnumDataType(com.ep.domain.repository.domain.enums.EpOrganCourseCourseType.class), this, "课程类型：课程；活动；");

    /**
     * The column <code>ep.ep_organ_course.course_catalog_id</code>. 课程目录ID
     */
    public final TableField<EpOrganCourseRecord, Long> COURSE_CATALOG_ID = createField("course_catalog_id", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "课程目录ID");

    /**
     * The column <code>ep.ep_organ_course.course_name</code>. 课名
     */
    public final TableField<EpOrganCourseRecord, String> COURSE_NAME = createField("course_name", org.jooq.impl.SQLDataType.VARCHAR.length(255).nullable(false), this, "课名");

    /**
     * The column <code>ep.ep_organ_course.course_introduce</code>. 课程简介
     */
    public final TableField<EpOrganCourseRecord, String> COURSE_INTRODUCE = createField("course_introduce", org.jooq.impl.SQLDataType.VARCHAR.length(255).nullable(false).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "课程简介");

    /**
     * The column <code>ep.ep_organ_course.course_content</code>. 课程内容
     */
    public final TableField<EpOrganCourseRecord, String> COURSE_CONTENT = createField("course_content", org.jooq.impl.SQLDataType.CLOB.nullable(false), this, "课程内容");

    /**
     * The column <code>ep.ep_organ_course.prize_min</code>. 最低价格
     */
    public final TableField<EpOrganCourseRecord, BigDecimal> PRIZE_MIN = createField("prize_min", org.jooq.impl.SQLDataType.DECIMAL.precision(12, 2).nullable(false).defaultValue(org.jooq.impl.DSL.inline("0.00", org.jooq.impl.SQLDataType.DECIMAL)), this, "最低价格");

    /**
     * The column <code>ep.ep_organ_course.vip_flag</code>. 是否会员才能报名
     */
    public final TableField<EpOrganCourseRecord, Boolean> VIP_FLAG = createField("vip_flag", org.jooq.impl.SQLDataType.BIT.nullable(false).defaultValue(org.jooq.impl.DSL.inline("b'0'", org.jooq.impl.SQLDataType.BIT)), this, "是否会员才能报名");

    /**
     * The column <code>ep.ep_organ_course.wechat_pay_flag</code>. 是否通过微信支付
     */
    public final TableField<EpOrganCourseRecord, Boolean> WECHAT_PAY_FLAG = createField("wechat_pay_flag", org.jooq.impl.SQLDataType.BIT.nullable(false).defaultValue(org.jooq.impl.DSL.inline("b'0'", org.jooq.impl.SQLDataType.BIT)), this, "是否通过微信支付");

    /**
     * The column <code>ep.ep_organ_course.course_status</code>. 课状态：已保存；已上线；已下线；
     */
    public final TableField<EpOrganCourseRecord, EpOrganCourseCourseStatus> COURSE_STATUS = createField("course_status", org.jooq.util.mysql.MySQLDataType.VARCHAR.asEnumDataType(com.ep.domain.repository.domain.enums.EpOrganCourseCourseStatus.class), this, "课状态：已保存；已上线；已下线；");

    /**
     * The column <code>ep.ep_organ_course.online_time</code>. 上线时间
     */
    public final TableField<EpOrganCourseRecord, Timestamp> ONLINE_TIME = createField("online_time", org.jooq.impl.SQLDataType.TIMESTAMP.nullable(false), this, "上线时间");

    /**
     * The column <code>ep.ep_organ_course.enter_time_start</code>. 报名开始时间
     */
    public final TableField<EpOrganCourseRecord, Timestamp> ENTER_TIME_START = createField("enter_time_start", org.jooq.impl.SQLDataType.TIMESTAMP.nullable(false), this, "报名开始时间");

    /**
     * The column <code>ep.ep_organ_course.enter_time_end</code>. 报名结束时间
     */
    public final TableField<EpOrganCourseRecord, Timestamp> ENTER_TIME_END = createField("enter_time_end", org.jooq.impl.SQLDataType.TIMESTAMP, this, "报名结束时间");

    /**
     * The column <code>ep.ep_organ_course.total_participate</code>. 总参与人数
     */
    public final TableField<EpOrganCourseRecord, Integer> TOTAL_PARTICIPATE = createField("total_participate", org.jooq.impl.SQLDataType.INTEGER.nullable(false).defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "总参与人数");

    /**
     * The column <code>ep.ep_organ_course.remark</code>. 备注信息
     */
    public final TableField<EpOrganCourseRecord, String> REMARK = createField("remark", org.jooq.impl.SQLDataType.VARCHAR.length(255), this, "备注信息");

    /**
     * The column <code>ep.ep_organ_course.create_at</code>. 创建时间
     */
    public final TableField<EpOrganCourseRecord, Timestamp> CREATE_AT = createField("create_at", org.jooq.impl.SQLDataType.TIMESTAMP.nullable(false).defaultValue(org.jooq.impl.DSL.inline("CURRENT_TIMESTAMP", org.jooq.impl.SQLDataType.TIMESTAMP)), this, "创建时间");

    /**
     * The column <code>ep.ep_organ_course.update_at</code>. 更新时间
     */
    public final TableField<EpOrganCourseRecord, Timestamp> UPDATE_AT = createField("update_at", org.jooq.impl.SQLDataType.TIMESTAMP, this, "更新时间");

    /**
     * The column <code>ep.ep_organ_course.del_flag</code>. 删除标志
     */
    public final TableField<EpOrganCourseRecord, Boolean> DEL_FLAG = createField("del_flag", org.jooq.impl.SQLDataType.BIT.nullable(false).defaultValue(org.jooq.impl.DSL.inline("b'0'", org.jooq.impl.SQLDataType.BIT)), this, "删除标志");

    /**
     * The column <code>ep.ep_organ_course.version</code>.
     */
    public final TableField<EpOrganCourseRecord, Long> VERSION = createField("version", org.jooq.impl.SQLDataType.BIGINT.nullable(false).defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.BIGINT)), this, "");

    /**
     * Create a <code>ep.ep_organ_course</code> table reference
     */
    public EpOrganCourse() {
        this("ep_organ_course", null);
    }

    /**
     * Create an aliased <code>ep.ep_organ_course</code> table reference
     */
    public EpOrganCourse(String alias) {
        this(alias, EP_ORGAN_COURSE);
    }

    private EpOrganCourse(String alias, Table<EpOrganCourseRecord> aliased) {
        this(alias, aliased, null);
    }

    private EpOrganCourse(String alias, Table<EpOrganCourseRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, "机构课程表");
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
    public Identity<EpOrganCourseRecord, Long> getIdentity() {
        return Keys.IDENTITY_EP_ORGAN_COURSE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<EpOrganCourseRecord> getPrimaryKey() {
        return Keys.KEY_EP_ORGAN_COURSE_PRIMARY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<EpOrganCourseRecord>> getKeys() {
        return Arrays.<UniqueKey<EpOrganCourseRecord>>asList(Keys.KEY_EP_ORGAN_COURSE_PRIMARY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TableField<EpOrganCourseRecord, Long> getRecordVersion() {
        return VERSION;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganCourse as(String alias) {
        return new EpOrganCourse(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public EpOrganCourse rename(String name) {
        return new EpOrganCourse(name, null);
    }
}
