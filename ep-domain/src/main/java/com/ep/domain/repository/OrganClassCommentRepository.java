package com.ep.domain.repository;

import com.ep.domain.pojo.bo.OrganClassCommentBo;
import com.ep.domain.pojo.po.EpOrganClassCommentPo;
import com.ep.domain.repository.domain.tables.records.EpOrganClassCommentRecord;
import com.google.common.collect.Lists;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.ep.domain.repository.domain.Tables.*;

/**
 * @Description:机构课程评分表Repository
 * @Author: J.W
 * @Date: 上午10:35 2017/11/27
 */
@Repository
public class OrganClassCommentRepository extends AbstractCRUDRepository<EpOrganClassCommentRecord, Long, EpOrganClassCommentPo> {

    @Autowired
    public OrganClassCommentRepository(DSLContext dslContext) {
        super(dslContext, EP_ORGAN_CLASS_COMMENT, EP_ORGAN_CLASS_COMMENT.ID, EpOrganClassCommentPo.class);
    }

    /**
     * 获取课程班次精选评论
     *
     * @param courseId
     * @return
     */
    public List<OrganClassCommentBo> getChosenByCourseId(Long courseId) {
        List<Field<?>> fieldList = Lists.newArrayList(EP_ORGAN_CLASS_COMMENT.fields());
        fieldList.add(EP_MEMBER_CHILD.CHILD_NICK_NAME);
        fieldList.add(EP_ORGAN_CLASS.CLASS_NAME);
        return dslContext.select(fieldList)
                .from(EP_ORGAN_CLASS_COMMENT)
                .leftJoin(EP_MEMBER_CHILD)
                .on(EP_ORGAN_CLASS_COMMENT.CHILD_ID.eq(EP_MEMBER_CHILD.ID))
                .leftJoin(EP_ORGAN_CLASS)
                .on(EP_ORGAN_CLASS_COMMENT.CLASS_ID.eq(EP_ORGAN_CLASS.ID))
                .where(EP_ORGAN_CLASS_COMMENT.COURSE_ID.eq(courseId))
                .and(EP_ORGAN_CLASS_COMMENT.CHOSEN_FLAG.eq(true))
                .and(EP_ORGAN_CLASS_COMMENT.DEL_FLAG.eq(false))
                .orderBy(EP_ORGAN_CLASS_COMMENT.CREATE_AT.desc())
                .fetchInto(OrganClassCommentBo.class);
    }

}

