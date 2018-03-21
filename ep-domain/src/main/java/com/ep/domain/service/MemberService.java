package com.ep.domain.service;

import com.ep.domain.constant.BizConstant;
import com.ep.domain.constant.MessageCode;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.po.EpMemberPo;
import com.ep.domain.repository.MemberRepository;
import com.ep.domain.repository.OrganAccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.jooq.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * @Description:会员业务接口
 * @Author: J.W
 * @Date: 上午10:33 2017/11/27
 */
@Slf4j
@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private OrganAccountRepository organAccountRepository;

    /**
     * 分页查询会员信息
     *
     * @param pageable
     * @param conditions
     * @return
     */
    public Page<EpMemberPo> getByPage(Pageable pageable, Collection<Condition> conditions) {
        return memberRepository.findByPageable(pageable, conditions);
    }


    /**
     * 冻结会员
     *
     * @param id
     */
    public ResultDo freezeById(Long id) {
        if (memberRepository.freezeById(id) == BizConstant.DB_NUM_ONE) {
            log.info("[平台会员]冻结成功。id={}。", id);
            return ResultDo.build();
        } else {
            log.error("[平台会员]冻结失败。id={}。", id);
            return ResultDo.build(MessageCode.ERROR_OPERATE_FAIL);
        }
    }

    /**
     * 解冻会员
     *
     * @param id
     */
    public ResultDo unfreezeById(Long id) {
        if (memberRepository.unfreezeById(id) == BizConstant.DB_NUM_ONE) {
            log.info("[平台会员]解冻成功。id={}。", id);
            return ResultDo.build();
        } else {
            log.error("[平台会员]解冻失败。id={}。", id);
            return ResultDo.build(MessageCode.ERROR_OPERATE_FAIL);
        }
    }

    /**
     * 注销会员
     *
     * @param id
     * @return
     */
    public ResultDo cancelById(Long id) {
        if (memberRepository.cancelById(id) == BizConstant.DB_NUM_ONE) {
            log.info("[平台会员]注销成功。id={}。", id);
            return ResultDo.build();
        } else {
            log.error("[平台会员]注销失败。id={}。", id);
            return ResultDo.build(MessageCode.ERROR_OPERATE_FAIL);
        }
    }

}
