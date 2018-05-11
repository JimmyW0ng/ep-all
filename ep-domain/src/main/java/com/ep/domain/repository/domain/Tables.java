/*
 * This file is generated by jOOQ.
*/
package com.ep.domain.repository.domain;


import com.ep.domain.repository.domain.tables.*;

import javax.annotation.Generated;


/**
 * Convenience access to all tables in ep
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.9.6"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Tables {

    /**
     * 课程类目表
     */
    public static final EpConstantCatalog EP_CONSTANT_CATALOG = com.ep.domain.repository.domain.tables.EpConstantCatalog.EP_CONSTANT_CATALOG;

    /**
     * 地区表
     */
    public static final EpConstantRegion EP_CONSTANT_REGION = com.ep.domain.repository.domain.tables.EpConstantRegion.EP_CONSTANT_REGION;

    /**
     * 标签表
     */
    public static final EpConstantTag EP_CONSTANT_TAG = com.ep.domain.repository.domain.tables.EpConstantTag.EP_CONSTANT_TAG;

    /**
     * 文件表
     */
    public static final EpFile EP_FILE = com.ep.domain.repository.domain.tables.EpFile.EP_FILE;

    /**
     * 会员信息表
     */
    public static final EpMember EP_MEMBER = com.ep.domain.repository.domain.tables.EpMember.EP_MEMBER;

    /**
     * 孩子信息表
     */
    public static final EpMemberChild EP_MEMBER_CHILD = com.ep.domain.repository.domain.tables.EpMemberChild.EP_MEMBER_CHILD;

    /**
     * 孩子上课评论表
     */
    public static final EpMemberChildComment EP_MEMBER_CHILD_COMMENT = com.ep.domain.repository.domain.tables.EpMemberChildComment.EP_MEMBER_CHILD_COMMENT;

    /**
     * 孩子荣誉表
     */
    public static final EpMemberChildHonor EP_MEMBER_CHILD_HONOR = com.ep.domain.repository.domain.tables.EpMemberChildHonor.EP_MEMBER_CHILD_HONOR;

    /**
     * 孩子签名表
     */
    public static final EpMemberChildSign EP_MEMBER_CHILD_SIGN = com.ep.domain.repository.domain.tables.EpMemberChildSign.EP_MEMBER_CHILD_SIGN;

    /**
     * 孩子标签记录表
     */
    public static final EpMemberChildTag EP_MEMBER_CHILD_TAG = com.ep.domain.repository.domain.tables.EpMemberChildTag.EP_MEMBER_CHILD_TAG;

    /**
     * 会员消息表
     */
    public static final EpMemberMessage EP_MEMBER_MESSAGE = com.ep.domain.repository.domain.tables.EpMemberMessage.EP_MEMBER_MESSAGE;

    /**
     * 验证码表
     */
    public static final EpMessageCaptcha EP_MESSAGE_CAPTCHA = com.ep.domain.repository.domain.tables.EpMessageCaptcha.EP_MESSAGE_CAPTCHA;

    /**
     * 订单表
     */
    public static final EpOrder EP_ORDER = com.ep.domain.repository.domain.tables.EpOrder.EP_ORDER;

    /**
     * 订单退款申请表
     */
    public static final EpOrderRefund EP_ORDER_REFUND = com.ep.domain.repository.domain.tables.EpOrderRefund.EP_ORDER_REFUND;

    /**
     * 机构信息表
     */
    public static final EpOrgan EP_ORGAN = com.ep.domain.repository.domain.tables.EpOrgan.EP_ORGAN;

    /**
     * 机构账户关联信息表
     */
    public static final EpOrganAccount EP_ORGAN_ACCOUNT = com.ep.domain.repository.domain.tables.EpOrganAccount.EP_ORGAN_ACCOUNT;

    /**
     * 机构类目表
     */
    public static final EpOrganCatalog EP_ORGAN_CATALOG = com.ep.domain.repository.domain.tables.EpOrganCatalog.EP_ORGAN_CATALOG;

    /**
     * 机构课程班次表
     */
    public static final EpOrganClass EP_ORGAN_CLASS = com.ep.domain.repository.domain.tables.EpOrganClass.EP_ORGAN_CLASS;

    /**
     * 班次课程内容目录表
     */
    public static final EpOrganClassCatalog EP_ORGAN_CLASS_CATALOG = com.ep.domain.repository.domain.tables.EpOrganClassCatalog.EP_ORGAN_CLASS_CATALOG;

    /**
     * 机构班级孩子表
     */
    public static final EpOrganClassChild EP_ORGAN_CLASS_CHILD = com.ep.domain.repository.domain.tables.EpOrganClassChild.EP_ORGAN_CLASS_CHILD;

    /**
     * 机构课程班次评分表
     */
    public static final EpOrganClassComment EP_ORGAN_CLASS_COMMENT = com.ep.domain.repository.domain.tables.EpOrganClassComment.EP_ORGAN_CLASS_COMMENT;

    /**
     * 班次行程表
     */
    public static final EpOrganClassSchedule EP_ORGAN_CLASS_SCHEDULE = com.ep.domain.repository.domain.tables.EpOrganClassSchedule.EP_ORGAN_CLASS_SCHEDULE;

    /**
     * 机构配置表
     */
    public static final EpOrganConfig EP_ORGAN_CONFIG = com.ep.domain.repository.domain.tables.EpOrganConfig.EP_ORGAN_CONFIG;

    /**
     * 机构课程表
     */
    public static final EpOrganCourse EP_ORGAN_COURSE = com.ep.domain.repository.domain.tables.EpOrganCourse.EP_ORGAN_COURSE;

    /**
     * 课程标签表
     */
    public static final EpOrganCourseTag EP_ORGAN_COURSE_TAG = com.ep.domain.repository.domain.tables.EpOrganCourseTag.EP_ORGAN_COURSE_TAG;

    /**
     * 机构课程团队信息表
     */
    public static final EpOrganCourseTeam EP_ORGAN_COURSE_TEAM = com.ep.domain.repository.domain.tables.EpOrganCourseTeam.EP_ORGAN_COURSE_TEAM;

    /**
     * 机构会员信息表
     */
    public static final EpOrganVip EP_ORGAN_VIP = com.ep.domain.repository.domain.tables.EpOrganVip.EP_ORGAN_VIP;

    /**
     * 鉴权表
     */
    public static final EpSystemClient EP_SYSTEM_CLIENT = com.ep.domain.repository.domain.tables.EpSystemClient.EP_SYSTEM_CLIENT;

    /**
     * 字典表
     */
    public static final EpSystemDict EP_SYSTEM_DICT = com.ep.domain.repository.domain.tables.EpSystemDict.EP_SYSTEM_DICT;

    /**
     * 日志表
     */
    public static final EpSystemLog EP_SYSTEM_LOG = com.ep.domain.repository.domain.tables.EpSystemLog.EP_SYSTEM_LOG;

    /**
     * 菜单表
     */
    public static final EpSystemMenu EP_SYSTEM_MENU = com.ep.domain.repository.domain.tables.EpSystemMenu.EP_SYSTEM_MENU;

    /**
     * 角色表
     */
    public static final EpSystemRole EP_SYSTEM_ROLE = com.ep.domain.repository.domain.tables.EpSystemRole.EP_SYSTEM_ROLE;

    /**
     * 角色权限表
     */
    public static final EpSystemRoleAuthority EP_SYSTEM_ROLE_AUTHORITY = com.ep.domain.repository.domain.tables.EpSystemRoleAuthority.EP_SYSTEM_ROLE_AUTHORITY;

    /**
     * 平台用户表
     */
    public static final EpSystemUser EP_SYSTEM_USER = com.ep.domain.repository.domain.tables.EpSystemUser.EP_SYSTEM_USER;

    /**
     * 用户-角色
     */
    public static final EpSystemUserRole EP_SYSTEM_USER_ROLE = com.ep.domain.repository.domain.tables.EpSystemUserRole.EP_SYSTEM_USER_ROLE;

    /**
     * token表
     */
    public static final EpToken EP_TOKEN = com.ep.domain.repository.domain.tables.EpToken.EP_TOKEN;

    /**
     * 公众号关联表
     */
    public static final EpWechatOpenid EP_WECHAT_OPENID = com.ep.domain.repository.domain.tables.EpWechatOpenid.EP_WECHAT_OPENID;

    /**
     * 微信支付退单表
     */
    public static final EpWechatPayRefund EP_WECHAT_PAY_REFUND = com.ep.domain.repository.domain.tables.EpWechatPayRefund.EP_WECHAT_PAY_REFUND;

    /**
     * 微信支付统一订单
     */
    public static final EpWechatUnifiedOrder EP_WECHAT_UNIFIED_ORDER = com.ep.domain.repository.domain.tables.EpWechatUnifiedOrder.EP_WECHAT_UNIFIED_ORDER;
}
