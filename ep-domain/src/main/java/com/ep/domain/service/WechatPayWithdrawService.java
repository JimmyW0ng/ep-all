package com.ep.domain.service;

import com.ep.common.tool.DateTools;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.WechatPayWithdrawBo;
import com.ep.domain.pojo.po.EpWechatPayWithdrawPo;
import com.ep.domain.repository.WechatPayWithdrawRepository;
import com.ep.domain.repository.domain.enums.EpWechatPayWithdrawStatus;
import lombok.extern.slf4j.Slf4j;
import org.jooq.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * @Description:
 * @Author: CC.F
 * @Date: 23:24 2018/5/16
 */
@Slf4j
@Service
public class WechatPayWithdrawService {
    @Autowired
    private WechatPayWithdrawRepository wechatPayWithdrawRepository;

    /**
     * 商户提现分页
     *
     * @param pageable
     * @param condition
     * @return
     */
    public Page<WechatPayWithdrawBo> findbyPageAndCondition(Pageable pageable, Collection<? extends Condition> condition) {
        return wechatPayWithdrawRepository.findbyPageAndCondition(pageable, condition);
    }

    /**
     * 按班次统计提现成功订单数
     *
     * @param classId
     * @return
     */
    public int countPayWithdrawByClassId(Long classId) {
        return wechatPayWithdrawRepository.countPayWithdrawByClassId(classId);
    }

    /**
     * 最近一次提现记录
     *
     * @param classId
     * @return
     */
    public EpWechatPayWithdrawPo getLastWithdrawByClassId(Long classId) {
        return wechatPayWithdrawRepository.getLastWithdrawByClassId(classId);
    }

    /**
     * @param classId
     * @param courseId
     * @return
     */
    public ResultDo applyPayWithdrawByClassId(Long classId, Long courseId) {
        log.info("[微信报名费提现]订单微信支付报名费提现申请开始，classId={},courseId={}。", classId, courseId);
        EpWechatPayWithdrawPo wechatPayWithdrawPo = new EpWechatPayWithdrawPo();
        wechatPayWithdrawPo.setClassId(classId);
        wechatPayWithdrawPo.setCourseId(courseId);
        wechatPayWithdrawPo.setOrderDeadline(DateTools.getCurrentDateTime());
        wechatPayWithdrawPo.setStatus(EpWechatPayWithdrawStatus.wait);
        wechatPayWithdrawRepository.insert(wechatPayWithdrawPo);
        log.info("[微信报名费提现]订单微信支付报名费提现申请，ep_wechat_pay_withdraw表插入数据。{}。", wechatPayWithdrawPo);
        log.info("[微信报名费提现]订单微信支付报名费提现申请成功，classId={},courseId={}。", classId, courseId);
        return ResultDo.build();
    }
}
