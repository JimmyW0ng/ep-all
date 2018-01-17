package com.ep.domain.pojo.dto;

import com.ep.domain.pojo.po.EpSystemMenuPo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by fcc on 2018/1/17.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SystemMenuDto extends EpSystemMenuPo {
    private EpSystemMenuPo epSystemMenuPo;
    private List<SystemMenuDto> childList;
}
