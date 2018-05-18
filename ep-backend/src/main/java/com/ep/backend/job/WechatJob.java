package com.ep.backend.job;

import com.ep.common.tool.DateTools;
import com.ep.common.tool.StringTools;
import com.ep.domain.component.WechatPayComponent;
import com.ep.domain.constant.BizConstant;
import com.ep.domain.pojo.ResultDo;
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

import java.util.Date;

/**
 * @Description: 微信定时任务
 * @Author J.W
 * @Date: 下午 5:08 2018/4/2 0002
 */
@Slf4j
@Component
public class WechatJob {

    @Value("${wechat.xcx.member.appid}")
    private String xcxMemberAppId;
    @Value("${wechat.xcx.member.secret}")
    private String xcxMemberSecret;

    @Value("${wechat.fwh.appid}")
    private String fwhAppId;
    @Value("${wechat.fwh.secret}")
    private String fwhSecret;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private SystemDictRepository systemDictRepository;
    @Autowired
    private WechatPayComponent wechatPayComponent;

    /**
     * 定时获取微信小程序（客户端）ACCESS_TOKEN
     */
    @Scheduled(fixedRate = 10 * 60 * 1000)
    public void getWechatAccessToken() {
        log.info("定时获取微信ACCESS_TOKEN...start");
        EpSystemDictPo sysDictPojo = systemDictRepository.findByGroupNameAndKey(BizConstant.DICT_GROUP_WECHAT, BizConstant.DICT_KEY_WECHAT_XCX_MEMBER_ACCESS_TOKEN);
        if (DateTools.addMinute(DateTools.getCurrentDate(), BizConstant.WECHAT_SESSION_TIME_OUT_M).before(sysDictPojo.getUpdateAt())) {
            return;
        }
        String url = String.format(BizConstant.WECHAT_URL_ACCESS_TOKEN, xcxMemberAppId, xcxMemberSecret);
        ResponseEntity<WechatAccessTokenBo> entity = restTemplate.getForEntity(url, WechatAccessTokenBo.class);
        log.info("定时获取微信ACCESS_TOKEN...返回：{}", entity);
        if (HttpStatus.OK.equals(entity.getStatusCode())) {
            WechatAccessTokenBo accessTokenPojo = entity.getBody();
            if (StringTools.isNotBlank(accessTokenPojo.getAccess_token())) {
                log.info("刷新微信ACCESS_TOKEN...accessToken={}", accessTokenPojo.getAccess_token());
                systemDictRepository.updateByGroupName(BizConstant.DICT_GROUP_WECHAT, BizConstant.DICT_KEY_WECHAT_XCX_MEMBER_ACCESS_TOKEN, accessTokenPojo.getAccess_token());
            }
        } else {
            log.error("刷新微信ACCESS_TOKEN失败！");
        }
    }

    /**
     * 定时获取微信服务号ACCESS_TOKEN
     */
    @Scheduled(fixedRate = 10 * 60 * 1000)
    public void getWechatFwhAccessToken() {
        log.info("定时获取微信服务号ACCESS_TOKEN...start");
        EpSystemDictPo sysDictPojo = systemDictRepository.findByGroupNameAndKey(BizConstant.DICT_GROUP_WECHAT, BizConstant.DICT_KEY_WECHAT_FWH_ACCESS_TOKEN);
        if (DateTools.addMinute(DateTools.getCurrentDate(), BizConstant.WECHAT_SESSION_TIME_OUT_M).before(sysDictPojo.getUpdateAt())) {
            return;
        }
        String url = String.format(BizConstant.WECHAT_URL_ACCESS_TOKEN, fwhAppId, fwhSecret);
        ResponseEntity<WechatAccessTokenBo> entity = restTemplate.getForEntity(url, WechatAccessTokenBo.class);
        log.info("定时获取微信服务号ACCESS_TOKEN...返回：{}", entity);
        if (HttpStatus.OK.equals(entity.getStatusCode())) {
            WechatAccessTokenBo accessTokenPojo = entity.getBody();
            if (StringTools.isNotBlank(accessTokenPojo.getAccess_token())) {
                log.info("刷新微信服务号ACCESS_TOKEN...accessToken={}", accessTokenPojo.getAccess_token());
                systemDictRepository.updateByGroupName(BizConstant.DICT_GROUP_WECHAT, BizConstant.DICT_KEY_WECHAT_FWH_ACCESS_TOKEN, accessTokenPojo.getAccess_token());
            }
        } else {
            log.error("刷新微信服务号ACCESS_TOKEN失败！");
        }
    }

    /**
     * 定时获取微信支付对账单
     */
    @Scheduled(cron = "0 0 10 * * ?")
    public void downloadWechatPayBill() {
        Date billDate = DateTools.addDay(DateTools.getCurrentDate(), BizConstant.DB_MINUS_ONE);
        log.info("定时获取微信支付对账单...start, billDate={}", billDate);
        ResultDo resultDo = wechatPayComponent.downloadbill(billDate);
        log.info("定时获取微信支付对账单...返回：{}", resultDo);
    }
}
