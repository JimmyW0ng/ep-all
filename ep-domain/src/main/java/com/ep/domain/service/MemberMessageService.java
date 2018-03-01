package com.ep.domain.service;

import com.ep.common.tool.CollectionsTools;
import com.ep.domain.constant.BizConstant;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.MemberMessageBo;
import com.ep.domain.pojo.po.EpFilePo;
import com.ep.domain.pojo.po.EpOrganClassCatalogPo;
import com.ep.domain.pojo.po.EpOrganClassPo;
import com.ep.domain.repository.FileRepository;
import com.ep.domain.repository.MemberMessageRepository;
import com.ep.domain.repository.OrganClassCatalogRepository;
import com.ep.domain.repository.OrganClassRepository;
import com.ep.domain.repository.domain.enums.EpMemberMessageType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @Description: 会员消息接口服务类
 * @Author: J.W
 * @Date: 下午3:39 2018/2/27
 */
@Slf4j
@Service
public class MemberMessageService {

    @Autowired
    private MemberMessageRepository memberMessageRepository;
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private OrganClassCatalogRepository organClassCatalogRepository;
    @Autowired
    private OrganClassRepository organClassRepository;

    /**
     * 孩子消息未读数
     *
     * @param childId
     * @param type
     * @return
     */
    public ResultDo<Integer> getUnreadNumByChildId(Long childId, EpMemberMessageType type) {
        ResultDo<Integer> resultDo = ResultDo.build();
        Integer count = memberMessageRepository.getUnreadNumByChildId(childId, type);
        return resultDo.setResult(count);
    }

    /**
     * 孩子消息-分页
     *
     * @param childId
     * @param type
     * @return
     */
    public ResultDo<Page<MemberMessageBo>> findByChildIdForPage(Pageable pageable,
                                                                Long childId,
                                                                EpMemberMessageType type) {
        // 设置已读
        int num = memberMessageRepository.readAllByChild(childId, type);
        log.info("设置已读消息数据{}条, childid={}, type={}", num, childId, type.getName());
        ResultDo<Page<MemberMessageBo>> resultDo = ResultDo.build();
        Page<MemberMessageBo> page = memberMessageRepository.findByChildIdForPage(pageable, childId, type);
        List<MemberMessageBo> data = page.getContent();
        if (CollectionsTools.isNotEmpty(data)) {
            for (MemberMessageBo messageBo : data) {
                Optional<EpFilePo> optAvatar = fileRepository.getOneByBizTypeAndSourceId(BizConstant.FILE_BIZ_TYPE_CODE_TEACHER_AVATAR, messageBo.getSenderId());
                String avatar = optAvatar.isPresent() ? optAvatar.get().getFileUrl() : null;
                messageBo.setAvatar(avatar);
            }
        }
        return resultDo.setResult(page);
    }

    /**
     * 发送课时评论消息
     *
     * @param classCatalogId
     * @param childId
     * @param tagIds
     * @param comment
     */
    public void sendClassCatalogCommentMessage(Long classCatalogId, Long childId, Set<Long> tagIds, String comment) {
        // 课时信息
        EpOrganClassCatalogPo classCatalogPo = organClassCatalogRepository.getById(classCatalogId);
        // 班次
        EpOrganClassPo classPo = organClassRepository.getById(classCatalogPo.getClassId());
        // 发送着-机构账户

    }
}
