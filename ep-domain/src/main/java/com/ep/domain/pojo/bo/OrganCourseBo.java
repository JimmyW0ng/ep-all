package com.ep.domain.pojo.bo;


import com.ep.common.tool.DateTools;
import com.ep.domain.pojo.po.EpOrganCoursePo;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrganCourseBo extends EpOrganCoursePo {

    private String label;
    private String mainPicUrl;
    private String ognName;
    private String ognPhone;
    private String courseCatalogName;

    public Long getEnterTimeStampStart() {
        if (super.getEnterTimeStart() != null) {
            return super.getEnterTimeStart().getTime();
        }
        return null;
    }

    public Long getServerTimeStamp() {
        return DateTools.getCurrentDateTime().getTime();
    }

}
