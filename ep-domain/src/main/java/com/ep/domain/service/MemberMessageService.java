package com.ep.domain.service;

import com.ep.common.tool.CollectionsTools;
import com.ep.common.tool.StringTools;
import com.ep.domain.constant.BizConstant;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.MemberMessageBo;
import com.ep.domain.pojo.po.*;
import com.ep.domain.repository.*;
import com.ep.domain.repository.domain.enums.EpMemberMessageStatus;
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
    @Autowired
    private OrganRepository organRepository;
    @Autowired
    private OrganAccountRepository organAccountRepository;
    @Autowired
    private MemberChildRepository memberChildRepository;
    @Autowired
    private OrganCourseRepository organCourseRepository;

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
     * @return
     */
    public ResultDo<Page<MemberMessageBo>> findClassCatalogCommentByChildIdForPage(Pageable pageable, Long childId) {
        // 设置已读
        int num = memberMessageRepository.readAllByChild(childId, EpMemberMessageType.class_catalog_comment);
        log.info("设置已读课时评价消息数据{}条, childid={}", num, childId);
        ResultDo<Page<MemberMessageBo>> resultDo = ResultDo.build();
        Page<MemberMessageBo> page = memberMessageRepository.findClassCatalogCommentByChildIdForPage(pageable, childId);
        List<MemberMessageBo> data = page.getContent();
        if (CollectionsTools.isNotEmpty(data)) {
            for (MemberMessageBo messageBo : data) {
                Optional<EpFilePo> optAvatar = fileRepository.getOneByBizTypeAndSourceId(BizConstant.FILE_BIZ_TYPE_CODE_TEACHER_AVATAR, messageBo.getSenderOgnAccountId());
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
        if (CollectionsTools.isEmpty(tagIds) && StringTools.isBlank(comment)) {
            return;
        }
        // 课时信息
        EpOrganClassCatalogPo classCatalogPo = organClassCatalogRepository.getById(classCatalogId);
        // 班次
        EpOrganClassPo classPo = organClassRepository.getById(classCatalogPo.getClassId());
        // 课程
        EpOrganCoursePo coursePo = organCourseRepository.getById(classPo.getCourseId());
        // 发送者-机构账户
        EpOrganAccountPo accountPo = organAccountRepository.getById(classPo.getOgnAccountId());
        // 发送者机构
        EpOrganPo organPo = organRepository.getById(classPo.getOgnId());
        // 孩子
        EpMemberChildPo childPo = memberChildRepository.getById(childId);
        // 消息内容
        String content = String.format(BizConstant.MESSAGE_CONTENT_CLASS_CATALOG_COMMENT,
                childPo.getChildNickName(),
                coursePo.getCourseName(),
                classPo.getClassName());
        // 保存消息
        EpMemberMessagePo messagePo = new EpMemberMessagePo();
        messagePo.setSenderOgnAccountId(accountPo.getId());
        messagePo.setSenderName(accountPo.getNickName());
        // 此处显示机构名称
        messagePo.setSenderDesc(organPo.getOgnName());
        messagePo.setMemberId(childPo.getMemberId());
        messagePo.setChildId(childId);
        messagePo.setType(EpMemberMessageType.class_catalog_comment);
        messagePo.setStatus(EpMemberMessageStatus.unread);
        messagePo.setContent(content);
        messagePo.setSourceId(classCatalogId);
        memberMessageRepository.insert(messagePo);
    }
}
