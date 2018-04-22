package com.ep.domain.pojo.bo;


import com.ep.common.tool.DateTools;
import com.ep.common.tool.StringTools;
import com.ep.domain.constant.BizConstant;
import com.ep.domain.pojo.po.EpOrganCoursePo;
import com.ep.domain.repository.domain.enums.EpOrganCourseCourseStatus;
import com.ep.domain.repository.domain.enums.EpOrganCourseCourseType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@NoArgsConstructor
public class OrganCourseBo extends EpOrganCoursePo {

    private String label;
    private String mainPicUrl;
    private String ognName;
    private String ognPhone;
    private String courseCatalogName;
    private String vipName;
    private Boolean canEnterFlag;

    public Long getEnterTimeStampStart() {
        if (super.getEnterTimeStart() != null) {
            return super.getEnterTimeStart().getTime();
        }
        return null;
    }

    public Long getServerTimeStamp() {
        return DateTools.getCurrentDateTime().getTime();
    }

    public String getCourseTypeText() {
        if (EpOrganCourseCourseType.training.equals(super.getCourseType())) {
            return "课程";
        } else if (EpOrganCourseCourseType.activity.equals(super.getCourseType())) {
            return "活动";
        }
        return null;
    }

    public String getCourseStatusText() {
        if (EpOrganCourseCourseStatus.offline.equals(super.getCourseStatus())) {
            return "已结束";
        }
        return null;
    }

    public String getSceneCode() {
        if (super.getOgnId() != null && super.getId() != null) {
            String oCode = StringTools.generateShortUrl(super.getOgnId(),
                    BizConstant.WECHAT_SCENE_PASSWORD,
                    BizConstant.WECHAT_SCENE_SALT_LENGTH);
            String cCode = StringTools.generateShortUrl(super.getId(),
                    BizConstant.WECHAT_SCENE_PASSWORD,
                    BizConstant.WECHAT_SCENE_SALT_LENGTH);
            return oCode + BizConstant.WECHAT_SCENE_SPLIT + cCode;
        }
        return null;
    }

}
