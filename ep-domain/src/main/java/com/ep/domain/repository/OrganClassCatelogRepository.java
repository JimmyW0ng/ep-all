package com.ep.domain.repository;

import com.ep.domain.pojo.po.EpOrganClassCatelogPo;
import com.ep.domain.repository.domain.tables.records.EpOrganClassCatelogRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.ep.domain.repository.domain.Tables.EP_ORGAN_CLASS_CATELOG;

/**
 * @Description: 班次课程内容目录表Repository
 * @Author: CC.F
 * @Date: 9:53 2018/2/12
 */
@Repository
public class OrganClassCatelogRepository extends AbstractCRUDRepository<EpOrganClassCatelogRecord, Long, EpOrganClassCatelogPo> {

    @Autowired
    public OrganClassCatelogRepository(DSLContext dslContext) {
        super(dslContext, EP_ORGAN_CLASS_CATELOG, EP_ORGAN_CLASS_CATELOG.ID, EpOrganClassCatelogPo.class);
    }
}
