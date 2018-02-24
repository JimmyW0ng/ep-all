package com.ep.domain.service;

import com.ep.common.tool.CollectionsTools;
import com.ep.domain.constant.BizConstant;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.MemberChildCommentBo;
import com.ep.domain.pojo.po.EpFilePo;
import com.ep.domain.repository.FileRepository;
import com.ep.domain.repository.MemberChildCommentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
    @Autowired
    private FileRepository fileRepository;

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
        List<MemberChildCommentBo> data = page.getContent();
        if (CollectionsTools.isNotEmpty(data)) {
            for (MemberChildCommentBo commentBo : data) {
                Optional<EpFilePo> optAvatar = fileRepository.getOneByBizTypeAndSourceId(BizConstant.FILE_BIZ_TYPE_CODE_TEACHER_AVATAR, commentBo.getOgnAccountId());
                String avatar = optAvatar.isPresent() ? optAvatar.get().getFileUrl() : null;
                commentBo.setAvatar(avatar);
            }
        }
        return resultDo.setResult(page);
    }

}
