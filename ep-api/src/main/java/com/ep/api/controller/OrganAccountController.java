package com.ep.api.controller;

import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.*;
import com.ep.domain.pojo.dto.OrganClassCatalogCommentDto;
import com.ep.domain.pojo.event.ClassCatalogCommentEventBo;
import com.ep.domain.pojo.po.EpOrganAccountPo;
import com.ep.domain.service.*;
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
    private OrganClassScheduleService organClassScheduleService;
    @Autowired
    private OrganClassChildService organClassChildService;
    @Autowired
    private MemberChildService memberChildService;
    @Autowired
    private ApplicationEventPublisher publisher;

    @ApiOperation(value = "机构账户信息")
    @PostMapping("/info")
    @PreAuthorize("hasAnyAuthority('api:base')")
    public ResultDo<OrganAccountBo> getOrganAccountInfo() {
        EpOrganAccountPo organAccountPo = super.getCurrentOrganAccount().get();
        return organAccountService.getOrganAccountInfo(organAccountPo);
    }

    @ApiOperation(value = "今日课时")
    @PostMapping("/class/today")
    @PreAuthorize("hasAnyAuthority('api:base')")
    public ResultDo<List<OrganAccountClassBo>> findTodayClassByOrganAccount() {
        EpOrganAccountPo organAccountPo = super.getCurrentOrganAccount().get();
        return organAccountService.findTodayClassByOrganAccount(organAccountPo);
    }

    @ApiOperation(value = "课时评价初始化")
    @PostMapping("/class/catalog/init")
    @PreAuthorize("hasAnyAuthority('api:base')")
    public ResultDo<OrganClassCatalogCommentDto> getClassCatalogCommentView(
            @RequestParam("classId") Long classId,
            @RequestParam("startTimeStamp") Long startTimeStamp) {
        EpOrganAccountPo organAccountPo = super.getCurrentOrganAccount().get();
        return organClassScheduleService.getClassCatalogCommentView(organAccountPo, classId, startTimeStamp);
    }

    @ApiOperation(value = "课时评价")
    @PostMapping("/class/catalog/comment")
    @PreAuthorize("hasAnyAuthority('api:base')")
    public ResultDo doClassCatalogComment(@RequestParam("classScheduleId") Long classScheduleId,
                                          @RequestParam("tagIds") List<Long> tagIds,
                                          @RequestParam("comment") String comment) {
        EpOrganAccountPo organAccountPo = super.getCurrentOrganAccount().get();
        ResultDo resultDo = organClassScheduleService.doClassCatalogComment(organAccountPo,
                classScheduleId,
                tagIds,
                comment);
        if (resultDo.isSuccess()) {
            ClassCatalogCommentEventBo eventBo = new ClassCatalogCommentEventBo(classScheduleId, tagIds, comment);
            publisher.publishEvent(eventBo);
        }
        return resultDo;
    }

    @ApiOperation(value = "课时评价(撤销)")
    @PostMapping("/class/catalog/comment/cancel")
    @PreAuthorize("hasAnyAuthority('api:base')")
    public ResultDo doClassCatalogComment(@RequestParam("classScheduleId") Long classScheduleId) {
        EpOrganAccountPo organAccountPo = super.getCurrentOrganAccount().get();
        return organClassScheduleService.cancelClassCatalogComment(organAccountPo, classScheduleId);
    }

    @ApiOperation(value = "全部课程-分页")
    @PostMapping("/class/all")
    @PreAuthorize("hasAnyAuthority('api:base')")
    public ResultDo<Page<OrganAccountAllClassBo>> findAllClassByOrganAccountForPage(@PageableDefault Pageable pageable) {
        EpOrganAccountPo organAccountPo = super.getCurrentOrganAccount().get();
        return organAccountService.findAllClassByOrganAccountForPage(pageable, organAccountPo);
    }

    @ApiOperation(value = "查看正常（固定课时）班次全部行程")
    @PostMapping("/normal/class/catalog/all")
    @PreAuthorize("hasAnyAuthority('api:base')")
    public ResultDo<List<ClassNormalAllScheduleBo>> getNomalClassAllCatalog(@RequestParam("classId") Long classId) {
        EpOrganAccountPo organAccountPo = super.getCurrentOrganAccount().get();
        return organClassCatalogService.getNomalClassAllCatalog(classId, organAccountPo);
    }

    @ApiOperation(value = "查看班次学员")
    @PostMapping("/class/child/all")
    @PreAuthorize("hasAnyAuthority('api:base')")
    public ResultDo<List<MemberChildBo>> findChildrenByClassId(@RequestParam("classId") Long classId) {
        EpOrganAccountPo organAccountPo = super.getCurrentOrganAccount().get();
        return organClassChildService.findChildrenByClassId(classId, organAccountPo);
    }

    @ApiOperation(value = "查看班次孩子摘要信息")
    @PostMapping("/class/child/abstract")
    @PreAuthorize("hasAnyAuthority('api:base')")
    public ResultDo<ClassChildAbstractBo> getClassChildAbstract(@RequestParam("classId") Long classId,
                                                                @RequestParam("childId") Long childId) {
        EpOrganAccountPo organAccountPo = super.getCurrentOrganAccount().get();
        return memberChildService.getClassChildAbstract(classId, childId, organAccountPo);
    }

}
