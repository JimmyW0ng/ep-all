package com.ep.domain.constant;

/**
 * @Description:业务常量
 * @Author: J.W
 * @Date: 下午8:29 2018/1/6
 */
public class BizConstant {

    /**
     * 评分最小单位
     */
    public static final byte SCORE_UNIT = 5;
    /**
     * 消息文案
     */
    public static final String MESSAGE_CONTENT_CLASS_CATALOG_COMMENT = "\"%s\"在\"%s-%s\"中收到评价，点击查看";
    /**
     * 会员
     */
    public static final String VIP_NAME = "会员";
    /**
     * 测试环境通用验证码
     */
    public static final String MESSAGE_CAPTCHA_IN_TEST = "8888";
    /**
     * 通用常量
     */
    public static String KEY_SPLIT = "KEY_SPLIT";
    public static String STRING_SPLIT = ",";
    /**
     * 客户端小程序标识
     */
    public static String WECHAT_APP_MEMBER_CLIENT = "WECHAT_APP_MEMBER_CLIENT";
    /**
     * 机构端小程序标识
     */
    public static String WECHAT_APP_ORGAN_CLIENT = "WECHAT_APP_ORGAN_CLIENT";
    /**
     * 当前会话中的用户信息
     */
    public static String CURENT_USER = "CURENT_USER";
    /**
     * 当前会话中的机构账户信息
     */
    public static String CURENT_ORGAN_ACCOUNT = "CURENT_ORGAN_ACCOUNT";
    /**
     * 用户信息生成密码盐的方法generateShortUrl()的入参minlength
     */
    public static int PASSWORD_SALT_MINLENGTH = 4;
    /**
     * 后台用户信息
     */
    public static String CURENT_BACKEND_USER = "CURENT_BACKEND_USER";
    /**
     * 后台登录验证码
     */
    public static String CAPTCHA_SESSION_KEY = "CAPTCHA_SESSION_KEY";
    /**
     * 后台菜单树顶点id
     */
    public static Long PLATFORM_MENU_PARENT_ID = 1L;
    /**
     * 商户菜单树顶点id
     */
    public static Long MERCHANT_MENU_PARENT_ID = 3L;
    /**
     * 数据常量
     */
    public static int DB_NUM_ZERO = 0;
    public static long LONG_ZERO = 0L;
    public static int DB_NUM_ONE = 1;
    public static int TIME_UNIT = 60;
    /**
     * 短信验证码
     */
    public static int CAPTCHA_SHORT_MSG_LENGTH = 6;
    public static int CAPTCHA_SHORT_MSG_NUM_LIMIT = 10;
    public static int CAPTCHA_SHORT_MSG_EXPIRE_MINUTE = 20;
    /**
     * 文件业务类型
     */
    public static short FILE_BIZ_TYPE_CODE_ORGAN_LOGO = 100;
    public static short FILE_BIZ_TYPE_CODE_ORGAN_BANNER = 101;
    public static short FILE_BIZ_TYPE_CODE_ORGAN_MAIN_PIC = 102;
    public static short FILE_BIZ_TYPE_CODE_COURSE_BANNER = 110;
    public static short FILE_BIZ_TYPE_CODE_COURSE_MAIN_PIC = 111;
    public static short FILE_BIZ_TYPE_CODE_COURSE_DESC_PIC = 112;
    public static short FILE_BIZ_TYPE_CODE_MEMBER_AVATAR = 120;
    public static short FILE_BIZ_TYPE_CODE_CHILD_AVATAR = 130;
    public static short FILE_BIZ_TYPE_CODE_TEACHER_AVATAR = 140;
    public static short FILE_BIZ_TYPE_CODE_COURSE_CLASS_COMMENT_PIC = 150;
    /**
     * 订单数量控制
     */
    public static int ORDER_BEYOND_NUM = 200;
    /**
     * 孩子数量控制
     */
    public static int CHILD_LIMIT_NUM = 4;
    /**
     * 紧急修改班次目录开始时间距离当前时间不得小于30分钟
     */
    public static long RECTIFY_CATALOG_STARTTIME_TONOW_LT30 = 30L;
}
