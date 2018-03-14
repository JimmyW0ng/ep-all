package com.ep.domain.repository;

import com.ep.domain.constant.BizConstant;
import com.ep.domain.pojo.bo.MemberChildHonorBo;
import com.ep.domain.pojo.po.EpMemberChildHonorPo;
import com.ep.domain.repository.domain.tables.records.EpMemberChildHonorRecord;
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
 * @Description: 孩子荣誉表Repository
 * @Author: J.W
 * @Date: 上午10:35 2017/11/27
 */
@Repository
public class MemberChildHonorRepository extends AbstractCRUDRepository<EpMemberChildHonorRecord, Long, EpMemberChildHonorPo> {

    @Autowired
    public MemberChildHonorRepository(DSLContext dslContext) {
        super(dslContext, EP_MEMBER_CHILD_HONOR, EP_MEMBER_CHILD_HONOR.ID, EpMemberChildHonorPo.class);
    }


    /**
     * 统计孩子的荣誉数
     *
     * @param childId
     * @return
     */
    public Long countByChildId(Long childId) {
        return dslContext.selectCount()
                .from(EP_MEMBER_CHILD_HONOR)
                .where(EP_MEMBER_CHILD_HONOR.CHILD_ID.eq(childId))
                .and(EP_MEMBER_CHILD_HONOR.DEL_FLAG.eq(false))
                .fetchOneInto(Long.class);
    }

    /**
     * 查询孩子获得的最新荣誉-分页
     *
     * @param pageable
     * @param childId
     * @return
     */
    public Page<MemberChildHonorBo> queryRecentForPage(Pageable pageable, Long childId) {
        Long count = dslContext.selectCount().from(EP_MEMBER_CHILD_HONOR)
                .where(EP_MEMBER_CHILD_HONOR.CHILD_ID.eq(childId))
                .and(EP_MEMBER_CHILD_HONOR.DEL_FLAG.eq(false))
                .fetchOneInto(Long.class);
        if (count == BizConstant.DB_NUM_ZERO) {
            return new PageImpl(Lists.newArrayList(), pageable, count);
        }
        List<Field<?>> fieldList = Lists.newArrayList(EP_MEMBER_CHILD_HONOR.CONTENT);
        fieldList.add(EP_MEMBER_CHILD_HONOR.COURSE_ID);
        fieldList.add(EP_MEMBER_CHILD_HONOR.CREATE_AT);
        fieldList.add(EP_ORGAN.OGN_NAME);
        fieldList.add(EP_ORGAN_COURSE.COURSE_NAME);
        fieldList.add(EP_MEMBER_CHILD.CHILD_NICK_NAME);
        List<MemberChildHonorBo> data = dslContext.select(fieldList)
                .from(EP_MEMBER_CHILD_HONOR)
                .leftJoin(EP_MEMBER_CHILD)
                .on(EP_MEMBER_CHILD_HONOR.CHILD_ID.eq(EP_MEMBER_CHILD.ID))
                .and(EP_MEMBER_CHILD.DEL_FLAG.eq(false))
                .leftJoin(EP_ORGAN)
                .on(EP_MEMBER_CHILD_HONOR.OGN_ID.eq(EP_ORGAN.ID))
                .and(EP_ORGAN.DEL_FLAG.eq(false))
                .leftJoin(EP_ORGAN_COURSE)
                .on(EP_MEMBER_CHILD_HONOR.COURSE_ID.eq(EP_ORGAN_COURSE.ID))
                .and(EP_ORGAN_COURSE.DEL_FLAG.eq(false))
                .where(EP_MEMBER_CHILD_HONOR.CHILD_ID.eq(childId))
                .and(EP_MEMBER_CHILD_HONOR.DEL_FLAG.eq(false))
                .orderBy(EP_MEMBER_CHILD_HONOR.CREATE_AT.desc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetchInto(MemberChildHonorBo.class);
        return new PageImpl(data, pageable, count);
    }

    /**
     * 查询孩子班次内获得的荣誉
     *
     * @param childId
     * @param classId
     * @return
     */
    public List<MemberChildHonorBo> queryByClassId(Long childId, Long classId) {
        List<Field<?>> fieldList = Lists.newArrayList(EP_MEMBER_CHILD_HONOR.CONTENT);
        fieldList.add(EP_MEMBER_CHILD_HONOR.CREATE_AT);
        fieldList.add(EP_ORGAN.OGN_NAME);
        fieldList.add(EP_ORGAN_COURSE.COURSE_NAME);
        fieldList.add(EP_MEMBER_CHILD.CHILD_NICK_NAME);
        return dslContext.select(fieldList)
                .from(EP_MEMBER_CHILD_HONOR)
                .leftJoin(EP_MEMBER_CHILD)
                .on(EP_MEMBER_CHILD_HONOR.CHILD_ID.eq(EP_MEMBER_CHILD.ID))
                .and(EP_MEMBER_CHILD.DEL_FLAG.eq(false))
                .leftJoin(EP_ORGAN)
                .on(EP_MEMBER_CHILD_HONOR.OGN_ID.eq(EP_ORGAN.ID))
                .and(EP_ORGAN.DEL_FLAG.eq(false))
                .leftJoin(EP_ORGAN_COURSE)
                .on(EP_MEMBER_CHILD_HONOR.COURSE_ID.eq(EP_ORGAN_COURSE.ID))
                .and(EP_ORGAN_COURSE.DEL_FLAG.eq(false))
                .where(EP_MEMBER_CHILD_HONOR.CHILD_ID.eq(childId))
                .and(EP_MEMBER_CHILD_HONOR.CLASS_ID.eq(classId))
                .and(EP_MEMBER_CHILD_HONOR.DEL_FLAG.eq(false))
                .orderBy(EP_MEMBER_CHILD_HONOR.CREATE_AT.desc())
                .fetchInto(MemberChildHonorBo.class);
    }

    /**
     * 后台获取孩子荣誉分页
     *
     * @param pageable
     * @param condition
     * @return
     */
    public Page<MemberChildHonorBo> findbyPageAndCondition(Pageable pageable, Collection<? extends Condition> condition) {
        long totalCount = dslContext.selectCount()
                .from(EP_MEMBER_CHILD_HONOR)
                .leftJoin(EP_ORGAN_COURSE).on(EP_MEMBER_CHILD_HONOR.COURSE_ID.eq(EP_ORGAN_COURSE.ID))
                .leftJoin(EP_ORGAN_CLASS).on(EP_MEMBER_CHILD_HONOR.CLASS_ID.eq(EP_ORGAN_CLASS.ID))
                .leftJoin(EP_MEMBER_CHILD).on(EP_MEMBER_CHILD_HONOR.CHILD_ID.eq(EP_MEMBER_CHILD.ID))
                .where(condition).fetchOne(0, Long.class);
        if (totalCount == BizConstant.DB_NUM_ZERO) {
            return new PageImpl<>(Lists.newArrayList(), pageable, totalCount);
        }
        List<Field<?>> fieldList = Lists.newArrayList(EP_MEMBER_CHILD_HONOR.fields());
        fieldList.add(EP_ORGAN_COURSE.COURSE_NAME);
        fieldList.add(EP_ORGAN_CLASS.CLASS_NAME);
        fieldList.add(EP_MEMBER_CHILD.CHILD_TRUE_NAME);

        SelectConditionStep<Record> record = dslContext.select(fieldList)
                .from(EP_MEMBER_CHILD_HONOR)
                .leftJoin(EP_ORGAN_COURSE).on(EP_MEMBER_CHILD_HONOR.COURSE_ID.eq(EP_ORGAN_COURSE.ID))
                .leftJoin(EP_ORGAN_CLASS).on(EP_MEMBER_CHILD_HONOR.CLASS_ID.eq(EP_ORGAN_CLASS.ID))
                .leftJoin(EP_MEMBER_CHILD).on(EP_MEMBER_CHILD_HONOR.CHILD_ID.eq(EP_MEMBER_CHILD.ID))
                .where(condition);

        List<MemberChildHonorBo> list = record.orderBy(getSortFields(pageable.getSort()))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetchInto(MemberChildHonorBo.class);
        PageImpl<MemberChildHonorBo> page = new PageImpl<MemberChildHonorBo>(list, pageable, totalCount);
        return page;
    }

    /**
     * 根据id获取孩子荣誉
     *
     * @param id
     * @return
     */
    public MemberChildHonorBo findBoById(Long id) {
        List<Field<?>> fieldList = Lists.newArrayList(EP_MEMBER_CHILD_HONOR.fields());
        fieldList.add(EP_ORGAN_COURSE.COURSE_NAME);
        fieldList.add(EP_ORGAN_CLASS.CLASS_NAME);
        fieldList.add(EP_MEMBER_CHILD.CHILD_TRUE_NAME);
        return dslContext.select(fieldList).from(EP_MEMBER_CHILD_HONOR)
                .leftJoin(EP_ORGAN_COURSE).on(EP_MEMBER_CHILD_HONOR.COURSE_ID.eq(EP_ORGAN_COURSE.ID))
                .leftJoin(EP_ORGAN_CLASS).on(EP_MEMBER_CHILD_HONOR.CLASS_ID.eq(EP_ORGAN_CLASS.ID))
                .leftJoin(EP_MEMBER_CHILD).on(EP_MEMBER_CHILD_HONOR.CHILD_ID.eq(EP_MEMBER_CHILD.ID))

                .where(EP_MEMBER_CHILD_HONOR.ID.eq(id))
                .and(EP_MEMBER_CHILD_HONOR.DEL_FLAG.eq(false))
                .fetchOneInto(MemberChildHonorBo.class);
    }

    /**
     * 更新孩子的荣誉记录
     *
     * @param po
     */
    public int updateChildHonor(EpMemberChildHonorPo po) {
        return dslContext.update(EP_MEMBER_CHILD_HONOR)
                .set(EP_MEMBER_CHILD_HONOR.CONTENT, po.getContent())
                .where(EP_MEMBER_CHILD_HONOR.ID.eq(po.getId()))
                .and(EP_MEMBER_CHILD_HONOR.DEL_FLAG.eq(false))
                .execute();
    }

    /**
     * 根据id逻辑删除记录
     *
     * @param id
     */
    public int deleteLogicById(Long id) {
        return dslContext.update(EP_MEMBER_CHILD_HONOR)
                .set(EP_MEMBER_CHILD_HONOR.DEL_FLAG, true)
                .where(EP_MEMBER_CHILD_HONOR.ID.eq(id))
                .and(EP_MEMBER_CHILD_HONOR.DEL_FLAG.eq(false))
                .execute();
    }
}

