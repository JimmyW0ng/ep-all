package com.ep.domain.service;

import com.ep.common.tool.CollectionsTools;
import com.ep.domain.constant.BizConstant;
import com.ep.domain.constant.MessageCode;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.OrganClassBo;
import com.ep.domain.pojo.po.EpOrderPo;
import com.ep.domain.pojo.po.EpOrganClassChildPo;
import com.ep.domain.pojo.po.EpOrganClassPo;
import com.ep.domain.pojo.po.EpSystemUserPo;
import com.ep.domain.repository.OrderRepository;
import com.ep.domain.repository.OrganClassChildRepository;
import com.ep.domain.repository.OrganClassRepository;
import com.ep.domain.repository.domain.enums.EpOrganClassStatus;
import lombok.extern.slf4j.Slf4j;
import org.jooq.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * @Description: 机构课程班次Service
 * @Author: CC.F
 * @Date: 17:12 2018/2/11
 */
@Service
@Slf4j
public class OrganClassService {

    @Autowired
    private OrganClassRepository organClassRepository;
    @Autowired
    private OrganClassChildRepository organClassChildRepository;
    @Autowired
    private OrderRepository orderRepository;

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
            return ResultDo.build(MessageCode.ERROR_COURSE_OGN_NOT_MATCH);
        }
        // 校验是否还有未处理的订单
        List<EpOrderPo> savedOrders = orderRepository.findSavedOrdersByClassId(id);
        if (CollectionsTools.isNotEmpty(savedOrders)) {
            log.error("存在未处理的订单, classId={}, savedOrderNum={}", id, savedOrders.size());
            return ResultDo.build(MessageCode.ERROR_COURSE_EXIST_SAVED_ORDER);
        }
        List<EpOrderPo> openingOrders = orderRepository.findOpeningOrdersByClassId(id);
        if (CollectionsTools.isEmpty(openingOrders)) {
            log.error("班次不存在已开班订单, classId={}", id);
            return ResultDo.build(MessageCode.ERROR_CLASS_CHILD_NOT_EXISTS);
        }
        // 开班
        int num = organClassRepository.openById(id);
        if (num == BizConstant.DB_NUM_ZERO) {
            return ResultDo.build();
        }
        // 更新订单状态为已开班
        orderRepository.openOrderByClassId(id);
        // 生成班级孩子记录
        for (EpOrderPo orderPo : openingOrders) {
            EpOrganClassChildPo classChildPo = new EpOrganClassChildPo();
            classChildPo.setClassId(id);
            classChildPo.setChildId(orderPo.getChildId());
            classChildPo.setOrderId(orderPo.getId());
            organClassChildRepository.insert(classChildPo);
        }
        return ResultDo.build();
    }

    /**
     * 机构下线，该机构下的班次结束
     *
     * @param ognId
     */
    public void updateClassByOfflineOgn(Long ognId) {
        organClassRepository.updateClassByOfflineOgn(ognId);
    }

}
