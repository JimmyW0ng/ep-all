package com.ep.domain.pojo.bo;

import com.ep.domain.pojo.AbstractBasePojo;
import com.ep.domain.repository.domain.enums.EpMemberType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class ApiPrincipalBo extends AbstractBasePojo {

    private String userName;
    private String clientId;
    @JsonIgnore
    private String captchaCode;
    @JsonIgnore
    private EpMemberType memberType;
    private String role;
    private Long createTime;
    private Long expireTime;

}
