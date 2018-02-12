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
 * 孩子标签记录表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EpMemberChildTagPo extends AbstractBasePojo {

    private Long id;
    private Long childId;
    private Long classCatelogId;
    private Long tagId;
    private Timestamp createAt;
    private Timestamp updateAt;
    private String remark;
    private Boolean delFlag;
    private Long version;


}
