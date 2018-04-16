package com.ep.api.controller;

import com.ep.domain.enums.ChildClassStatusEnum;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.MemberChildClassBo;
import com.ep.domain.pojo.bo.MemberChildScheduleBo;
import com.ep.domain.pojo.dto.OrganClassCatalogDetailDto;
import com.ep.domain.service.*;
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

import java.util.List;

/**
 * @Description: 孩子课程班次控制类
 * @Author: J.W
 * @Date: 下午8:26 2018/1/6
 */
@Slf4j
@RequestMapping("auth/child/class")
@RestController
@Api(value = "api-auth-child-class", description = "孩子课程班次接口")
public class ChildClassController extends ApiController {

    @Autowired
    private MemberChildService memberChildService;
    @Autowired
    private MemberChildCommentService memberChildCommentService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrganClassCommentService organClassCommentService;
    @Autowired
    private OrganClassScheduleService organClassScheduleService;

    @ApiOperation(value = "孩子全部课程分页列表")
    @PostMapping("/page")
    @PreAuthorize("hasAnyAuthority('api:base')")
    public ResultDo<Page<MemberChildClassBo>> findChildClassPage(@PageableDefault Pageable pageable,
                                                                 @RequestParam("childId") Long childId,
                                                                 @RequestParam("status") ChildClassStatusEnum statusEnum) {
        ResultDo<Page<MemberChildClassBo>> resultDo = ResultDo.build();
        Long memberId = super.getCurrentUser().get().getId();
        ResultDo checkedChild = memberChildService.getCheckedMemberChild(memberId, childId);
        if (checkedChild.isError()) {
            return resultDo.setError(checkedChild.getError());
        }
        Page<MemberChildClassBo> data = orderService.findChildClassPage(pageable, childId, statusEnum);
        return resultDo.setResult(data);
    }

    @ApiOperation(value = "孩子行程")
    @PostMapping("/schedule")
    @PreAuthorize("hasAnyAuthority('api:base')")
    public ResultDo<Page<MemberChildScheduleBo>> findChildSchedulePage(@PageableDefault Pageable pageable) {
        Long memberId = super.getCurrentUser().get().getId();
        return orderService.findChildSchedulePage(pageable, memberId);
    }

    @ApiOperation(value = "班次评价")
    @PostMapping("/add/comment")
    @PreAuthorize("hasAnyAuthority('api:base')")
    public ResultDo addClassComment(@RequestParam("orderId") Long orderId,
                                    @RequestParam("score") Byte score,
                                    @RequestParam(value = "content", required = false) String content,
                                    @RequestParam(value = "pic", required = false) List<String> picList) {
        Long memberId = super.getCurrentUser().get().getId();
        return organClassCommentService.addClassComment(memberId, orderId, score, content, picList);
    }

    @ApiOperation(value = "班次老师评价信息")
    @PostMapping("/catalog/detail")
    @PreAuthorize("hasAnyAuthority('api:base')")
    public ResultDo<OrganClassCatalogDetailDto> getCatalogDetail(@RequestParam("orderId") Long orderId) {
        Long memberId = super.getCurrentUser().get().getId();
        return organClassScheduleService.getCatalogDetail(memberId, orderId);
    }

    @ApiOperation(value = "班次老师评价回复")
    @PostMapping("/catalog/detail/replay")
    @PreAuthorize("hasAnyAuthority('api:base')")
    public ResultDo replayComment(@RequestParam("commentId") Long commentId,
                                  @RequestParam("content") String content) {
        Long memberId = super.getCurrentUser().get().getId();
        return memberChildCommentService.replayComment(memberId, commentId, content);
    }

}
