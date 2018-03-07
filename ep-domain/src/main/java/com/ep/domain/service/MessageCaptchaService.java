package com.ep.domain.service;

import com.ep.common.tool.DateTools;
import com.ep.common.tool.RegexTools;
import com.ep.common.tool.StringTools;
import com.ep.domain.constant.BizConstant;
import com.ep.domain.constant.MessageCode;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.po.EpMessageCaptchaPo;
import com.ep.domain.repository.MessageCaptchaRepository;
import com.ep.domain.repository.domain.enums.EpMessageCaptchaCaptchaScene;
import com.ep.domain.repository.domain.enums.EpMessageCaptchaCaptchaType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

/**
 * @Description:验证码业务接口
 * @Author: J.W
 * @Date: 上午10:33 2017/11/27
 */
@Slf4j
@Service
public class MessageCaptchaService {

    @Autowired
    private MessageCaptchaRepository messageCaptchaRepository;

    /**
     * 获取短信验证码
     *
     * @param sourceId
     * @param scene
     * @param ip
     * @return
     */
    public ResultDo getCaptcha(Long sourceId, EpMessageCaptchaCaptchaType type, EpMessageCaptchaCaptchaScene scene, String ip) {
        // 校验手机格式
        if (!RegexTools.checkMobile(sourceId.toString())) {
            return ResultDo.build(MessageCode.ERROR_MOBILE_FORMAT);
        }
        // 校验手机发送短信频率是否异常
        ResultDo checkMsgActionResult = this.checkMsgAction(sourceId, type);
        if (checkMsgActionResult.isError()) {
            return checkMsgActionResult;
        }
        // 生成随机码
        String captchaContent = RandomStringUtils.randomNumeric(BizConstant.CAPTCHA_SHORT_MSG_LENGTH);
        // 生成业务编码
        String bizCode = StringTools.getUUID();
        // 保存到验证码表
        EpMessageCaptchaPo insertPo = new EpMessageCaptchaPo();
        insertPo.setCaptchaType(type);
        insertPo.setSourceId(sourceId);
        insertPo.setCaptchaCode(bizCode);
        insertPo.setCaptchaContent(captchaContent);
        insertPo.setCaptchaScene(scene);
        insertPo.setIp(ip);
        insertPo.setExpireTime(DateTools.addMinuteTimestamp(DateTools.getCurrentDate(), BizConstant.CAPTCHA_SHORT_MSG_EXPIRE_MINUTE));
        messageCaptchaRepository.insert(insertPo);
        return this.getByType(insertPo);
    }

    /**
     * 校验用户获取验证码行为是否存在异常
     *
     * @param sourceId
     * @return
     */
    private ResultDo checkMsgAction(Long sourceId, EpMessageCaptchaCaptchaType type) {
        if (type.equals(EpMessageCaptchaCaptchaType.short_msg)) {
            // 查看当天已经发送次数
            int count = messageCaptchaRepository.countBySourceId(sourceId);
            if (count > BizConstant.CAPTCHA_SHORT_MSG_NUM_LIMIT) {
                return ResultDo.build(MessageCode.ERROR_GET_CAPTCHA_NUM_OUT_LIMIT);
            }
        }
        return ResultDo.build();
    }

    /**
     * 根据类型返回字符串
     *
     * @param insertPo
     * @return
     */
    private ResultDo getByType(EpMessageCaptchaPo insertPo) {
        // TODO 目前只有短信验证码，直接返回业务编码
        return ResultDo.build().setResult(insertPo.getCaptchaCode());
    }

    /**
     * 验证码校验
     *
     * @param sourceId
     * @param captchaCode
     * @param captchaContent
     */
    public void checkAndHandleCaptcha(Long sourceId, String captchaCode, String captchaContent) {
        EpMessageCaptchaPo captchaPo = messageCaptchaRepository.getBySourceIdAndCaptchaCode(sourceId,
                EpMessageCaptchaCaptchaType.short_msg,
                EpMessageCaptchaCaptchaScene.login,
                captchaCode);
        if (captchaPo == null || captchaPo.getExpireTime().before(DateTools.getCurrentDateTime())) {
            throw new BadCredentialsException("验证码无效，请重新获取");
        } else if (!captchaContent.equals(captchaPo.getCaptchaContent())) {
            throw new BadCredentialsException("验证码错误");
        }
        // 验证码使用后删除
        messageCaptchaRepository.delBySourceIdAndTypeAndSence(sourceId,
                EpMessageCaptchaCaptchaType.short_msg,
                EpMessageCaptchaCaptchaScene.login);
    }


}
