package com.ep.domain.repository;

import com.ep.domain.constant.BizConstant;
import com.ep.domain.pojo.bo.HomeMemberChildReplyBo;
import com.ep.domain.pojo.bo.MemberChildCommentBo;
import com.ep.domain.pojo.dto.OrganClassScheduleDto;
import com.ep.domain.pojo.po.EpMemberChildCommentPo;
import com.ep.domain.repository.domain.enums.EpMemberChildCommentType;
import com.ep.domain.repository.domain.tables.EpMemberChildComment;
import com.ep.domain.repository.domain.tables.records.EpMemberChildCommentRecord;
import com.google.common.collect.Lists;
import org.jooq.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

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

    public Optional<EpMemberChildCommentPo> findById(Long id) {
        EpMemberChildCommentPo data = dslContext.selectFrom(EP_MEMBER_CHILD_COMMENT)
                .where(EP_MEMBER_CHILD_COMMENT.ID.eq(id))
                .and(EP_MEMBER_CHILD_COMMENT.DEL_FLAG.eq(false))
                .fetchOneInto(EpMemberChildCommentPo.class);
        return Optional.ofNullable(data);
    }

    /**
     * 查询孩子获得的最新评价-分页
     *
     * @param pageable
     * @param childId
     * @return
     */
    public Page<MemberChildCommentBo> queryRecentForPage(Pageable pageable, Long childId) {
        Long count = dslContext.selectCount()
                .from(EP_MEMBER_CHILD_COMMENT)
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
        fieldList.add(EP_ORGAN_CLASS_CHILD.ORDER_ID);
        List<MemberChildCommentBo> data = dslContext.select(fieldList)
                .from(EP_MEMBER_CHILD_COMMENT)
                .leftJoin(EP_ORGAN_ACCOUNT)
                .on(EP_MEMBER_CHILD_COMMENT.OGN_ACCOUNT_ID.eq(EP_ORGAN_ACCOUNT.ID))
                .and(EP_MEMBER_CHILD_COMMENT.DEL_FLAG.eq(false))
                .leftJoin(EP_ORGAN)
                .on(EP_MEMBER_CHILD_COMMENT.OGN_ID.eq(EP_ORGAN.ID))
                .and(EP_ORGAN.DEL_FLAG.eq(false))
                .leftJoin(EP_ORGAN_CLASS_CHILD)
                .on(EP_MEMBER_CHILD_COMMENT.CLASS_ID.eq(EP_ORGAN_CLASS_CHILD.CLASS_ID))
                .and(EP_MEMBER_CHILD_COMMENT.CHILD_ID.eq(EP_ORGAN_CLASS_CHILD.CHILD_ID))
                .and(EP_ORGAN_CLASS_CHILD.DEL_FLAG.eq(false))
                .where(EP_MEMBER_CHILD_COMMENT.CHILD_ID.eq(childId))
                .and(EP_MEMBER_CHILD_COMMENT.TYPE.eq(EpMemberChildCommentType.launch))
                .and(EP_MEMBER_CHILD_COMMENT.DEL_FLAG.eq(false))
                .orderBy(EP_MEMBER_CHILD_COMMENT.CREATE_AT.desc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetchInto(MemberChildCommentBo.class);
        return new PageImpl(data, pageable, count);
    }

    /**
     * 查询班次内孩子获得的评价
     *
     * @param childId
     * @param classId
     * @return
     */
    public List<MemberChildCommentBo> findByChildIdAndClassId(Long childId, Long classId) {
        List<Field<?>> fieldList = Lists.newArrayList(EP_MEMBER_CHILD_COMMENT.CONTENT);
        fieldList.add(EP_ORGAN_CLASS_SCHEDULE.CATALOG_TITLE.as("classCatalogTitle"));
        fieldList.add(EP_MEMBER_CHILD_COMMENT.CREATE_AT);
        return dslContext.select(fieldList)
                         .from(EP_MEMBER_CHILD_COMMENT)
                         .leftJoin(EP_ORGAN_CLASS_SCHEDULE)
                         .on(EP_MEMBER_CHILD_COMMENT.CLASS_SCHEDULE_ID.eq(EP_ORGAN_CLASS_SCHEDULE.ID))
                         .where(EP_MEMBER_CHILD_COMMENT.CHILD_ID.eq(childId))
                         .and(EP_MEMBER_CHILD_COMMENT.CLASS_ID.eq(classId))
                         .and(EP_MEMBER_CHILD_COMMENT.TYPE.eq(EpMemberChildCommentType.launch))
                         .and(EP_MEMBER_CHILD_COMMENT.DEL_FLAG.eq(false))
                         .orderBy(EP_ORGAN_CLASS_SCHEDULE.CATALOG_INDEX.asc())
                         .fetchInto(MemberChildCommentBo.class);
    }

    /**
     * 根据父级id查询是否存在回复
     *
     * @param pId
     * @return
     */
    public Optional<EpMemberChildCommentPo> existByPId(Long pId) {
        EpMemberChildCommentPo commentPo = dslContext.selectFrom(EP_MEMBER_CHILD_COMMENT)
                .where(EP_MEMBER_CHILD_COMMENT.P_ID.eq(pId))
                .fetchOneInto(EpMemberChildCommentPo.class);
        return Optional.ofNullable(commentPo);
    }

//    /**
//     * 商户后台获取分页
//     *
//     * @param pageable
//     * @param condition
//     * @return
//     */
//    public Page<MemberChildCommentBo> findbyPageAndCondition(Long classId, Long classCatalogId, Pageable pageable, Collection<? extends Condition> condition) {
//        EpMemberChildComment memberChildCommentCopy = EP_MEMBER_CHILD_COMMENT.as("member_child_comment_copy");
//        long totalCount = dslContext.selectCount()
//                .from(EP_ORGAN_CLASS_CHILD)
//                .leftJoin(EP_MEMBER_CHILD).on(EP_MEMBER_CHILD.ID.eq(EP_ORGAN_CLASS_CHILD.CHILD_ID))
//                .leftJoin(EP_MEMBER_CHILD_COMMENT)
//                .on(EP_MEMBER_CHILD_COMMENT.CHILD_ID.eq(EP_MEMBER_CHILD.ID).and(EP_ORGAN_CLASS_CHILD.CLASS_ID.eq(EP_MEMBER_CHILD_COMMENT.CLASS_ID))
//                        .and(EP_MEMBER_CHILD_COMMENT.CLASS_CATALOG_ID.eq(classCatalogId).or(EP_MEMBER_CHILD_COMMENT.CLASS_CATALOG_ID.isNull())))
//                .leftJoin(memberChildCommentCopy).on(memberChildCommentCopy.P_ID.eq(EP_MEMBER_CHILD_COMMENT.ID))
//                .where(condition)
//                .and(EP_ORGAN_CLASS_CHILD.CLASS_ID.eq(classId))
//                .fetchOne(0, Long.class);
//
//        if (totalCount == BizConstant.DB_NUM_ZERO) {
//            return new PageImpl<>(Lists.newArrayList(), pageable, totalCount);
//        }
//        List<Field<?>> fieldList = Lists.newArrayList(EP_MEMBER_CHILD_COMMENT.fields());
//        fieldList.add(EP_MEMBER_CHILD.CHILD_NICK_NAME.as("nickName"));
//        fieldList.add(EP_MEMBER_CHILD.CHILD_TRUE_NAME);
//        fieldList.add(EP_ORGAN_CLASS_CHILD.CHILD_ID.as("classChildId"));
//        fieldList.add(memberChildCommentCopy.ID.as("replyId"));
//        fieldList.add(memberChildCommentCopy.CONTENT.as("contentReply"));
//        SelectConditionStep<Record> record = dslContext.select(fieldList)
//                .from(EP_ORGAN_CLASS_CHILD)
//                .leftJoin(EP_MEMBER_CHILD).on(EP_MEMBER_CHILD.ID.eq(EP_ORGAN_CLASS_CHILD.CHILD_ID))
//                .leftJoin(EP_MEMBER_CHILD_COMMENT)
//                .on(EP_MEMBER_CHILD_COMMENT.CHILD_ID.eq(EP_MEMBER_CHILD.ID).and(EP_ORGAN_CLASS_CHILD.CLASS_ID.eq(EP_MEMBER_CHILD_COMMENT.CLASS_ID))
//                        .and(EP_MEMBER_CHILD_COMMENT.CLASS_CATALOG_ID.eq(classCatalogId).or(EP_MEMBER_CHILD_COMMENT.CLASS_CATALOG_ID.isNull())))
//                .leftJoin(memberChildCommentCopy).on(memberChildCommentCopy.P_ID.eq(EP_MEMBER_CHILD_COMMENT.ID))
//                .where(condition)
//                .and(EP_ORGAN_CLASS_CHILD.CLASS_ID.eq(classId));
//
//        List<MemberChildCommentBo> list = record.orderBy(EP_ORGAN_CLASS_CHILD.ID.asc())
//                .limit(pageable.getPageSize())
//                .offset(pageable.getOffset())
//                .fetchInto(MemberChildCommentBo.class);
//        PageImpl<MemberChildCommentBo> pPage = new PageImpl<MemberChildCommentBo>(list, pageable, totalCount);
//        return pPage;
//    }

    /**
     * 商户后台获取分页
     *
     * @param pageable
     * @param condition
     * @return
     */
    public Page<OrganClassScheduleDto> findbyPageAndCondition(Pageable pageable, Collection<? extends Condition> condition) {
        EpMemberChildComment memberChildCommentCopy = EP_MEMBER_CHILD_COMMENT.as("member_child_comment_copy");
        long totalCount = dslContext.selectCount()
                .from(EP_ORGAN_CLASS_SCHEDULE)
                .leftJoin(EP_MEMBER_CHILD_COMMENT)
                .on(EP_MEMBER_CHILD_COMMENT.CHILD_ID.eq(EP_ORGAN_CLASS_SCHEDULE.CHILD_ID)
                                                    .and(EP_MEMBER_CHILD_COMMENT.CLASS_SCHEDULE_ID.eq(EP_ORGAN_CLASS_SCHEDULE.ID)))
                .leftJoin(EP_MEMBER_CHILD).on(EP_MEMBER_CHILD.ID.eq(EP_ORGAN_CLASS_SCHEDULE.CHILD_ID))

                .leftJoin(memberChildCommentCopy).on(memberChildCommentCopy.P_ID.eq(EP_MEMBER_CHILD_COMMENT.ID))
                .where(condition)
                .fetchOne(0, Long.class);

        if (totalCount == BizConstant.DB_NUM_ZERO) {
            return new PageImpl<>(Lists.newArrayList(), pageable, totalCount);
        }
        List<Field<?>> fieldList = Lists.newArrayList();
        fieldList.add(EP_ORGAN_CLASS_SCHEDULE.CHILD_ID);
        fieldList.add(EP_ORGAN_CLASS_SCHEDULE.CLASS_ID);
        fieldList.add(EP_ORGAN_CLASS_SCHEDULE.START_TIME);
        fieldList.add(EP_ORGAN_CLASS_SCHEDULE.CATALOG_INDEX);
        fieldList.add(EP_ORGAN_CLASS_SCHEDULE.CLASS_CATALOG_ID);
        fieldList.add(EP_ORGAN_CLASS_SCHEDULE.STATUS);
        fieldList.add(EP_MEMBER_CHILD.CHILD_NICK_NAME);
        fieldList.add(EP_MEMBER_CHILD.CHILD_TRUE_NAME);
        fieldList.add(EP_MEMBER_CHILD_COMMENT.ID.as("launchId"));
        fieldList.add(EP_MEMBER_CHILD_COMMENT.TYPE);
        fieldList.add(EP_MEMBER_CHILD_COMMENT.CONTENT.as("contentLaunch"));

        fieldList.add(memberChildCommentCopy.ID.as("replyId"));
        fieldList.add(memberChildCommentCopy.CONTENT.as("contentReply"));
        SelectConditionStep<Record> record = dslContext.select(fieldList)
                                                       .from(EP_ORGAN_CLASS_SCHEDULE)
                                                       .leftJoin(EP_MEMBER_CHILD_COMMENT)
                                                       .on(EP_MEMBER_CHILD_COMMENT.CHILD_ID.eq(EP_ORGAN_CLASS_SCHEDULE.CHILD_ID)
                                                    .and(EP_MEMBER_CHILD_COMMENT.CLASS_SCHEDULE_ID.eq(EP_ORGAN_CLASS_SCHEDULE.ID)))
                                                       .leftJoin(EP_MEMBER_CHILD).on(EP_MEMBER_CHILD.ID.eq(EP_ORGAN_CLASS_SCHEDULE.CHILD_ID))

                                                       .leftJoin(memberChildCommentCopy).on(memberChildCommentCopy.P_ID.eq(EP_MEMBER_CHILD_COMMENT.ID))
                                                       .where(condition);

        List<OrganClassScheduleDto> list = record.orderBy(EP_ORGAN_CLASS_SCHEDULE.ID.asc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetchInto(OrganClassScheduleDto.class);
        PageImpl<OrganClassScheduleDto> pPage = new PageImpl<OrganClassScheduleDto>(list, pageable, totalCount);
        return pPage;
    }

    /**
     * 商户后台修改评论内容
     *
     * @param id
     * @param content
     */
    public int updateContent(Long id, String content) {
        return dslContext.update(EP_MEMBER_CHILD_COMMENT)
                .set(EP_MEMBER_CHILD_COMMENT.CONTENT, content)
                .where(EP_MEMBER_CHILD_COMMENT.ID.eq(id))
                .and(EP_MEMBER_CHILD_COMMENT.DEL_FLAG.eq(false))
                .execute();
    }

    /**
     * 根据行程id物理删除
     *
     * @param classScheduleId
     * @return
     */
    public int physicalDeleteByScheduleId(Long classScheduleId) {
        return dslContext.delete(EP_MEMBER_CHILD_COMMENT)
                         .where(EP_MEMBER_CHILD_COMMENT.CLASS_SCHEDULE_ID.eq(classScheduleId))
                         .execute();
    }

    /**
     * 根据pid获取回复
     *
     * @param pid
     * @return
     */
    public Optional<EpMemberChildCommentPo> findReplyByPid(Long pid) {
        EpMemberChildCommentPo data = dslContext.selectFrom(EP_MEMBER_CHILD_COMMENT)
                .where(EP_MEMBER_CHILD_COMMENT.P_ID.eq(pid))
                .and(EP_MEMBER_CHILD_COMMENT.DEL_FLAG.eq(false))
                .fetchOneInto(EpMemberChildCommentPo.class);
        return Optional.ofNullable(data);
    }

    /**
     * 根据id逻辑删除
     *
     * @param id
     * @return
     */
    public int deleteLogicById(Long id) {
        return dslContext.update(EP_MEMBER_CHILD_COMMENT)
                .set(EP_MEMBER_CHILD_COMMENT.DEL_FLAG, true)
                .where(EP_MEMBER_CHILD_COMMENT.ID.eq(id))
                .and(EP_MEMBER_CHILD_COMMENT.DEL_FLAG.eq(false))
                .execute();
    }

    /**
     * 商户后台主页新回复集合
     *
     * @param ognId
     * @param homeReplySize
     * @return
     */
    public List<HomeMemberChildReplyBo> findHomeReply(Long ognId, int homeReplySize) {
        return dslContext.select(
                EP_MEMBER_CHILD_COMMENT.ID,
                EP_MEMBER_CHILD_COMMENT.CONTENT,
                EP_MEMBER_CHILD_COMMENT.CREATE_AT,
                EP_MEMBER_CHILD.CHILD_NICK_NAME
        ).from(EP_MEMBER_CHILD_COMMENT)
                .innerJoin(EP_MEMBER_CHILD)
                .on(EP_MEMBER_CHILD_COMMENT.CHILD_ID.eq(EP_MEMBER_CHILD.ID))
                .where(EP_MEMBER_CHILD_COMMENT.OGN_ID.eq(ognId))
                .and(EP_MEMBER_CHILD_COMMENT.TYPE.eq(EpMemberChildCommentType.reply))
                .and(EP_MEMBER_CHILD_COMMENT.DEL_FLAG.eq(false))
                .orderBy(EP_MEMBER_CHILD_COMMENT.CREATE_AT.desc())
                .limit(homeReplySize)
                .fetchInto(HomeMemberChildReplyBo.class);
    }
}
