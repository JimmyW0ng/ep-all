package com.ep.domain.pojo.bo;


import com.ep.common.tool.DateTools;
import com.ep.domain.pojo.po.EpOrganCoursePo;
import com.ep.domain.repository.domain.enums.EpOrganCourseCourseStatus;
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
    private String vipName;

    public Long getEnterTimeStampStart() {
        if (super.getEnterTimeStart() != null) {
            return super.getEnterTimeStart().getTime();
        }
        return null;
    }

    public Long getServerTimeStamp() {
        return DateTools.getCurrentDateTime().getTime();
    }

    public String getCourseStatusText() {
        if (EpOrganCourseCourseStatus.offline.equals(super.getCourseStatus())) {
            return "已结束";
        }
        return null;
    }

}
