package com.ep.domain.repository;

import com.ep.domain.pojo.bo.MemberChildTagAndCommentBo;
import com.ep.domain.pojo.po.EpOrganClassCatelogPo;
import com.ep.domain.repository.domain.enums.EpMemberChildCommentType;
import com.ep.domain.repository.domain.tables.records.EpOrganClassCatelogRecord;
import com.google.common.collect.Lists;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.ep.domain.repository.domain.Tables.*;

/**
 * @Description: 班次课程内容目录表Repository
 * @Author: CC.F
 * @Date: 9:53 2018/2/12
 */
@Repository
public class OrganClassCatelogRepository extends AbstractCRUDRepository<EpOrganClassCatelogRecord, Long, EpOrganClassCatelogPo> {

    @Autowired
    public OrganClassCatelogRepository(DSLContext dslContext) {
        super(dslContext, EP_ORGAN_CLASS_CATELOG, EP_ORGAN_CLASS_CATELOG.ID, EpOrganClassCatelogPo.class);
    }

    public List<EpOrganClassCatelogPo> findByCourseId(Long courseId){
        List<Long> classId=dslContext.select(EP_ORGAN_CLASS.ID)
                .from(EP_ORGAN_CLASS)
                .where(EP_ORGAN_CLASS.COURSE_ID.eq(courseId))
                .and(EP_ORGAN_CLASS.DEL_FLAG.eq(false))
                .fetchInto(Long.class);
        return dslContext.selectFrom(EP_ORGAN_CLASS_CATELOG)
                .where(EP_ORGAN_CLASS_CATELOG.CLASS_ID.in(classId))
                .and(EP_ORGAN_CLASS_CATELOG.DEL_FLAG.eq(false))
                .fetchInto(EpOrganClassCatelogPo.class);
    }

    public List<EpOrganClassCatelogPo> findByClassId(Long classId){
        return dslContext.selectFrom(EP_ORGAN_CLASS_CATELOG)
                .where(EP_ORGAN_CLASS_CATELOG.CLASS_ID.eq(classId))
                .and(EP_ORGAN_CLASS_CATELOG.DEL_FLAG.eq(false))
                .fetchInto(EpOrganClassCatelogPo.class);
    }

    /**
     * 获取课时评论
     *
     * @param classId
     * @param classCatelogId
     * @return
     */
    public List<MemberChildTagAndCommentBo> findChildComments(Long classId, Long classCatelogId) {
        List<Field<?>> fieldList = Lists.newArrayList(EP_ORGAN_CLASS_CHILD.CHILD_ID);
        fieldList.add(EP_MEMBER_CHILD_COMMENT.CONTENT.as("comment"));
        return dslContext.select(fieldList)
                         .from(EP_ORGAN_CLASS_CHILD)
                         .leftJoin(EP_MEMBER_CHILD_COMMENT)
                         .on(EP_MEMBER_CHILD_COMMENT.CLASS_CATELOG_ID.eq(classCatelogId))
                         .and(EP_MEMBER_CHILD_COMMENT.CHILD_ID.eq(EP_ORGAN_CLASS_CHILD.CHILD_ID))
                         .and(EP_MEMBER_CHILD_COMMENT.TYPE.eq(EpMemberChildCommentType.launch))
                         .and(EP_MEMBER_CHILD_COMMENT.DEL_FLAG.eq(false))
                         .where(EP_ORGAN_CLASS_CHILD.CLASS_ID.eq(classId))
                         .and(EP_ORGAN_CLASS_CHILD.DEL_FLAG.eq(false))
                         .orderBy(EP_ORGAN_CLASS_CHILD.CREATE_AT.asc())
                         .fetchInto(MemberChildTagAndCommentBo.class);
    }

    /**
     * 根据ids批量逻辑删除
     * @param ids
     */
    public void deleteByIds(List<Long> ids){
        dslContext.update(EP_ORGAN_CLASS_CATELOG)
                .set(EP_ORGAN_CLASS_CATELOG.DEL_FLAG,true)
                .where(EP_ORGAN_CLASS_CATELOG.ID.in(ids))
                .execute();
    }
}
