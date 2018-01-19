package com.ep.domain.pojo.bo;

import com.ep.domain.pojo.AbstractBasePojo;
import lombok.Data;

@Data
public class BackendPrincipalBo extends AbstractBasePojo {

    private String userName;
    private String captchaCode;
    private String role;
    private Long createTime;
    private Long expireTime;

}
