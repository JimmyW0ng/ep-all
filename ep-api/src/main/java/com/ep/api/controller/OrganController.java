package com.ep.api.controller;

import com.ep.domain.pojo.ResultDo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: 机构api控制类
 * @Author: J.W
 * @Date: 下午8:26 2018/1/6
 */
@Slf4j
@RequestMapping("org")
@RestController
public class OrganController {

    @GetMapping("detail")
    public ResultDo detail(@RequestParam("orgId") Long organId) {
        return ResultDo.build();
    }

}
