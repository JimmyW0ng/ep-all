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
     * 机构
     */
    public static final String ERROR_ORGAN_EXISTS = "ERROR_ORGAN_EXISTS";
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
     * 课程
     */
    public static final String ERROR_COURSE_NOT_EXISTS = "ERROR_COURSE_NOT_EXISTS";
    public static final String ERROR_COURSE_ENTER_NOT_START = "ERROR_COURSE_ENTER_NOT_START";
    public static final String ERROR_COURSE_ENTER_END = "ERROR_COURSE_ENTER_END";
    public static final String ERROR_COURSE_NOT_ONLINE = "ERROR_COURSE_NOT_ONLINE";
    public static final String ERROR_COURSE_IS_OFF = "ERROR_COURSE_IS_OFF";
    public static final String ERROR_CLASS_NOT_EXISTS = "ERROR_CLASS_NOT_EXISTS";
    public static final String ERROR_CLASS_CATELOG_NOT_EXISTS = "ERROR_CLASS_CATELOG_NOT_EXISTS";
    public static final String ERROR_CLASS_ACCOUNT_NOT_MATCH = "ERROR_CLASS_ACCOUNT_NOT_MATCH";
    /**
     * 订单
     */
    public static final String ERROR_ORDER_NOT_EXISTS = "ERROR_ORDER_NOT_EXISTS";
    public static final String ERROR_ORDER_DUPLICATE = "ERROR_ORDER_DUPLICATE";
    public static final String ERROR_ORDER = "ERROR_ORDER";
    public static final String ERROR_ORDER_ENTERED_FULL = "ERROR_ORDER_ENTERED_FULL";
    public static final String ERROR_ORDER_ORDERED_NUM_FULL = "ERROR_ORDER_ORDERED_NUM_FULL";
    public static final String ERROR_ORDER_NOT_END = "ERROR_ORDER_NOT_END";
    /**
     * 课程评论
     */
    public static final String ERROR_COURSE_COMMENT_DUPLICATE = "ERROR_COURSE_COMMENT_DUPLICATE";
    /**
     * 机构账户
     */
    public static final String ERROR_ORGAN_ACCOUNT_NOT_EXISTS = "ERROR_ORGAN_ACCOUNT_NOT_EXISTS";
}
