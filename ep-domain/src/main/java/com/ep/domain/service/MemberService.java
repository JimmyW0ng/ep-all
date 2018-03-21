package com.ep.domain.service;

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
    public int freezeMember(Long id) {
        return memberRepository.freezeMember(id);
    }

    /**
     * 注销会员
     *
     * @param id
     * @return
     */
    public int cancelMember(Long id) {
        return memberRepository.cancelMember(id);
    }

}
