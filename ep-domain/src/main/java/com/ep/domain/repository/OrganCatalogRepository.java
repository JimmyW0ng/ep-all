package com.ep.domain.repository;

import com.ep.domain.constant.BizConstant;
import com.ep.domain.pojo.po.EpOrganCatalogPo;
import com.ep.domain.repository.domain.tables.records.EpOrganCatalogRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.ep.domain.repository.domain.Tables.EP_ORGAN_CATALOG;

/**
 * @Description: 机构类目表Repository
 * @Author: CC.F
 * @Date: 10:37 2018/3/2
 */
@Repository
public class OrganCatalogRepository extends AbstractCRUDRepository<EpOrganCatalogRecord, Long, EpOrganCatalogPo> {

    @Autowired
    public OrganCatalogRepository(DSLContext dslContext) {
        super(dslContext, EP_ORGAN_CATALOG, EP_ORGAN_CATALOG.ID, EpOrganCatalogPo.class);
    }

    /**
     * 根据机构id物理删除记录
     * @param ognId
     */
    public void deletePhysicByOgnId(Long ognId){
        dslContext.deleteFrom(EP_ORGAN_CATALOG)
                .where(EP_ORGAN_CATALOG.OGN_ID.eq(ognId))
                .execute();
    }

    /**
     * 根据机构id和courseCatalogId物理删除记录
     *
     * @param ognId
     */
    public void deletePhysicByOgnIdAndCatalogId(Long ognId, Long courseCatalogId) {
        dslContext.deleteFrom(EP_ORGAN_CATALOG)
                .where(EP_ORGAN_CATALOG.OGN_ID.eq(ognId))
                .and(EP_ORGAN_CATALOG.COURSE_CATALOG_ID.eq(courseCatalogId))
                .execute();
    }

    /**
     * 是否存在机构id和课程类目id组合的重复
     *
     * @param ognId
     * @param catalogId
     * @return
     */
    public boolean existOgnAndCatalog(Long ognId, Long catalogId) {
        return dslContext.selectCount().from(EP_ORGAN_CATALOG)
                .where(EP_ORGAN_CATALOG.OGN_ID.eq(ognId))
                .and(EP_ORGAN_CATALOG.COURSE_CATALOG_ID.eq(catalogId))
                .and(EP_ORGAN_CATALOG.DEL_FLAG.eq(false))
                .fetchOneInto(Long.class) > BizConstant.DB_NUM_ZERO;
    }
}
