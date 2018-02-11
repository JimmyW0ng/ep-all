package com.ep.domain.repository;

import com.ep.domain.pojo.po.EpConstantTagPo;
import com.ep.domain.repository.domain.tables.records.EpConstantTagRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.ep.domain.repository.domain.tables.EpConstantTag.EP_CONSTANT_TAG;

/**
 * @Description: 标签仓库
 * @Author: CC.F
 * @Date: 13:46 2018/2/9
 */
@Repository
public class ConstantTagPepository extends AbstractCRUDRepository<EpConstantTagRecord, Long, EpConstantTagPo> {

    @Autowired
    public ConstantTagPepository(DSLContext dslContext) {
        super(dslContext, EP_CONSTANT_TAG, EP_CONSTANT_TAG.ID, EpConstantTagPo.class);
    }

    /**
     * 根据课程类目id和机构id获取标签
     *
     * @param catalogId
     * @param ognId
     * @return
     */
    public List<EpConstantTagPo> findByCatalogIdAndOgnId(Long catalogId, Long ognId) {
        return dslContext.selectFrom(EP_CONSTANT_TAG)
                .where(EP_CONSTANT_TAG.CATALOG_ID.eq(catalogId))
                .and(ognId == null ? EP_CONSTANT_TAG.OGN_ID.isNull() : EP_CONSTANT_TAG.OGN_ID.eq(ognId))
                .and(EP_CONSTANT_TAG.DEL_FLAG.eq(false))
                .fetchInto(EpConstantTagPo.class);
    }
}