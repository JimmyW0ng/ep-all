package com.ep.domain.pojo.bo;

import com.ep.domain.pojo.AbstractBasePojo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class ApiPrincipalBo extends AbstractBasePojo {

    private String userName;
    private Long mobile;
    private Long ognId;
    private String clientId;
    @JsonIgnore
    private String captchaCode;
    @JsonIgnore
    private String type;
    private String role;
    private Long createTime;
    private Long expireTime;

}
