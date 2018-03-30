/*
 * This file is generated by jOOQ.
*/
package com.ep.domain.pojo.po;


import com.ep.domain.pojo.AbstractBasePojo;
import com.ep.domain.repository.domain.enums.EpOrganClassScheduleStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.sql.Timestamp;


/**
 * 班次行程表
 */
@Data
public class EpOrganClassSchedulePo extends AbstractBasePojo {

    private Long id;
    private Long childId;
    private Long classId;
    private Long orderId;
    private Timestamp startTime;
    private Integer duration;
    private String catalogTitle;
    private String catalogDesc;
    private Integer catalogIndex;
    private EpOrganClassScheduleStatus status;
    private Boolean evaluateFlag;
    private Long classCatalogId;
    @JsonIgnore
    private Timestamp createAt;
    @JsonIgnore
    private Timestamp updateAt;
    @JsonIgnore
    private String remark;
    @JsonIgnore
    private Boolean delFlag;
    @JsonIgnore
    private Long version;

}
