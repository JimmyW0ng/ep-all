package com.ep.domain.repository;

import com.ep.domain.constant.BizConstant;
import com.ep.domain.pojo.dto.OrganClassScheduleDto;
import com.ep.domain.pojo.po.EpOrganClassSchedulePo;
import com.ep.domain.repository.domain.tables.EpMemberChildComment;
import com.ep.domain.repository.domain.tables.records.EpOrganClassScheduleRecord;
import com.google.common.collect.Lists;
import org.jooq.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

import static com.ep.domain.repository.domain.Tables.*;

/**
 * @Description: 班次行程表Repository
 * @Author: CC.F
 * @Date: 9:53 2018/2/12
 */
@Repository
public class OrganClassScheduleRepository extends AbstractCRUDRepository<EpOrganClassScheduleRecord, Long, EpOrganClassSchedulePo> {

    @Autowired
    public OrganClassScheduleRepository(DSLContext dslContext) {
        super(dslContext, EP_ORGAN_CLASS_SCHEDULE, EP_ORGAN_CLASS_SCHEDULE.ID, EpOrganClassSchedulePo.class);
    }

    /**
     * 商户后台获取随堂评价分页
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
                        .and(EP_MEMBER_CHILD_COMMENT.CLASS_CATALOG_ID.eq(EP_ORGAN_CLASS_SCHEDULE.CLASS_CATALOG_ID)))
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
                        .and(EP_MEMBER_CHILD_COMMENT.CLASS_CATALOG_ID.eq(EP_ORGAN_CLASS_SCHEDULE.CLASS_CATALOG_ID)))
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

}
