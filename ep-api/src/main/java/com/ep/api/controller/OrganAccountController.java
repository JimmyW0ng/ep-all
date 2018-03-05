package com.ep.api.controller;

import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.*;
import com.ep.domain.pojo.dto.OrganClassCatalogCommentDto;
import com.ep.domain.pojo.event.ClassCatalogCommentEventBo;
import com.ep.domain.pojo.po.EpMemberPo;
import com.ep.domain.service.MemberChildService;
import com.ep.domain.service.OrganAccountService;
import com.ep.domain.service.OrganClassCatalogService;
import com.ep.domain.service.OrganClassChildService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @Autowired
    private OrganAccountService organAccountService;
    @Autowired
    private OrganClassCatalogService organClassCatalogService;
    @Autowired
    private OrganClassChildService organClassChildService;
    @Autowired
    private ApplicationEventPublisher publisher;
    @Autowired
    private MemberChildService memberChildService;

    @ApiOperation(value = "机构账户信息")
    @PostMapping("/info")
    @PreAuthorize("hasAnyAuthority('api:base')")
    public ResultDo<OrganAccountBo> getOrganAccountInfo() {
        EpMemberPo memberPo = super.getCurrentUser().get();
        return organAccountService.getOrganAccountInfo(memberPo.getMobile());
    }

    @ApiOperation(value = "今日课时")
    @PostMapping("/class/today")
    @PreAuthorize("hasAnyAuthority('api:base')")
    public ResultDo<List<OrganAccountClassBo>> findTodayClassByOrganAccount() {
        EpMemberPo memberPo = super.getCurrentUser().get();
        return organAccountService.findTodayClassByOrganAccount(memberPo.getMobile());
    }

    @ApiOperation(value = "课时评价初始化")
    @PostMapping("/class/catalog/init")
    @PreAuthorize("hasAnyAuthority('api:base')")
    public ResultDo<OrganClassCatalogCommentDto> getClassCatalogCommentView(@RequestParam("classCatalogId") Long classCatalogId) {
        EpMemberPo memberPo = super.getCurrentUser().get();
        return organClassCatalogService.getClassCatalogCommentView(memberPo.getMobile(), classCatalogId);
    }

    @ApiOperation(value = "课时评价")
    @PostMapping("/class/catalog/comment")
    @PreAuthorize("hasAnyAuthority('api:base')")
    public ResultDo doClassCatalogComment(@RequestParam("classCatalogId") Long classCatalogId,
                                          @RequestParam("childId") Long childId,
                                          @RequestParam("tagIds") List<Long> tagIds,
                                          @RequestParam("comment") String comment) {
        EpMemberPo memberPo = super.getCurrentUser().get();
        ResultDo resultDo = organClassCatalogService.doClassCatalogComment(memberPo.getMobile(),
                classCatalogId,
                childId,
                tagIds,
                comment);
        if (resultDo.isSuccess()) {
            ClassCatalogCommentEventBo eventBo = new ClassCatalogCommentEventBo(classCatalogId, childId, tagIds, comment);
            publisher.publishEvent(eventBo);
        }
        return resultDo;
    }

    @ApiOperation(value = "全部课程-分页")
    @PostMapping("/class/all")
    @PreAuthorize("hasAnyAuthority('api:base')")
    public ResultDo<Page<OrganAccountClassBo>> findAllClassByOrganAccountForPage(@PageableDefault Pageable pageable) {
        EpMemberPo memberPo = super.getCurrentUser().get();
        return organAccountService.findAllClassByOrganAccountForPage(pageable, memberPo.getMobile());
    }

    @ApiOperation(value = "查看班次全部课时")
    @PostMapping("/class/catalog/all")
    @PreAuthorize("hasAnyAuthority('api:base')")
    public ResultDo<List<OrganClassCatalogBo>> getClassAllCatalog(@RequestParam("classId") Long classId) {
        EpMemberPo memberPo = super.getCurrentUser().get();
        return organClassCatalogService.getClassAllCatalog(classId, memberPo.getMobile());
    }

    @ApiOperation(value = "查看班次学员")
    @PostMapping("/class/all")
    @PreAuthorize("hasAnyAuthority('api:base')")
    public ResultDo<List<MemberChildBo>> findChildrenByClassId(@RequestParam("classId") Long classId) {
        EpMemberPo memberPo = super.getCurrentUser().get();
        return organClassChildService.findChildrenByClassId(classId, memberPo.getMobile());
    }

    @ApiOperation(value = "查看班次孩子摘要信息")
    @PostMapping("/class/child/abstract")
    @PreAuthorize("hasAnyAuthority('api:base')")
    public ResultDo<ClassChildAbstractBo> getClassChildAbstract(@RequestParam("classId") Long classId,
                                                                @RequestParam("childId") Long childId) {
        EpMemberPo memberPo = super.getCurrentUser().get();
        return memberChildService.getClassChildAbstract(classId, childId, memberPo.getMobile());
    }

}
