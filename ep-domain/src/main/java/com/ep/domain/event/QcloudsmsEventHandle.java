package com.ep.domain.event;

import com.ep.domain.component.QcloudsmsComponent;
import com.ep.domain.pojo.event.QcloudsmsEventBo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Author: CC.F
 * @Date: 21:51 2018/4/25
 */
@Slf4j
@Component
public class QcloudsmsEventHandle {
    @Autowired
    private QcloudsmsComponent qcloudsmsComponent;

    @Async
    @EventListener
    public void handle(QcloudsmsEventBo event) {
        log.info("腾讯云短信发送, event={}", event);
        qcloudsmsComponent.singleSend(event.getTemplateId(), event.getPhoneNumber(), event.getParams());
    }
}
