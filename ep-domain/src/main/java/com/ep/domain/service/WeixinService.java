package com.ep.domain.service;

import com.ep.common.tool.HttpClientTools;
import com.ep.common.tool.WeixinTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Map;

/**
 * @Description:
 * @Author: CC.F
 * @Date: 0:20 2018/4/23
 */
@Slf4j
@Service
public class WeixinService {
    @Value("${weixin4j.oauth.appid}")
    private String weixin4jOauthAppid;
    @Value("${weixin4j.oauth.secret}")
    private String weixin4jOauthSecret;

    public void msgCustomSend() {
    }

    public String getAccessToken() throws Exception {
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential" +
                "&appid=" + weixin4jOauthAppid + "&secret=" + weixin4jOauthSecret;
        InputStream ins = HttpClientTools.doGet(url).getContent();
        Map<String, String> resultMap = WeixinTools.inputStreamToMap(ins);

        return "";
    }
}
