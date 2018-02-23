package com.ep.domain.repository;

import com.ep.domain.pojo.bo.MemberChildCommentBo;
import com.ep.domain.pojo.po.EpMemberChildCommentPo;
import com.ep.domain.repository.domain.tables.records.EpMemberChildCommentRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import static com.ep.domain.repository.domain.Tables.EP_MEMBER_CHILD_COMMENT;

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

        return null;
    }
}
