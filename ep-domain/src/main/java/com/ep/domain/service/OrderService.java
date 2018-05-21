package com.ep.domain.service;

import com.ep.common.component.SpringComponent;
import com.ep.common.exception.ServiceRuntimeException;
import com.ep.common.tool.CollectionsTools;
import com.ep.common.tool.DateTools;
import com.ep.common.tool.ExcelUtil;
import com.ep.common.tool.NumberTools;
import com.ep.domain.component.WechatPayComponent;
import com.ep.domain.constant.BizConstant;
import com.ep.domain.constant.MessageCode;
import com.ep.domain.enums.ChildClassStatusEnum;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.*;
import com.ep.domain.pojo.dto.OrderChildStatisticsDto;
import com.ep.domain.pojo.dto.OrderDto;
import com.ep.domain.pojo.dto.OrderInitDto;
import com.ep.domain.pojo.po.*;
import com.ep.domain.repository.*;
import com.ep.domain.repository.domain.enums.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.jooq.Condition;
import org.jooq.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

import static com.ep.domain.repository.domain.Tables.*;

/**
 * @Description: 订单服务类
 * @Author: J.W
 * @Date: 下午2:19 2018/1/28
 */
@Slf4j
@Service
public class OrderService {

    @Autowired
    private MemberChildRepository memberChildRepository;
    @Autowired
    private OrganClassRepository organClassRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrganRepository organRepository;
    @Autowired
    private OrganConfigRepository organConfigRepository;
    @Autowired
    private OrganVipRepository organVipRepository;
    @Autowired
    private OrganCourseRepository organCourseRepository;
    @Autowired
    private OrganClassChildRepository organClassChildRepository;
    @Autowired
    private OrganClassScheduleRepository organClassScheduleRepository;
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private WechatPayComponent wechatPayComponent;
    @Autowired
    private WechatUnifiedOrderService wechatUnifiedOrderService;
    @Autowired
    private WechatPayRefundService wechatPayRefundService;

    /**
     * 加载会员下单需要的数据
     *
     * @param memberId
     * @param courseId
     * @return
     */
    public ResultDo<OrderInitDto> initInfo(Long memberId, Long courseId) {
        ResultDo<OrderInitDto> resultDo = ResultDo.build();
        EpOrganCoursePo coursePo = organCourseRepository.getById(courseId);
        log.info("加载会员下单需要的数据, coursePo={}", coursePo);
        if (coursePo == null || coursePo.getDelFlag()) {
            return resultDo.setError(MessageCode.ERROR_COURSE_NOT_EXIST);
        } else if (coursePo.getCourseStatus().equals(EpOrganCourseCourseStatus.save)) {
            return resultDo.setError(MessageCode.ERROR_COURSE_NOT_EXIST);
        }
        List<MemberCourseOrderInitBo> data = orderRepository.findChildrenAndOrders(memberId, courseId);
        int childrenNum = 0;
        if (CollectionsTools.isNotEmpty(data)) {
            childrenNum = data.size();
            for (MemberCourseOrderInitBo initBo : data) {
                Optional<EpFilePo> existAvatar = fileRepository.getOneByBizTypeAndSourceId(BizConstant.FILE_BIZ_TYPE_CODE_CHILD_AVATAR, initBo.getChildId());
                if (existAvatar.isPresent()) {
                    initBo.setAvatar(existAvatar.get().getFileUrl());
                }
            }
        }
        return resultDo.setResult(new OrderInitDto(data, childrenNum, BizConstant.CHILD_LIMIT_NUM));
    }

    /**
     * 下单接口
     *
     * @param memberId
     * @param childId
     * @param classId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResultDo<OrderDto> order(Long memberId, Long childId, Long classId) {
        ResultDo<OrderDto> resultDo = ResultDo.build();
        log.info("下单开始: memberId={}, childId={}, classId={}", memberId, childId, classId);
        if (childId == null) {
            log.error("下单失败，孩子id为空！");
            resultDo.setError(MessageCode.ERROR_SYSTEM_PARAM_FORMAT);
            return resultDo;
        }
        // 查询孩子信息
        EpMemberChildPo childPo = memberChildRepository.getById(childId);
        if (childPo == null
                || !childPo.getMemberId().equals(memberId)
                || childPo.getDelFlag()) {
            log.error("下单失败，孩子信息不存在或已删除！");
            resultDo.setError(MessageCode.ERROR_CHILD_NOT_EXISTS);
            return resultDo;
        }
        // 查询是否有重复下单
        List<EpOrderPo> orders = orderRepository.findByChildIdAndClassId(childId, classId);
        if (CollectionsTools.isNotEmpty(orders)) {
            log.error("下单失败，同一个班次重复下单！");
            resultDo.setError(MessageCode.ERROR_ORDER_DUPLICATE);
            return resultDo;
        }
        // 查询课程班次
        EpOrganClassPo classPo = organClassRepository.getById(classId);
        if (classPo == null || classPo.getDelFlag()) {
            log.error("下单失败，班次不存在！");
            resultDo.setError(MessageCode.ERROR_COURSE_NOT_EXIST);
            return resultDo;
        }
        // 校验课程状态
        if (classPo.getStatus().equals(EpOrganClassStatus.save)) {
            log.error("下单失败，班次未上线！");
            resultDo.setError(MessageCode.ERROR_COURSE_NOT_ONLINE);
            return resultDo;
        }
        if (classPo.getStatus().equals(EpOrganClassStatus.opening)) {
            log.error("下单失败，班次已开班！");
            resultDo.setError(MessageCode.ERROR_CLASS_IS_OPENING);
            return resultDo;
        }
        if (classPo.getStatus().equals(EpOrganClassStatus.end)) {
            log.error("下单失败，课程已结束！");
            resultDo.setError(MessageCode.ERROR_CLASS_IS_END);
            return resultDo;
        }
        // 校验报名时间
        EpOrganCoursePo coursePo = organCourseRepository.getById(classPo.getCourseId());
        Date now = DateTools.getCurrentDate();
        if (now.before(coursePo.getEnterTimeStart())) {
            log.error("下单失败，班次报名未开始！");
            resultDo.setError(MessageCode.ERROR_COURSE_ENTER_NOT_START);
            return resultDo;
        }
        if (coursePo.getEnterTimeEnd() != null && now.after(coursePo.getEnterTimeEnd())) {
            log.error("下单失败，班次报名已结束！");
            resultDo.setError(MessageCode.ERROR_COURSE_ENTER_END);
            return resultDo;
        }
        // 校验是否会员才能报名
        if (coursePo.getVipFlag()) {
            Boolean existVip = organVipRepository.existVipByOgnIdAndChildId(classPo.getOgnId(), childId);
            if (!existVip) {
                EpOrganPo organPo = organRepository.getById(classPo.getOgnId());
                String vipName = (organPo != null && !organPo.getDelFlag() && organPo.getVipFlag()) ? organPo.getVipName() : BizConstant.VIP_NAME;
                String errorMsg = SpringComponent.messageSource(MessageCode.ERROR_CLASS_NEED_VIP, new Object[]{vipName});
                resultDo.setError(MessageCode.ERROR_CLASS_NEED_VIP).setErrorDescription(errorMsg);
                return resultDo;
            }
        }
        // 校验班次报名人数
        if (classPo.getEnterLimitFlag()) {
            // 成功报名数已满
            if (classPo.getEnteredNum() >= classPo.getEnterRequireNum()) {
                log.error("下单失败，成功报名数已满！");
                resultDo.setError(MessageCode.ERROR_ORDER_ENTERED_FULL);
                return resultDo;
            }
            // 下单人数超过阈值
            if (classPo.getEnterRequireNum() + BizConstant.ORDER_BEYOND_NUM <= classPo.getOrderedNum()) {
                log.error("下单失败，下单人数超过阈值！");
                resultDo.setError(MessageCode.ERROR_ORDER_ORDERED_NUM_FULL);
                return resultDo;
            }
        }
        // 更新下单数
        int orderNum;
        if (classPo.getEnterLimitFlag()) {
            orderNum = organClassRepository.orderWithLimit(classId);
        } else {
            orderNum = organClassRepository.orderWithNoLimit(classId);
        }
        if (orderNum < BizConstant.DB_NUM_ONE) {
            resultDo.setError(MessageCode.ERROR_ORDER);
            return resultDo;
        }
        // 订单金额
        BigDecimal prize = classPo.getDiscountAmount() != null ? classPo.getDiscountAmount() : classPo.getClassPrize();
        // 判断是否需要微信支付
        boolean waitPayFlag = false;
        Optional<EpOrganConfigPo> existOrganConfig = organConfigRepository.getByOgnId(classPo.getOgnId());
        if (existOrganConfig.isPresent()
                && existOrganConfig.get().getWechatPayFlag()
                && coursePo.getWechatPayFlag()
                && NumberTools.compareBigDecimal(prize, BigDecimal.ZERO)) {
            waitPayFlag = true;
        }
        // 创建订单记录
        EpOrderPo orderPo = new EpOrderPo();
        orderPo.setMemberId(memberId);
        orderPo.setChildId(childId);
        orderPo.setOgnId(classPo.getOgnId());
        orderPo.setCourseId(classPo.getCourseId());
        orderPo.setClassId(classId);
        orderPo.setPrize(prize);
        orderPo.setStatus(EpOrderStatus.save);
        if (waitPayFlag) {
            orderPo.setPayStatus(EpOrderPayStatus.wait_pay);
        }
        orderRepository.insert(orderPo);
        // 判断是否需要微信支付
        OrderDto result = new OrderDto();
        result.setOrderId(orderPo.getId());
        result.setWaitPayFlag(waitPayFlag);
        return resultDo.setResult(result);
    }

    /**
     * 分页查询孩子的订单和课程班次
     *
     * @param pageable
     * @param childId
     * @param statusEnum
     * @return
     */
    public Page<MemberChildClassBo> findChildClassPage(Pageable pageable, Long childId, ChildClassStatusEnum statusEnum) {
        Page<MemberChildClassBo> page = orderRepository.findChildClassPage(pageable, childId, statusEnum);
        if (CollectionsTools.isNotEmpty(page.getContent())) {
            for (MemberChildClassBo bo : page.getContent()) {
                // 课程宣传图
                Optional<EpFilePo> existPic = fileRepository.getOneByBizTypeAndSourceId(BizConstant.FILE_BIZ_TYPE_CODE_COURSE_MAIN_PIC, bo.getCourseId());
                String mainPicUrl = existPic.isPresent() ? existPic.get().getFileUrl() : null;
                bo.setMainPicUrl(mainPicUrl);
            }
        }
        return page;
    }

    /**
     * 分页查询孩子行程
     *
     * @param pageable
     * @param memberId
     * @return
     */
    public ResultDo<Page<MemberChildScheduleBo>> findChildSchedulePage(Pageable pageable, Long memberId) {
        ResultDo<Page<MemberChildScheduleBo>> resultDo = ResultDo.build();
        Page<MemberChildScheduleBo> page = orderRepository.findChildSchedulePage(pageable, memberId);
        if (CollectionsTools.isEmpty(page.getContent())) {
            return resultDo.setResult(page);
        }
        Map<Long, String> courseIdMap = Maps.newHashMap();
        Map<Long, MemberChildBo> childMap = Maps.newHashMap();
        for (MemberChildScheduleBo bo : page.getContent()) {
            // 课程主图
            if (!courseIdMap.containsKey(bo.getCourseId())) {
                Optional<EpFilePo> optional = fileRepository.getOneByBizTypeAndSourceId(BizConstant.FILE_BIZ_TYPE_CODE_COURSE_MAIN_PIC, bo.getCourseId());
                String mainPicUrl = optional.isPresent() ? optional.get().getFileUrl() : null;
                courseIdMap.put(bo.getCourseId(), mainPicUrl);
            }
            bo.setMainPicUrl(courseIdMap.get(bo.getCourseId()));
            // 孩子头像
            if (!childMap.containsKey(bo.getChildId())) {
                EpMemberChildPo childPo = memberChildRepository.getById(bo.getChildId());
                if (childPo == null || childPo.getDelFlag()) {
                    log.error("获取孩子行程失败, 孩子不存在，childId={}", bo.getChildId());
                    return resultDo.setError(MessageCode.ERROR_CHILD_NOT_EXISTS);
                }
                Optional<EpFilePo> existAvatar = fileRepository.getOneByBizTypeAndSourceId(BizConstant.FILE_BIZ_TYPE_CODE_CHILD_AVATAR, bo.getChildId());
                String childAvatar = existAvatar.isPresent() ? existAvatar.get().getFileUrl() : null;
                MemberChildBo childBo = new MemberChildBo();
                childBo.setChildNickName(childPo.getChildNickName());
                childBo.setAvatar(childAvatar);
                childMap.put(bo.getChildId(), childBo);
            }
            MemberChildBo child = childMap.get(bo.getChildId());
            bo.setAvatar(child.getAvatar());
            bo.setNickName(child.getChildNickName());
        }
        return resultDo.setResult(page);
    }

    /**
     * 商户后台获取分页
     *
     * @param pageable
     * @param conditions
     * @return
     */
    public Page<OrderBo> findbyPageAndCondition(Pageable pageable, Collection<? extends Condition> conditions) {
        return orderRepository.findbyPageAndCondition(pageable, conditions);
    }

    /**
     * 商户后台获取预约管理分页
     *
     * @param pageable
     * @param conditions
     * @return
     */
    public Page<OrderBo> findOrderBespeakByPageAndCondition(Pageable pageable, Collection<? extends Condition> conditions) {
        return orderRepository.findOrderBespeakByPageAndCondition(pageable, conditions);
    }

    /**
     * 单个订单报名成功
     *
     * @param id
     */
    @Transactional(rollbackFor = Exception.class)
    public ResultDo orderSuccessById(Long id) {
        log.info("[订单报名成功]开始，订单id={}", id);
        Optional<EpOrderPo> existOrder = orderRepository.findById(id);
        if (!existOrder.isPresent()) {
            return ResultDo.build(MessageCode.ERROR_ORDER_NOT_EXISTS);
        }
        EpOrderPo orderPo = existOrder.get();
        if (!EpOrderStatus.save.equals(orderPo.getStatus())) {
            return ResultDo.build(MessageCode.ERROR_OPERATE_FAIL);
        }
        if (orderPo.getPayStatus() != null && !EpOrderPayStatus.paid.equals(orderPo.getPayStatus())) {
            return ResultDo.build(MessageCode.ERROR_ORDER_PAY_STATUS_NOT_PAID);
        }
        int count = orderRepository.orderSuccessById(id);
        if (count == BizConstant.DB_NUM_ONE) {
            // 增加班次成功报名人数
            organClassRepository.updateEnteredNumWithEnterNotLimit(orderPo.getClassId(), count);
            // 增加课程总参与人数
            organCourseRepository.addTotalParticipate(orderPo.getCourseId(), count);
            // 增加机构总参与人数
            organRepository.addTotalParticipate(orderPo.getOgnId(), count);
            log.info("[订单]订单报名成功，订单id={},班次classId={}。", id, orderPo.getClassId());
            return ResultDo.build().setResult(orderPo);
        } else {
            log.error("[订单]订单报名失败，订单id={}", id);
            return ResultDo.build(MessageCode.ERROR_OPERATE_FAIL);
        }
    }

    /**
     * 批量报名（不同孩子的班次批量报名）
     *
     * @param pos
     */
    @Transactional(rollbackFor = Exception.class)
    public ResultDo batchOrderSuccess(List<EpOrderPo> pos) {
//        //按classId排序
//        Collections.sort(pos, new Comparator() {
//            @Override
//            public int compare(Object o1, Object o2) {
//                EpOrderPo po1 = (EpOrderPo) o1;
//                EpOrderPo po2 = (EpOrderPo) o2;
//                if (po1.getClassId().longValue() > po2.getClassId().longValue()) {
//                    return 1;
//                } else if (po1.getClassId().longValue() == po2.getClassId().longValue()) {
//                    return 0;
//                } else {
//                    return -1;
//                }
//            }
//        });
//        //不同classId的记录封装在不同的map中
//        Map<Long, Object> map = Maps.newHashMap();
//        List<EpOrderPo> list = Lists.newArrayList();
//        Long classId = pos.get(0).getClassId();
//        for (int i = 0; i < pos.size(); i++) {
//            if (classId.longValue() == pos.get(i).getClassId().longValue()) {
//                list.add(pos.get(i));
//            } else {
//                map.put(classId, list);
//                list = Lists.newArrayList();
//                classId = pos.get(i).getClassId();
//                list.add(pos.get(i));
//            }
//            map.put(classId, list);
//        }
//        //不同班次的孩子批量报名
//        log.info("[订单],订单批量报名成功，pos={}。", pos);
//        //相同班次批量报名
//        map.keySet().forEach(key -> {
//            List<EpOrderPo> orderPos = (List<EpOrderPo>) map.get(key);
//            List<Long> classlist = Lists.newArrayList();
//            for (int i = 0; i < orderPos.size(); i++) {
//                classlist.add(orderPos.get(i).getId());
//            }
//            int count = orderRepository.orderSuccessByIds(classlist);
//            organClassRepository.updateEnteredNumByorderSuccess(key, count);
//        });
//        log.info("[订单],订单批量报名成功，pos={}。", pos);
        return ResultDo.build();
    }

    /**
     * 订单拒绝
     *
     * @param id
     */
    public ResultDo orderRefuseById(Long id, String remark) {
        log.info("[订单]订单拒绝操作开始，订单id={},remark={}。", id, remark);
        if (orderRepository.orderRefuseById(id, remark) == BizConstant.DB_NUM_ONE) {
            log.info("[订单]订单拒绝操作成功，订单id={}。", id);
            return ResultDo.build();
        } else {
            log.info("[订单]订单拒绝操作失败，订单id={}。", id);
            return ResultDo.build(MessageCode.ERROR_OPERATE_FAIL);
        }
    }

    /**
     * 根据id获取EpOrderPo
     *
     * @param id
     * @return
     */
    public Optional<EpOrderPo> findById(Long id) {
        return orderRepository.findById(id);
    }

    /**
     * 根据id取消报名成功/拒绝的订单
     *
     * @param id
     */
    @Transactional(rollbackFor = Exception.class)
    public ResultDo orderCancelById(Long id) {
        Optional<OrderTypeBo> orderOptional = orderRepository.getOrderTypeBoById(id);
        if (!orderOptional.isPresent()) {
            return ResultDo.build(MessageCode.ERROR_ORDER_NOT_EXISTS);
        }
        EpOrderStatus status = orderOptional.get().getStatus();
        String logAction = "";
        if ((status.equals(EpOrderStatus.success) || status.equals(EpOrderStatus.refuse))) {
            logAction = status.equals(EpOrderStatus.success) ? "报名成功" : "拒绝";
            log.info("[订单]订单取消{}操作开始，订单id={}，status={}。", logAction, id, status);
        } else {
            log.error("[订单]订单取消报名成功/拒绝操作失败，订单状态错误，订单id={}，status={}。", id, status);
            return ResultDo.build(MessageCode.ERROR_ORDER_CANCEL_STATUS_WRONG);
        }
        EpOrderPo orderPo = orderRepository.findById(id).get();
        //行锁班次记录，防止班次下线并发
        EpOrganClassPo classPo = organClassRepository.getByIdAndStatusLock(orderPo.getClassId(), EpOrganClassStatus.online);
        if (classPo == null) {
            log.error("[订单]订单取消{}操作失败，未获取到班次行级锁(班次不存在)，订单id={},班次classId={}。", logAction, id, orderPo.getClassId());
            return ResultDo.build(MessageCode.ERROR_OPERATE_FAIL);
        }
        int count = orderRepository.orderCancelById(id, status);
        if (count == BizConstant.DB_NUM_ONE && status.equals(EpOrderStatus.success)) {
            // 班次成功报名人数扣减
            int subEnteredNum = organClassRepository.enteredNumByOrderCancel(orderPo.getClassId(), count);
            // 课程总参加人数扣减
            int subCourseTotalParticipate = organCourseRepository.totalParticipateCancel(orderPo.getCourseId(), count);
            // 机构总参加人数扣减
            int subOrganTotalParticipate = organRepository.totalParticipateCancel(orderPo.getOgnId(), count);
            if (subEnteredNum == BizConstant.DB_NUM_ONE && subCourseTotalParticipate == BizConstant.DB_NUM_ONE
                    && subOrganTotalParticipate == BizConstant.DB_NUM_ONE) {
                log.info("[订单]订单取消{}操作成功，订单id={}。", logAction, id);
                return ResultDo.build();
            } else {
                log.error("[订单]订单取消{}操作失败，班次成功报名人数扣减或课程总参加人数扣减或机构总参加人数扣减失败。", logAction);
                throw new ServiceRuntimeException("班次成功报名人数扣减或课程总参加人数扣减或机构总参加人数扣减异常");
            }
        } else if (count == BizConstant.DB_NUM_ONE && status.equals(EpOrderStatus.refuse)) {
            log.info("[订单]订单取消{}操作成功，订单id={}。", logAction, id);
            return ResultDo.build();
        } else {
            log.error("[订单]订单取消{}操作失败，订单id={}。", logAction, id);
            return ResultDo.build(MessageCode.ERROR_OPERATE_FAIL);
        }
    }

    /**
     * 根据班次id获取该班次下订单详情
     *
     * @param ognId
     * @param classId
     * @return
     */
    public List<OrderBo> findOrdersByClassId(Long ognId, Long classId) {
        return orderRepository.findOrdersByClassId(ognId, classId);
    }

    /**
     * 提交预约
     *
     * @param id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResultDo orderBespeakById(Long id) {
        log.info("[订单]订单提交预约开始，订单id={}。", id);
        Optional<OrderTypeBo> orderOptional = orderRepository.getOrderTypeBoById(id);
        if (!orderOptional.isPresent()) {
            log.info("[订单]订单提交预约失败，订单不存在，订单id={}。", id);
            return ResultDo.build(MessageCode.ERROR_ORDER_NOT_EXISTS);
        }
        if (!orderOptional.get().getType().equals(EpOrganClassType.bespeak)) {
            log.error("[订单]订单提交预约失败，班次类型不是预约类型，订单id={}。", id);
            return ResultDo.build(MessageCode.ERROR_SYSTEM_PARAM_FORMAT);
        }
        if (orderRepository.orderBespeakById(id) == BizConstant.DB_NUM_ONE) {
            EpOrganClassChildPo epOrganClassChildPo = new EpOrganClassChildPo();
            OrderTypeBo orderTypeBo = orderOptional.get();
            epOrganClassChildPo.setChildId(orderTypeBo.getChildId());
            epOrganClassChildPo.setClassId(orderTypeBo.getClassId());
            epOrganClassChildPo.setOrderId(orderTypeBo.getId());
            //班次孩子表插入数据
            organClassChildRepository.insert(epOrganClassChildPo);
            log.info("[订单]订单提交预约成功，订单id={}。", id);
            return ResultDo.build();
        } else {
            log.error("[订单]订单提交预约失败，订单id={}。", id);
            return ResultDo.build(MessageCode.ERROR_OPERATE_FAIL);
        }
    }


    /**
     * 退单
     *
     * @param id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResultDo orderBreak(Long id, BigDecimal refundAmount, Long firstClassCatalogId) {
        log.info("[订单]订单退单开始，订单id={}。", id);
        //订单结束
        if (orderRepository.endById(id, refundAmount) == BizConstant.DB_NUM_ONE) {
            //关闭该订单下行程从选择目录开始后的所有行程
            if (null != firstClassCatalogId) {
                organClassScheduleRepository.closeByOrderIdAndFirstClassCatalogId(id, firstClassCatalogId);
            }
            log.info("[订单]订单退单成功，订单id={}。", id);
            return ResultDo.build();
        } else {
            log.error("[订单]订单退单失败，订单id={}。", id);
            return ResultDo.build(MessageCode.ERROR_OPERATE_FAIL);
        }
    }

    /**
     * 商户后台获取订单学员统计
     *
     * @param pageable
     * @param condition
     * @return
     */
    public Page<OrderChildStatisticsDto> statisticsChild(Pageable pageable, Collection<? extends Condition> condition) {
        return orderRepository.statisticsChild(pageable, condition);
    }

    /**
     * 孩子已参加的班次
     *
     * @param childId
     */
    public List<OrganClassBo> findEnteredClassByChildId(Long ognId, Long childId, EpOrganClassType classType) {
        return orderRepository.findEnteredClassByChildId(ognId, childId, classType);
    }

    /**
     * 根据搜索条件导出excel
     *
     * @param pageable
     * @param condition
     * @return
     */
    public void indexExportExcel(HttpServletRequest request, HttpServletResponse response, String fileName, Pageable pageable, Collection<? extends Condition> condition) {
        log.info("[订单]导出excel开始。");
        List<Field<?>> fieldList = Lists.newArrayList();
        fieldList.add(EP_MEMBER.MOBILE);
        fieldList.add(EP_MEMBER_CHILD.CHILD_TRUE_NAME);
        fieldList.add(EP_MEMBER_CHILD.CHILD_NICK_NAME);
        fieldList.add(EP_ORGAN_COURSE.COURSE_NAME);
        fieldList.add(EP_ORGAN_CLASS.CLASS_NAME);
        fieldList.add(EP_ORGAN_CLASS.TYPE.as("classType"));
        fieldList.add(EP_ORGAN_CLASS.STATUS.as("classStatus"));
        fieldList.add(EP_ORDER.PRIZE);
        fieldList.add(EP_ORDER.STATUS);
        fieldList.add(EP_ORDER.REMARK);
        fieldList.add(EP_ORDER.CREATE_AT);
        List<OrderExcelBo> list = orderRepository.indexExportExcel(fieldList, pageable, condition);
        List<String> fieldNameStrs = Lists.newArrayList();
        fieldNameStrs.add("mobile");
        fieldNameStrs.add("childTrueName");
        fieldNameStrs.add("childNickName");
        fieldNameStrs.add("courseName");
        fieldNameStrs.add("className");
        fieldNameStrs.add("classTypeText");
        fieldNameStrs.add("classStatusText");
        fieldNameStrs.add("prize");
        fieldNameStrs.add("statusText");
        fieldNameStrs.add("remark");
        fieldNameStrs.add("fmtCreateAt");
        String[] titles = {"会员账号", "姓名", "昵称", "产品", "班次", "班次类型", "班次状态", "价格", "订单状态", "备注", "创建时间"};
        try {
            ExcelUtil.exportExcel(request, response, fileName, fieldList.size(), list, fieldNameStrs, titles);
        } catch (Exception e) {
            log.error("[订单]导出excel失败", e);
        }
        log.info("[订单]导出excel成功。");
    }

    /**
     * 批量拒绝订单
     *
     * @param ids
     * @param ognId
     * @return
     */
    public ResultDo batchRefuse(List<Long> ids, String batchRefuseRemark, Long ognId) {
        log.info("[订单]，订单批量拒绝开始，ids={}。", ids);
        int count = orderRepository.batchRefuseByIds(ids, batchRefuseRemark, ognId);
        log.info("[订单]，订单批量拒绝结束，拒绝订单数={}。", count);
        return ResultDo.build();
    }

    /**
     * 机构总订单数
     *
     * @param ognId
     * @return
     */
    public long countSaveOrder(Long ognId) {
        return orderRepository.countSaveOrder(ognId);
    }

    /**
     * @param ognId
     * @param homeMonthSize
     * @return
     */
    public Map<String, Object> homeOrderChart(Long ognId, int homeMonthSize, int homeRecentlyDays) {
        Map<String, Object> resultMap = Maps.newHashMap();
        List<Long> saveOrderCounts = Lists.newArrayList();
        for (int i = 0; i < homeMonthSize; i++) {
            long count = orderRepository.countAdvanceMonthOrder(ognId, homeMonthSize - BizConstant.DB_NUM_ONE - i);
            saveOrderCounts.add(count);
        }
        //最近6个月内报名数
        resultMap.put("saveOrderCounts", saveOrderCounts);
        List<Long> successOrderCounts = Lists.newArrayList();
        for (int i = 0; i < homeMonthSize; i++) {
            long count = orderRepository.countAdvanceMonthSuccessOrder(ognId, homeMonthSize - BizConstant.DB_NUM_ONE - i);
            successOrderCounts.add(count);
        }
        //最近6个月内报名成功数
        resultMap.put("successOrderCounts", successOrderCounts);
        //机构所有订单数
        resultMap.put("allOrderCount", orderRepository.countAllOrders(ognId));
        //机构最近几天内的订单数
        resultMap.put("ordersRecentlyCount", orderRepository.countOrdersRecently(ognId, homeRecentlyDays));
        //机构最近几天内的订单销售额
        resultMap.put("orderPrizeSum", orderRepository.sumOrderPrize(ognId, homeRecentlyDays));
        return resultMap;
    }

    /**
     * 微信支付生成预支付订单
     *
     * @param memberId
     * @param openid
     * @param orderId
     * @param ip
     * @return
     */
    public ResultDo prePayByWechatPay(Long memberId, String openid, Long orderId, String ip) throws Exception {
        log.info("【小程序报名支付】订单校验开始, orderId={}", orderId);
        // 校验订单
        Optional<EpOrderPo> existOrder = orderRepository.findById(orderId);
        if (!existOrder.isPresent()) {
            log.error("【小程序报名支付】订单不存在, orderId={}", orderId);
            return ResultDo.build(MessageCode.ERROR_ORDER_NOT_EXISTS);
        }
        EpOrderPo orderPo = existOrder.get();
        if (!orderPo.getMemberId().equals(memberId)) {
            log.error("【小程序报名支付】订单不属于当前登录会员, orderId={}, orderMember={}, currentMember", orderId, orderPo.getMemberId(), memberId);
            return ResultDo.build(MessageCode.ERROR_ORDER_NOT_EXISTS);
        }
        if (!NumberTools.compareBigDecimal(orderPo.getPrize(), BigDecimal.ZERO)) {
            log.error("【小程序报名支付】订单金额不需支付, orderId={}, orderPrize", orderId, orderPo.getPrize());
            return ResultDo.build(MessageCode.ERROR_WECHAT_ORDER_NEED_NOT_PAY);
        }
        if (!EpOrderStatus.save.equals(orderPo.getStatus())) {
            log.error("【小程序报名支付】订单状态不是“保存”状态, orderId={}, status={}", orderId, orderPo.getStatus());
            return ResultDo.build(MessageCode.ERROR_WECHAT_ORDER_NEED_NOT_PAY);
        }
        if (EpOrderPayStatus.paid.equals(orderPo.getPayStatus())) {
            log.error("【小程序报名支付】订单已支付, orderId={}", orderId);
            return ResultDo.build(MessageCode.ERROR_WECHAT_ORDER_IS_PAID);
        }
        EpOrganPo organPo = organRepository.getById(orderPo.getOgnId());
        // 校验机构
        if (organPo == null || organPo.getDelFlag()) {
            log.error("【小程序报名支付】机构不存在, orderId={}, ognId={}", orderId, orderPo.getOgnId());
            return ResultDo.build(MessageCode.ERROR_ORGAN_NOT_EXISTS);
        }
        return wechatPayComponent.xcxUnifiedorder(orderId, organPo.getOgnName(), orderPo.getPrize(), openid, ip);
    }

    /**
     * 根据订单id获取支付订单信息
     *
     * @param memberId
     * @param orderId
     * @return
     */
    public ResultDo<OrderPayInfoBo> getPayInfo(Long memberId, Long orderId) {
        EpOrderPo orderPo = orderRepository.getById(orderId);
        if (orderPo == null || orderPo.getDelFlag() || !orderPo.getMemberId().equals(memberId)) {
            ResultDo.build(MessageCode.ERROR_ORDER_NOT_EXISTS);
        }
        OrderPayInfoBo payInfoBo = new OrderPayInfoBo();
        EpOrganCoursePo coursePo = organCourseRepository.getById(orderPo.getCourseId());
        EpOrganClassPo classPo = organClassRepository.getById(orderPo.getClassId());
        EpMemberChildPo childPo = memberChildRepository.getById(orderPo.getChildId());
        payInfoBo.setId(orderId);
        payInfoBo.setOgnId(orderPo.getOgnId());
        payInfoBo.setCourseId(orderPo.getCourseId());
        payInfoBo.setPrize(orderPo.getPrize());
        payInfoBo.setCourseName(coursePo.getCourseName());
        payInfoBo.setClassName(classPo.getClassName());
        payInfoBo.setChildNickName(childPo.getChildNickName());
        payInfoBo.setPayType(orderPo.getPayType());
        payInfoBo.setPayStatus(orderPo.getPayStatus());
        ResultDo<OrderPayInfoBo> resultDo = ResultDo.build();
        return resultDo.setResult(payInfoBo);
    }

    /**
     * 确认线下支付已完成
     * @param orderId
     * @param payConfirmTime
     * @return
     */
    public ResultDo offlinePaidByOrderId(Long orderId, Timestamp payConfirmTime) {
        log.info("[订单]确认线下支付已完成开始，orderId={},payConfirmTime={}。", orderId, payConfirmTime);
        if (orderRepository.offlinePaidByOrderId(orderId, payConfirmTime) == BizConstant.DB_NUM_ONE) {
            log.info("[订单]确认线下支付已完成成功，orderId={},payConfirmTime={}。", orderId, payConfirmTime);
            return ResultDo.build();
        } else {
            log.error("[订单]确认线下支付已完成失败，orderId={},payConfirmTime={}。", orderId, payConfirmTime);
            return ResultDo.build(MessageCode.ERROR_OPERATE_FAIL);
        }
    }

    /**
     * 统计班次内微信支付成功订单数
     *
     * @param classId
     * @return
     */
    public int countWechatPaidOrderByClassId(Long classId) {
        return orderRepository.countWechatPaidOrderByClassId(classId);
    }

    public long countByClassIdAndPayTypeAndPayStatus(Long classId, EpOrderPayType orderPayType, EpOrderPayStatus orderPayStatus, Timestamp endTime) {
        return orderRepository.countByClassIdAndPayTypeAndPayStatus(classId, orderPayType, orderPayStatus, endTime);
    }

    public int countWaitWithdrawOrderByClassId(Long classId, Timestamp endTime) {
        return orderRepository.countWaitWithdrawOrderByClassId(classId, endTime);
    }

    public BigDecimal sumWaitWithdrawOrderByClassId(Long classId, Timestamp endTime) {
        return orderRepository.sumWaitWithdrawOrderByClassId(classId, endTime);
    }

    public BigDecimal sumWaitWithdrawPoundageByClassId(Long classId, Timestamp endTime) {
        return orderRepository.sumWaitWithdrawPoundageByClassId(classId, endTime);
    }

    public BigDecimal sumWechatPaidOrderTotalFee(Long classId, Timestamp endTime) {
        return orderRepository.sumWechatPaidOrderTotalFeeByClassId(classId, endTime);
    }


    public BigDecimal sumWechatPoundage(Long classId, Timestamp endTime) {
        return orderRepository.sumWechatPoundageByClassId(classId, endTime);
    }

    public BigDecimal sumOfflinePaidOrderTotalFee(Long classId, Timestamp endTime) {
        return orderRepository.sumOfflinePaidOrderTotalFee(classId, endTime);
    }

//    public Page<ClassWithdrawQueryDto> findClassWithdrawQueryDtoByPage(Pageable pageable, Collection<? extends Condition> condition) {
//        return orderRepository.findClassWithdrawQueryDtoByPage(pageable, condition);
//    }


}
