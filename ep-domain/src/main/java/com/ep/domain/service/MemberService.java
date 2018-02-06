package com.ep.domain.service;

import com.ep.common.tool.CollectionsTools;
import com.ep.domain.pojo.po.EpMemberPo;
import com.ep.domain.pojo.po.EpOrganAccountPo;
import com.ep.domain.repository.MemberRepository;
import com.ep.domain.repository.OrganAccountRepository;
import com.ep.domain.repository.domain.enums.EpMemberStatus;
import com.ep.domain.repository.domain.enums.EpMemberType;
import lombok.extern.slf4j.Slf4j;
import org.jooq.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

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
     * 根据手机号查询
     *
     * @param mobile
     * @return
     */
    public EpMemberPo getByMobile(Long mobile) {
        return memberRepository.getByMobile(mobile);
    }

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
     * 校验会员身份
     *
     * @param mobile
     * @return
     */
    public EpMemberPo checkExistAndType(Long mobile) {
        EpMemberPo memberPo = this.getByMobile(mobile);
        // 判断是否是机构账户
        boolean isOrganMan;
        List<EpOrganAccountPo> accounts = organAccountRepository.getByMobile(mobile);
        isOrganMan = CollectionsTools.isNotEmpty(accounts);
        if (memberPo != null) {
            // 机构账户直接返回
            if (memberPo.getType().equals(EpMemberType.organ_account)) {
                return memberPo;
            }
            // 普通会员但是有机构账户数据
            if (isOrganMan && memberPo.getType().equals(EpMemberType.member)) {
                // 变更会员类型为机构账户
                memberPo.setType(EpMemberType.organ_account);
                memberRepository.changeTypeToOrganAccount(memberPo.getId());
            }
            return memberPo;
        }
        // 新增会员信息
        EpMemberPo insertPo = new EpMemberPo();
        insertPo.setMobile(mobile);
        insertPo.setStatus(EpMemberStatus.normal);
        if (isOrganMan) {
            insertPo.setType(EpMemberType.organ_account);
        } else {
            insertPo.setType(EpMemberType.member);
        }
        memberRepository.insert(insertPo);
        return insertPo;
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
