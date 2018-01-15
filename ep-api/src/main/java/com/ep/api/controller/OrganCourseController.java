package com.ep.api.controller;

import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.dto.OrganCourseDto;
import com.ep.domain.service.OrganCourseService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:机构课程api控制类
 * @Author: J.W
 * @Date: 下午7:04 2018/1/15
 */
@Slf4j
@RequestMapping("security/course")
@RestController
public class OrganCourseController {

    @Autowired
    private OrganCourseService organCourseService;

    /**
     * 课程详情
     */
    @ApiOperation(value = "课程详情")
    @PostMapping("/detail")
    public ResultDo<OrganCourseDto> getCourseInfo(@RequestParam("courseId") Long courseId) {
        return organCourseService.getCourseDetail(courseId);
    }

}
