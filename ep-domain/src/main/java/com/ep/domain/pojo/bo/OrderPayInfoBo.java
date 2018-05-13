package com.ep.domain.pojo.bo;

import com.ep.common.tool.DateTools;
import com.ep.common.tool.NumberTools;
import com.ep.domain.pojo.po.EpOrderPo;
import com.ep.domain.repository.domain.enums.EpOrderPayStatus;
import com.ep.domain.repository.domain.enums.EpOrderPayType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class OrderPayInfoBo extends EpOrderPo {

    private String ognName;
    private String courseName;
    private String className;
    private String childNickName;
    private String childAvatar;

    /**
     * 返回金额格式化
     *
     * @return
     */
    public String getPirzeFormat() {
        return NumberTools.getFormatPrice(super.getPrize());
    }

    /**
     * 支付确认时间
     *
     * @return
     */
    public String getPayConfirmTimeFormat() {
        if (super.getPayConfirmTime() != null) {
            return DateTools.timestampToString(super.getPayConfirmTime(), DateTools.TIME_PATTERN_MILLISECOND);
        }
        return null;
    }

    /**
     * 支付状态文案
     *
     * @return
     */
    public String getPayStatusFormat() {
        if (super.getPayStatus() != null) {
            if (EpOrderPayStatus.wait_pay.equals(super.getPayStatus())) {
                return "未支付";
            } else if (EpOrderPayStatus.paid.equals(super.getPayStatus())) {
                return "支付成功";
            } else if (EpOrderPayStatus.refund_apply.equals(super.getPayStatus())) {
                return "退单申请中";
            } else if (EpOrderPayStatus.refund_finish.equals(super.getPayStatus())) {
                return "已退单";
            }
        }
        return null;
    }

    /**
     * 支付类型文案
     *
     * @return
     */
    public String getPayTypeFormat() {
        if (super.getPayType() != null) {
            if (EpOrderPayType.offline.equals(super.getPayType())) {
                return "商户支付途径支付";
            } else if (EpOrderPayType.wechat_pay.equals(super.getPayType())) {
                return "平台在线支付";
            }
        }
        return null;
    }

}
