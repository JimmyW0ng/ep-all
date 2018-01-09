package com.ep.domain.pojo.bo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author J.W
 * @date 2017/10/27 0027
 */
@Data
@AllArgsConstructor
public class SecurityCredentialBo implements Serializable {

    private String password;
    private String clientSecret;

}
