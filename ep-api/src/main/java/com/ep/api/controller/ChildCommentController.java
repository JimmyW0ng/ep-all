package com.ep.api.controller;

import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.MemberChildCommentBo;
import com.ep.domain.service.MemberChildCommentService;
import com.ep.domain.service.MemberChildService;
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
 * @Description: 孩子评价api控制类
 * @Author: J.W
 * @Date: 下午8:26 2018/1/6
 */
@Slf4j
@RequestMapping("auth/child/comment")
@RestController
@Api(value = "api-auth-child-comment", description = "孩子评价接口")
public class ChildCommentController extends ApiController {

    @Autowired
    private MemberChildService memberChildService;
    @Autowired
    private MemberChildCommentService memberChildCommentService;

    @ApiOperation(value = "查询孩子获得的最新评价-分页")
    @PostMapping("/recent/page")
    @PreAuthorize("hasAnyAuthority('api:base')")
    public ResultDo<Page<MemberChildCommentBo>> queryRecentForPage(@PageableDefault Pageable pageable, @RequestParam("childId") Long childId) {
        Long memberId = super.getCurrentUser().get().getId();
        ResultDo checkedChild = memberChildService.getCheckedMemberChild(memberId, childId);
        if (checkedChild.isError()) {
            ResultDo<Page<MemberChildCommentBo>> resultDo = ResultDo.build();
            return resultDo.setError(checkedChild.getError());
        }
        return memberChildCommentService.queryRecentForPage(pageable, childId);
    }

}
