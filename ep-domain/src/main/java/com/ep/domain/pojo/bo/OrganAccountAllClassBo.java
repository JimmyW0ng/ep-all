package com.ep.domain.pojo.bo;

import com.ep.domain.pojo.po.EpOrganClassPo;
import com.ep.domain.repository.domain.enums.EpOrganClassStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class OrganAccountAllClassBo extends EpOrganClassPo {

    private String courseName;
    private String mainPicUrl;

    public String getStatusText() {
        if (EpOrganClassStatus.save.equals(super.getStatus())) {
            return "待上线";
        } else if (EpOrganClassStatus.online.equals(super.getStatus())) {
            return "已上线";
        } else if (EpOrganClassStatus.opening.equals(super.getStatus())) {
            return "已开班";
        } else if (EpOrganClassStatus.end.equals(super.getStatus())) {
            return "已结束";
        }
        return null;
    }

}
