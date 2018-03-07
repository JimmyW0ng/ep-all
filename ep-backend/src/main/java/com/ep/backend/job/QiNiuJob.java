package com.ep.backend.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Description: 七牛文件服务定时任务
 * @Author: J.W
 * @Date: 下午5:30 2018/3/7
 */
@Slf4j
@Component
public class QiNiuJob {

    /**
     * 文件服务器端公共空间删除废弃的文件
     */
    @Scheduled
    public void deletePublicJob() {
        log.info("【定时任务】文件服务器端公共空间删除废弃的文件开始");
        int num = 0;
        log.info("【定时任务】文件服务器端公共空间删除废弃的文件结束, 删除条数：{}", num);
    }
}
