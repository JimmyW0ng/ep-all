package com.ep.backend.controller;

import com.ep.common.component.SpringComponent;
import com.ep.common.tool.DateTools;
import com.ep.common.tool.StringTools;
import com.ep.domain.constant.MessageCode;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.OrderBo;
import com.ep.domain.pojo.bo.OrganClassScheduleBo;
import com.ep.domain.pojo.dto.OrderChildStatisticsDto;
import com.ep.domain.pojo.po.EpOrderPo;
import com.ep.domain.pojo.po.EpOrganClassPo;
import com.ep.domain.repository.domain.enums.EpOrderPayStatus;
import com.ep.domain.repository.domain.enums.EpOrderStatus;
import com.ep.domain.repository.domain.enums.EpOrganClassType;
import com.ep.domain.service.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.jooq.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.ep.domain.repository.domain.Ep.EP;
import static com.ep.domain.repository.domain.Tables.*;
import static com.ep.domain.repository.domain.tables.EpOrganClass.EP_ORGAN_CLASS;
import static com.ep.domain.repository.domain.tables.EpOrganCourse.EP_ORGAN_COURSE;


/**
 * @Description: 订单控制器
 * @Author: CC.F
 * @Date: 20:59 2018/2/25
 */
@Slf4j
@Controller
@RequestMapping("auth/order")
public class OrderController extends BackendController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrganClassScheduleService organClassScheduleService;
    @Autowired
    private OrganClassService organClassService;
    @Autowired
    private WechatUnifiedOrderService wechatUnifiedOrderService;
    @Autowired
    private WechatPayWithdrawService wechatPayWithdrawService;
    @Autowired
    private WechatPayBillService wechatPayBillService;
    @Autowired
    private OrderRefundService orderRefundService;

    private Collection<Condition> formatJooqSearchConditions(String mobile, String childTrueName, String childNickName, String courseName,
                                                             String className, String classType, String status, Timestamp crStartTime, Timestamp crEndTime) {
        Collection<Condition> conditions = Lists.newArrayList();
        if (StringTools.isNotBlank(mobile)) {
            conditions.add(EP.EP_MEMBER.MOBILE.eq(Long.parseLong(mobile)));
        }
        if (StringTools.isNotBlank(childTrueName)) {
            conditions.add(EP.EP_MEMBER_CHILD.CHILD_TRUE_NAME.like("%" + childTrueName + "%"));
        }
        if (StringTools.isNotBlank(childNickName)) {
            conditions.add(EP.EP_MEMBER_CHILD.CHILD_NICK_NAME.like("%" + childNickName + "%"));
        }
        if (StringTools.isNotBlank(courseName)) {
            conditions.add(EP.EP_ORGAN_COURSE.COURSE_NAME.like("%" + courseName + "%"));
        }
        if (StringTools.isNotBlank(className)) {
            conditions.add(EP.EP_ORGAN_CLASS.CLASS_NAME.like("%" + className + "%"));
        }
        if (StringTools.isNotBlank(classType)) {
            conditions.add(EP.EP_ORGAN_CLASS.TYPE.eq(EpOrganClassType.valueOf(classType)));
        }
        if (StringTools.isNotBlank(status)) {
            conditions.add(EP.EP_ORDER.STATUS.eq(EpOrderStatus.valueOf(status)));
        }
        if (null != crStartTime) {
            conditions.add(EP.EP_ORDER.CREATE_AT.greaterOrEqual(crStartTime));
        }
        if (null != crEndTime) {
            conditions.add(EP.EP_ORDER.CREATE_AT.lessOrEqual(crEndTime));
        }
        conditions.add(EP.EP_ORDER.DEL_FLAG.eq(false));
        conditions.add(EP.EP_ORDER.OGN_ID.eq(super.getCurrentUser().get().getOgnId()));
        return conditions;
    }

    /**
     * 订单列表(商户)
     *
     * @param model
     * @param pageable
     * @param mobile
     * @param childTrueName
     * @param childNickName
     * @param courseName
     * @param className
     * @param status
     * @param crStartTime
     * @param crEndTime
     * @return
     */
    @GetMapping("index")
    @PreAuthorize("hasAnyAuthority('merchant:order:index')")
    public String index(Model model,
                        @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                        @RequestParam(value = "mobile", required = false) String mobile,
                        @RequestParam(value = "childTrueName", required = false) String childTrueName,
                        @RequestParam(value = "childNickName", required = false) String childNickName,
                        @RequestParam(value = "courseName", required = false) String courseName,
                        @RequestParam(value = "className", required = false) String className,
                        @RequestParam(value = "classType", required = false) String classType,
                        @RequestParam(value = "status", required = false) String status,
                        @RequestParam(value = "payStatus", required = false) String payStatus,
                        @RequestParam(value = "crStartTime", required = false) Timestamp crStartTime,
                        @RequestParam(value = "crEndTime", required = false) Timestamp crEndTime

    ) {
        Map<String, Object> searchMap = Maps.newHashMap();
        Collection<Condition> conditions = Lists.newArrayList();
        if (StringTools.isNotBlank(mobile)) {
            conditions.add(EP.EP_MEMBER.MOBILE.eq(Long.parseLong(mobile)));
        }
        searchMap.put("mobile", mobile);
        if (StringTools.isNotBlank(childTrueName)) {
            conditions.add(EP.EP_MEMBER_CHILD.CHILD_TRUE_NAME.like("%" + childTrueName + "%"));
        }
        searchMap.put("childTrueName", childTrueName);
        if (StringTools.isNotBlank(childNickName)) {
            conditions.add(EP.EP_MEMBER_CHILD.CHILD_NICK_NAME.like("%" + childNickName + "%"));
        }
        searchMap.put("childNickName", childNickName);
        if (StringTools.isNotBlank(courseName)) {
            conditions.add(EP.EP_ORGAN_COURSE.COURSE_NAME.like("%" + courseName + "%"));
        }
        searchMap.put("courseName", courseName);
        if (StringTools.isNotBlank(className)) {
            conditions.add(EP.EP_ORGAN_CLASS.CLASS_NAME.like("%" + className + "%"));
        }
        searchMap.put("className", className);
        if (StringTools.isNotBlank(classType)) {
            conditions.add(EP.EP_ORGAN_CLASS.TYPE.eq(EpOrganClassType.valueOf(classType)));
        }
        searchMap.put("classType", classType);
        if (StringTools.isNotBlank(status)) {
            conditions.add(EP.EP_ORDER.STATUS.eq(EpOrderStatus.valueOf(status)));
        }
        searchMap.put("status", status);
        if (StringTools.isNotBlank(payStatus)) {
            conditions.add(EP.EP_ORDER.PAY_STATUS.eq(EpOrderPayStatus.valueOf(payStatus)));
        }
        searchMap.put("payStatus", payStatus);
        if (null != crStartTime) {
            conditions.add(EP.EP_ORDER.CREATE_AT.greaterOrEqual(crStartTime));
        }
        searchMap.put("crStartTime", crStartTime);
        if (null != crEndTime) {
            conditions.add(EP.EP_ORDER.CREATE_AT.lessOrEqual(crEndTime));
        }
        searchMap.put("crEndTime", crEndTime);
        conditions.add(EP.EP_ORDER.DEL_FLAG.eq(false));
        conditions.add(EP.EP_ORDER.OGN_ID.eq(super.getCurrentUser().get().getOgnId()));

        Page<OrderBo> page = orderService.findbyPageAndCondition(pageable, conditions);
        model.addAttribute("page", page);
        model.addAttribute("searchMap", searchMap);
        return "order/index";
    }


    /**
     * 预约订单列表
     *
     * @param model
     * @param pageable
     * @param mobile
     * @param childTrueName
     * @param childNickName
     * @param courseName
     * @param className
     * @param status
     * @param crStartTime
     * @param crEndTime
     * @return
     */
    @GetMapping("bespeakIndex")
    @PreAuthorize("hasAnyAuthority('merchant:order:index')")
    public String bespeakIndex(Model model,
                               @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                               @RequestParam(value = "mobile", required = false) String mobile,
                               @RequestParam(value = "childTrueName", required = false) String childTrueName,
                               @RequestParam(value = "childNickName", required = false) String childNickName,
                               @RequestParam(value = "courseName", required = false) String courseName,
                               @RequestParam(value = "className", required = false) String className,
                               @RequestParam(value = "status", required = false) String status,
                               @RequestParam(value = "crStartTime", required = false) Timestamp crStartTime,
                               @RequestParam(value = "crEndTime", required = false) Timestamp crEndTime
    ) {
        Map<String, Object> searchMap = Maps.newHashMap();
        Collection<Condition> conditions = Lists.newArrayList();
        if (StringTools.isNotBlank(mobile)) {
            conditions.add(EP_MEMBER.MOBILE.eq(Long.parseLong(mobile)));
        }
        searchMap.put("mobile", mobile);
        if (StringTools.isNotBlank(childTrueName)) {
            conditions.add(EP_MEMBER_CHILD.CHILD_TRUE_NAME.like("%" + childTrueName + "%"));
        }
        searchMap.put("childTrueName", childTrueName);
        if (StringTools.isNotBlank(childNickName)) {
            conditions.add(EP_MEMBER_CHILD.CHILD_NICK_NAME.like("%" + childNickName + "%"));
        }
        searchMap.put("childNickName", childNickName);
        if (StringTools.isNotBlank(courseName)) {
            conditions.add(EP_ORGAN_COURSE.COURSE_NAME.like("%" + courseName + "%"));
        }
        searchMap.put("courseName", courseName);
        if (StringTools.isNotBlank(className)) {
            conditions.add(EP_ORGAN_CLASS.CLASS_NAME.like("%" + className + "%"));
        }
        searchMap.put("className", className);
        if (StringTools.isNotBlank(status)) {
            conditions.add(EP_ORDER.STATUS.eq(EpOrderStatus.valueOf(status)));
        }
        searchMap.put("status", status);
        if (null != crStartTime) {
            conditions.add(EP_ORDER.CREATE_AT.greaterOrEqual(crStartTime));
        }
        searchMap.put("crStartTime", crStartTime);
        if (null != crEndTime) {
            conditions.add(EP_ORDER.CREATE_AT.lessOrEqual(crEndTime));
        }
        searchMap.put("crEndTime", crEndTime);
        conditions.add(EP_ORDER.DEL_FLAG.eq(false));
        conditions.add(EP_ORDER.STATUS.eq(EpOrderStatus.opening));
        conditions.add(EP_ORDER.OGN_ID.eq(super.getCurrentUser().get().getOgnId()));
        conditions.add(EP_ORGAN_CLASS.TYPE.eq(EpOrganClassType.bespeak));

        Page<OrderBo> page = orderService.findOrderBespeakByPageAndCondition(pageable, conditions);
        model.addAttribute("page", page);
        model.addAttribute("searchMap", searchMap);
        return "order/bespeakIndex";
    }

    /**
     * 学员统计
     *
     * @return
     */
    @GetMapping("childIndex")
    @PreAuthorize("hasAnyAuthority('merchant:order:index')")
    public String childIndex(Model model,
                             @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                             @RequestParam(value = "mobile", required = false) String mobile,
                             @RequestParam(value = "childTrueName", required = false) String childTrueName,
                             @RequestParam(value = "childNickName", required = false) String childNickName
    ) {
        Map<String, Object> searchMap = Maps.newHashMap();
        Collection<Condition> conditions = Lists.newArrayList();
        if (StringTools.isNotBlank(mobile)) {
            conditions.add(EP_MEMBER.MOBILE.eq(Long.parseLong(mobile)));
        }
        searchMap.put("mobile", mobile);
        if (StringTools.isNotBlank(childTrueName)) {
            conditions.add(EP_MEMBER_CHILD.CHILD_TRUE_NAME.like("%" + childTrueName + "%"));
        }
        searchMap.put("childTrueName", childTrueName);
        if (StringTools.isNotBlank(childNickName)) {
            conditions.add(EP_MEMBER_CHILD.CHILD_NICK_NAME.like("%" + childNickName + "%"));
        }
        searchMap.put("childNickName", childNickName);

        conditions.add(EP_ORDER.DEL_FLAG.eq(false));
        Long ognId = super.getCurrentUserOgnId();
        conditions.add(EP_ORDER.OGN_ID.eq(ognId));
        Page<OrderChildStatisticsDto> page = orderService.statisticsChild(pageable, conditions);
        model.addAttribute("page", page);
        model.addAttribute("searchMap", searchMap);
        return "order/childIndex";
    }

    /**
     * 单个订单报名成功
     *
     * @param id
     */
    @PostMapping("orderSuccess")
    @PreAuthorize("hasAnyAuthority('merchant:order:index')")
    @ResponseBody
    public ResultDo orderSuccess(@RequestParam(value = "id") Long id
    ) {
        if (null == this.innerOgnOrPlatformReq(id, super.getCurrentUserOgnId())) {
            return ResultDo.build(MessageCode.ERROR_ILLEGAL_RESOURCE);
        }

        return orderService.orderSuccessById(id);
    }

    /**
     * 报名拒绝
     *
     * @param id
     */
    @GetMapping("orderRefuse")
    @PreAuthorize("hasAnyAuthority('merchant:order:index')")
    @ResponseBody
    public ResultDo orderRefuse(
            @RequestParam(value = "id") Long id,
            @RequestParam(value = "remark", required = false) String remark) {
        if (null == this.innerOgnOrPlatformReq(id, super.getCurrentUserOgnId())) {
            return ResultDo.build(MessageCode.ERROR_ILLEGAL_RESOURCE);
        }

        return orderService.orderRefuseById(id, remark);
    }

    /**
     * 报名/拒绝取消
     *
     * @param id
     */
    @GetMapping("orderCancel")
    @PreAuthorize("hasAnyAuthority('merchant:order:index')")
    @ResponseBody
    public ResultDo orderCancel(
            @RequestParam(value = "id") Long id
    ) {
        if (null == this.innerOgnOrPlatformReq(id, super.getCurrentUserOgnId())) {
            return ResultDo.build(MessageCode.ERROR_ILLEGAL_RESOURCE);
        }

        return orderService.orderCancelById(id);
    }

    /**
     * 提交预约
     *
     * @param id
     */
    @GetMapping("orderBespeak/{id}")
    @PreAuthorize("hasAnyAuthority('merchant:order:index')")
    @ResponseBody
    public ResultDo orderBespeak(
            @PathVariable("id") Long id
    ) {
        if (null == this.innerOgnOrPlatformReq(id, super.getCurrentUserOgnId())) {
            return ResultDo.build(MessageCode.ERROR_ILLEGAL_RESOURCE);
        }

        return orderService.orderBespeakById(id);
    }



    /**
     * 订单退单
     *
     * @param id
     */
    @PostMapping("orderBreak")
    @PreAuthorize("hasAnyAuthority('merchant:order:index')")
    @ResponseBody
    public ResultDo orderBespeakBreak(
            @RequestParam(value = "id") Long id,
            @RequestParam(value = "refundAmount") BigDecimal refundAmount,
            @RequestParam(value = "firstClassCatalogId", required = false) Long firstClassCatalogId
    ) {
        if (null == this.innerOgnOrPlatformReq(id, super.getCurrentUserOgnId())) {
            return ResultDo.build(MessageCode.ERROR_ILLEGAL_RESOURCE);
        }
        return orderService.orderBreak(id, refundAmount, firstClassCatalogId);
    }

    /**
     * 退单初始化
     *
     * @param id
     */
    @GetMapping("breakInit/{id}")
    @PreAuthorize("hasAnyAuthority('merchant:order:index')")
    @ResponseBody
    public ResultDo breakInit(@PathVariable("id") Long id) {
        if (null == this.innerOgnOrPlatformReq(id, super.getCurrentUserOgnId())) {
            return ResultDo.build(MessageCode.ERROR_ILLEGAL_RESOURCE);
        }
        Optional<EpOrderPo> orderOptional = orderService.findById(id);
        if (!orderOptional.isPresent()) {
            return ResultDo.build(MessageCode.ERROR_ORDER_NOT_EXISTS);
        }
        Map<String, Object> resultMap = Maps.newHashMap();
        //系统当前时间
        resultMap.put("currentTime", DateTools.getCurrentDateTime());
        //该订单下的行程
        List<OrganClassScheduleBo> bos = organClassScheduleService.findBoByOrderId(id);
        resultMap.put("classScheduleBos", bos);
        //订单费
        resultMap.put("orderPrize", orderOptional.get().getPrize());
        //预约订单
        Long classId = orderOptional.get().getClassId();
        Optional<EpOrganClassPo> classOptional = organClassService.findById(classId);
        if (!classOptional.isPresent()) {
            return ResultDo.build(MessageCode.ERROR_CLASS_NOT_EXIST);
        }
        EpOrganClassPo organClassPo = classOptional.get();
        if (organClassPo.getType().equals(EpOrganClassType.bespeak)) {
            resultMap.put("courseNum", organClassPo.getCourseNum());
        }
        return ResultDo.build().setResult(resultMap);
    }

    /**
     * 孩子已参加的班次
     *
     * @param childId
     */
    @GetMapping("viewEnteredClass/{childId}")
    @PreAuthorize("hasAnyAuthority('merchant:order:index')")
    @ResponseBody
    public ResultDo viewEnteredClass(@PathVariable("childId") Long childId) {
        return ResultDo.build().setResult(orderService.findEnteredClassByChildId(super.getCurrentUserOgnId(), childId, null));
    }

    /**
     * 孩子已参加的预约班次
     *
     * @param childId
     */
    @GetMapping("viewEnteredBespeakClass/{childId}")
    @PreAuthorize("hasAnyAuthority('merchant:order:index')")
    @ResponseBody
    public ResultDo viewEnteredBespeakClass(@PathVariable("childId") Long childId) {
        return ResultDo.build().setResult(orderService.findEnteredClassByChildId(super.getCurrentUserOgnId(), childId, EpOrganClassType.bespeak));
    }

    /**
     * 导出excel
     */
    @GetMapping("indexExportExcel")
    @PreAuthorize("hasAnyAuthority('merchant:order:index')")
    public void indexExportExcel(@PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                                 @RequestParam(value = "mobile", required = false) String mobile,
                                 @RequestParam(value = "childTrueName", required = false) String childTrueName,
                                 @RequestParam(value = "childNickName", required = false) String childNickName,
                                 @RequestParam(value = "courseName", required = false) String courseName,
                                 @RequestParam(value = "className", required = false) String className,
                                 @RequestParam(value = "classType", required = false) String classType,
                                 @RequestParam(value = "status", required = false) String status,
                                 @RequestParam(value = "crStartTime", required = false) Timestamp crStartTime,
                                 @RequestParam(value = "crEndTime", required = false) Timestamp crEndTime,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {
        Collection<Condition> conditions = formatJooqSearchConditions(mobile, childTrueName, childNickName, courseName, className, classType, status, crStartTime, crEndTime);
        orderService.indexExportExcel(request, response, "订单列表", pageable, conditions);
    }

    /**
     * 批量拒绝订单
     *
     * @param ids
     * @return
     */
    @PostMapping("/batchRefuse")
    @PreAuthorize("hasAnyAuthority('merchant:order:index')")
    @ResponseBody
    public ResultDo delete(@RequestParam("ids[]") List<Long> ids,
                           @RequestParam(value = "batchRefuseRemark") String batchRefuseRemark) {
        Long ognId = super.getCurrentUserOgnId();
        return orderService.batchRefuse(ids, batchRefuseRemark, ognId);
    }



    /**
     * 确认线下支付完成
     *
     * @param id
     * @return
     */
    @PostMapping("/offlinePaidDo")
    @PreAuthorize("hasAnyAuthority('merchant:order:index')")
    @ResponseBody
    public ResultDo offlinePaidDo(@RequestParam(value = "id") Long id, @RequestParam(value = "payConfirmTime") Timestamp payConfirmTime) {
        if (null == this.innerOgnOrPlatformReq(id, super.getCurrentUserOgnId())) {
            return ResultDo.build(MessageCode.ERROR_ILLEGAL_RESOURCE);
        }
        return orderService.offlinePaidByOrderId(id, payConfirmTime);
    }



    /**
     * 校验业务对象是否属于该机构，是：返回po;否：返回null
     *
     * @param sourceId
     * @param ognId
     * @return
     */
    private EpOrderPo innerOgnOrPlatformReq(Long sourceId, Long ognId) {
        if (sourceId == null) {
            return null;
        }
        Optional<EpOrderPo> optional = orderService.findById(sourceId);
        if (!optional.isPresent()) {
            return null;
        }
        if (optional.get().getOgnId().equals(ognId)) {
            return optional.get();
        } else {
            log.error(SpringComponent.messageSource("ERROR_ILLEGAL_RESOURCE"));
            return null;
        }
    }
}

