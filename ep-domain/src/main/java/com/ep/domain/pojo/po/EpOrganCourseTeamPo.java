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
 * 机构课程团队信息表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EpOrganCourseTeamPo extends AbstractBasePojo {

    private Long id;
    private Long courseId;
    private Long ognAccountId;
    private Long sort;
    private Timestamp createAt;
    private Timestamp updateAt;
    private String remark;
    private Boolean delFlag;
    private Long version;

}
