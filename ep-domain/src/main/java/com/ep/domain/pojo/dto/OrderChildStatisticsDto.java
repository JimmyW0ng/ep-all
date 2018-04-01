package com.ep.domain.pojo.dto;

import com.ep.domain.pojo.AbstractBasePojo;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description:
 * @Author: CC.F
 * @Date: 12:34 2018/4/1
 */
@Data
@NoArgsConstructor
public class OrderChildStatisticsDto extends AbstractBasePojo {
    private Long childId;
    private Long memberId;
    private Long mobile;
    private String childTrueName;
    private String childNickName;
    private Integer enteredClassNum;
    private Integer enteredBespeakNum;
}
