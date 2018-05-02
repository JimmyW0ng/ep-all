package com.ep.domain.pojo.bo;

import com.ep.domain.pojo.AbstractBasePojo;

/**
 * @Description:
 * @Author: CC.F
 * @Date: 7:08 2018/5/3
 */
public class XcxOrderBo extends AbstractBasePojo {
    /**
     * 公众账号ID,必填（32）
     */
    private String appid;
    /**
     * 商户号,必填（32）
     * 微信支付分配的商户号
     */
    private String mch_id;
    /**
     * 设备号（32）
     * 自定义参数，可以为终端设备号(门店号或收银设备ID)，PC网页或公众号内支付可以传"WEB"
     */
    private String device_info;
    /**
     * 随机字符串，必填（32）
     */
    private String nonce_str;
    /**
     * 签名，必填（32）
     */
    private String sign;
    /**
     * 签名类型（32）
     * 默认为MD5，支持HMAC-SHA256和MD5
     */
    private String sign_type;
    /**
     * 商品描述	,必填（128）
     */
    private String body;
    /**
     * 商品详情，（6000）
     */
    private String detail;
    /**
     * 附加数据(127)
     */
    private String attach;
    /**
     * 商户订单号,必填（32）
     */
    private String out_trade_no;
    /**
     * 标价币种（16）
     * CNY	符合ISO 4217标准的三位字母代码，默认人民币：CNY
     */
    private String fee_type;
    /**
     * 标价金额，必填
     * 订单总金额，单位为分
     */
    private int total_fee;

    /**
     * 终端IP，必填（16）
     * APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP
     */
    private String spbill_create_ip;
    /**
     * 交易起始时间（14）
     * 订单生成时间，格式为yyyyMMddHHmmss
     */
    private String time_start;

    /**
     * 交易结束时间（14）
     * 订单失效时间，格式为yyyyMMddHHmmss,最短失效时间间隔大于1分钟
     */
    private String time_expire;


    /**
     * 订单优惠标记（32）
     * WXG	订单优惠标记，使用代金券或立减优惠功能时需要的参数
     */
    private String goods_tag;

    /**
     * 通知地址,必填（256）
     * 异步接收微信支付结果通知的回调地址，通知url必须为外网可访问的url，不能携带参数
     */
    private String notify_url;
    /**
     * 交易类型,必填（16）
     * JSAPI 公众号支付,NATIVE 扫码支付,APP APP支付
     */
    private String trade_type;


    /**
     * 商品ID（32）
     * NATIVE时（即扫码支付），此参数必传。此参数为二维码中包含的商品ID，商户自行定义
     */
    private String product_id;
    /**
     * 指定支付方式（32）
     * 上传此参数no_credit--可限制用户不能使用信用卡支付
     */
    private String limit_pay;
    /**
     * 用户标识（128）
     * trade_type=JSAPI时（即公众号支付），此参数必传，此参数为微信用户在商户对应appid下的唯一标识。openid如何获取，可参考【获取openid】。企业号请使用【企业号OAuth2.0接口】获取企业号内成员userid，再调用【企业号userid转openid接口】进行转换
     */
    private String openid;
    /**
     * 场景信息	（256）
     */
    private String scene_info;
}
