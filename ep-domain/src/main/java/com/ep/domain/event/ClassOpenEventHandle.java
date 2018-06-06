package com.ep.domain.event;

import com.ep.common.component.SpringComponent;
import com.ep.common.tool.DateTools;
import com.ep.common.tool.StringTools;
import com.ep.domain.component.DictComponent;
import com.ep.domain.component.QcloudsmsComponent;
import com.ep.domain.constant.BizConstant;
import com.ep.domain.pojo.event.ClassOpenEventBo;
import com.ep.domain.pojo.po.*;
import com.ep.domain.repository.*;
import com.ep.domain.repository.domain.enums.EpWechatFormBizType;
import com.ep.domain.service.OrderService;
import com.ep.domain.service.OrganClassScheduleService;
import com.ep.domain.service.WechatXcxService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

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
    @Autowired
    private WechatXcxService wechatXcxService;
    @Autowired
    private DictComponent dictComponent;
    @Autowired
    private WechatFormRepository wechatFormRepository;
    @Autowired
    private OrderService orderService;

    @Autowired
    private OrganCourseRepository organCourseRepository;
    @Autowired
    private OrganClassRepository organClassRepository;
    @Autowired
    private OrganClassCatalogRepository organClassCatalogRepository;
    @Autowired
    private OrganAccountRepository organAccountRepository;

    @Async
    @EventListener
    public void handle(ClassOpenEventBo event) {
        log.info("开班事件处理开始, event={}", event);
        //创建行程
        organClassScheduleService.batchInitByClassId(event.getClassId(), event.getOpeningOrders());
        //发送短信
        sendOpenClassMsg(event.getClassId(), event.getOpeningOrders());
        //小程序发送模板消息
        List<Long> orderIds = Lists.newArrayList();
        event.getOpeningOrders().forEach(order -> {
            orderIds.add(order.getId());
        });
        messageTemplateSend(orderIds);

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
            String[] params = new String[]{name};
            log.info("发送开班短信：模版id={}, mobile={}, params={}", templateId, mobileStr, params);
            qcloudsmsComponent.singleSend(templateId, mobileStr, params);
        }
    }

    /**
     * 小程序发送报名成功通知
     *
     * @param orderIds
     */
    private void messageTemplateSend(List<Long> orderIds) {
        log.info("[微信小程序]发送报名成功通知开始。");
        List<EpWechatFormPo> wechatFormPos = wechatFormRepository.findBySourceIdsAndBizType(orderIds, EpWechatFormBizType.order);
        EpSystemDictPo templateDictPo = dictComponent.getByGroupNameAndKey(BizConstant.DICT_GROUP_WECHAT, BizConstant.WECHAT_XCX_TEMPLATE_NORMAL_CLASS_OPEN);
        EpSystemDictPo pageDictPo = dictComponent.getByGroupNameAndKey(BizConstant.DICT_GROUP_WECHAT, BizConstant.WECHAT_XCX_PAGE_SCHEDULE);
        if (templateDictPo != null && pageDictPo != null) {
            String templateId = templateDictPo.getValue();
            String page = pageDictPo.getValue();
            for (EpWechatFormPo po : wechatFormPos) {
                //formId是否过期
                if (po.getExpireTime().before(DateTools.getCurrentDateTime())) {
                    continue;
                }
                Optional<EpOrderPo> orderOptional = orderService.findById(po.getSourceId());
                if (!orderOptional.isPresent()) {
                    continue;
                }
                EpOrderPo orderPo = orderOptional.get();
                Optional<EpMemberChildPo> childOptional = memberChildRepository.findById(orderPo.getChildId());
                String childNickName = childOptional.isPresent() ? childOptional.get().getChildNickName() : "";
                Optional<EpOrganCoursePo> courseOptional = organCourseRepository.findById(orderPo.getCourseId());
                String courseName = courseOptional.isPresent() ? courseOptional.get().getCourseName() : "";
                String orderTime = DateTools.timestampToString(orderPo.getCreateAt(), DateTools.DATE_FMT_19);
                Optional<EpOrganClassPo> classOptional = organClassRepository.findById(orderPo.getClassId());
                String address = classOptional.isPresent() ? classOptional.get().getAddress() : "";
                EpOrganClassCatalogPo catalogPo = organClassCatalogRepository.findFirstByClassId(orderPo.getClassId());
                String startTime = catalogPo != null ? DateTools.timestampToString(catalogPo.getStartTime(), DateTools.DATE_FMT_19) : "";
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
                //集合地点
                JSONObject jsonAddress = new JSONObject();
                jsonAddress.put("value", address);
                jsonData.put("keyword4", jsonAddress);
                //集合时间
                JSONObject jsonStartTime = new JSONObject();
                jsonStartTime.put("value", startTime);
                jsonData.put("keyword5", jsonStartTime);
                //联系人
                JSONObject jsonReferAccount = new JSONObject();
                jsonReferAccount.put("value", referAccount);
                jsonData.put("keyword6", jsonReferAccount);
                //备注
                JSONObject jsonRemark = new JSONObject();
                jsonRemark.put("value", SpringComponent.messageSource("WECHAT_XCX_TEMPLATE_NORMAL_CLASS_OPEN_REMARK"));
                jsonData.put("keyword7", jsonRemark);
                wechatXcxService.messageTemplateSend(po.getTouser(), templateId, page, po.getFormId(), jsonData, null, null);
                log.info("[微信小程序]发送报名成功通知：模版id={}, openid={}。", templateId, po.getTouser());

            }
        }
    }

}
