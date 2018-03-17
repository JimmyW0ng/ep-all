package com.ep.domain.pojo.bo;

import com.ep.domain.pojo.AbstractBasePojo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * @Description:
 * @Author: CC.F
 * @Date: 14:56 2018/3/16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RectifyOrganClassCatalogBo extends AbstractBasePojo {
    private Long id;
    private String catalogTitle;
    private String catalogDesc;
    private Timestamp startTime;
    private Timestamp endTime;
    private Boolean rectifyFlag;
}
