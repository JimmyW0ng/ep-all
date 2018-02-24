/*
 * This file is generated by jOOQ.
*/
package com.ep.domain.pojo.po;


import com.ep.domain.pojo.AbstractBasePojo;
import com.ep.domain.repository.domain.enums.EpMemberChildCommentType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;


/**
 * 孩子上课评论表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EpMemberChildCommentPo extends AbstractBasePojo {

    private Long id;
    private Long pId;
    private Long childId;
    private Long ognId;
    private Long courseId;
    private Long classId;
    private Long classCatelogId;
    private EpMemberChildCommentType type;
    private String content;
    private Long ognAccountId;
    private Long replyMemberId;
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
