/*
 * This file is generated by jOOQ.
*/
package com.ep.domain.pojo.po;


import com.ep.domain.pojo.AbstractBasePojo;
import com.ep.domain.repository.domain.enums.EpTokenType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;


/**
 * token表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EpTokenPo extends AbstractBasePojo {

    private Long id;
    private Long mobile;
    private EpTokenType type;
    private String role;
    private String lastAccessIp;
    private Long ognId;
    private Timestamp expireTime;
    private Timestamp createAt;
    private Timestamp updateAt;
    private String remark;
    private Boolean delFlag;
    private Long version;

}
