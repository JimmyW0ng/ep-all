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
public class ConstantTagRepository extends AbstractCRUDRepository<EpConstantTagRecord, Long, EpConstantTagPo> {

    @Autowired
    public ConstantTagRepository(DSLContext dslContext) {
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

    /**
     * 根据机构id获取标签
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
     * 根据id删除课程私有标签
     * @param id
     */
    public void deleteOgnTagByIds(List<Long> id){
        dslContext.update(EP_CONSTANT_TAG)
                .set(EP_CONSTANT_TAG.DEL_FLAG,true)
                .where(EP_CONSTANT_TAG.OGN_FLAG.eq(true))
                .and(EP_CONSTANT_TAG.ID.in(id))
                .execute();
    }

    /**
     * 根据id逻辑删除
     * @param id
     */
    public void deleteById(Long id){
        dslContext.update(EP_CONSTANT_TAG)
                .set(EP_CONSTANT_TAG.DEL_FLAG,true)
                .where(EP_CONSTANT_TAG.ID.eq(id))
                .execute();
    }
}
