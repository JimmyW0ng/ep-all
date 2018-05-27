package com.ep.domain.event;

import com.ep.common.component.SpringComponent;
import com.ep.common.tool.StringTools;
import com.ep.domain.component.QcloudsmsComponent;
import com.ep.domain.constant.BizConstant;
import com.ep.domain.pojo.event.ScheduleEventBo;
import com.ep.domain.pojo.po.EpMemberChildPo;
import com.ep.domain.pojo.po.EpMemberPo;
import com.ep.domain.pojo.po.EpOrganClassSchedulePo;
import com.ep.domain.pojo.po.EpSystemDictPo;
import com.ep.domain.repository.MemberChildRepository;
import com.ep.domain.repository.MemberRepository;
import com.ep.domain.repository.SystemDictRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @Description: 创建行程事件
 * @Author: J.W
 * @Date: 上午10:02 2018/5/27
 */
@Slf4j
@Component
public class ScheduleEventHandle {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberChildRepository memberChildRepository;
    @Autowired
    private SystemDictRepository systemDictRepository;
    @Autowired
    private QcloudsmsComponent qcloudsmsComponent;

    @Async
    @EventListener
    public void handle(ScheduleEventBo event) {
        log.info("创建行程事件处理开始, event={}", event);
        sendCreateScheduleMsg(event.getClassSchedulePo());
        log.info("创建行程事件处理结束");
    }

    /**
     * 发送短信
     *
     * @param schedulePo
     */
    private void sendCreateScheduleMsg(EpOrganClassSchedulePo schedulePo) {
        log.info("创建行程发送短信开始, schedulePo={}", schedulePo);
        if (!SpringComponent.isProduct()) {
            return;
        }
        if (schedulePo == null || schedulePo.getDelFlag()) {
            log.error("创建行程发送短信失败行程不存在, schedulePo={}", schedulePo);
            return;
        }
        EpMemberChildPo childPo = memberChildRepository.getById(schedulePo.getChildId());
        if (childPo == null || childPo.getDelFlag()) {
            log.error("创建行程发送短信失败, 孩子数据不存在");
            return;
        }
        EpMemberPo memberPo = memberRepository.getById(childPo.getMemberId());
        if (memberPo == null || memberPo.getDelFlag()) {
            log.error("创建行程发送短信失败, 会员数据不存在");
            return;
        }
        // 孩子姓名
        String name;
        if (StringTools.isNotBlank(childPo.getChildTrueName())) {
            name = childPo.getChildTrueName();
        } else {
            name = childPo.getChildNickName();
        }
        // 手机号
        String mobileStr = memberPo.getMobile().toString();
        // 发送短信
        EpSystemDictPo dictPo = systemDictRepository.findByGroupNameAndKey(BizConstant.DICT_GROUP_QCLOUDSMS, BizConstant.DICT_KEY_BESPEAK_SCHEDULE);
        //短信模板id
        int templateId = Integer.parseInt(dictPo.getValue());
        String[] params = new String[]{StringTools.encodeUTF(name)};
        log.info("发送订单提交预约短信：模版id={}, mobile={}, params={}", templateId, mobileStr, params);
        qcloudsmsComponent.singleSend(templateId, mobileStr, params);
    }
}
