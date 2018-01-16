package com.ep.api.controller;

import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.MemberChildBo;
import com.ep.domain.pojo.dto.MemberInfoDto;
import com.ep.domain.pojo.po.EpMemberPo;
import com.ep.domain.service.MemberChildService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Description: 会员api控制类
 * @Author: J.W
 * @Date: 下午8:26 2018/1/6
 */
@Slf4j
@RequestMapping("auth/member")
@RestController
@Api(value = "api-auth-member", description = "会员接口")
public class MemberController extends ApiController {

    @Autowired
    private MemberChildService memberChildService;

    @ApiOperation(value = "当前用户信息")
    @PostMapping("/detail")
    @PreAuthorize("hasAnyAuthority('api:base')")
    public ResultDo<MemberInfoDto> index() {
        EpMemberPo currentMbr = super.getCurrentUser().get();
        List<MemberChildBo> children = memberChildService.queryAllByMemberId(currentMbr.getId());
        MemberInfoDto mbrInfoDto = new MemberInfoDto(currentMbr, children);
        ResultDo<MemberInfoDto> resultDo = ResultDo.build();
        return resultDo.setResult(mbrInfoDto);
    }

}
