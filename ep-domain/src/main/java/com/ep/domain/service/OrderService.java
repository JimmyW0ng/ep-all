package com.ep.domain.service;

import com.ep.common.tool.CollectionsTools;
import com.ep.common.tool.DateTools;
import com.ep.domain.constant.BizConstant;
import com.ep.domain.constant.MessageCode;
import com.ep.domain.enums.ChildClassStatusEnum;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.MemberChildClassBo;
import com.ep.domain.pojo.bo.MemberChildScheduleBo;
import com.ep.domain.pojo.bo.MemberCourseOrderInitBo;
import com.ep.domain.pojo.bo.OrderBo;
import com.ep.domain.pojo.po.*;
import com.ep.domain.repository.*;
import com.ep.domain.repository.domain.enums.EpOrderStatus;
import com.ep.domain.repository.domain.enums.EpOrganCourseCourseStatus;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.jooq.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
    private OrganCourseRepository organCourseRepository;
    @Autowired
    private FileRepository fileRepository;

    /**
     * 加载会员下单需要的数据
     *
     * @param memberId
     * @param courseId
     * @return
     */
    public ResultDo<List<MemberCourseOrderInitBo>> initInfo(Long memberId, Long courseId) {
        ResultDo<List<MemberCourseOrderInitBo>> resultDo = ResultDo.build();
        EpOrganCoursePo coursePo = organCourseRepository.getById(courseId);
        if (coursePo == null || coursePo.getDelFlag()) {
            return resultDo.setError(MessageCode.ERROR_COURSE_NOT_EXIST);
        } else if (!coursePo.getCourseStatus().equals(EpOrganCourseCourseStatus.online)) {
            return resultDo.setError(MessageCode.ERROR_COURSE_NOT_EXIST);
        }
        List<MemberCourseOrderInitBo> data = orderRepository.findChildrenAndOrders(memberId, courseId);
        if (CollectionsTools.isNotEmpty(data)) {
            for (MemberCourseOrderInitBo initBo : data) {
                Optional<EpFilePo> existAvatar = fileRepository.getOneByBizTypeAndSourceId(BizConstant.FILE_BIZ_TYPE_CODE_CHILD_AVATAR, initBo.getChildId());
                if (existAvatar.isPresent()) {
                    initBo.setAvatar(existAvatar.get().getFileUrl());
                }
            }
        }
        return resultDo.setResult(data);
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
    public ResultDo order(Long memberId, Long childId, Long classId) {
        ResultDo<?> resultDo = ResultDo.build();
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
        // 查询课程信息
        EpOrganCoursePo coursePo = organCourseRepository.getById(classPo.getCourseId());
        // 校验课程状态
        if (coursePo.getCourseStatus().equals(EpOrganCourseCourseStatus.save)) {
            log.error("下单失败，课程未上线！");
            resultDo.setError(MessageCode.ERROR_COURSE_NOT_ONLINE);
            return resultDo;
        }
        if (coursePo.getCourseStatus().equals(EpOrganCourseCourseStatus.offline)) {
            log.error("下单失败，课程已下线！");
            resultDo.setError(MessageCode.ERROR_COURSE_IS_OFF);
            return resultDo;
        }
        // 校验报名时间
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
        // 创建订单记录
        EpOrderPo orderPo = new EpOrderPo();
        orderPo.setMemberId(memberId);
        orderPo.setChildId(childId);
        orderPo.setOgnId(classPo.getOgnId());
        orderPo.setCourseId(classPo.getCourseId());
        orderPo.setClassId(classId);
        orderPo.setPrize(classPo.getClassPrize());
        orderPo.setStatus(EpOrderStatus.save);
        orderRepository.insert(orderPo);
        return resultDo;
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
            Map<Long, String> courseIdMap = Maps.newHashMap();
            for (MemberChildClassBo bo : page.getContent()) {
                String mainPicUrl;
                if (!courseIdMap.containsKey(bo.getCourseId())) {
                    Optional<EpFilePo> optional = fileRepository.getOneByBizTypeAndSourceId(BizConstant.FILE_BIZ_TYPE_CODE_COURSE_MAIN_PIC, bo.getCourseId());
                    mainPicUrl = optional.isPresent() ? optional.get().getFileUrl() : null;
                } else {
                    mainPicUrl = courseIdMap.get(bo.getCourseId());
                }
                bo.setMainPicUrl(mainPicUrl);
            }
        }
        return page;
    }

    /**
     * 分页查询孩子行程
     *
     * @param pageable
     * @param childId
     * @return
     */
    public Page<MemberChildScheduleBo> findChildSchedulePage(Pageable pageable, Long childId) {
        Page<MemberChildScheduleBo> page = orderRepository.findChildSchedulePage(pageable, childId);
        if (CollectionsTools.isNotEmpty(page.getContent())) {
            Map<Long, String> courseIdMap = Maps.newHashMap();
            for (MemberChildScheduleBo bo : page.getContent()) {
                String mainPicUrl;
                if (!courseIdMap.containsKey(bo.getCourseId())) {
                    Optional<EpFilePo> optional = fileRepository.getOneByBizTypeAndSourceId(BizConstant.FILE_BIZ_TYPE_CODE_COURSE_MAIN_PIC, bo.getCourseId());
                    mainPicUrl = optional.isPresent() ? optional.get().getFileUrl() : null;
                } else {
                    mainPicUrl = courseIdMap.get(bo.getCourseId());
                }
                bo.setMainPicUrl(mainPicUrl);
            }
        }
        return page;
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
     * 订单报名成功
     *
     * @param id
     */
    @Transactional(rollbackFor = Exception.class)
    public int orderSuccessById(Long id, Long classId) {
        int count = orderRepository.orderSuccessById(id);
        organClassRepository.updateEnteredNumByorderSuccess(classId, count);
        return count;
    }

    /**
     * 批量报名（不同孩子的班次批量报名）
     *
     * @param map
     */
    @Transactional(rollbackFor = Exception.class)
    public void batchOrderSuccess(Map<Long, Object> map) {
        //相同班次批量报名
        map.keySet().forEach(key -> {
            List<EpOrderPo> pos = (List<EpOrderPo>) map.get(key);
            List<Long> list = Lists.newArrayList();
            for (int i = 0; i < pos.size(); i++) {
                list.add(pos.get(i).getId());
            }
            int count = orderRepository.orderSuccessByIds(list);
            organClassRepository.updateEnteredNumByorderSuccess(key, count);
        });
    }

    /**
     * 订单拒绝
     *
     * @param id
     */
    public int orderRefuseById(Long id, String remark) {
        return orderRepository.orderRefuseById(id, remark);
    }

    /**
     * 根据id获取EpOrderPo
     *
     * @param id
     * @return
     */
    public EpOrderPo getById(Long id) {
        return orderRepository.findById(id);
    }

    /**
     * 根据id取消报名成功/拒绝的订单
     * @param id
     * @param status
     */
    @Transactional(rollbackFor = Exception.class)
    public void orderCancelById(Long id, EpOrderStatus status) {
        Long classId = orderRepository.findById(id).getClassId();
        int count = orderRepository.orderCancelById(id, status);
        if (status.equals(EpOrderStatus.success)) {
            organClassRepository.enteredNumByOrderCancel(classId, count);
        }
    }
}
