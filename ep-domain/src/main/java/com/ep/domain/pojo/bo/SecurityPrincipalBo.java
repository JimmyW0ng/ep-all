package com.ep.domain.pojo.bo;

import lombok.Data;

import java.io.Serializable;

/**
 *
 * @author JW
 * @date 17/11/13
 */
@Data
public class SecurityPrincipalBo implements Serializable {

    private String userName;
    private String clientId;
    private String captchaCode;
    private String role;
    private Long createTime;
    private Long expireTime;

    public SecurityPrincipalBo(String userName, String clientId) {
        this.userName = userName;
        this.clientId = clientId;
    }

}
