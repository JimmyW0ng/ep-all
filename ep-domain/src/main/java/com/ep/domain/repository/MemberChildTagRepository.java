package com.ep.domain.repository;

import com.ep.domain.pojo.bo.MemberChildTagBo;
import com.ep.domain.pojo.po.EpMemberChildTagPo;
import com.ep.domain.repository.domain.tables.records.EpMemberChildTagRecord;
import com.google.common.collect.Lists;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.ep.domain.repository.domain.Tables.EP_CONSTANT_TAG;
import static com.ep.domain.repository.domain.Tables.EP_MEMBER_CHILD_TAG;

/**
 * @Description: 孩子标签记录表Repository
 * @Author J.W
 * @Date: 下午 5:17 2018/2/22 0022
 */
@Repository
public class MemberChildTagRepository extends AbstractCRUDRepository<EpMemberChildTagRecord, Long, EpMemberChildTagPo> {

    @Autowired
    public MemberChildTagRepository(DSLContext dslContext) {
        super(dslContext, EP_MEMBER_CHILD_TAG, EP_MEMBER_CHILD_TAG.ID, EpMemberChildTagPo.class);
    }

    /**
     * 根据孩子和获取课时标签
     *
     * @param childId
     * @param classScheduleId
     * @return
     */
    public List<EpMemberChildTagPo> findByChildIdAndClassCatalogId(Long childId, Long classScheduleId) {
        return dslContext.selectFrom(EP_MEMBER_CHILD_TAG)
                         .where(EP_MEMBER_CHILD_TAG.CLASS_SCHEDULE_ID.eq(classScheduleId))
                         .and(EP_MEMBER_CHILD_TAG.CHILD_ID.eq(childId))
                         .and(EP_MEMBER_CHILD_TAG.DEL_FLAG.eq(false))
                         .fetchInto(EpMemberChildTagPo.class);
    }

    /**
     * 根据孩子和获取课时标签和便签名
     *
     * @param childId
     * @param classScheduleId
     * @return
     */
    public List<MemberChildTagBo> findDetailByChildIdAndClassScheduleId(Long childId, Long classScheduleId) {
        List<Field<?>> fieldList = Lists.newArrayList(EP_MEMBER_CHILD_TAG.fields());
        fieldList.add(EP_CONSTANT_TAG.TAG_NAME);
        return dslContext.select(fieldList)
                         .from(EP_MEMBER_CHILD_TAG)
                         .leftJoin(EP_CONSTANT_TAG)
                         .on(EP_MEMBER_CHILD_TAG.TAG_ID.eq(EP_CONSTANT_TAG.ID))
                         .and(EP_CONSTANT_TAG.DEL_FLAG.eq(false))
                         .where(EP_MEMBER_CHILD_TAG.CLASS_SCHEDULE_ID.eq(classScheduleId))
                         .and(EP_MEMBER_CHILD_TAG.CHILD_ID.eq(childId))
                         .and(EP_MEMBER_CHILD_TAG.DEL_FLAG.eq(false))
                         .fetchInto(MemberChildTagBo.class);
    }

    /**
     * 以孩子获取分组获取孩子的标签数
     *
     * @param childId
     * @return
     */
    public List<MemberChildTagBo> findTagsByChildId(Long childId) {
        List<Field<?>> fieldList = Lists.newArrayList(EP_MEMBER_CHILD_TAG.TAG_ID);
        fieldList.add(DSL.count(EP_MEMBER_CHILD_TAG.ID).as("num"));
        fieldList.add(EP_CONSTANT_TAG.TAG_NAME);
        return dslContext.select(fieldList)
                .from(EP_MEMBER_CHILD_TAG)
                .innerJoin(EP_CONSTANT_TAG)
                .on(EP_MEMBER_CHILD_TAG.TAG_ID.eq(EP_CONSTANT_TAG.ID))
                .and(EP_CONSTANT_TAG.DEL_FLAG.eq(false))
                .where(EP_MEMBER_CHILD_TAG.CHILD_ID.eq(childId))
                .and(EP_MEMBER_CHILD_TAG.DEL_FLAG.eq(false))
                .groupBy(EP_MEMBER_CHILD_TAG.TAG_ID)
                .orderBy(DSL.max(EP_MEMBER_CHILD_TAG.CREATE_AT).desc())
                .fetchInto(MemberChildTagBo.class);
    }

    /**
     * 以孩子班次分组获取孩子的标签数
     *
     * @param childId
     * @param classId
     * @return
     */
    public List<MemberChildTagBo> findTagsByChildIdAndClassId(Long childId, Long classId) {
        List<Field<?>> fieldList = Lists.newArrayList(EP_MEMBER_CHILD_TAG.TAG_ID);
        fieldList.add(DSL.count(EP_MEMBER_CHILD_TAG.ID).as("num"));
        fieldList.add(EP_CONSTANT_TAG.TAG_NAME);
        return dslContext.select(fieldList)
                .from(EP_MEMBER_CHILD_TAG)
                .innerJoin(EP_CONSTANT_TAG)
                .on(EP_MEMBER_CHILD_TAG.TAG_ID.eq(EP_CONSTANT_TAG.ID))
                .and(EP_CONSTANT_TAG.DEL_FLAG.eq(false))
                .where(EP_MEMBER_CHILD_TAG.CHILD_ID.eq(childId))
                .and(EP_MEMBER_CHILD_TAG.CLASS_ID.eq(classId))
                .and(EP_MEMBER_CHILD_TAG.DEL_FLAG.eq(false))
                .groupBy(EP_MEMBER_CHILD_TAG.TAG_ID)
                .orderBy(DSL.max(EP_MEMBER_CHILD_TAG.CREATE_AT).desc())
                .fetchInto(MemberChildTagBo.class);
    }

    /**
     * 根据孩子childId和班次目录classScheduleId获取孩子的标签
     *
     * @param childId
     * @param classScheduleId
     * @return
     */
    public List<MemberChildTagBo> findBosByChildIdAndClassCatalogId(Long childId, Long classScheduleId) {
        List<Field<?>> fieldList = Lists.newArrayList(EP_MEMBER_CHILD_TAG.TAG_ID);
        fieldList.add(EP_CONSTANT_TAG.ID);
        fieldList.add(EP_CONSTANT_TAG.TAG_NAME);
        return dslContext.select(fieldList)
                         .from(EP_MEMBER_CHILD_TAG)
                         .innerJoin(EP_CONSTANT_TAG)
                         .on(EP_MEMBER_CHILD_TAG.TAG_ID.eq(EP_CONSTANT_TAG.ID))
                         .and(EP_CONSTANT_TAG.DEL_FLAG.eq(false))
                         .where(EP_MEMBER_CHILD_TAG.CHILD_ID.eq(childId))
                         .and(EP_MEMBER_CHILD_TAG.CLASS_SCHEDULE_ID.eq(classScheduleId))
                         .and(EP_MEMBER_CHILD_TAG.DEL_FLAG.eq(false))
                         .groupBy(EP_MEMBER_CHILD_TAG.TAG_ID)
                         .orderBy(DSL.max(EP_MEMBER_CHILD_TAG.CREATE_AT).desc())
                         .fetchInto(MemberChildTagBo.class);
    }

    /**
     * 根据id逻辑删除孩子标签记录
     *
     * @param id
     */
    public void deleteLogicChildTagById(Long id) {
        dslContext.update(EP_MEMBER_CHILD_TAG)
                .set(EP_MEMBER_CHILD_TAG.DEL_FLAG, true)
                .where(EP_MEMBER_CHILD_TAG.ID.eq(id))
                .execute();
    }

    /**
     * 根据行程id物理删除
     * @param classScheduleId
     */
    public int physicalDeleteByScheduleId(Long classScheduleId) {
        return dslContext.delete(EP_MEMBER_CHILD_TAG)
                         .where(EP_MEMBER_CHILD_TAG.CLASS_SCHEDULE_ID.eq(classScheduleId))
                  .execute();
    }

}
