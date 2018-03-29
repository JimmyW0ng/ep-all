package com.ep.domain.pojo.bo;

import com.ep.common.tool.DateTools;
import com.ep.domain.pojo.po.EpOrganClassCatalogPo;
import lombok.Data;
import lombok.ToString;

import java.sql.Timestamp;

@Data
@ToString(callSuper = true)
public class OrganClassCatalogBo extends EpOrganClassCatalogPo {

    private Long commentId;
    private String comment;
    private Timestamp commentTime;
    private String replay;

    public String getStartTimeFormat() {
        return DateTools.formatDatetoString(super.getStartTime(), DateTools.DATE_FMT_4);
    }

    public String getCommentTimeFormat() {
        return DateTools.formatDatetoString(this.commentTime, DateTools.DATE_FMT_4);
    }

}
