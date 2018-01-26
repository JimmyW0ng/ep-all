package com.ep.domain.constant;

/**
 * @Description:业务常量
 * @Author: J.W
 * @Date: 下午8:29 2018/1/6
 */
public class BizConstant {

    /**
     * 通用常量
     */
    public static String KEY_SPLIT = "KEY_SPLIT";

    /**
     * 用户信息
     */
    public static String CURENT_USER = "CURENT_USER";

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
     * 数据常量
     */
    public static int DB_NUM_ZERO = 0;

    public static int DB_NUM_ONE = 1;

    /**
     * wechat
     */
    public static String DICT_GROUP_WECHAT = "WECHAT";

    public static String DICT_KEY_WECHAT_ACCESS_TOKEN = "WECHAT_ACCESS_TOKEN";

    public static String WECHAT_REQ_CURRENT_SESSION = "currentWechatSession";

    public static int WECHAT_SESSION_TIME_OUT_M = -60;

    public static String WECHAT_URL_SESSION = "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code";

    public static String WECHAT_URL_ACCESS_TOKEN = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";

    public static String WECHAT_URL_WECHAT_CODE = "https://api.weixin.qq.com/wxa/getwxacode?access_token=%s";

    public static String WECHAT_URL_WECHAT_CODE_UNLIMIT = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=%s";

    /**
     * 短信验证码
     */
    public static int CAPTCHA_SHORT_MSG_LENGTH = 6;

    public static int CAPTCHA_SHORT_MSG_NUM_LIMIT = 10;

    public static int CAPTCHA_SHORT_MSG_EXPIRE_MINUTE = 20;

    public static int CAPTCHA_EXPIRE_DEL_HOUR_LIMIT = -1;

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
}
