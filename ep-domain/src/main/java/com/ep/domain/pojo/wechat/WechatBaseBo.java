package com.ep.domain.pojo.wechat;

import com.ep.common.tool.StringTools;
import com.ep.domain.pojo.AbstractBasePojo;
import lombok.Data;

/**
 * Created by JW on 17/9/9.
 */
@Data
public class WechatBaseBo extends AbstractBasePojo {

    private String errcode;
    private String errmsg;

    public Boolean isSuccess() {
        if (StringTools.isBlank(errcode)) {
            return true;
        }
        return false;
    }

}
