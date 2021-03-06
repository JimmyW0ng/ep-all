/*
 * This file is generated by jOOQ.
*/
package com.ep.domain.pojo.po;


import com.ep.domain.pojo.AbstractBasePojo;
import com.ep.domain.repository.domain.enums.EpSystemRoleTarget;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;


/**
 * 角色表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EpSystemRolePo extends AbstractBasePojo {

    private Long id;
    private EpSystemRoleTarget target;
    private String roleName;
    private String roleCode;
    private Long createBy;
    private Timestamp createAt;
    private Long updateBy;
    private Timestamp updateAt;
    private String remark;
    private Boolean delFlag;
    private Long version;

}
