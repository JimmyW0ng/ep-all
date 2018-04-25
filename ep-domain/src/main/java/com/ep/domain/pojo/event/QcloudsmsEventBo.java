package com.ep.domain.pojo.event;

import com.ep.domain.pojo.AbstractBasePojo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description:
 * @Author: CC.F
 * @Date: 21:53 2018/4/25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QcloudsmsEventBo extends AbstractBasePojo {

    private Integer templateId;
    private String phoneNumber;
    private String[] params;
}
