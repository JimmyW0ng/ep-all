package com.ep.domain.pojo.bo;

import com.ep.common.component.SpringComponent;
import com.ep.common.tool.DateTools;
import com.ep.domain.pojo.AbstractBasePojo;
import com.ep.domain.repository.domain.enums.*;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @Description: 订单excel导出Bo
 * @Author: CC.F
 * @Date: 9:50 2018/4/17/017
 */
@Data
public class OrderExcelBo extends AbstractBasePojo {
    private String mobile;
    private String childTrueName;
    private String childNickName;
    private String childIdentity;
    private Timestamp childBirthday;
    private String currentClass;
    private String currentSchool;
    private String courseName;
    private String className;
    private EpOrganClassType classType;
    private EpOrganClassStatus classStatus;
    private BigDecimal prize;
    private EpOrderStatus status;
    private EpOrderPayType payType;
    private EpOrderPayStatus payStatus;
    private String remark;
    private Timestamp createAt;

    public String getStatusText() {
        return (classType.equals(EpOrganClassType.bespeak) && status.equals(EpOrderStatus.opening)) ?
                SpringComponent.messageSource("EpOrderStatusBespeak.opening")
                : SpringComponent.messageSource("EpOrderStatus." + status.getLiteral());
    }

    public String getClassTypeText() {
        return SpringComponent.messageSource("EpOrganClassType." + classType.getLiteral());
    }

    public String getClassStatusText() {
        return SpringComponent.messageSource("EpOrganClassStatus." + classStatus.getLiteral());
    }

    public String getPayTypeStatusText() {
        String payTypeText = "";
        if (null != payType) {
            payTypeText = SpringComponent.messageSource("EpOrderPayType." + payType.getLiteral()) + "--";
        }
        return payTypeText + SpringComponent.messageSource("EpOrderPayStatus." + payStatus.getLiteral());
    }

    public String getFmtChildBirthday() {
        return DateTools.timestampToString(childBirthday, DateTools.DATE_FMT_3);
    }

    public String getFmtCreateAt() {
        return DateTools.timestampToString(createAt, DateTools.TIME_PATTERN);
    }

}
