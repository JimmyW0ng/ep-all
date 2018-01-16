package com.ep.api.controller;

import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.dto.OrganInfoDto;
import com.ep.domain.service.OrganService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("security/organ")
@RestController
@Api(value = "api-security-organ", description = "机构开放接口")
public class OrganController extends ApiController {

    @Autowired
    private OrganService organService;

    @ApiOperation(value = "机构详情")
    @PostMapping("/detail")
    public ResultDo<OrganInfoDto> getOgnInfo(@RequestParam("organId") Long ognId) {
        return organService.getOgnDetail(ognId);
    }

//    @ApiOperation(value = "机构列表")
//    @PostMapping("/list")
//    public ResultDo<OrganInfoDto> getOgnList(@RequestParam("organId") Long ognId) {
//        return organService.getOgnList(ognId);
//    }

}
