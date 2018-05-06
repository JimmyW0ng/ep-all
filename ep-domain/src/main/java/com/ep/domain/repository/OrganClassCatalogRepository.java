package com.ep.domain.repository;

import com.ep.domain.pojo.bo.RectifyOrganClassCatalogBo;
import com.ep.domain.pojo.po.EpOrganClassCatalogPo;
import com.ep.domain.repository.domain.tables.records.EpOrganClassCatalogRecord;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.ep.domain.repository.domain.Tables.EP_ORGAN_CLASS;
import static com.ep.domain.repository.domain.Tables.EP_ORGAN_CLASS_CATALOG;

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
     *
     * @param po
     */
    public void updateEpOrganClassCatalogPo(EpOrganClassCatalogPo po) {
        dslContext.update(EP_ORGAN_CLASS_CATALOG)
                .set(EP_ORGAN_CLASS_CATALOG.CATALOG_TITLE, po.getCatalogTitle())
                .set(EP_ORGAN_CLASS_CATALOG.CATALOG_INDEX, po.getCatalogIndex())
                .set(EP_ORGAN_CLASS_CATALOG.CATALOG_DESC, po.getCatalogDesc())
                .set(EP_ORGAN_CLASS_CATALOG.START_TIME, po.getStartTime())
                .set(EP_ORGAN_CLASS_CATALOG.UPDATE_AT, DSL.currentTimestamp())
                .execute();
    }

    /**
     * 根据id获取EpOrganClassCatalogPo
     *
     * @param id
     * @return
     */
    public Optional<EpOrganClassCatalogPo> findById(Long id) {
        EpOrganClassCatalogPo data = dslContext.selectFrom(EP_ORGAN_CLASS_CATALOG)
                .where(EP_ORGAN_CLASS_CATALOG.ID.eq(id))
                .and(EP_ORGAN_CLASS_CATALOG.DEL_FLAG.eq(false))
                .fetchOneInto(EpOrganClassCatalogPo.class);
        return Optional.ofNullable(data);
    }

    /**
     * 根据课程id获取EpOrganClassCatalogPo
     *
     * @param courseId
     * @return
     */
    public List<EpOrganClassCatalogPo> findByCourseId(Long courseId) {
        List<Long> classId = dslContext.select(EP_ORGAN_CLASS.ID)
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
     *
     * @param classId
     * @return
     */
    public List<EpOrganClassCatalogPo> findByClassId(Long classId) {
        return dslContext.selectFrom(EP_ORGAN_CLASS_CATALOG)
                .where(EP_ORGAN_CLASS_CATALOG.CLASS_ID.eq(classId))
                .and(EP_ORGAN_CLASS_CATALOG.DEL_FLAG.eq(false))
                .fetchInto(EpOrganClassCatalogPo.class);
    }

    /**
     * 根据班次id获取RectifyOrganClassCatalogBo
     *
     * @param classId
     * @return
     */
    public List<RectifyOrganClassCatalogBo> findRectifyBoByClassId(Long classId) {
        return dslContext.selectFrom(EP_ORGAN_CLASS_CATALOG)
                .where(EP_ORGAN_CLASS_CATALOG.CLASS_ID.eq(classId))
                .and(EP_ORGAN_CLASS_CATALOG.DEL_FLAG.eq(false))
                .fetchInto(RectifyOrganClassCatalogBo.class);
    }

    /**
     * 根据ids批量逻辑删除
     *
     * @param ids
     */
    public void deleteByIds(List<Long> ids) {
        dslContext.update(EP_ORGAN_CLASS_CATALOG)
                .set(EP_ORGAN_CLASS_CATALOG.DEL_FLAG, true)
                .where(EP_ORGAN_CLASS_CATALOG.ID.in(ids))
                .execute();
    }

    /**
     * 根据班次classIds批量逻辑删除记录
     *
     * @param classIds
     */
    public void deleteLogicByClassIds(List<Long> classIds) {
        dslContext.update(EP_ORGAN_CLASS_CATALOG)
                .set(EP_ORGAN_CLASS_CATALOG.DEL_FLAG, true)
                .where(EP_ORGAN_CLASS_CATALOG.CLASS_ID.in(classIds))
                .execute();
    }

    /**
     * 根据班次classIds批量物理删除记录
     *
     * @param classIds
     */
    public void deletePhysicByClassIds(List<Long> classIds) {
        dslContext.delete(EP_ORGAN_CLASS_CATALOG)
                .where(EP_ORGAN_CLASS_CATALOG.CLASS_ID.in(classIds))
                .execute();
    }

    /**
     * 紧急修改课程时修改班次目录
     *
     * @param bo
     * @return
     */
    public int rectifyByRectifyCourse(RectifyOrganClassCatalogBo bo) {
        return dslContext.update(EP_ORGAN_CLASS_CATALOG)
                .set(EP_ORGAN_CLASS_CATALOG.CATALOG_TITLE, bo.getCatalogTitle())
                .set(EP_ORGAN_CLASS_CATALOG.CATALOG_DESC, bo.getCatalogDesc())
                .set(EP_ORGAN_CLASS_CATALOG.START_TIME, bo.getStartTime())
                .set(EP_ORGAN_CLASS_CATALOG.DURATION, bo.getDuration())
                .set(EP_ORGAN_CLASS_CATALOG.DURA_TYPE, bo.getDuraType())
                .where(EP_ORGAN_CLASS_CATALOG.ID.eq(bo.getId()))
                .and(EP_ORGAN_CLASS_CATALOG.DEL_FLAG.eq(false))
                .execute();
    }

    /**
     * 根据班次id获取班次目录
     *
     * @param classId
     * @return
     */
    public List<EpOrganClassCatalogPo> findClassCatalogByClassId(Long classId) {
        return dslContext.selectFrom(EP_ORGAN_CLASS_CATALOG)
                .where(EP_ORGAN_CLASS_CATALOG.CLASS_ID.eq(classId))
                .and(EP_ORGAN_CLASS_CATALOG.DEL_FLAG.eq(false))
                .fetchInto(EpOrganClassCatalogPo.class);
    }

    /**
     * 根据classId统计该班次下未结束的班次目录
     *
     * @param classId
     * @return
     */
    public int countUnendNormalClassCatalogByClassId(Long classId) {
        return dslContext.selectCount().from(EP_ORGAN_CLASS_CATALOG)
                .where(EP_ORGAN_CLASS_CATALOG.CLASS_ID.eq(classId))
                .and(EP_ORGAN_CLASS_CATALOG.START_TIME.greaterThan(DSL.currentTimestamp()))
                .and(EP_ORGAN_CLASS_CATALOG.DEL_FLAG.eq(false))
                .fetchOneInto(Integer.class);
    }
}
