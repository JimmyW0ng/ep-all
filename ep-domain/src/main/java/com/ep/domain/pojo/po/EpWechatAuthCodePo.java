
package com.ep.domain.pojo.po;


import com.ep.domain.pojo.AbstractBasePojo;
import com.ep.domain.repository.domain.enums.EpWechatAuthCodeType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class EpWechatAuthCodePo extends AbstractBasePojo {
    private Long id;
    private String openId;
    private Long mobile;
    private String authCode;
    private EpWechatAuthCodeType type;
    private Integer expirTime;
    private Timestamp createAt;
    private Boolean delFlag;
}
