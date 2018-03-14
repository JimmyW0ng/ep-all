package com.ep.domain.service;

import com.ep.common.tool.CollectionsTools;
import com.ep.common.tool.StringTools;
import com.ep.domain.constant.BizConstant;
import com.ep.domain.constant.MessageCode;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.MemberChildCommentBo;
import com.ep.domain.pojo.po.EpFilePo;
import com.ep.domain.pojo.po.EpMemberChildCommentPo;
import com.ep.domain.pojo.po.EpMemberChildTagPo;
import com.ep.domain.repository.FileRepository;
import com.ep.domain.repository.MemberChildCommentRepository;
import com.ep.domain.repository.MemberChildTagRepository;
import com.ep.domain.repository.domain.enums.EpMemberChildCommentType;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.jooq.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
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
    private MemberChildService memberChildService;
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private MemberChildTagRepository memberChildTagRepository;

    public Optional<EpMemberChildCommentPo> findById(Long id) {
        return memberChildCommentRepository.findById(id);
    }

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

    /**
     * 班次老师评价回复
     *
     * @param memberId
     * @param commentId
     * @return
     */
    public ResultDo replayComment(Long memberId, Long commentId, String content) {
        // 前置校验
        if (StringTools.isBlank(content) || commentId == BizConstant.DB_NUM_ZERO) {
            return ResultDo.build(MessageCode.ERROR_SYSTEM_PARAM_FORMAT);
        }
        // 查看是否已经回复过
        Optional<EpMemberChildCommentPo> optional = memberChildCommentRepository.existByPId(commentId);
        if (optional.isPresent()) {
            return ResultDo.build(MessageCode.ERROR_CLASS_CATALOG_COMMENT_REPLAY_EXIST);
        }
        EpMemberChildCommentPo commentPo = memberChildCommentRepository.getById(commentId);
        if (commentPo == null || !commentPo.getType().equals(EpMemberChildCommentType.launch) || commentPo.getDelFlag()) {
            return ResultDo.build(MessageCode.ERROR_CLASS_CATALOG_COMMENT_NOT_EXIST);
        }
        // 查询孩子是否属于当前会员
        ResultDo checkedChild = memberChildService.getCheckedMemberChild(memberId, commentPo.getChildId());
        if (checkedChild.isError()) {
            return checkedChild;
        }
        EpMemberChildCommentPo replay = new EpMemberChildCommentPo();
        replay.setPId(commentId);
        replay.setChildId(commentPo.getChildId());
        replay.setOgnId(commentPo.getOgnId());
        replay.setCourseId(commentPo.getCourseId());
        replay.setClassId(commentPo.getClassId());
        replay.setClassCatalogId(commentPo.getClassCatalogId());
        replay.setType(EpMemberChildCommentType.reply);
        replay.setContent(content);
        replay.setReplyMemberId(memberId);
        memberChildCommentRepository.insert(replay);
        return ResultDo.build();
    }

    /**
     * 商户后台获取分页
     *
     * @param pageable
     * @param conditions
     * @return
     */
    public Page<MemberChildCommentBo> findbyPageAndCondition(Pageable pageable, Collection<? extends Condition> conditions) {
        return memberChildCommentRepository.findbyPageAndCondition(pageable, conditions);
    }

    /**
     * 商户后台修改评论内容
     *
     * @param id
     * @param content
     */
    public void updateContent(Long id, String content) {
        memberChildCommentRepository.updateContent(id, content);
    }

    /**
     * 商户后台修改评论
     *
     * @param id
     * @param content
     * @param childId
     * @param classCatalogId
     * @param ognId
     * @param tagIds
     */
    @Transactional(rollbackFor = Exception.class)
    public ResultDo updateComment(Long id, String content, Long childId, Long classCatalogId, Long ognId, List<Long> tagIds) {
        log.info("[评论]修改评论开始，评论id={},评论内容content={},childId={},classCatalogId={},ognId={},tagIds={}。", id,
                content, childId, classCatalogId, ognId, tagIds);
        Optional<EpMemberChildCommentPo> optional = memberChildCommentRepository.findById(id);
        if (!optional.isPresent()) {
            log.error("[评论]修改评论失败，次评论不存在，id={}。", id);
            return ResultDo.build(MessageCode.ERROR_CLASS_CATALOG_COMMENT_NOT_EXIST);
        }
        EpMemberChildCommentPo memberChildCommentPo = optional.get();
        List<EpMemberChildTagPo> insertPos = Lists.newArrayList();
        if (CollectionsTools.isNotEmpty(tagIds)) {
            tagIds.forEach(p -> {
                EpMemberChildTagPo po = new EpMemberChildTagPo();
                po.setChildId(childId);
                po.setOgnId(ognId);
                po.setCourseId(memberChildCommentPo.getCourseId());
                po.setClassId(memberChildCommentPo.getClassId());
                po.setClassCatalogId(classCatalogId);
                po.setTagId(p);
                insertPos.add(po);
            });
        }
        memberChildCommentRepository.updateContent(id, content);
        //先物理删除孩子标签，再插入
        memberChildTagRepository.deletePhysicByChildIdAndClassCatalogId(childId, classCatalogId);
        memberChildTagRepository.insert(insertPos);
        log.info("[评论]修改评论成功，评论id={}。");
        return ResultDo.build();
    }
}
