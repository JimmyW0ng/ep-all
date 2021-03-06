package com.ep.domain.repository;

import com.ep.common.tool.DateTools;
import com.ep.domain.constant.BizConstant;
import com.ep.domain.pojo.bo.ConstantTagBo;
import com.ep.domain.pojo.po.EpConstantTagPo;
import com.ep.domain.repository.domain.enums.EpConstantTagStatus;
import com.ep.domain.repository.domain.tables.records.EpConstantTagRecord;
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

import static com.ep.domain.repository.domain.tables.EpConstantTag.EP_CONSTANT_TAG;
import static com.ep.domain.repository.domain.tables.EpOrganCourseTag.EP_ORGAN_COURSE_TAG;

/**
 * @Description: 标签仓库
 * @Author: CC.F
 * @Date: 13:46 2018/2/9
 */
@Repository
public class ConstantTagRepository extends AbstractCRUDRepository<EpConstantTagRecord, Long, EpConstantTagPo> {

    @Autowired
    public ConstantTagRepository(DSLContext dslContext) {
        super(dslContext, EP_CONSTANT_TAG, EP_CONSTANT_TAG.ID, EpConstantTagPo.class);
    }

    public Optional<EpConstantTagPo> findById(Long id) {
        EpConstantTagPo data = dslContext.selectFrom(EP_CONSTANT_TAG)
                .where(EP_CONSTANT_TAG.ID.eq(id))
                .and(EP_CONSTANT_TAG.DEL_FLAG.eq(false))
                .fetchOneInto(EpConstantTagPo.class);
        return Optional.ofNullable(data);
    }

    /**
     * 根据状态和机构id获取标签
     *
     * @param ognId
     * @return
     */
    public List<EpConstantTagPo> findByOgnIdAndStatus(Long ognId, EpConstantTagStatus[] status) {
        return dslContext.selectFrom(EP_CONSTANT_TAG)
                .where(ognId == null ? EP_CONSTANT_TAG.OGN_ID.isNull() : EP_CONSTANT_TAG.OGN_ID.eq(ognId))
                .and(EP_CONSTANT_TAG.STATUS.in(status))
                .and(EP_CONSTANT_TAG.DEL_FLAG.eq(false))
                .orderBy(EP_CONSTANT_TAG.SORT.desc())
                .fetchInto(EpConstantTagPo.class);
    }

    /**
     * 根据课程类目id和机构id获取标签Bo
     *
     * @param catalogId
     * @param ognId
     * @return
     */
    public List<ConstantTagBo> findBosByCatalogIdAndOgnId(Long catalogId, Long ognId) {
        List<Field<?>> fieldList = Lists.newArrayList();
        fieldList.add(EP_CONSTANT_TAG.ID);
        fieldList.add(EP_CONSTANT_TAG.TAG_NAME);

        fieldList.add(EP_ORGAN_COURSE_TAG.ID.count().as("usedOrganCourseTag"));
        return dslContext.select(fieldList)
                .from(EP_CONSTANT_TAG)
                .leftJoin(EP_ORGAN_COURSE_TAG)
                .on(EP_CONSTANT_TAG.ID.eq(EP_ORGAN_COURSE_TAG.TAG_ID))
                .and(ognId == null ? EP_CONSTANT_TAG.OGN_ID.isNull() : EP_CONSTANT_TAG.OGN_ID.eq(ognId))
                .and(EP_CONSTANT_TAG.DEL_FLAG.eq(false))
                .groupBy(EP_CONSTANT_TAG.ID)
                .fetchInto(ConstantTagBo.class);
    }

    /**
     * 根据机构id获取标签
     *
     * @param ognId
     * @return
     */
    public List<EpConstantTagPo> findByOgnId(Long ognId) {
        return dslContext.selectFrom(EP_CONSTANT_TAG)
                .where(EP_CONSTANT_TAG.OGN_ID.eq(ognId))
                .and(EP_CONSTANT_TAG.DEL_FLAG.eq(false))
                .fetchInto(EpConstantTagPo.class);
    }

    /**
     * 根据id获取标签
     *
     * @param id
     * @param ognId
     * @return
     */
    public Optional<EpConstantTagPo> findByIdAndOgnId(Long id, Long ognId) {
        EpConstantTagPo data = dslContext.selectFrom(EP_CONSTANT_TAG)
                .where(EP_CONSTANT_TAG.ID.eq(id))
                .and(ognId == null ? EP_CONSTANT_TAG.OGN_ID.isNull() : EP_CONSTANT_TAG.OGN_ID.eq(ognId))
                .and(EP_CONSTANT_TAG.DEL_FLAG.eq(false))
                .fetchOneInto(EpConstantTagPo.class);
        return Optional.ofNullable(data);
    }


    /**
     * 根据id删除课程私有标签
     *
     * @param id
     */
    public void deleteOgnTagByIds(List<Long> id) {
        dslContext.update(EP_CONSTANT_TAG)
                .set(EP_CONSTANT_TAG.DEL_FLAG, true)
                .where(EP_CONSTANT_TAG.OGN_FLAG.eq(true))
                .and(EP_CONSTANT_TAG.ID.in(id))
                .execute();
    }

    /**
     * 根据id逻辑删除
     *
     * @param id
     */
    public int deleteByIdAndOgnId(Long id, Long ognId) {
        return dslContext.update(EP_CONSTANT_TAG)
                .set(EP_CONSTANT_TAG.DEL_FLAG, true)
                .where(EP_CONSTANT_TAG.ID.eq(id))
                .and(EP_CONSTANT_TAG.STATUS.eq(EpConstantTagStatus.save))
                .and(ognId == null ? EP_CONSTANT_TAG.OGN_ID.isNull() : EP_CONSTANT_TAG.OGN_ID.eq(ognId))
                .and(EP_CONSTANT_TAG.DEL_FLAG.eq(false))
                .execute();
    }

    /**
     * 机构标签分页/后台公用标签分页
     *
     * @param pageable
     * @param condition
     * @return
     */
    public Page<ConstantTagBo> findbyPageAndCondition(Pageable pageable, Collection<? extends Condition> condition) {
        long totalCount = dslContext.selectCount()
                .from(EP_CONSTANT_TAG)
                .where(condition)
                .fetchOne(0, Long.class);
        if (totalCount == BizConstant.DB_NUM_ZERO) {
            return new PageImpl<>(Lists.newArrayList(), pageable, totalCount);
        }
        List<Field<?>> fieldList = Lists.newArrayList(EP_CONSTANT_TAG.fields());

        fieldList.add(EP_ORGAN_COURSE_TAG.ID.count().as("usedOrganCourseTag"));

        SelectHavingStep<Record> record = dslContext.select(fieldList)
                .from(EP_CONSTANT_TAG)
                .leftJoin(EP_ORGAN_COURSE_TAG)
                .on(EP_CONSTANT_TAG.ID.eq(EP_ORGAN_COURSE_TAG.TAG_ID))
                .where(condition)
                .groupBy(EP_CONSTANT_TAG.ID);


        List<ConstantTagBo> list = record.orderBy(EP_CONSTANT_TAG.SORT.desc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetchInto(ConstantTagBo.class);
        PageImpl<ConstantTagBo> pPage = new PageImpl<ConstantTagBo>(list, pageable, totalCount);
        return pPage;
    }

    /**
     * 根据id修改标签
     *
     * @param po
     * @return
     */
    public int updatePo(EpConstantTagPo po) {
        return dslContext.update(EP_CONSTANT_TAG)
                .set(EP_CONSTANT_TAG.TAG_NAME, po.getTagName())
                .set(EP_CONSTANT_TAG.TAG_LEVEL, po.getTagLevel())
                .where(EP_CONSTANT_TAG.ID.eq(po.getId()))
                .and(EP_CONSTANT_TAG.STATUS.eq(EpConstantTagStatus.save))
                .and(po.getOgnId() == null ? EP_CONSTANT_TAG.OGN_ID.isNull() : EP_CONSTANT_TAG.OGN_ID.eq(po.getOgnId()))
                .and(EP_CONSTANT_TAG.DEL_FLAG.eq(false)).execute();
    }

    /**
     * 根据id上线标签
     *
     * @param id
     * @return
     */
    public int onlineById(Long id, Long ognId) {
        if (ognId == null) {
            return dslContext.update(EP_CONSTANT_TAG)
                    .set(EP_CONSTANT_TAG.STATUS, EpConstantTagStatus.online)
                    .where(EP_CONSTANT_TAG.ID.eq(id))
                    .and(EP_CONSTANT_TAG.OGN_ID.isNull())
                    .and(EP_CONSTANT_TAG.DEL_FLAG.eq(false))
                    .execute();
        } else {
            return dslContext.update(EP_CONSTANT_TAG)
                    .set(EP_CONSTANT_TAG.STATUS, EpConstantTagStatus.online)
                    .where(EP_CONSTANT_TAG.ID.eq(id))
                    .and(EP_CONSTANT_TAG.OGN_ID.eq(ognId))
                    .and(EP_CONSTANT_TAG.DEL_FLAG.eq(false))
                    .execute();
        }

    }

    /**
     * 根据id下线标签
     *
     * @param id
     * @return
     */
    public int offlineById(Long id, Long ognId) {
        if (ognId == null) {
            return dslContext.update(EP_CONSTANT_TAG)
                    .set(EP_CONSTANT_TAG.STATUS, EpConstantTagStatus.offline)
                    .where(EP_CONSTANT_TAG.ID.eq(id))
                    .and(EP_CONSTANT_TAG.STATUS.eq(EpConstantTagStatus.online))
                    .and(EP_CONSTANT_TAG.OGN_ID.isNull())
                    .and(EP_CONSTANT_TAG.DEL_FLAG.eq(false))
                    .execute();
        } else {
            return dslContext.update(EP_CONSTANT_TAG)
                    .set(EP_CONSTANT_TAG.STATUS, EpConstantTagStatus.offline)
                    .where(EP_CONSTANT_TAG.ID.eq(id))
                    .and(EP_CONSTANT_TAG.STATUS.eq(EpConstantTagStatus.online))
                    .and(EP_CONSTANT_TAG.OGN_ID.eq(ognId))
                    .and(EP_CONSTANT_TAG.DEL_FLAG.eq(false))
                    .execute();
        }
    }

    /**
     * 根据id置顶标签
     *
     * @param id
     * @return
     */
    public int upTopById(Long id) {
        Long sort = DateTools.getCurrentDateTime().getTime();
        return dslContext.update(EP_CONSTANT_TAG)
                .set(EP_CONSTANT_TAG.SORT, sort)
                .where(EP_CONSTANT_TAG.ID.eq(id))
                .and(EP_CONSTANT_TAG.DEL_FLAG.eq(false))
                .execute();
    }

    /**
     * 根据id取消置顶标签
     *
     * @param id
     * @return
     */
    public int unTopById(Long id) {
        return dslContext.update(EP_CONSTANT_TAG)
                .set(EP_CONSTANT_TAG.SORT, 1L)
                .where(EP_CONSTANT_TAG.ID.eq(id))
                .and(EP_CONSTANT_TAG.DEL_FLAG.eq(false))
                .execute();

    }
}
