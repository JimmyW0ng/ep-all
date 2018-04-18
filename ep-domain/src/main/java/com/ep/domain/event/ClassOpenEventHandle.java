package com.ep.domain.event;

import com.ep.domain.pojo.event.ClassOpenEventBo;
import com.ep.domain.service.OrganClassScheduleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @Description: 开班事件处理
 * @Author: J.W
 * @Date: 下午3:38 2018/3/1
 */
@Slf4j
@Component
public class ClassOpenEventHandle {

    @Autowired
    private OrganClassScheduleService organClassScheduleService;

    @Async
    @EventListener
    public void handle(ClassOpenEventBo event) {
        log.info("开班事件处理开始, event={}", event);
        organClassScheduleService.batchInitByClassId(event.getClassId(), event.getOpeningOrders());
    }

}
