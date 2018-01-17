package com.ep.api.controller;

import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.OrganBo;
import com.ep.domain.pojo.dto.OrganInfoDto;
import com.ep.domain.service.OrganService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

    @ApiOperation(value = "机构分页列表")
    @PostMapping("/page")
    public ResultDo<Page<OrganBo>> queryOgnPage(@PageableDefault Pageable pageable) {
        Page<OrganBo> data = organService.queryOgnPage(pageable);
        ResultDo<Page<OrganBo>> resultDo = ResultDo.build();
        return resultDo.setResult(data);
    }

}
