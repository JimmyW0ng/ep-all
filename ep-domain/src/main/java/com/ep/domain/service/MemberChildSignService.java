package com.ep.domain.service;

import com.ep.domain.pojo.po.EpMemberChildSignPo;
import com.ep.domain.repository.MemberChildSignRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description:孩子签名业务接口
 * @Author: J.W
 * @Date: 上午10:33 2017/11/27
 */
@Slf4j
@Service
public class MemberChildSignService {

    @Autowired
    private MemberChildSignRepository memberChildSignRepository;

    /**
     * 孩子添加签名
     *
     * @param childId
     * @param content
     * @return
     */
    public void sign(Long childId, String content) {
        EpMemberChildSignPo signPo = memberChildSignRepository.getByChildId(childId);
        if (signPo == null) {
            EpMemberChildSignPo insertPo = new EpMemberChildSignPo();
            insertPo.setChildId(childId);
            insertPo.setContent(content);
            memberChildSignRepository.insert(insertPo);
        } else {
            memberChildSignRepository.updateSign(childId, content);
        }
    }
}
