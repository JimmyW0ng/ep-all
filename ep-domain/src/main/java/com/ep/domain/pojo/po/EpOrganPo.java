/*
 * This file is generated by jOOQ.
*/
package com.ep.domain.pojo.po;


import com.ep.domain.pojo.AbstractBasePojo;
import com.ep.domain.repository.domain.enums.EpOrganStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;


/**
 * 机构信息表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EpOrganPo extends AbstractBasePojo {

    private Long id;
    private String ognName;
    private String ognAddress;
    private Long ognRegion;
    private String ognLng;
    private String ognLat;
    private String ognShortIntroduce;
    private Timestamp ognCreateDate;
    private String ognPhone;
    private String ognEmail;
    private String ognUrl;
    private String ognIntroduce;
    private Boolean vipFlag;
    private String vipName;
    private Byte marketWeight;
    private EpOrganStatus status;
    private Byte togetherScore;
    private Integer totalParticipate;
    @JsonIgnore
    private String remark;
    @JsonIgnore
    private Timestamp createAt;
    @JsonIgnore
    private Timestamp updateAt;
    @JsonIgnore
    private Boolean delFlag;
    @JsonIgnore
    private Long version;

}
