/*
 * This file is generated by jOOQ.
*/
package com.ep.domain.pojo.po;


import com.ep.domain.pojo.AbstractBasePojo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;


/**
 * 机构课程sku目录表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EpOrganCourseSkuCatelogPo extends AbstractBasePojo {

    private Long id;
    private Long skuId;
    private String catelogTitle;
    private String catelogDesc;
    private Integer catelogIndex;
    private String remark;
    private Timestamp createAt;
    private Timestamp updateAt;
    private Boolean delFlag;
    private Long version;

}
