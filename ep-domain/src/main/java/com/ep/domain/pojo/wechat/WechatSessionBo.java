package com.ep.domain.pojo.wechat;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class WechatSessionBo extends WechatBaseBo {

    private String openid;
    private String session_key;
    private String unionid;
    private Integer expires_in;

}
