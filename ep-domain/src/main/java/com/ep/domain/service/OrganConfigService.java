package com.ep.domain.service;

import com.ep.domain.pojo.po.EpOrganConfigPo;
import com.ep.domain.repository.OrganConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @Description:
 * @Author: CC.F
 * @Date: 23:09 2018/3/20
 */
@Service
public class OrganConfigService {
    @Autowired
    private OrganConfigRepository organConfigRepository;

    /**
     * 根据机构id获取配置
     *
     * @param ognId
     * @return
     */
    public Optional<EpOrganConfigPo> getByOgnId(Long ognId) {
        return organConfigRepository.getByOgnId(ognId);
    }
}
