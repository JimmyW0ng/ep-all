package com.ep.domain.repository;

import com.ep.domain.pojo.po.EpConstantCatalogPo;
import com.ep.domain.repository.domain.tables.records.EpConstantCatalogRecord;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.ep.domain.repository.domain.Tables.EP_CONSTANT_CATALOG;

/**
 * @Description:课程类目表Repository
 * @Author: J.W
 * @Date: 上午10:35 2017/11/27
 */
@Repository
public class ConstantCatalogRepository extends AbstractCRUDRepository<EpConstantCatalogRecord, Long, EpConstantCatalogPo> {

    @Autowired
    public ConstantCatalogRepository(DSLContext dslContext) {
        super(dslContext, EP_CONSTANT_CATALOG, EP_CONSTANT_CATALOG.ID, EpConstantCatalogPo.class);
    }

    public List<EpConstantCatalogPo> getAll() {
        return dslContext.selectFrom(EP_CONSTANT_CATALOG)
                .where(EP_CONSTANT_CATALOG.DEL_FLAG.equal(false))
                .fetchInto(EpConstantCatalogPo.class);
    }

    public int updateConstantCatalogPo(EpConstantCatalogPo po){
        return dslContext.update(EP_CONSTANT_CATALOG)
                .set(EP_CONSTANT_CATALOG.LABEL,po.getLabel())
                .set(EP_CONSTANT_CATALOG.REMARK,po.getRemark())
                .set(EP_CONSTANT_CATALOG.UPDATE_AT, DSL.currentTimestamp())
                .where(EP_CONSTANT_CATALOG.ID.eq(po.getId()))
                .execute();
    }

    public void deleteById(Long id){
        dslContext.update(EP_CONSTANT_CATALOG)
                .set(EP_CONSTANT_CATALOG.DEL_FLAG,true)
                .where(EP_CONSTANT_CATALOG.ID.eq(id))
                .execute();
    }

}

