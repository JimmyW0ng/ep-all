package com.ep.domain.service;

import com.ep.common.tool.CollectionsTools;
import com.ep.domain.constant.BizConstant;
import com.ep.domain.pojo.bo.OrganClassCommentBo;
import com.ep.domain.pojo.po.EpFilePo;
import com.ep.domain.repository.FileRepository;
import com.ep.domain.repository.OrganClassCommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @Description: 机构课程班次评分服务接口
 * @Author: J.W
 * @Date: 下午5:47 2018/2/8
 */
@Service
public class OrganClassCommentService {

    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private OrganClassCommentRepository organClassCommentRepository;

    /**
     * 分页查询课程全部评论
     *
     * @param pageable
     * @param courseId
     * @return
     */
    public Page<OrganClassCommentBo> findCourseCommentForPage(Pageable pageable, Long courseId) {
        Page<OrganClassCommentBo> page = organClassCommentRepository.findCourseCommentForPage(pageable, courseId);
        List<OrganClassCommentBo> data = page.getContent();
        if (CollectionsTools.isEmpty(data)) {
            return page;
        }
        for (OrganClassCommentBo classCommentBo : data) {
            Optional<EpFilePo> optAvatar = fileRepository.getOneByBizTypeAndSourceId(BizConstant.FILE_BIZ_TYPE_CODE_CHILD_AVATAR, classCommentBo.getChildId());
            String avatar = optAvatar.isPresent() ? optAvatar.get().getFileUrl() : null;
            classCommentBo.setChildAvatar(avatar);
        }
        return page;
    }

}
