package com.ep.api.controller;

import com.ep.common.tool.NumberTools;
import com.ep.common.tool.StringTools;
import com.ep.domain.constant.BizConstant;
import com.ep.domain.constant.MessageCode;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.OrganBo;
import com.ep.domain.pojo.dto.OrganInfoDto;
import com.ep.domain.service.OrganService;
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
 * @Description: 机构api控制类
 * @Author: J.W
 * @Date: 下午8:26 2018/1/6
 */
@Slf4j
@RequestMapping("security/organ")
@RestController
@Api(value = "api-security-organ", description = "机构开放接口")
public class OrganController extends ApiController {

    @Autowired
    private OrganService organService;

    @ApiOperation(value = "机构详情")
    @PostMapping("/detail")
    public ResultDo<OrganInfoDto> getOgnInfo(@RequestParam("ognId") Long ognId) {
        return organService.getOgnDetail(ognId);
    }

    @ApiOperation(value = "根据场景值获取机构详情")
    @PostMapping("/scene/detail")
    public ResultDo<OrganInfoDto> getOgnInfoByScene(@RequestParam("scene") String scene) {
        String[] sceneArr = scene.split(BizConstant.WECHAT_SCENE_SPLIT);
        if (!NumberTools.isLongToString(sceneArr[BizConstant.DB_NUM_ZERO])) {
            return ResultDo.build(MessageCode.ERROR_SYSTEM_PARAM_FORMAT);
        }
        return organService.getOgnDetail(Long.valueOf(sceneArr[BizConstant.DB_NUM_ZERO]));
    }

    @ApiOperation(value = "机构分页列表")
    @PostMapping("/page")
    public ResultDo<Page<OrganBo>> queryOrganForPage(@PageableDefault Pageable pageable) {
        return organService.queryOrganForPage(pageable);
    }

    @ApiOperation(value = "根据场景值获取机构分页列表")
    @PostMapping("/scene/page")
    public ResultDo<Page<OrganBo>> queryOrganBySceneForPage(@RequestParam(value = "scene", required = false) String scene, @PageableDefault Pageable pageable) {
        if (StringTools.isBlank(scene)) {
            return organService.queryOrganForPage(pageable);
        }
        String[] sceneArr = scene.split(BizConstant.WECHAT_SCENE_SPLIT);
        if (!NumberTools.isLongToString(sceneArr[BizConstant.DB_NUM_ZERO])) {
            return ResultDo.build(MessageCode.ERROR_SYSTEM_PARAM_FORMAT);
        }
        return organService.queryOrganBySceneForPage(Long.valueOf(sceneArr[BizConstant.DB_NUM_ZERO]), pageable);
    }

}
