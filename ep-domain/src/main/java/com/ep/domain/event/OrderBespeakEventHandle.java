package com.ep.domain.event;

import com.ep.common.component.SpringComponent;
import com.ep.common.tool.StringTools;
import com.ep.domain.component.QcloudsmsComponent;
import com.ep.domain.constant.BizConstant;
import com.ep.domain.pojo.event.OrderBespeakEventBo;
import com.ep.domain.pojo.po.EpMemberChildPo;
import com.ep.domain.pojo.po.EpMemberPo;
import com.ep.domain.pojo.po.EpOrderPo;
import com.ep.domain.pojo.po.EpSystemDictPo;
import com.ep.domain.repository.MemberChildRepository;
import com.ep.domain.repository.MemberRepository;
import com.ep.domain.repository.OrderRepository;
import com.ep.domain.repository.SystemDictRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @Description: 订单提交预约
 * @Author: J.W
 * @Date: 上午9:21 2018/5/27
 */
@Slf4j
@Component
public class OrderBespeakEventHandle {

    @Autowired
    private OrderRepository orderRepository;
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
    public void handle(OrderBespeakEventBo event) {
        log.info("订单提交预约事件处理开始, event={}", event);
        sendOrderBespeakClassMsg(event.getOrderId());
        log.info("订单提交预约事件处理结束");
    }

    /**
     * 发送短信
     *
     * @param orderId
     */
    private void sendOrderBespeakClassMsg(Long orderId) {
        log.info("订单提交预约发送短信开始, orderId={}", orderId);
        if (!SpringComponent.isProduct()) {
            return;
        }
        EpOrderPo orderPo = orderRepository.getById(orderId);
        if (orderPo == null || orderPo.getDelFlag()) {
            log.error("订单提交预约发送短信失败订单不存在, orderId={}", orderId);
            return;
        }
        EpMemberPo memberPo = memberRepository.getById(orderPo.getMemberId());
        if (memberPo == null || memberPo.getDelFlag()) {
            log.error("订单提交预约发送短信失败, 会员数据不存在");
            return;
        }
        EpMemberChildPo childPo = memberChildRepository.getById(orderPo.getChildId());
        if (childPo == null || childPo.getDelFlag()) {
            log.error("订单提交预约发送短信失败, 孩子数据不存在");
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
        EpSystemDictPo dictPo = systemDictRepository.findByGroupNameAndKey(BizConstant.DICT_GROUP_QCLOUDSMS, BizConstant.DICT_KEY_BESPEAK_SUCCESS);
        //短信模板id
        int templateId = Integer.parseInt(dictPo.getValue());
        String[] params = new String[]{StringTools.encodeUTF(name)};
        log.info("订单提交预约开班短信：模版id={}, mobile={}, params={}", templateId, mobileStr, params);
        qcloudsmsComponent.singleSend(templateId, mobileStr, params);
    }

}
