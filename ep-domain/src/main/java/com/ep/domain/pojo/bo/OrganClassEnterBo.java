package com.ep.domain.pojo.bo;

import com.ep.domain.pojo.po.EpOrganClassPo;
import com.ep.domain.repository.domain.enums.EpOrganClassStatus;
import lombok.Data;

@Data
public class OrganClassEnterBo extends EpOrganClassPo {

    /**
     * 是否允许报名标识
     *
     * @return
     */
    public Boolean getCanEnterFlag() {
        // 不是上线状态不允许报名
        if (EpOrganClassStatus.online.equals(super.getStatus())) {
            if (!super.getEnterLimitFlag()) {
                return true;
            } else if (super.getEnteredNum() < super.getEnterRequireNum()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 不允许报名文案
     *
     * @return
     */
    public String getCanNotEnterText() {
        if (EpOrganClassStatus.save.equals(super.getStatus())) {
            return "未上线";
        } else if (EpOrganClassStatus.opening.equals(super.getStatus())) {
            return "已开班";
        } else if (EpOrganClassStatus.end.equals(super.getStatus())) {
            return "已结束";
        } else if (EpOrganClassStatus.online.equals(super.getStatus())) {
            if (super.getEnterLimitFlag() && super.getEnteredNum() >= super.getEnterRequireNum()) {
                return "已满额";
            }
        }
        return null;
    }
}
