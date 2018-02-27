package com.ep.domain.service;

import com.ep.domain.pojo.bo.MemberChildCommentBo;
import com.ep.domain.repository.ChildCommentRepository;
import lombok.extern.slf4j.Slf4j;
import org.jooq.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * @Description: 孩子评论服务类
 * @Author: CC.F
 * @Date: 下午2:19 2018/1/28
 */
@Slf4j
@Service
public class ChildCommentService {
    @Autowired
    private ChildCommentRepository childCommentRepository;

    /**
     * 商户后台获取分页
     *
     * @param pageable
     * @param conditions
     * @return
     */
    public Page<MemberChildCommentBo> findbyPageAndCondition(Pageable pageable, Collection<? extends Condition> conditions) {
        return childCommentRepository.findbyPageAndCondition(pageable, conditions);
    }

}
