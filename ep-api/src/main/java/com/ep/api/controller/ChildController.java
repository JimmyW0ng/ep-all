package com.ep.api.controller;

import com.ep.common.tool.DateTools;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.MemberChildBo;
import com.ep.domain.pojo.po.EpMemberPo;
import com.ep.domain.repository.domain.enums.EpMemberChildChildSex;
import com.ep.domain.service.MemberChildService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * @Description: 孩子api控制类
 * @Author: J.W
 * @Date: 下午8:26 2018/1/6
 */
@Slf4j
@RequestMapping("auth/child")
@RestController
@Api(value = "api-auth-child", description = "孩子接口")
public class ChildController extends ApiController {

    @Autowired
    private MemberChildService memberChildService;

    @ApiOperation(value = "获取当前用户孩子列表")
    @PostMapping("/list")
    @PreAuthorize("hasAnyAuthority('api:base')")
    public ResultDo<List<MemberChildBo>> children() {
        EpMemberPo currentMbr = super.getCurrentUser().get();
        List<MemberChildBo> children = memberChildService.queryAllByMemberId(currentMbr.getId());
        ResultDo<List<MemberChildBo>> resultDo = ResultDo.build();
        return resultDo.setResult(children);
    }

    @ApiOperation(value = "新增孩子档案")
    @PostMapping("/add")
    @PreAuthorize("hasAnyAuthority('api:base')")
    public ResultDo addChild(@RequestParam(value = "childNickName") String childNickName,
                             @RequestParam(value = "childTrueName", required = false) String childTrueName,
                             @RequestParam("childSex") EpMemberChildChildSex childSex,
                             @RequestParam("childBirthday") String childBirthday,
                             @RequestParam(value = "childIdentity", required = false) String childIdentity,
                             @RequestParam(value = "currentSchool", required = false) String currentSchool,
                             @RequestParam(value = "currentClass", required = false) String currentClass,
                             @RequestParam(value = "avatarCode", required = false) String avatar,
                             @RequestParam(value = "sign", required = false) String sign
    ) {
        EpMemberPo currentMbr = super.getCurrentUser().get();
        Date birthday = DateTools.stringToDate(childBirthday, DateTools.DATE_FMT_3);
        return memberChildService.addChild(currentMbr.getId(), childNickName, childTrueName, childSex, birthday, childIdentity, currentSchool, currentClass, avatar, sign);
    }

    @ApiOperation(value = "编辑孩子档案")
    @PostMapping("/edit")
    @PreAuthorize("hasAnyAuthority('api:base')")
    public ResultDo editChild(@RequestParam("childId") Long childId,
                              @RequestParam(value = "childNickName", required = false) String childNickName,
                              @RequestParam("childTrueName") String childTrueName,
                              @RequestParam("childSex") EpMemberChildChildSex childSex,
                              @RequestParam("childBirthday") String childBirthday,
                              @RequestParam(value = "childIdentity", required = false) String childIdentity,
                              @RequestParam(value = "currentSchool", required = false) String currentSchool,
                              @RequestParam(value = "currentClass", required = false) String currentClass,
                              @RequestParam(value = "avatarCode", required = false) String avatar,
                              @RequestParam(value = "sign", required = false) String sign
    ) {
        EpMemberPo currentMbr = super.getCurrentUser().get();
        Date birthday = DateTools.stringToDate(childBirthday, DateTools.DATE_FMT_3);
        return memberChildService.editChild(currentMbr.getId(), childId, childNickName, childTrueName, childSex, birthday, childIdentity, currentSchool, currentClass, avatar, sign);
    }

    @ApiOperation(value = "查看孩子档案")
    @PostMapping("/get")
    @PreAuthorize("hasAnyAuthority('api:base')")
    public ResultDo<MemberChildBo> getChild(@RequestParam("childId") Long childId) {
        EpMemberPo currentMbr = super.getCurrentUser().get();
        return memberChildService.getAllByMemberIdAndChildId(currentMbr.getId(), childId);
    }

    @ApiOperation(value = "删除孩子档案")
    @PostMapping("/del")
    @PreAuthorize("hasAnyAuthority('api:base')")
    public ResultDo delChild(@RequestParam("childId") Long childId) {
        EpMemberPo currentMbr = super.getCurrentUser().get();
        return memberChildService.delChild(currentMbr.getId(), childId);
    }

}
