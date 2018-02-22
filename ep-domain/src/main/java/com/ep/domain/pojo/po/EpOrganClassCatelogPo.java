/*
 * This file is generated by jOOQ.
*/
package com.ep.domain.pojo.po;


import com.ep.domain.pojo.AbstractBasePojo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;


/**
 * 班次课程内容目录表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EpOrganClassCatelogPo extends AbstractBasePojo {

    private Long id;
    private Long classId;
    private String catelogTitle;
    private String catelogDesc;
    private Integer catelogIndex;
    private Timestamp startTime;
    private Timestamp endTime;
    private String remark;
    private Timestamp createAt;
    @JsonIgnore
    private Timestamp updateAt;
    @JsonIgnore
    private Boolean delFlag;
    @JsonIgnore
    private Long version;

}
