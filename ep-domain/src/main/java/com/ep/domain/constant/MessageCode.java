package com.ep.domain.constant;

/**
 * @Description:常量码
 * @Author: J.W
 * @Date: 下午8:29 2018/1/6
 */
public class MessageCode {

    /**
     * 系统异常码
     */
    public static final String ERROR_SYSTEM = "ERROR_SYSTEM";
    public static final String ERROR_SYSTEM_PARAM_FORMAT = "ERROR_SYSTEM_PARAM_FORMAT";
    public static final String ERROR_DATA_MISS = "ERROR_DATA_MISS";
    public static final String ERROR_SAVE_MISS = "ERROR_SAVE_MISS";
    public static final String ERROR_OPERATE_FAIL = "ERROR_OPERATE_FAIL";
    public static final String ERROR_MOBILE_FORMAT = "ERROR_MOBILE_FORMAT";
    public static final String ERROR_ILLEGAL_RESOURCE = "ERROR_ILLEGAL_RESOURCE";

    /**
     * 鉴权
     */
    public static final String ERROR_ENCODE = "ERROR_ENCODE";
    public static final String ERROR_DECODE = "ERROR_DECODE";
    public static final String ERROR_ACCESS_NEED_AUTH = "ERROR_ACCESS_NEED_AUTH";
    public static final String ERROR_ACCESS_DENIED = "ERROR_ACCESS_DENIED";
    public static final String ERROR_PRINCIPAL_CHECK = "ERROR_PRINCIPAL_CHECK";
    public static final String ERROR_SESSION_TOKEN = "ERROR_SESSION_TOKEN";
    public static final String ERROR_WECHAT_HTTP_REQUEST = "ERROR_WECHAT_HTTP_REQUEST";
    /**
     * 验证码
     */
    public static final String ERROR_GET_CAPTCHA_NUM_OUT_LIMIT = "ERROR_GET_CAPTCHA_NUM_OUT_LIMIT";
    /**
     * 会员不存在
     */
    public static final String ERROR_MEMBER_NOT_EXISTS = "ERROR_MEMBER_NOT_EXISTS";
    public static final String ERROR_MEMBER_IS_CANCEL = "ERROR_MEMBER_IS_CANCEL";
    public static final String ERROR_MEMBER_IS_FREEZE = "ERROR_MEMBER_IS_FREEZE";
    /**
     * 机构
     */
    public static final String ERROR_ORGAN_EXISTS = "ERROR_ORGAN_EXISTS";
    public static final String ERROR_ORGAN_NOT_EXISTS = "ERROR_ORGAN_NOT_EXISTS";
    public static final String ERROR_ORGAN_NAME_EXISTS = "ERROR_ORGAN_NAME_EXISTS";
    public static final String ERROR_ORGAN_MAINPIC_NOT_EXISTS = "ERROR_ORGAN_MAINPIC_NOT_EXISTS";
    public static final String ERROR_ORGAN_LOGO_NOT_EXISTS = "ERROR_ORGAN_LOGO_NOT_EXISTS";
    public static final String ERROR_ORGAN_CONFIG_NOT_EXISTS = "ERROR_ORGAN_CONFIG_NOT_EXISTS";
    public static final String ERROR_OFFLINE_EXISTS_ONLINE_COURSE = "ERROR_OFFLINE_EXISTS_ONLINE_COURSE";
    public static final String ERROR_OFFLINE_CURRSTATUS_NOT_ONLINE = "ERROR_OFFLINE_CURRSTATUS_NOT_ONLINE";
    /**
     * 孩子
     */
    public static final String ERROR_CHILD_NICK_NAME_EXISTS = "ERROR_CHILD_NICK_NAME_EXISTS";
    public static final String ERROR_CHILD_TRUE_NAME_EXISTS = "ERROR_CHILD_TRUE_NAME_EXISTS";
    public static final String ERROR_CHILD_NOT_EXISTS = "ERROR_CHILD_NOT_EXISTS";
    public static final String ERROR_CHILD_CAN_NOT_DEL = "ERROR_CHILD_CAN_NOT_DEL";
    public static final String ERROR_CHILD_LIMIT_NUM = "ERROR_CHILD_LIMIT_NUM";
    public static final String ERROR_CHILD_UPLOAD_AVATAR = "ERROR_CHILD_UPLOAD_AVATAR";
    /**
     * 产品
     */
    public static final String ERROR_CONSTANT_CATALOG_NOT_EXIST = "ERROR_CONSTANT_CATALOG_NOT_EXIST";
    public static final String ERROR_COURSE_NOT_EXIST = "ERROR_COURSE_NOT_EXIST";
    public static final String ERROR_COURSE_ENTER_NOT_START = "ERROR_COURSE_ENTER_NOT_START";
    public static final String ERROR_COURSE_ENTER_END = "ERROR_COURSE_ENTER_END";
    public static final String ERROR_COURSE_NOT_SAVE = "ERROR_COURSE_NOT_SAVE";
    public static final String ERROR_COURSE_NOT_ONLINE = "ERROR_COURSE_NOT_ONLINE";
    public static final String ERROR_COURSE_IS_OFF = "ERROR_COURSE_IS_OFF";
    public static final String ERROR_COURSE_OGN_NOT_MATCH = "ERROR_COURSE_OGN_NOT_MATCH";
    public static final String ERROR_COURSE_EXIST_SAVED_ORDER = "ERROR_COURSE_EXIST_SAVED_ORDER";
    public static final String ERROR_CLASS_NOT_EXIST = "ERROR_CLASS_NOT_EXIST";
    public static final String ERROR_CLASS_NOT_ONLINE = "ERROR_CLASS_NOT_ONLINE";
    public static final String ERROR_CLASS_NOT_START = "ERROR_CLASS_NOT_START";
    public static final String ERROR_CLASS_NOT_OPENING = "ERROR_CLASS_NOT_OPENING";
    public static final String ERROR_CLASS_NOT_ONLINE_OR_OPENING = "ERROR_CLASS_NOT_ONLINE_OR_OPENING";
    public static final String ERROR_CLASS_BESPEAK_NOT_ALLOW_OPEN = "ERROR_CLASS_BESPEAK_NOT_ALLOW_OPEN";
    public static final String ERROR_CLASS_SUCCESS_ORDER_NOT_EXISTS = "ERROR_CLASS_SUCCESS_ORDER_NOT_EXISTS";
    public static final String ERROR_CLASS_CATALOG_NOT_EXISTS = "ERROR_CLASS_CATALOG_NOT_EXISTS";
    public static final String ERROR_CLASS_IS_OPENING = "ERROR_CLASS_IS_OPENING";
    public static final String ERROR_CLASS_IS_END = "ERROR_CLASS_IS_END";
    public static final String ERROR_CLASS_NEED_VIP = "ERROR_CLASS_NEED_VIP";
    public static final String ERROR_CLASS_EXIST_SAVED_SUCCESS_ORDER = "ERROR_CLASS_EXIST_SAVED_SUCCESS_ORDER";
    public static final String ERROR_CLASS_NOT_EXIST_SUCCESS_ORDER = "ERROR_CLASS_NOT_EXIST_SUCCESS_ORDER";
    public static final String ERROR_CLASS_OPENING_ORDER_DUPLICATE = "ERROR_CLASS_OPENING_ORDER_DUPLICATE";
    public static final String ERROR_CLASS_CATALOG_STARTTIME_TONOW_LT30 = "ERROR_CLASS_CATALOG_STARTTIME_TONOW_LT30";
    public static final String ERROR_CLASS_SCHEDULE_NOT_EXIST = "ERROR_CLASS_SCHEDULE_NOT_EXIST";
    public static final String ERROR_CLASS_SCHEDULE_NOT_START = "ERROR_CLASS_SCHEDULE_NOT_START";
    /**
     * 订单
     */
    public static final String ERROR_ORDER_NOT_EXISTS = "ERROR_ORDER_NOT_EXISTS";
    public static final String ERROR_ORDER_DUPLICATE = "ERROR_ORDER_DUPLICATE";
    public static final String ERROR_ORDER = "ERROR_ORDER";
    public static final String ERROR_ORDER_ENTERED_FULL = "ERROR_ORDER_ENTERED_FULL";
    public static final String ERROR_ORDER_ORDERED_NUM_FULL = "ERROR_ORDER_ORDERED_NUM_FULL";
    public static final String ERROR_ORDER_NOT_END = "ERROR_ORDER_NOT_END";
    public static final String ERROR_ORDER_CANCEL_STATUS_WRONG = "ERROR_ORDER_CANCEL_STATUS_WRONG";

    /**
     * 产品评论
     */
    public static final String ERROR_COURSE_COMMENT_DUPLICATE = "ERROR_COURSE_COMMENT_DUPLICATE";
    /**
     * 随堂评论
     */
    public static final String ERROR_CLASS_CATALOG_COMMENT_IS_EXIST = "ERROR_CLASS_CATALOG_COMMENT_IS_EXIST";
    public static final String ERROR_CLASS_CATALOG_COMMENT_NOT_EXIST = "ERROR_CLASS_CATALOG_COMMENT_NOT_EXIST";
    public static final String ERROR_CLASS_CATALOG_COMMENT_REPLAY_EXIST = "ERROR_CLASS_CATALOG_COMMENT_REPLAY_EXIST";
    public static final String ERROR_CLASS_CATALOG_COMMENT_OTHER_TAG_EXIST = "ERROR_CLASS_CATALOG_COMMENT_OTHER_TAG_EXIST";
    /**
     * 机构账户
     */
    public static final String ERROR_ORGAN_ACCOUNT_NOT_EXISTS = "ERROR_ORGAN_ACCOUNT_NOT_EXISTS";
    public static final String ERROR_ORGAN_ACCOUNT_IS_CANCEL = "ERROR_ORGAN_ACCOUNT_IS_CANCEL";
    public static final String ERROR_ORGAN_ACCOUNT_IS_FREEZE = "ERROR_ORGAN_ACCOUNT_IS_FREEZE";
    public static final String ERROR_ORGAN_ACCOUNT_NOT_MATCH_CLASS = "ERROR_ORGAN_ACCOUNT_NOT_MATCH_CLASS";
    public static final String ERROR_ORGAN_ACCOUNT_CANCEL_EXIST_CLASS = "ERROR_ORGAN_ACCOUNT_CANCEL_EXIST_CLASS";
    /**
     * 系统用户
     */
    public static final String ERROR_SYSTEM_USER_NOT_EXISTS = "ERROR_SYSTEM_USER_NOT_EXISTS";
    public static final String ERROR_SYSTEM_USER_ORGAN_NOT_ONLINE = "ERROR_SYSTEM_USER_ORGAN_NOT_ONLINE";
    public static final String ERROR_SYSTEM_USER_OLDPSD_WRONG = "ERROR_SYSTEM_USER_OLDPSD_WRONG";
    /**
     * 角色
     */
    public static final String ERROR_SYSTEM_ROLE_NOT_EXISTS = "ERROR_SYSTEM_ROLE_NOT_EXISTS";

    /**
     * 标签
     */
    public static final String ERROR_CONSTANT_TAG_NOT_EXISTS = "ERROR_CONSTANT_TAG_NOT_EXISTS";
    public static final String ERROR_CONSTANT_TAG_DELETE_WHEN_USED = "ERROR_CONSTANT_TAG_DELETE_WHEN_USED";

    /**
     * 产品类目
     */
    public static final String ERROR_CONSTANT_CATALOG_DELETE_WHEN_USED = "ERROR_CONSTANT_CATALOG_DELETE_WHEN_USED";
    /**
     * 预约行程
     */
    public static final String RECTIFY_SCHEDULE_STARTTIME_TONOW_LT30 = "RECTIFY_SCHEDULE_STARTTIME_TONOW_LT30";
    /**
     * 微信接口
     */
    public static final String ERROR_WECHAT_API_REQPARAM = "ERROR_WECHAT_API_REQPARAM";
}
