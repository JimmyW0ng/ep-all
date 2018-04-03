package com.ep.backend.job;

import com.ep.common.tool.DateTools;
import com.ep.common.tool.StringTools;
import com.ep.domain.constant.BizConstant;
import com.ep.domain.pojo.po.EpSystemDictPo;
import com.ep.domain.pojo.wechat.WechatAccessTokenBo;
import com.ep.domain.repository.SystemDictRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @Description: 微信定时任务
 * @Author J.W
 * @Date: 下午 5:08 2018/4/2 0002
 */
@Slf4j
@Component
public class WechatJob {

    @Value("${wechat.appid}")
    private String appId;
    @Value("${wechat.secret}")
    private String secret;

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private SystemDictRepository systemDictRepository;

    /**
     * 定时获取微信ACCESS_TOKEN
     */
    @Scheduled(fixedRate = 10 * 60 * 1000)
    public void getWechatAccessToken() {
        log.info("定时获取微信ACCESS_TOKEN...start");
        EpSystemDictPo sysDictPojo = systemDictRepository.findByGroupNameAndKey(BizConstant.DICT_GROUP_WECHAT, BizConstant.DICT_KEY_WECHAT_ACCESS_TOKEN);
        if (DateTools.addMinute(DateTools.getCurrentDate(), BizConstant.WECHAT_SESSION_TIME_OUT_M).before(sysDictPojo.getUpdateAt())) {
            return;
        }
        String url = String.format(BizConstant.WECHAT_URL_ACCESS_TOKEN, appId, secret);
        ResponseEntity<WechatAccessTokenBo> entity = restTemplate.getForEntity(url, WechatAccessTokenBo.class);
        log.info("定时获取微信ACCESS_TOKEN...返回：{}", entity);
        if (HttpStatus.OK.equals(entity.getStatusCode())) {
            WechatAccessTokenBo accessTokenPojo = entity.getBody();
            if (StringTools.isNotBlank(accessTokenPojo.getAccess_token())) {
                log.info("刷新微信ACCESS_TOKEN...accessToken={}", accessTokenPojo.getAccess_token());
                systemDictRepository.updateByGroupName(BizConstant.DICT_GROUP_WECHAT, BizConstant.DICT_KEY_WECHAT_ACCESS_TOKEN, accessTokenPojo.getAccess_token());
            }
        } else {
            log.error("刷新微信ACCESS_TOKEN失败！");
        }
    }
}
