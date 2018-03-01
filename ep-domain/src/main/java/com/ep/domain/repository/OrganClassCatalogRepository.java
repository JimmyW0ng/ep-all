package com.ep.domain.repository;

import com.ep.domain.pojo.bo.MemberChildTagAndCommentBo;
import com.ep.domain.pojo.bo.OrganClassCatalogBo;
import com.ep.domain.pojo.po.EpOrganClassCatalogPo;
import com.ep.domain.repository.domain.enums.EpMemberChildCommentType;
import com.ep.domain.repository.domain.tables.records.EpOrganClassCatalogRecord;
import com.google.common.collect.Lists;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.impl.DSL;
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
public class OrganClassCatalogRepository extends AbstractCRUDRepository<EpOrganClassCatalogRecord, Long, EpOrganClassCatalogPo> {

    @Autowired
    public OrganClassCatalogRepository(DSLContext dslContext) {
        super(dslContext, EP_ORGAN_CLASS_CATALOG, EP_ORGAN_CLASS_CATALOG.ID, EpOrganClassCatalogPo.class);
    }

    /**
     * 更新EpOrganClassCatalogPo
     * @param po
     */
    public void updateEpOrganClassCatalogPo(EpOrganClassCatalogPo po){
        dslContext.update(EP_ORGAN_CLASS_CATALOG)
                .set(EP_ORGAN_CLASS_CATALOG.CATALOG_TITLE,po.getCatalogTitle())
                .set(EP_ORGAN_CLASS_CATALOG.CATALOG_INDEX,po.getCatalogIndex())
                .set(EP_ORGAN_CLASS_CATALOG.CATALOG_DESC,po.getCatalogDesc())
                .set(EP_ORGAN_CLASS_CATALOG.START_TIME,po.getStartTime())
                .set(EP_ORGAN_CLASS_CATALOG.UPDATE_AT, DSL.currentTimestamp())
                .execute();
    }

    /**
     * 根据id获取EpOrganClassCatalogPo
     * @param id
     * @return
     */
    public EpOrganClassCatalogPo findById(Long id){
        return dslContext.selectFrom(EP_ORGAN_CLASS_CATALOG)
                .where(EP_ORGAN_CLASS_CATALOG.ID.eq(id))
                .fetchOneInto(EpOrganClassCatalogPo.class);
    }

    /**
     * 根据课程id获取EpOrganClassCatalogPo
     * @param courseId
     * @return
     */
    public List<EpOrganClassCatalogPo> findByCourseId(Long courseId){
        List<Long> classId=dslContext.select(EP_ORGAN_CLASS.ID)
                .from(EP_ORGAN_CLASS)
                .where(EP_ORGAN_CLASS.COURSE_ID.eq(courseId))
                .and(EP_ORGAN_CLASS.DEL_FLAG.eq(false))
                .fetchInto(Long.class);
        return dslContext.selectFrom(EP_ORGAN_CLASS_CATALOG)
                .where(EP_ORGAN_CLASS_CATALOG.CLASS_ID.in(classId))
                .and(EP_ORGAN_CLASS_CATALOG.DEL_FLAG.eq(false))
                .fetchInto(EpOrganClassCatalogPo.class);
    }

    /**
     * 根据班次id获取EpOrganClassCatalogPo
     * @param classId
     * @return
     */
    public List<EpOrganClassCatalogPo> findByClassId(Long classId){
        return dslContext.selectFrom(EP_ORGAN_CLASS_CATALOG)
                .where(EP_ORGAN_CLASS_CATALOG.CLASS_ID.eq(classId))
                .and(EP_ORGAN_CLASS_CATALOG.DEL_FLAG.eq(false))
                .fetchInto(EpOrganClassCatalogPo.class);
    }

    /**
     * 获取课时评论
     *
     * @param classId
     * @param classCatalogId
     * @return
     */
    public List<MemberChildTagAndCommentBo> findChildComments(Long classId, Long classCatalogId) {
        List<Field<?>> fieldList = Lists.newArrayList(EP_ORGAN_CLASS_CHILD.CHILD_ID);
        fieldList.add(EP_MEMBER_CHILD_COMMENT.CONTENT.as("comment"));
        return dslContext.select(fieldList)
                         .from(EP_ORGAN_CLASS_CHILD)
                         .leftJoin(EP_MEMBER_CHILD_COMMENT)
                         .on(EP_MEMBER_CHILD_COMMENT.CLASS_CATALOG_ID.eq(classCatalogId))
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
        dslContext.update(EP_ORGAN_CLASS_CATALOG)
                .set(EP_ORGAN_CLASS_CATALOG.DEL_FLAG,true)
                .where(EP_ORGAN_CLASS_CATALOG.ID.in(ids))
                .execute();
    }

    /**
     * 根据班次classIds批量逻辑删除记录
     * @param classIds
     */
    public void deleteByClassIds(List<Long> classIds){
        dslContext.update(EP_ORGAN_CLASS_CATALOG)
                .set(EP_ORGAN_CLASS_CATALOG.DEL_FLAG,true)
                .where(EP_ORGAN_CLASS_CATALOG.CLASS_ID.in(classIds))
                .execute();
    }

    /**
     * 根据班次classIds批量物理删除记录
     * @param classIds
     */
    public void deletePhysicByClassIds(List<Long> classIds){
        dslContext.delete(EP_ORGAN_CLASS_CATALOG)
                .where(EP_ORGAN_CLASS_CATALOG.CLASS_ID.in(classIds))
                .execute();
    }

    /**
     * 根据班次获取课时明细
     *
     * @param classId
     * @return
     */
    public List<OrganClassCatalogBo> findDetailByClassId(Long classId) {
        List<Field<?>> fieldList = Lists.newArrayList(EP_MEMBER_CHILD_COMMENT.as("co").CONTENT.as("comment"));
        fieldList.add(EP_MEMBER_CHILD_COMMENT.as("co").ID.as("commentId"));
        fieldList.add(EP_MEMBER_CHILD_COMMENT.as("co").CREATE_AT.as("commentTime"));
        fieldList.add(EP_MEMBER_CHILD_COMMENT.as("re").CONTENT.as("replay"));
        fieldList.add(EP_ORGAN_CLASS_CATALOG.ID);
        fieldList.add(EP_ORGAN_CLASS_CATALOG.CATALOG_TITLE);
        fieldList.add(EP_ORGAN_CLASS_CATALOG.CATALOG_DESC);
        fieldList.add(EP_ORGAN_CLASS_CATALOG.START_TIME);
        return dslContext.select(fieldList)
                .from(EP_ORGAN_CLASS_CATALOG)
                .leftJoin(EP_MEMBER_CHILD_COMMENT.as("co"))
                .on(EP_ORGAN_CLASS_CATALOG.ID.eq(EP_MEMBER_CHILD_COMMENT.as("co").CLASS_CATALOG_ID)
                        .and(EP_MEMBER_CHILD_COMMENT.as("co").TYPE.eq(EpMemberChildCommentType.launch))
                        .and(EP_MEMBER_CHILD_COMMENT.as("co").DEL_FLAG.eq(false)))
                .leftJoin(EP_MEMBER_CHILD_COMMENT.as("re"))
                .on(EP_ORGAN_CLASS_CATALOG.ID.eq(EP_MEMBER_CHILD_COMMENT.as("re").CLASS_CATALOG_ID)
                        .and(EP_MEMBER_CHILD_COMMENT.as("re").TYPE.eq(EpMemberChildCommentType.reply))
                        .and(EP_MEMBER_CHILD_COMMENT.as("re").DEL_FLAG.eq(false)))
                .where(EP_ORGAN_CLASS_CATALOG.CLASS_ID.eq(classId))
                .and(EP_ORGAN_CLASS_CATALOG.DEL_FLAG.eq(false))
                .orderBy(EP_ORGAN_CLASS_CATALOG.CATALOG_INDEX.asc())
                .fetchInto(OrganClassCatalogBo.class);
    }
}
