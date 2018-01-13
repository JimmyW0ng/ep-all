/*
 * This file is generated by jOOQ.
*/
package com.ep.domain.pojo.po;


import com.ep.domain.pojo.AbstractBasePojo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;


/**
 * 机构课程分班表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EpOrganClassPo extends AbstractBasePojo {

    private Long id;
    private Long ognId;
    private Long courseId;
    private Long skuId;
    private String className;
    private String classDesc;
    private Long classLeaderId;
    private Timestamp createAt;
    private Timestamp updateAt;
    private String remark;
    private Boolean delFlag;
    private Long version;

}
