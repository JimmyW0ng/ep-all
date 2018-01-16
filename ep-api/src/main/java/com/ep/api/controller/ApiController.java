package com.ep.api.controller;

import com.ep.domain.constant.BizConstant;
import com.ep.domain.pojo.po.EpMemberPo;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

import static com.ep.common.tool.WebTools.getCurrentRequest;

/**
 * @Description:Api控制基类
 * @Author: J.W
 * @Date: 下午3:22 2018/1/16
 */
public class ApiController {

    /**
     * 获取当前用户
     *
     * @return
     */
    protected Optional<EpMemberPo> getCurrentUser() {
        HttpServletRequest currentRequest = getCurrentRequest();
        return Optional.ofNullable((EpMemberPo) currentRequest.getAttribute(BizConstant.CURENT_USER));
    }

}
