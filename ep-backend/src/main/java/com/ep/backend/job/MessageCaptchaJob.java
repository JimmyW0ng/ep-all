package com.ep.backend.job;

import com.ep.common.tool.DateTools;
import com.ep.domain.repository.MessageCaptchaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

/**
 * @Description: 验证码定时任务
 * @Author: J.W
 * @Date: 下午4:25 2018/3/7
 */
@Slf4j
@Component
public class MessageCaptchaJob {

    @Autowired
    private MessageCaptchaRepository messageCaptchaRepository;

    /**
     * 物理删除过期的验证码数据
     */
    @Scheduled(cron = "0 0 22 * * ?")
    public void deleteJob() {
        log.info("【定时任务】物理删除过期的验证码数据开始");
        Timestamp now = DateTools.getCurrentDateTime();
        Timestamp delTime = DateTools.dateToTimestamp(DateTools.zerolizedTime(now));
        log.info("【定时任务】删除{}之前的验证码", delTime);
        int num = messageCaptchaRepository.delCaptcha(delTime);
        log.info("【定时任务】物理删除过期的验证码数据完成, 删除条数：{}", num);
    }
}
