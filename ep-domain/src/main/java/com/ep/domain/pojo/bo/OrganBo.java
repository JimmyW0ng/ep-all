package com.ep.domain.pojo.bo;

import com.ep.common.tool.StringTools;
import com.ep.domain.constant.BizConstant;
import com.ep.domain.pojo.po.EpOrganPo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class OrganBo extends EpOrganPo {

    private String catalogIds;
    @JsonIgnore
    private String catalogLabels;
    private String fileUrl;

    /**
     * 机构标签格式化
     *
     * @return
     */
    public String[] getCatalogLabelFormat() {
        if (StringTools.isNotEmpty(catalogLabels)
                && catalogLabels.indexOf(BizConstant.STRING_SPLIT) >= BizConstant.DB_NUM_ZERO) {
            return catalogLabels.split(BizConstant.STRING_SPLIT);
        }
        return null;
    }

}
