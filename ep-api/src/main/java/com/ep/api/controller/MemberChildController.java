package com.ep.api.controller;

import com.ep.common.tool.DateTools;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.po.EpMemberChildPo;
import com.ep.domain.pojo.po.EpMemberPo;
import com.ep.domain.repository.domain.enums.EpMemberChildChildSex;
import com.ep.domain.service.MemberChildService;
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
public class MemberChildController extends ApiController {

    @Autowired
    private MemberChildService memberChildService;

    /**
     * 获取当前用户所有孩子
     *
     * @return
     */
    @PostMapping("/get/all")
    @PreAuthorize("hasAnyAuthority('api:base')")
    public ResultDo<List<EpMemberChildPo>> children() {
        EpMemberPo currentMbr = super.getCurrentUser().get();
        List<EpMemberChildPo> children = memberChildService.getChildrenByMemberId(currentMbr.getId());
        ResultDo<List<EpMemberChildPo>> resultDo = ResultDo.build();
        return resultDo.setResult(children);
    }

    /**
     * 新增孩子档案
     *
     * @return
     */
    @PostMapping("/add")
    @PreAuthorize("hasAnyAuthority('api:base')")
    public ResultDo addChild(@RequestParam(value = "childNickName", required = false) String childNickName,
                             @RequestParam("childTrueName") String childTrueName,
                             @RequestParam("childSex") EpMemberChildChildSex childSex,
                             @RequestParam("childBirthday") String childBirthday,
                             @RequestParam(value = "childIdentity", required = false) String childIdentity,
                             @RequestParam(value = "currentSchool", required = false) String currentSchool,
                             @RequestParam(value = "currentClass", required = false) String currentClass
    ) {
        EpMemberPo currentMbr = super.getCurrentUser().get();
        Date birthday = DateTools.stringToDate(childBirthday, DateTools.DATE_FMT_3);
        return memberChildService.addChild(currentMbr.getId(), childNickName, childTrueName, childSex, birthday, childIdentity, currentSchool, currentClass);
    }

    /**
     * 编辑孩子档案
     *
     * @return
     */
    @PostMapping("/edit")
    @PreAuthorize("hasAnyAuthority('api:base')")
    public ResultDo editChild(@RequestParam("childId") Long childId,
                              @RequestParam(value = "childNickName", required = false) String childNickName,
                              @RequestParam("childTrueName") String childTrueName,
                              @RequestParam("childSex") EpMemberChildChildSex childSex,
                              @RequestParam("childBirthday") String childBirthday,
                              @RequestParam(value = "childIdentity", required = false) String childIdentity,
                              @RequestParam(value = "currentSchool", required = false) String currentSchool,
                              @RequestParam(value = "currentClass", required = false) String currentClass
    ) {
        EpMemberPo currentMbr = super.getCurrentUser().get();
        Date birthday = DateTools.stringToDate(childBirthday, DateTools.DATE_FMT_3);
        return memberChildService.editChild(currentMbr.getId(), childId, childNickName, childTrueName, childSex, birthday, childIdentity, currentSchool, currentClass);
    }

    /**
     * 查看孩子档案
     *
     * @return
     */
    @PostMapping("/get")
    @PreAuthorize("hasAnyAuthority('api:base')")
    public ResultDo<EpMemberChildPo> getChild(@RequestParam("childId") Long childId) {
        return memberChildService.getById(childId);
    }

    /**
     * 删除孩子档案
     *
     * @return
     */
    @PostMapping("/del")
    @PreAuthorize("hasAnyAuthority('api:base')")
    public ResultDo delChild(@RequestParam("childId") Long childId) {
        memberChildService.delChild(childId);
        return ResultDo.build();
    }

}
