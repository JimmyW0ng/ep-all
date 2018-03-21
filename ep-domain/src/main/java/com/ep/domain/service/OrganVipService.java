package com.ep.domain.service;

import com.ep.domain.pojo.bo.OrganVipBo;
import com.ep.domain.repository.MemberRepository;
import com.ep.domain.repository.OrganVipRepository;
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
 * @Date: 0:08 2018/3/20
 */
@Slf4j
@Service
public class OrganVipService {
    @Autowired
    private OrganVipRepository organVipRepository;
    @Autowired
    private MemberRepository memberRepository;

    /**
     * 分页查询机构会员列表
     *
     * @param pageable
     * @param condition
     * @return
     */
    public Page<OrganVipBo> findbyPageAndCondition(Pageable pageable, Collection<? extends Condition> condition) {
        return organVipRepository.findbyPageAndCondition(pageable, condition);
    }

//    public ResultDo createOgnVip(OrganVipBo bo){
//        System.out.println(bo);
//        memberRepository.findByPageable()
//        EpOrganVipPo po = new EpOrganVipPo();
//        po.set
//
//    }
}
