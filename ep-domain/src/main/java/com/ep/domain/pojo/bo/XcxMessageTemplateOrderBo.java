package com.ep.domain.pojo.bo;

import com.ep.domain.pojo.AbstractBasePojo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * @Description:
 * @Author: CC.F
 * @Date: 21:45 2018/6/5
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class XcxMessageTemplateOrderBo extends AbstractBasePojo {
    private Long orderId;
    private String formId;
    private String touser;
    private Timestamp expireTime;
    private String childTrueName;
    private String childNickName;
    private String courseName;
    private String createAt;
}
