package com.ep.domain.repository;

import com.ep.domain.constant.BizConstant;
import com.ep.domain.pojo.po.EpConstantCatalogPo;
import com.ep.domain.repository.domain.tables.records.EpConstantCatalogRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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

    /**
     * 修改po
     *
     * @param po
     * @return
     */
    public int updateConstantCatalogPo(EpConstantCatalogPo po) {
        return dslContext.update(EP_CONSTANT_CATALOG)
                .set(EP_CONSTANT_CATALOG.LABEL, po.getLabel())
                .set(EP_CONSTANT_CATALOG.REMARK, po.getRemark())
                .where(EP_CONSTANT_CATALOG.ID.eq(po.getId()))
                .and(EP_CONSTANT_CATALOG.DEL_FLAG.eq(false))
                .execute();
    }

    /**
     * 根据id删除类目
     *
     * @param id
     * @return
     */
    public int deleteById(Long id) {
        return dslContext.update(EP_CONSTANT_CATALOG)
                .set(EP_CONSTANT_CATALOG.DEL_FLAG, true)
                .where(EP_CONSTANT_CATALOG.ID.eq(id))
                .and(EP_CONSTANT_CATALOG.DEL_FLAG.eq(false))
                .execute();
    }

    /**
     * 根据ids删除类目
     *
     * @param ids
     * @return
     */
    public int deleteByIds(Long[] ids) {
        return dslContext.update(EP_CONSTANT_CATALOG)
                .set(EP_CONSTANT_CATALOG.DEL_FLAG, true)
                .where(EP_CONSTANT_CATALOG.ID.in(ids))
                .and(EP_CONSTANT_CATALOG.DEL_FLAG.eq(false))
                .execute();
    }

    /**
     * 获取二级目录
     *
     * @return
     */
    public List<EpConstantCatalogPo> findSecondCatalog() {
        return dslContext.selectFrom(EP_CONSTANT_CATALOG)
                .where(EP_CONSTANT_CATALOG.PARENT_ID.notEqual(0L))
                .and(EP_CONSTANT_CATALOG.DEL_FLAG.eq(false))
                .fetchInto(EpConstantCatalogPo.class);
    }

    /**
     * 获取一级目录下拉框
     *
     * @return
     */
    public List<EpConstantCatalogPo> findFirstCatalogSelectModel() {
        return dslContext.select(EP_CONSTANT_CATALOG.ID, EP_CONSTANT_CATALOG.LABEL).from(EP_CONSTANT_CATALOG)
                .where(EP_CONSTANT_CATALOG.PARENT_ID.equal(BizConstant.FIRST_CONSTANT_CATALOG_PID))
                .and(EP_CONSTANT_CATALOG.DEL_FLAG.eq(false))
                .fetchInto(EpConstantCatalogPo.class);
    }

    /**
     * 根据父级id获取二级目录下拉框
     *
     * @param pid
     * @return
     */
    public List<EpConstantCatalogPo> findSecondCatalogSelectModelByPid(Long pid) {
        return dslContext.select(EP_CONSTANT_CATALOG.ID, EP_CONSTANT_CATALOG.LABEL).from(EP_CONSTANT_CATALOG)
                .where(EP_CONSTANT_CATALOG.PARENT_ID.equal(pid))
                .and(EP_CONSTANT_CATALOG.DEL_FLAG.eq(false))
                .fetchInto(EpConstantCatalogPo.class);
    }

    /**
     * 根据id 获取po
     *
     * @param id
     * @return
     */
    public Optional<EpConstantCatalogPo> findById(Long id) {
        EpConstantCatalogPo data = dslContext.selectFrom(EP_CONSTANT_CATALOG)
                .where(EP_CONSTANT_CATALOG.ID.eq(id))
                .and(EP_CONSTANT_CATALOG.DEL_FLAG.eq(false))
                .fetchOneInto(EpConstantCatalogPo.class);
        return Optional.ofNullable(data);
    }
}

