package com.ep.api.controller;

import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.OrganAccountBo;
import com.ep.domain.pojo.bo.OrganAccountClassBo;
import com.ep.domain.pojo.dto.OrganClassCatalogCommentDto;
import com.ep.domain.pojo.event.ClassCatalogCommentEventBo;
import com.ep.domain.pojo.po.EpMemberPo;
import com.ep.domain.service.OrganAccountService;
import com.ep.domain.service.OrganClassCatalogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
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
    private ApplicationEventPublisher publisher;

    @ApiOperation(value = "机构账户信息")
    @PostMapping("/info")
    public ResultDo<OrganAccountBo> getOrganAccountInfo() {
        EpMemberPo memberPo = super.getCurrentUser().get();
        return organAccountService.getOrganAccountInfo(memberPo.getMobile());
    }

    @ApiOperation(value = "今日课时")
    @PostMapping("/today/class")
    public ResultDo<List<OrganAccountClassBo>> findClassByOrganAccount() {
        EpMemberPo memberPo = super.getCurrentUser().get();
        return organAccountService.findClassByOrganAccount(memberPo.getMobile());
    }

    @ApiOperation(value = "课时评价初始化")
    @PostMapping("/class/catalog/init")
    public ResultDo<OrganClassCatalogCommentDto> getClassCatalogCommentView(@RequestParam("classCatalogId") Long classCatalogId) {
        EpMemberPo memberPo = super.getCurrentUser().get();
        return organClassCatalogService.getClassCatalogCommentView(memberPo.getMobile(), classCatalogId);
    }

    @ApiOperation(value = "课时评价")
    @PostMapping("/class/catalog/comment")
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

}
