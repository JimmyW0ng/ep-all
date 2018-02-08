package com.ep.api.controller;

import com.ep.domain.constant.BizConstant;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.dto.FileDto;
import com.ep.domain.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @Description: 文件api控制类
 * @Author: J.W
 * @Date: 上午11:21 2018/2/8
 */
@Slf4j
@RequestMapping("auth/file")
@RestController
@Api(value = "api-auth-file", description = "文件接口")
public class FileController extends ApiController {

    @Autowired
    private FileService fileService;

    @ApiOperation(value = "上传孩子头像")
    @PostMapping("/upload/child/avatar")
    @PreAuthorize("hasAnyAuthority('api:base')")
    public ResultDo<FileDto> uploadChildAvatar(@RequestParam(value = "file") MultipartFile file) throws IOException {
        return fileService.addFileByBizType(file.getOriginalFilename(),
                file.getBytes(),
                BizConstant.FILE_BIZ_TYPE_CODE_CHILD_AVATAR,
                BizConstant.DB_NUM_ONE);
    }

}
