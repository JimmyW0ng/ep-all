package com.ep.api.controller;

import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.dto.OrganInfoDto;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: 机构api控制类
 * @Author: J.W
 * @Date: 下午8:26 2018/1/6
 */
@Slf4j
@RequestMapping("security/ogn")
@RestController
public class OrganController {

    /**
     * 机构详情
     *
     * @param ognId
     * @return
     */
    @ApiOperation(value = "机构详情")
    @PostMapping("/detail")
    public ResultDo<OrganInfoDto> getOgnInfo(@RequestParam("ognId") Long ognId) {
        // return ognInfoService.getOgnDetail(ognId);
        return null;
    }

}
