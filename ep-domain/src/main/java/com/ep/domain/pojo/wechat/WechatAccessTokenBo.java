package com.ep.domain.pojo.wechat;

import lombok.Data;

@Data
public class WechatAccessTokenBo extends WechatBaseBo {

    private String access_token;
    private Integer expires_in;

}
