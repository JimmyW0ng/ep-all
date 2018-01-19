package com.ep.domain.pojo.bo;

import lombok.Data;

import java.io.Serializable;

@Data
public class BackendPrincipalBo implements Serializable {

    private String userName;
    private String clientId;
    private String captchaCode;
    private String role;
    private Long createTime;
    private Long expireTime;

}
