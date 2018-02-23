package com.ep.domain.service;

import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.MemberChildCommentBo;
import com.ep.domain.repository.MemberChildCommentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * @Description: 孩子评价服务接口
 * @Author J.W
 * @Date: 下午 5:32 2018/2/23 0023
 */
@Slf4j
@Service
public class MemberChildCommentService {

    @Autowired
    private MemberChildCommentRepository memberChildCommentRepository;

    /**
     * 查询孩子获得的最新评价-分页
     *
     * @param pageable
     * @param childId
     * @return
     */
    public ResultDo<Page<MemberChildCommentBo>> queryRecentForPage(Pageable pageable, Long childId) {
        ResultDo<Page<MemberChildCommentBo>> resultDo = ResultDo.build();
        Page<MemberChildCommentBo> page = memberChildCommentRepository.queryRecentForPage(pageable, childId);
        return resultDo.setResult(page);
    }

}
