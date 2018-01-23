package com.ep.domain.repository;

import com.ep.domain.pojo.po.EpConstantCatalogPo;
import com.ep.domain.repository.domain.tables.records.EpConstantCatalogRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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

}

