package com.ep.domain.service;

import com.ep.common.tool.CollectionsTools;
import com.ep.domain.constant.BizConstant;
import com.ep.domain.constant.MessageCode;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.OrganClassBo;
import com.ep.domain.pojo.dto.ClassWithdrawQueryDto;
import com.ep.domain.pojo.event.ClassOpenEventBo;
import com.ep.domain.pojo.po.*;
import com.ep.domain.repository.*;
import com.ep.domain.repository.domain.enums.EpOrderStatus;
import com.ep.domain.repository.domain.enums.EpOrganClassStatus;
import com.ep.domain.repository.domain.enums.EpOrganClassType;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.jooq.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @Description: 机构课程班次Service
 * @Author: CC.F
 * @Date: 17:12 2018/2/11
 */
@Service
@Slf4j
public class OrganClassService {

    @Autowired
    private OrganCourseRepository organCourseRepository;
    @Autowired
    private OrganClassRepository organClassRepository;
    @Autowired
    private OrganClassChildRepository organClassChildRepository;
    @Autowired
    private OrganClassCatalogRepository organClassCatalogRepository;
    @Autowired
    private OrganClassScheduleRepository organClassScheduleRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrganCatalogRepository organCatalogRepository;
    @Autowired
    private ApplicationEventPublisher publisher;

    public List<EpOrganClassPo> findByCourseId(Long courseId){
        return organClassRepository.getByCourseId(courseId);
    }

    public Page<OrganClassBo> findbyPageAndCondition(Pageable pageable, Collection<? extends Condition> condition) {
        return organClassRepository.findbyPageAndCondition(pageable, condition);
    }

    /**
     * 开班
     *
     * @param currentUser
     * @param id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResultDo openById(EpSystemUserPo currentUser, Long id) {
        log.info("班次开班, sysUserId={}, classId={}", currentUser.getId(), id);
        // 校验班次
        EpOrganClassPo classPo = organClassRepository.getById(id);
        if (classPo == null || classPo.getDelFlag()) {
            log.error("班次不存在, classId={}", id);
            return ResultDo.build(MessageCode.ERROR_CLASS_NOT_EXIST);
        }
        // 预约类班次不允许开班操作
        if (classPo.getType().equals(EpOrganClassType.bespeak)) {
            log.error("预约类班次不允许开班操作, classId={}", id);
            return ResultDo.build(MessageCode.ERROR_CLASS_BESPEAK_NOT_ALLOW_OPEN);
        }
        if (!classPo.getStatus().equals(EpOrganClassStatus.online)) {
            log.error("班次不是已上线状态, classId={}, status={}", id, classPo.getStatus());
            return ResultDo.build(MessageCode.ERROR_CLASS_NOT_ONLINE);
        }
        if (!classPo.getOgnId().equals(currentUser.getOgnId())) {
            log.error("课程与当前操作机构不匹配, classId={}, classOgnId={}, userOgnId={}", id, classPo.getOgnId(), currentUser.getOgnId());
            return ResultDo.build(MessageCode.ERROR_COURSE_OGN_NOT_MATCH);
        }
        if (classPo.getEnteredNum() < BizConstant.DB_NUM_ONE) {
            log.error("成功报名人数小于1, classId={}, enteredNum={}", id, classPo.getEnteredNum());
            return ResultDo.build(MessageCode.ERROR_CLASS_NOT_EXIST_SUCCESS_ORDER);
        }
        // 校验是否还有未处理的订单
        List<EpOrderPo> savedOrders = orderRepository.findSavedOrdersByClassId(id);
        if (CollectionsTools.isNotEmpty(savedOrders)) {
            log.error("存在未处理的订单, classId={}, savedOrderNum={}", id, savedOrders.size());
            return ResultDo.build(MessageCode.ERROR_COURSE_EXIST_SAVED_ORDER);
        }
        List<EpOrderPo> openingOrders = orderRepository.findSuccessOrdersByClassId(id);
        if (CollectionsTools.isEmpty(openingOrders)) {
            log.error("班次内未发现报名成功的订单, classId={}", id);
            return ResultDo.build(MessageCode.ERROR_CLASS_SUCCESS_ORDER_NOT_EXISTS);
        }
        // 校验是否有重复的孩子成功订单
        List<Long> childMap = Lists.newArrayList();
        for (EpOrderPo orderPo : openingOrders) {
            if (childMap.contains(orderPo.getChildId())) {
                log.error("存在重复的班次成员成功订单, classId={}, childId={}", id, orderPo.getChildId());
                return ResultDo.build(MessageCode.ERROR_CLASS_OPENING_ORDER_DUPLICATE);
            }
            childMap.add(orderPo.getChildId());
        }
        // 开班
        int num = organClassRepository.openById(id);
        if (num == BizConstant.DB_NUM_ZERO) {
            return ResultDo.build();
        }
        // 更新订单状态为已开班
        orderRepository.openByClassId(id);
        // 生成班级孩子记录
        List<EpOrganClassChildPo> classChildPos = Lists.newArrayList();
        for (EpOrderPo orderPo : openingOrders) {
            EpOrganClassChildPo classChildPo = new EpOrganClassChildPo();
            classChildPo.setClassId(id);
            classChildPo.setChildId(orderPo.getChildId());
            classChildPo.setOrderId(orderPo.getId());
            classChildPos.add(classChildPo);
        }
        if (classChildPos.size() > BizConstant.NUM_ONE_HUNDRED) {
            List<List<EpOrganClassChildPo>> partClassChildList = Lists.partition(classChildPos, BizConstant.NUM_ONE_HUNDRED);
            for (List<EpOrganClassChildPo> data : partClassChildList) {
                organClassChildRepository.insert(data);
            }
        } else {
            organClassChildRepository.insert(classChildPos);
        }
        ClassOpenEventBo eventBo = new ClassOpenEventBo(id, openingOrders);
        publisher.publishEvent(eventBo);
        return ResultDo.build();
    }

    /**
     * 班次结束
     *
     * @param currentUser
     * @param id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResultDo endById(EpSystemUserPo currentUser, Long id) {
        log.info("班次结束开始, sysUserId={}, classId={}", currentUser.getId(), id);
        // 校验班次
        EpOrganClassPo classPo = organClassRepository.getById(id);
        log.info("班次状态, status={}, classId={}", classPo.getStatus(), id);
        if (classPo == null || classPo.getDelFlag()) {
            log.error("班次不存在, classId={}", id);
            return ResultDo.build(MessageCode.ERROR_CLASS_NOT_EXIST);
        }
        if (!classPo.getStatus().equals(EpOrganClassStatus.online) && !classPo.getStatus().equals(EpOrganClassStatus.opening)) {
            log.error("班次不是已开班状态, classId={}, status={}", id, classPo.getStatus());
            return ResultDo.build(MessageCode.ERROR_CLASS_NOT_ONLINE_OR_OPENING);
        }
        if (!classPo.getOgnId().equals(currentUser.getOgnId())) {
            log.error("课程与当前操作机构不匹配, classId={}, classOgnId={}, userOgnId={}", id, classPo.getOgnId(), currentUser.getOgnId());
            return ResultDo.build(MessageCode.ERROR_COURSE_OGN_NOT_MATCH);
        }
        // 不允许存在未处理和成功的订单
        List<EpOrderPo> orders = orderRepository.findByClassIdAndOrderStatus(id, EpOrderStatus.save, EpOrderStatus.success);
        if (CollectionsTools.isNotEmpty(orders)) {
            return ResultDo.build(MessageCode.ERROR_CLASS_EXIST_SAVED_SUCCESS_ORDER);
        }
        // 结束班次
        int num = organClassRepository.endById(id);
        if (num == BizConstant.DB_NUM_ZERO) {
            return ResultDo.build();
        }
        // 结束订单
        int endNum = orderRepository.endByClassId(id);
        log.info("结束订单数{}笔, classId={}", endNum, id);
        // 结束行程
        organClassScheduleRepository.endByClassId(id);
        // 如果没有上线、开班状态的班次了则下线课程
        Long courseId = classPo.getCourseId();
        List<EpOrganClassPo> openingClasses = organClassRepository.getByCourseIdAndStatus(courseId, EpOrganClassStatus.online, EpOrganClassStatus.opening);
        if (CollectionsTools.isNotEmpty(openingClasses)) {
            return ResultDo.build();
        }
        log.info("课程班次已全部结束，课程状态更新为下线, courseId={}", courseId);
        organCourseRepository.offlineById(courseId);
        // 刷新机构类目
        EpOrganCoursePo coursePo = organCourseRepository.getById(courseId);
        if (!organCourseRepository.existCatalog(coursePo.getOgnId(), coursePo.getCourseCatalogId())) {
            organCatalogRepository.deletePhysicByOgnIdAndCatalogId(coursePo.getOgnId(), coursePo.getCourseCatalogId());
        }
        return ResultDo.build();
    }

    /**
     * 撤班
     *
     * @param currentUser
     * @param id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResultDo cancelById(EpSystemUserPo currentUser, Long id) {
        log.info("撤班开始, sysUserId={}, classId={}", currentUser.getId(), id);
        // 校验班次
        EpOrganClassPo classPo = organClassRepository.getById(id);
        log.info("班次状态, status={}, classId={}", classPo.getStatus(), id);
        if (classPo == null || classPo.getDelFlag()) {
            log.error("班次不存在, classId={}", id);
            return ResultDo.build(MessageCode.ERROR_CLASS_NOT_EXIST);
        }
        if (!classPo.getStatus().equals(EpOrganClassStatus.online)) {
            log.error("班次不是已上线状态, classId={}, status={}", id, classPo.getStatus());
            return ResultDo.build(MessageCode.ERROR_CLASS_NOT_ONLINE);
        }
        if (!classPo.getOgnId().equals(currentUser.getOgnId())) {
            log.error("课程与当前操作机构不匹配, classId={}, classOgnId={}, userOgnId={}", id, classPo.getOgnId(), currentUser.getOgnId());
            return ResultDo.build(MessageCode.ERROR_COURSE_OGN_NOT_MATCH);
        }
        // 校验是否还有未处理、成功、开始、结束的订单
        Integer countNormalOrders = orderRepository.countNormalStatusByClassId(id);
        if (countNormalOrders > BizConstant.DB_NUM_ZERO) {
            log.error("存在未处理或已成功的报名，无法撤班, classId={}, countNormalOrders={}", id, countNormalOrders);
            return ResultDo.build(MessageCode.ERROR_COURSE_EXIST_NORMAL_ORDER);
        }
        // 结束班次
        int num = organClassRepository.endById(id);
        if (num == BizConstant.DB_NUM_ZERO) {
            return ResultDo.build();
        }
        // 如果没有上线、开班状态的班次了则下线课程
        Long courseId = classPo.getCourseId();
        List<EpOrganClassPo> openingClasses = organClassRepository.getByCourseIdAndStatus(courseId, EpOrganClassStatus.online, EpOrganClassStatus.opening);
        if (CollectionsTools.isNotEmpty(openingClasses)) {
            return ResultDo.build();
        }
        log.info("课程班次已全部结束，课程状态更新为下线, courseId={}", courseId);
        organCourseRepository.offlineById(courseId);
        // 刷新机构类目
        EpOrganCoursePo coursePo = organCourseRepository.getById(courseId);
        if (!organCourseRepository.existCatalog(coursePo.getOgnId(), coursePo.getCourseCatalogId())) {
            organCatalogRepository.deletePhysicByOgnIdAndCatalogId(coursePo.getOgnId(), coursePo.getCourseCatalogId());
        }
        return ResultDo.build();
    }

    /**
     * 获取班级成员昵称
     *
     * @param ognId
     * @param id
     * @return
     */
    public List<String> findClassChildNickName(Long ognId, Long id) {
        return organClassRepository.findClassChildNickNameByClassId(ognId, id);

    }

    /**
     * 根据id获取记录
     *
     * @param id
     * @return
     */
    public Optional<EpOrganClassPo> findById(Long id) {
        return organClassRepository.findById(id);

    }

    /**
     * 根据课程id和状态获取记录
     *
     * @param courseId
     * @return
     */
    public List<EpOrganClassPo> findByCourseIdAndStatus(Long courseId, EpOrganClassStatus[] statuses) {
        return organClassRepository.findByCourseIdAndStatus(courseId, statuses);
    }

    /**
     * 获取opening/end的正常班次和normal/end的预约班次,提供给随堂评价页面
     */
    public List<EpOrganClassPo> findProceedClassByCourseId(Long courseId) {
        return organClassRepository.findProceedClassByCourseId(courseId);
    }

    /**
     * 根据classId统计正常班次下未结束的班次目录
     *
     * @param classId
     * @return
     */
    public int countUnendNormalClassCatalogByClassId(Long classId) {
        return organClassCatalogRepository.countUnendNormalClassCatalogByClassId(classId);
    }

    /**
     * 根据classId统计预约班次下未结束的预约
     *
     * @param classId
     * @return
     */
    public int countUnendBesprakClassScheduleByClassId(Long classId) {
        return organClassScheduleRepository.countUnendBesprakClassScheduleByClassId(classId);
    }

    /**
     * 商户按班次提现分页
     *
     * @param pageable
     * @param condition
     * @return
     */
    public Page<ClassWithdrawQueryDto> findClassWithdrawQueryDtoByPage(Pageable pageable, Collection<? extends Condition> condition, Timestamp withdrawDeadline) {

        return organClassRepository.findClassWithdrawQueryDtoByPage(pageable, condition, withdrawDeadline);
    }

}
