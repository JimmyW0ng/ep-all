package com.ep.api.controller;

import com.ep.common.tool.NumberTools;
import com.ep.domain.constant.BizConstant;
import com.ep.domain.constant.MessageCode;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.OrganClassCommentBo;
import com.ep.domain.pojo.bo.OrganCourseBo;
import com.ep.domain.pojo.dto.OrganCourseDto;
import com.ep.domain.service.OrganClassCommentService;
import com.ep.domain.service.OrganCourseService;
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
 * @Description: 机构课程api控制类
 * @Author: J.W
 * @Date: 下午7:04 2018/1/15
 */
@Slf4j
@RequestMapping("security/course")
@RestController
@Api(value = "api-security-course", description = "机构课程开放接口")
public class OrganCourseController extends ApiController {

    @Autowired
    private OrganCourseService organCourseService;
    @Autowired
    private OrganClassCommentService organClassCommentService;

    @ApiOperation(value = "课程分页列表")
    @PostMapping("/page")
    public ResultDo<Page<OrganCourseBo>> findCourseForPage(@PageableDefault Pageable pageable,
                                                           @RequestParam("ognId") Long ognId) {
        Page<OrganCourseBo> data = organCourseService.findCourseByOgnIdForPage(pageable, ognId);
        ResultDo<Page<OrganCourseBo>> resultDo = ResultDo.build();
        return resultDo.setResult(data);
    }

    @ApiOperation(value = "课程详情")
    @PostMapping("/detail")
    public ResultDo<OrganCourseDto> getCourseInfo(@RequestParam("courseId") Long courseId) {
        return organCourseService.getCourseDetail(courseId);
    }

    @ApiOperation(value = "根据场景值获取课程详情")
    @PostMapping("/scene/detail")
    public ResultDo<OrganCourseDto> getCourseInfoByScene(@RequestParam("scene") String scene) {
        String[] sceneArr = scene.split(BizConstant.WECHAT_SCENE_SPLIT);
        if (sceneArr.length < BizConstant.DB_NUM_TWO || !NumberTools.isLongToString(sceneArr[BizConstant.DB_NUM_ONE])) {
            return ResultDo.build(MessageCode.ERROR_SYSTEM_PARAM_FORMAT);
        }
        return organCourseService.getCourseDetail(Long.valueOf(sceneArr[BizConstant.DB_NUM_ONE]));
    }

    @ApiOperation(value = "分页查询课程全部评论")
    @PostMapping("/comment/page")
    public ResultDo<Page<OrganClassCommentBo>> findCourseCommentForPage(@PageableDefault Pageable pageable,
                                                                        @RequestParam("courseId") Long courseId) {
        return organClassCommentService.findCourseCommentForPage(pageable, courseId);
    }

}
