package com.ep.domain.service;

import com.ep.domain.pojo.bo.OrganAccountBo;
import com.ep.domain.pojo.po.EpOrganAccountPo;
import com.ep.domain.pojo.po.EpSystemUserPo;
import com.ep.domain.repository.OrganAccountRepository;
import com.ep.domain.repository.domain.tables.EpOrganAccount;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * @Description: 机构账户关联信息服务
 * @Author: CC.F
 * @Date: 9:45 2018/2/6
 */
@Service
@Slf4j
public class OrganAccountService {

    @Autowired
    private OrganAccountRepository organAccountRepository;

    public EpOrganAccountPo getById(Long id) {
        return organAccountRepository.getById(id);

    }

    public Page<OrganAccountBo> findbyPageAndCondition(Pageable pageable, Collection<? extends Condition> condition) {
        return organAccountRepository.findbyPageAndCondition(pageable, condition);
    }

    /**
     * 新增机构账户关联信息
     * @param po
     * @return
     */
    public EpOrganAccountPo create(EpOrganAccountPo po){
        return organAccountRepository.insertNew(po);
    }

    /**
     * 修改机构账户关联信息
     * @param po
     * @return
     */
    public int update(EpOrganAccountPo po){
        return organAccountRepository.updateById(po);
    }

    /**
     * 删除机构账户关联信息
     * @param id
     * @return
     */
    public int delete(Long id){
        return organAccountRepository.deleteLogical(id);
    }

}
