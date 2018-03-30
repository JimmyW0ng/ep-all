package com.ep.domain.event;

import com.ep.common.tool.CollectionsTools;
import com.ep.domain.pojo.event.ClassCatalogCommentEventBo;
import com.ep.domain.service.MemberMessageService;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @Description: 课时评价事件处理
 * @Author: J.W
 * @Date: 下午3:38 2018/3/1
 */
@Slf4j
@Component
public class ClassCatalogCommentEventHandle {

    @Autowired
    private MemberMessageService memberMessageService;

    @Async
    @EventListener
    public void handle(ClassCatalogCommentEventBo event) {
        log.info("课时评价事件处理开始, event={}", event);
        Set<Long> tagIds = CollectionsTools.isNotEmpty(event.getTagIds()) ? Sets.newHashSet(event.getTagIds()) : null;
        // 发送课时评论消息
        memberMessageService.sendClassCatalogCommentMessage(event.getClassScheduleId(), tagIds, event.getComment());
    }

}
