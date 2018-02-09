package com.ep.api.controller;

import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.po.EpMemberPo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: 机构账户控制类
 * @Author: J.W
 * @Date: 下午3:55 2018/2/9
 */
@Slf4j
@RequestMapping("auth/organ/account")
@RestController
@Api(value = "api-auth-organ-account", description = "机构账户接口")
public class OrganAccountController extends ApiController {

    @ApiOperation(value = "机构账户信息")
    @PostMapping("/info")
    public ResultDo getOrganAccountInfo() {
        EpMemberPo memberPo = super.getCurrentUser().get();
        return null;
    }

}
