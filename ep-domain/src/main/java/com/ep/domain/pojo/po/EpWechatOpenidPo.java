package com.ep.domain.pojo.po;

import com.ep.domain.pojo.AbstractBasePojo;
import com.ep.domain.repository.domain.enums.EpWechatOpenidType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @Description:
 * @Author: CC.F
 * @Date: 17:29 2018/5/3/003
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EpWechatOpenidPo extends AbstractBasePojo {
    private Long id;
    private String openid;
    private EpWechatOpenidType type;
    private Long mobile;

}
