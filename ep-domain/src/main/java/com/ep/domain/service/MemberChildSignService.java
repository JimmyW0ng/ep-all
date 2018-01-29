package com.ep.domain.service;

import com.ep.domain.constant.MessageCode;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.po.EpMemberChildPo;
import com.ep.domain.pojo.po.EpMemberChildSignPo;
import com.ep.domain.repository.MemberChildRepository;
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
    private MemberChildRepository memberChildRepository;
    @Autowired
    private MemberChildSignRepository memberChildSignRepository;

    /**
     * 孩子添加签名
     *
     * @param childId
     * @param content
     * @return
     */
    public ResultDo sign(Long memberId, Long childId, String content) {
        ResultDo resultDo = ResultDo.build();
        EpMemberChildPo childPo = memberChildRepository.getById(childId);
        if (childPo == null
                || !childPo.getMemberId().equals(memberId)
                || childPo.getDelFlag()) {
            log.error("下单失败，孩子信息不存在或已删除！");
            resultDo.setError(MessageCode.ERROR_CHILD_NOT_EXISTS);
            return resultDo;
        }
        EpMemberChildSignPo signPo = memberChildSignRepository.getByChildId(childId);
        if (signPo == null) {
            EpMemberChildSignPo insertPo = new EpMemberChildSignPo();
            insertPo.setChildId(childId);
            insertPo.setContent(content);
            memberChildSignRepository.insert(insertPo);
        } else {
            memberChildSignRepository.updateSign(childId, content);
        }
        return resultDo;
    }
}
