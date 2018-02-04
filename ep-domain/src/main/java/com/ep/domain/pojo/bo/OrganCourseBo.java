package com.ep.domain.pojo.bo;


import com.ep.domain.pojo.po.EpOrganCoursePo;
import lombok.Data;

@Data
public class OrganCourseBo extends EpOrganCoursePo {

    private String label;
    private String mainPicUrl;
    private String ognName;
    private String ognPhone;

}
