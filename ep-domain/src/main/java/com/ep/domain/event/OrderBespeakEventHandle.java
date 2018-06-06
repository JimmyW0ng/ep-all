package com.ep.domain.event;

import com.ep.common.component.SpringComponent;
import com.ep.common.tool.DateTools;
import com.ep.common.tool.StringTools;
import com.ep.domain.component.DictComponent;
import com.ep.domain.component.QcloudsmsComponent;
import com.ep.domain.constant.BizConstant;
import com.ep.domain.pojo.event.OrderBespeakEventBo;
import com.ep.domain.pojo.po.*;
import com.ep.domain.repository.*;
import com.ep.domain.repository.domain.enums.EpWechatFormBizType;
import com.ep.domain.service.OrderService;
import com.ep.domain.service.WechatXcxService;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Optional;

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
    @Autowired
    private DictComponent dictComponent;
    @Autowired
    private WechatFormRepository wechatFormRepository;
    @Autowired
    private OrganCourseRepository organCourseRepository;
    @Autowired
    private OrganClassRepository organClassRepository;
    @Autowired
    private OrganAccountRepository organAccountRepository;
    @Autowired
    private WechatXcxService wechatXcxService;
    @Autowired
    private OrderService orderService;

    @Async
    @EventListener
    public void handle(OrderBespeakEventBo event) {
        log.info("订单提交预约事件处理开始, event={}", event);
        sendOrderBespeakClassMsg(event.getOrderId());
        messageTemplateSend(event.getOrderId());
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
        String[] params = new String[]{name};
        log.info("订单提交预约开班短信：模版id={}, mobile={}, params={}", templateId, mobileStr, params);
        qcloudsmsComponent.singleSend(templateId, mobileStr, params);
    }

    /**
     * 小程序模板消息发送报名成功通知
     *
     * @param orderId
     */
    private void messageTemplateSend(Long orderId) {
        log.info("[微信小程序]发送报名成功通知开始。");
        EpWechatFormPo wechatFormPo = wechatFormRepository.findBySourceIdAndBizType(orderId, EpWechatFormBizType.order);
        EpSystemDictPo templateDictPo = dictComponent.getByGroupNameAndKey(BizConstant.DICT_GROUP_WECHAT, BizConstant.WECHAT_XCX_TEMPLATE_BESPEAK_CLASS_OPEN);
        EpSystemDictPo pageDictPo = dictComponent.getByGroupNameAndKey(BizConstant.DICT_GROUP_WECHAT, BizConstant.WECHAT_XCX_PAGE_ORDER);
        Optional<EpOrderPo> orderOptional = orderService.findById(wechatFormPo.getSourceId());
        if (templateDictPo != null && pageDictPo != null && wechatFormPo.getExpireTime().after(DateTools.getCurrentDateTime()) && orderOptional.isPresent()) {
            //消息模板id
            String templateId = templateDictPo.getValue();
            //小程序跳转页面
            String page = pageDictPo.getValue();
            EpOrderPo orderPo = orderOptional.get();
            Optional<EpMemberChildPo> childOptional = memberChildRepository.findById(orderPo.getChildId());
            String childNickName = childOptional.isPresent() ? childOptional.get().getChildNickName() : "";
            Optional<EpOrganCoursePo> courseOptional = organCourseRepository.findById(orderPo.getCourseId());
            String courseName = courseOptional.isPresent() ? courseOptional.get().getCourseName() : "";
            String orderTime = DateTools.timestampToString(orderPo.getCreateAt(), DateTools.DATE_FMT_19);
            Optional<EpOrganClassPo> classOptional = organClassRepository.findById(orderPo.getClassId());
            String referAccount = "";
            if (classOptional.isPresent()) {
                Optional<EpOrganAccountPo> accountOptional = organAccountRepository.findById(classOptional.get().getOgnAccountId());
                referAccount = accountOptional.isPresent() ? accountOptional.get().getNickName() + accountOptional.get().getReferMobile() : "";

            }
            JSONObject jsonData = new JSONObject();
            //报名姓名
            JSONObject jsonChildNickName = new JSONObject();
            jsonChildNickName.put("value", childNickName);
            jsonData.put("keyword1", jsonChildNickName);
            //报名项目
            JSONObject jsonCourseName = new JSONObject();
            jsonCourseName.put("value", courseName);
            jsonData.put("keyword2", jsonCourseName);
            //报名时间
            JSONObject jsonOrderTime = new JSONObject();
            jsonOrderTime.put("value", orderTime);
            jsonData.put("keyword3", jsonOrderTime);
            //联系人
            JSONObject jsonReferAccount = new JSONObject();
            jsonReferAccount.put("value", referAccount);
            jsonData.put("keyword4", jsonReferAccount);
            //备注
            JSONObject jsonRemark = new JSONObject();
            jsonRemark.put("value", SpringComponent.messageSource("WECHAT_XCX_TEMPLATE_BESPEAK_CLASS_OPEN_REMARK"));
            jsonData.put("keyword5", jsonRemark);
            wechatXcxService.messageTemplateSend(wechatFormPo.getTouser(), templateId, page, wechatFormPo.getFormId(), jsonData, null, null);
            log.info("[微信小程序]发送报名成功通知：模版id={}, openid={}。", templateId, wechatFormPo.getTouser());
        }
    }
}
