package com.ep.domain.repository;

import com.ep.domain.constant.BizConstant;
import com.ep.domain.pojo.bo.MemberChildCommentBo;
import com.ep.domain.pojo.po.EpMemberChildCommentPo;
import com.ep.domain.repository.domain.enums.EpMemberChildCommentType;
import com.ep.domain.repository.domain.tables.records.EpMemberChildCommentRecord;
import com.google.common.collect.Lists;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.ep.domain.repository.domain.Tables.*;

/**
 * @Description: 孩子上课评论表Repository
 * @Author J.W
 * @Date: 下午 5:17 2018/2/22 0022
 */
@Repository
public class MemberChildCommentRepository extends AbstractCRUDRepository<EpMemberChildCommentRecord, Long, EpMemberChildCommentPo> {

    @Autowired
    public MemberChildCommentRepository(DSLContext dslContext) {
        super(dslContext, EP_MEMBER_CHILD_COMMENT, EP_MEMBER_CHILD_COMMENT.ID, EpMemberChildCommentPo.class);
    }

    /**
     * 查询孩子获得的最新评价-分页
     *
     * @param pageable
     * @param childId
     * @return
     */
    public Page<MemberChildCommentBo> queryRecentForPage(Pageable pageable, Long childId) {
        Long count = dslContext.selectCount().from(EP_MEMBER_CHILD_COMMENT)
                               .where(EP_MEMBER_CHILD_COMMENT.CHILD_ID.eq(childId))
                               .and(EP_MEMBER_CHILD_COMMENT.TYPE.eq(EpMemberChildCommentType.launch))
                               .and(EP_MEMBER_CHILD_COMMENT.DEL_FLAG.eq(false))
                               .fetchOneInto(Long.class);
        if (count == BizConstant.DB_NUM_ZERO) {
            return new PageImpl(Lists.newArrayList(), pageable, count);
        }
        List<Field<?>> fieldList = Lists.newArrayList(EP_MEMBER_CHILD_COMMENT.CONTENT);
        fieldList.add(EP_MEMBER_CHILD_COMMENT.OGN_ACCOUNT_ID);
        fieldList.add(EP_MEMBER_CHILD_COMMENT.CREATE_AT);
        fieldList.add(EP_ORGAN_ACCOUNT.NICK_NAME);
        fieldList.add(EP_ORGAN.OGN_NAME);
        List<MemberChildCommentBo> data = dslContext.select(fieldList).from(EP_MEMBER_CHILD_COMMENT)
                                                    .leftJoin(EP_ORGAN_ACCOUNT)
                                                    .on(EP_MEMBER_CHILD_COMMENT.OGN_ACCOUNT_ID.eq(EP_ORGAN_ACCOUNT.ID))
                                                    .and(EP_MEMBER_CHILD_COMMENT.DEL_FLAG.eq(false))
                                                    .leftJoin(EP_ORGAN)
                                                    .on(EP_MEMBER_CHILD_COMMENT.OGN_ID.eq(EP_ORGAN.ID))
                                                    .and(EP_ORGAN.DEL_FLAG.eq(false))
                                                    .where(EP_MEMBER_CHILD_COMMENT.CHILD_ID.eq(childId))
                                                    .and(EP_MEMBER_CHILD_COMMENT.TYPE.eq(EpMemberChildCommentType.launch))
                                                    .and(EP_MEMBER_CHILD_COMMENT.DEL_FLAG.eq(false))
                                                    .orderBy(EP_MEMBER_CHILD_COMMENT.CREATE_AT.desc())
                                                    .fetchInto(MemberChildCommentBo.class);
        return new PageImpl(data, pageable, count);
    }
}