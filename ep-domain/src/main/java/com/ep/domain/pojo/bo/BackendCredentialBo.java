package com.ep.domain.pojo.bo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class BackendCredentialBo implements Serializable {

    private String password;
    private String clientSecret;

}
