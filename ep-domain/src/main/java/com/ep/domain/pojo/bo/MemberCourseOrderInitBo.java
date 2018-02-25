package com.ep.domain.pojo.bo;


import com.ep.domain.pojo.AbstractBasePojo;
import lombok.Data;

@Data
public class MemberCourseOrderInitBo extends AbstractBasePojo {

    private Long childId;
    private String avatar;
    private String childNickName;
    private String joinedClasses;

}
