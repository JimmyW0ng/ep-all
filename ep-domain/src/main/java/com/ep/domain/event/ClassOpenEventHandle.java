package com.ep.domain.event;

import com.ep.common.component.SpringComponent;
import com.ep.common.tool.StringTools;
import com.ep.domain.component.QcloudsmsComponent;
import com.ep.domain.constant.BizConstant;
import com.ep.domain.pojo.event.ClassOpenEventBo;
import com.ep.domain.pojo.po.EpMemberChildPo;
import com.ep.domain.pojo.po.EpMemberPo;
import com.ep.domain.pojo.po.EpOrderPo;
import com.ep.domain.pojo.po.EpSystemDictPo;
import com.ep.domain.repository.MemberChildRepository;
import com.ep.domain.repository.MemberRepository;
import com.ep.domain.repository.SystemDictRepository;
import com.ep.domain.service.OrganClassScheduleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

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
    public void handle(ClassOpenEventBo event) {
        log.info("开班事件处理开始, event={}", event);
        organClassScheduleService.batchInitByClassId(event.getClassId(), event.getOpeningOrders());
        sendOpenClassMsg(event.getClassId(), event.getOpeningOrders());
        log.info("开班事件处理结束");
    }

    /**
     * 发送短信
     *
     * @param classId
     */
    private void sendOpenClassMsg(Long classId, List<EpOrderPo> openingOrders) {
        log.info("开班发送短信开始, classId={}, orderNum={}", classId, openingOrders.size());
        if (!SpringComponent.isProduct()) {
            return;
        }
        for (EpOrderPo orderPo : openingOrders) {
            EpMemberPo memberPo = memberRepository.getById(orderPo.getMemberId());
            if (memberPo == null || memberPo.getDelFlag()) {
                log.error("开班发送短信失败, 会员数据不存在");
                return;
            }
            EpMemberChildPo childPo = memberChildRepository.getById(orderPo.getChildId());
            if (childPo == null || childPo.getDelFlag()) {
                log.error("开班发送短信失败, 孩子数据不存在");
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
            EpSystemDictPo dictPo = systemDictRepository.findByGroupNameAndKey(BizConstant.DICT_GROUP_QCLOUDSMS, BizConstant.DICT_KEY_OPEN_CLASS);
            //短信模板id
            int templateId = Integer.parseInt(dictPo.getValue());
            String[] params = new String[]{StringTools.encodeUTF(name)};
            log.info("发送开班短信：模版id={}, mobile={}, params={}", templateId, mobileStr, params);
            qcloudsmsComponent.singleSend(templateId, mobileStr, params);
        }
    }

}
