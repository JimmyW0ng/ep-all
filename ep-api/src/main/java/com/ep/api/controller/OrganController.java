package com.ep.api.controller;

import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.dto.OrganInfoDto;
import com.ep.domain.service.OrganInfoService;
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
public class OrganController extends ApiController {

    @Autowired
    private OrganInfoService organInfoService;

    /**
     * 机构详情
     *
     * @param ognId
     * @return
     */
    @ApiOperation(value = "机构详情")
    @PostMapping("/detail")
    public ResultDo<OrganInfoDto> getOgnInfo(@RequestParam("organId") Long ognId) {
        return organInfoService.getOgnDetail(ognId);
    }

}
