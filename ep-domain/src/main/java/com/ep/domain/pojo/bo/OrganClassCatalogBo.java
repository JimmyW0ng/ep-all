package com.ep.domain.pojo.bo;

import com.ep.common.tool.DateTools;
import com.ep.domain.pojo.po.EpOrganClassCatalogPo;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class OrganClassCatalogBo extends EpOrganClassCatalogPo {

    private Long commentId;
    private String comment;
    private Timestamp commentTime;
    private String reply;

    public String getStartTimeFormat() {
        return DateTools.formatDatetoString(super.getStartTime(), DateTools.DATE_FMT_4);
    }

    public String getCommentTimeFormat() {
        return DateTools.formatDatetoString(this.commentTime, DateTools.DATE_FMT_4);
    }

}