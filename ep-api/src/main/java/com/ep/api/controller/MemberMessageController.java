package com.ep.api.controller;

import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.MemberMessageBo;
import com.ep.domain.pojo.po.EpMemberChildPo;
import com.ep.domain.repository.domain.enums.EpMemberMessageType;
import com.ep.domain.service.MemberChildService;
import com.ep.domain.service.MemberMessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: 会员消息api控制类
 * @Author: J.W
 * @Date: 下午8:26 2018/1/6
 */
@Slf4j
@RequestMapping("auth/member/message")
@RestController
@Api(value = "api-auth-member-message", description = "会员消息接口")
public class MemberMessageController extends ApiController {

    @Autowired
    private MemberChildService memberChildService;
    @Autowired
    private MemberMessageService memberMessageService;

    @ApiOperation(value = "孩子评价类消息未读数")
    @PostMapping("/comment/unread/num")
    @PreAuthorize("hasAnyAuthority('api:base')")
    public ResultDo<Integer> getClassCommentUnreadNum(@RequestParam("childId") Long childId) {
        ResultDo<Integer> resultDo = ResultDo.build();
        Long memberId = super.getCurrentUser().get().getId();
        ResultDo<EpMemberChildPo> checkedChild = memberChildService.getCheckedMemberChild(memberId, childId);
        if (checkedChild.isError()) {
            return resultDo.setError(checkedChild.getError());
        }
        return memberMessageService.getUnreadNumByChildId(childId, EpMemberMessageType.class_catalog_comment);
    }

    @ApiOperation(value = "孩子评价类消息-分页")
    @PostMapping("/comment/page")
    @PreAuthorize("hasAnyAuthority('api:base')")
    public ResultDo<Page<MemberMessageBo>> getClassCommentForPage(@PageableDefault Pageable pageable,
                                                                  @RequestParam("childId") Long childId) {
        ResultDo<Page<MemberMessageBo>> resultDo = ResultDo.build();
        Long memberId = super.getCurrentUser().get().getId();
        ResultDo<EpMemberChildPo> checkedChild = memberChildService.getCheckedMemberChild(memberId, childId);
        if (checkedChild.isError()) {
            return resultDo.setError(checkedChild.getError());
        }
        return memberMessageService.findClassCatalogCommentByChildIdForPage(pageable, childId);
    }

}
