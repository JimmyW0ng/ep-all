package com.ep.domain.service;

import com.ep.common.tool.CollectionsTools;
import com.ep.common.tool.DateTools;
import com.ep.domain.constant.BizConstant;
import com.ep.domain.constant.MessageCode;
import com.ep.domain.enums.ChildClassStatusEnum;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.MemberChildClassBo;
import com.ep.domain.pojo.po.EpMemberChildPo;
import com.ep.domain.pojo.po.EpOrderPo;
import com.ep.domain.pojo.po.EpOrganClassPo;
import com.ep.domain.pojo.po.EpOrganCoursePo;
import com.ep.domain.repository.MemberChildRepository;
import com.ep.domain.repository.OrderRepository;
import com.ep.domain.repository.OrganClassRepository;
import com.ep.domain.repository.OrganCourseRepository;
import com.ep.domain.repository.domain.enums.EpOrderStatus;
import com.ep.domain.repository.domain.enums.EpOrganCourseCourseStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

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
            resultDo.setError(MessageCode.ERROR_COURSE_NOT_EXISTS);
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
        return orderRepository.findChildClassPage(pageable, childId, statusEnum);
    }
}
