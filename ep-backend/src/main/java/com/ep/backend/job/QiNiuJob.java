package com.ep.backend.job;

import com.ep.common.tool.CollectionsTools;
import com.ep.common.tool.DateTools;
import com.ep.domain.component.QiNiuComponent;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.po.EpFilePo;
import com.ep.domain.repository.FileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;

/**
 * @Description: 七牛文件服务定时任务
 * @Author: J.W
 * @Date: 下午5:30 2018/3/7
 */
@Slf4j
@Component
public class QiNiuJob {

    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private QiNiuComponent qiNiuComponent;

    /**
     * 文件表删除过期废弃的文件数据
     */
    @Scheduled(cron = "0 10 22 * * ?")
    public void logicDeletePublicJob() {
        log.info("【定时任务】文件表删除过期废弃的文件数据开始");
        Timestamp now = DateTools.getCurrentDateTime();
        Timestamp delTime = DateTools.dateToTimestamp(DateTools.zerolizedTime(now));
        log.info("【定时任务】删除{}之前的过期废弃的文件数据", delTime);
        int num = fileRepository.logicDel(delTime);
        log.info("【定时任务】文件表删除过期废弃的文件数据结束, 删除条数：{}", num);
    }

    /**
     * 删除七牛云上废弃的文件
     */
    @Scheduled(cron = "0 20 22 * * ?")
    public void deletePublicJob() {
        log.info("【定时任务】删除七牛云上废弃的文件开始");
        Timestamp now = DateTools.getCurrentDateTime();
        Timestamp delTime = DateTools.dateToTimestamp(DateTools.zerolizedTime(now));
        log.info("【定时任务】删除{}之前的过期废弃的文件", delTime);
        List<EpFilePo> waitDeleteFiles = fileRepository.getWaitDelete(delTime);
        int num = 0;
        if (CollectionsTools.isNotEmpty(waitDeleteFiles)) {
            log.info("【定时任务】待删除{}个文件", waitDeleteFiles.size());
            // 七牛云删除
            for (EpFilePo waitDeleteFile : waitDeleteFiles) {
                ResultDo<Integer> resultDo = qiNiuComponent.deletePublic(waitDeleteFile.getFileName());
                if (resultDo.isSuccess() || "612".equals(resultDo.getError())) {
                    // 物理删除本地文件记录
                    fileRepository.delete(waitDeleteFile.getId());
                    num++;
                }
            }
        }
        log.info("【定时任务】删除七牛云上废弃的文件结束, 删除条数：{}", num);
    }
}
