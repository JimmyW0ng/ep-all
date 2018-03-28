package com.ep.domain.repository;

import com.ep.domain.pojo.po.EpOrganClassSchedulePo;
import com.ep.domain.repository.domain.tables.records.EpOrganClassScheduleRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.ep.domain.repository.domain.Tables.EP_ORGAN_CLASS_SCHEDULE;

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
     * 后台机构班次随堂分页列表
     *
     * @param pageable
     * @param condition
     * @return
     */
//    public Page<OrganClassScheduleBo> findbyPageAndCondition(Pageable pageable, Collection<? extends Condition> condition) {
//        long totalCount = dslContext.selectCount()
//                .from(EP_ORGAN_CLASS_SCHEDULE)
//                .leftJoin(EP_ORGAN).on(EP_ORGAN_CLASS_COMMENT.OGN_ID.eq(EP_ORGAN.ID))
//                .leftJoin(EP_ORGAN_COURSE).on(EP_ORGAN_CLASS_COMMENT.COURSE_ID.eq(EP_ORGAN_COURSE.ID))
//                .leftJoin(EP_ORGAN_CLASS).on(EP_ORGAN_CLASS_COMMENT.CLASS_ID.eq(EP_ORGAN_CLASS.ID))
//                .leftJoin(EP_MEMBER_CHILD).on(EP_ORGAN_CLASS_COMMENT.CHILD_ID.eq(EP_MEMBER_CHILD.ID))
//                .where(condition).fetchOne(0, Long.class);
//        if (totalCount == BizConstant.DB_NUM_ZERO) {
//            return new PageImpl<>(Lists.newArrayList(), pageable, totalCount);
//        }
//        List<Field<?>> fieldList = Lists.newArrayList(EP_ORGAN_CLASS_COMMENT.fields());
//        fieldList.add(EP_ORGAN.OGN_NAME);
//        fieldList.add(EP_ORGAN_COURSE.COURSE_NAME);
//        fieldList.add(EP_ORGAN_CLASS.CLASS_NAME);
//        fieldList.add(EP_MEMBER_CHILD.CHILD_NICK_NAME);
//
//        SelectConditionStep<Record> record = dslContext.select(fieldList)
//                .from(EP_ORGAN_CLASS_COMMENT)
//                .leftJoin(EP_ORGAN).on(EP_ORGAN_CLASS_COMMENT.OGN_ID.eq(EP_ORGAN.ID))
//                .leftJoin(EP_ORGAN_COURSE).on(EP_ORGAN_CLASS_COMMENT.COURSE_ID.eq(EP_ORGAN_COURSE.ID))
//                .leftJoin(EP_ORGAN_CLASS).on(EP_ORGAN_CLASS_COMMENT.CLASS_ID.eq(EP_ORGAN_CLASS.ID))
//                .leftJoin(EP_MEMBER_CHILD).on(EP_ORGAN_CLASS_COMMENT.CHILD_ID.eq(EP_MEMBER_CHILD.ID))
//                .where(condition);
//
//        List<OrganClassCommentBo> list = record.orderBy(getSortFields(pageable.getSort()))
//                .limit(pageable.getPageSize())
//                .offset(pageable.getOffset())
//                .fetchInto(OrganClassCommentBo.class);
//        PageImpl<OrganClassCommentBo> pPage = new PageImpl<OrganClassCommentBo>(list, pageable, totalCount);
//        return pPage;
//    }

}
