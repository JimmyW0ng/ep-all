/*
 * This file is generated by jOOQ.
*/
package com.ep.domain.repository.domain;


import com.ep.domain.repository.domain.tables.EpConstantCatalog;
import com.ep.domain.repository.domain.tables.EpConstantRegion;
import com.ep.domain.repository.domain.tables.EpFile;
import com.ep.domain.repository.domain.tables.EpMember;
import com.ep.domain.repository.domain.tables.EpMemberChild;
import com.ep.domain.repository.domain.tables.EpMemberChildHonor;
import com.ep.domain.repository.domain.tables.EpMemberChildSign;
import com.ep.domain.repository.domain.tables.EpMemberChildTag;
import com.ep.domain.repository.domain.tables.EpMessageCaptcha;
import com.ep.domain.repository.domain.tables.EpOrder;
import com.ep.domain.repository.domain.tables.EpOrgan;
import com.ep.domain.repository.domain.tables.EpOrganAccount;
import com.ep.domain.repository.domain.tables.EpOrganCatalog;
import com.ep.domain.repository.domain.tables.EpOrganClass;
import com.ep.domain.repository.domain.tables.EpOrganClassCatelog;
import com.ep.domain.repository.domain.tables.EpOrganClassChild;
import com.ep.domain.repository.domain.tables.EpOrganClassComment;
import com.ep.domain.repository.domain.tables.EpOrganClassSchedule;
import com.ep.domain.repository.domain.tables.EpOrganClassScheduleComment;
import com.ep.domain.repository.domain.tables.EpOrganCourse;
import com.ep.domain.repository.domain.tables.EpOrganCourseTag;
import com.ep.domain.repository.domain.tables.EpOrganCourseTeam;
import com.ep.domain.repository.domain.tables.EpSystemClient;
import com.ep.domain.repository.domain.tables.EpSystemDict;
import com.ep.domain.repository.domain.tables.EpSystemMenu;
import com.ep.domain.repository.domain.tables.EpSystemRole;
import com.ep.domain.repository.domain.tables.EpSystemRoleAuthority;
import com.ep.domain.repository.domain.tables.EpSystemUser;
import com.ep.domain.repository.domain.tables.EpSystemUserRole;
import com.ep.domain.repository.domain.tables.EpWechatSessionToken;

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
     * 验证码表
     */
    public static final EpMessageCaptcha EP_MESSAGE_CAPTCHA = com.ep.domain.repository.domain.tables.EpMessageCaptcha.EP_MESSAGE_CAPTCHA;

    /**
     * 订单表
     */
    public static final EpOrder EP_ORDER = com.ep.domain.repository.domain.tables.EpOrder.EP_ORDER;

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
    public static final EpOrganClassCatelog EP_ORGAN_CLASS_CATELOG = com.ep.domain.repository.domain.tables.EpOrganClassCatelog.EP_ORGAN_CLASS_CATELOG;

    /**
     * 机构班级孩子表
     */
    public static final EpOrganClassChild EP_ORGAN_CLASS_CHILD = com.ep.domain.repository.domain.tables.EpOrganClassChild.EP_ORGAN_CLASS_CHILD;

    /**
     * 机构课程班次评分表
     */
    public static final EpOrganClassComment EP_ORGAN_CLASS_COMMENT = com.ep.domain.repository.domain.tables.EpOrganClassComment.EP_ORGAN_CLASS_COMMENT;

    /**
     * 机构行程信息表
     */
    public static final EpOrganClassSchedule EP_ORGAN_CLASS_SCHEDULE = com.ep.domain.repository.domain.tables.EpOrganClassSchedule.EP_ORGAN_CLASS_SCHEDULE;

    /**
     * 机构行程评论表
     */
    public static final EpOrganClassScheduleComment EP_ORGAN_CLASS_SCHEDULE_COMMENT = com.ep.domain.repository.domain.tables.EpOrganClassScheduleComment.EP_ORGAN_CLASS_SCHEDULE_COMMENT;

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
     * 鉴权表
     */
    public static final EpSystemClient EP_SYSTEM_CLIENT = com.ep.domain.repository.domain.tables.EpSystemClient.EP_SYSTEM_CLIENT;

    /**
     * 字典表
     */
    public static final EpSystemDict EP_SYSTEM_DICT = com.ep.domain.repository.domain.tables.EpSystemDict.EP_SYSTEM_DICT;

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
     * 微信会话token表
     */
    public static final EpWechatSessionToken EP_WECHAT_SESSION_TOKEN = com.ep.domain.repository.domain.tables.EpWechatSessionToken.EP_WECHAT_SESSION_TOKEN;
}
