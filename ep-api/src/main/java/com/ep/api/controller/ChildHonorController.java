package com.ep.api.controller;

import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.MemberChildHonorBo;
import com.ep.domain.service.MemberChildHonorService;
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

import java.util.List;

/**
 * @Description: 孩子荣誉api控制类
 * @Author: J.W
 * @Date: 下午8:26 2018/1/6
 */
@Slf4j
@RequestMapping("auth/child/honor")
@RestController
@Api(value = "api-auth-child-honor", description = "孩子荣誉接口")
public class ChildHonorController extends ApiController {

    @Autowired
    private MemberChildService memberChildService;
    @Autowired
    private MemberChildHonorService memberChildHonorService;

    @ApiOperation(value = "查询孩子获得的最新荣誉-分页")
    @PostMapping("/class")
    @PreAuthorize("hasAnyAuthority('api:base')")
    public ResultDo<List<MemberChildHonorBo>> queryRecentForPage(@RequestParam("childId") Long childId,
                                                                 @RequestParam("classId") Long classId) {
        Long memberId = super.getCurrentUser().get().getId();
        ResultDo checkedChild = memberChildService.getCheckedMemberChild(memberId, childId);
        if (checkedChild.isError()) {
            ResultDo<List<MemberChildHonorBo>> resultDo = ResultDo.build();
            return resultDo.setError(checkedChild.getError());
        }
        return memberChildHonorService.queryByClassId(childId, classId);
    }

    @ApiOperation(value = "查询孩子获得的最新荣誉-分页")
    @PostMapping("/recent/page")
    @PreAuthorize("hasAnyAuthority('api:base')")
    public ResultDo<Page<MemberChildHonorBo>> queryRecentForPage(@PageableDefault Pageable pageable, @RequestParam("childId") Long childId) {
        Long memberId = super.getCurrentUser().get().getId();
        ResultDo checkedChild = memberChildService.getCheckedMemberChild(memberId, childId);
        if (checkedChild.isError()) {
            ResultDo<Page<MemberChildHonorBo>> resultDo = ResultDo.build();
            return resultDo.setError(checkedChild.getError());
        }
        return memberChildHonorService.queryRecentForPage(pageable, childId);
    }

}
