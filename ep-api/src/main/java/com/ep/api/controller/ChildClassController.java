package com.ep.api.controller;

import com.ep.domain.enums.ChildClassStatusEnum;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.MemberChildClassBo;
import com.ep.domain.service.MemberChildService;
import com.ep.domain.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    private OrderService orderService;

    @ApiOperation(value = "孩子全部课程分页列表")
    @PostMapping("/page")
    public ResultDo<Page<MemberChildClassBo>> getChildClassPage(@PageableDefault Pageable pageable,
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

}
