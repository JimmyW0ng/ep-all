package com.ep.domain.repository;

import com.ep.domain.constant.BizConstant;
import com.ep.domain.pojo.bo.OrganBo;
import com.ep.domain.pojo.po.EpOrganPo;
import com.ep.domain.repository.domain.enums.EpOrganStatus;
import com.ep.domain.repository.domain.tables.records.EpOrganRecord;
import com.google.common.collect.Lists;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.ep.domain.repository.domain.Tables.*;


/**
 * @Description:机构表Repository
 * @Author: J.W
 * @Date: 上午10:35 2017/11/27
 */
@Repository
public class OrganRepository extends AbstractCRUDRepository<EpOrganRecord, Long, EpOrganPo> {

    @Autowired
    public OrganRepository(DSLContext dslContext) {
        super(dslContext, EP_ORGAN, EP_ORGAN.ID, EpOrganPo.class);
    }

    /**
     * 更新机构信息
     *
     * @param updatePo
     * @return
     */
    public int editOrgan(EpOrganPo updatePo) {
        return dslContext.update(EP_ORGAN)
                .set(EP_ORGAN.ORGAN_NAME, updatePo.getOrganName())
                .set(EP_ORGAN.ORGAN_ADDRESS, updatePo.getOrganAddress())
                .set(EP_ORGAN.ORGAN_REGION, updatePo.getOrganRegion())
                .set(EP_ORGAN.ORGAN_LNG, updatePo.getOrganLng())
                .set(EP_ORGAN.ORGAN_LAT, updatePo.getOrganLat())
                .set(EP_ORGAN.ORGAN_SHORT_INTRODUCE, updatePo.getOrganShortIntroduce())
                .set(EP_ORGAN.ORGAN_INTRODUCE, updatePo.getOrganIntroduce())
                .set(EP_ORGAN.ORGAN_CREATE_DATE, updatePo.getOrganCreateDate())
                .set(EP_ORGAN.ORGAN_PHONE, updatePo.getOrganPhone())
                .set(EP_ORGAN.ORGAN_EMAIL, updatePo.getOrganEmail())
                .set(EP_ORGAN.ORGAN_URL, updatePo.getOrganUrl())
                .set(EP_ORGAN.STATUS, updatePo.getStatus())
                .where(EP_ORGAN.ID.eq(updatePo.getId())).and(EP_ORGAN.DEL_FLAG.eq(false)).execute();
    }

    /**
     * 逻辑删除
     *
     * @param id
     * @return
     */
    public int delOrgan(Long id) {
        return dslContext.update(EP_ORGAN)
                .set(EP_ORGAN.DEL_FLAG, true)
                .where(EP_ORGAN.ID.eq(id))
                .and(EP_ORGAN.DEL_FLAG.eq(false))
                .execute();
    }

    /**
     * 按机构名称查重
     *
     * @param name
     * @return
     */
    public Optional<EpOrganPo> findByName(String name) {
        EpOrganPo ognPo = dslContext.selectFrom(EP_ORGAN)
                .where(EP_ORGAN.DEL_FLAG.eq(false))
                .and(EP_ORGAN.ORGAN_NAME.eq(name))
                .limit(BizConstant.DB_NUM_ONE)
                .fetchOneInto(EpOrganPo.class);
        return Optional.ofNullable(ognPo);
    }

    /**
     * 分页查询机构列表
     *
     * @param pageable
     * @return
     */
    public Page<OrganBo> queryOgnPage(Pageable pageable) {
        long count = dslContext.selectCount().from(EP_ORGAN)
                .where(EP_ORGAN.STATUS.eq(EpOrganStatus.normal))
                .and(EP_ORGAN.DEL_FLAG.eq(false))
                .fetchOneInto(Long.class);
        if (count == BizConstant.DB_NUM_ZERO) {
            return new PageImpl<>(Lists.newArrayList(), pageable, count);
        }
        List<Field<?>> fieldList = Lists.newArrayList(EP_ORGAN.fields());
        fieldList.add(DSL.groupConcat(EP_CONSTANT_CATALOG.ID).as("catalogIds"));
        fieldList.add(DSL.groupConcat(EP_CONSTANT_CATALOG.LABEL).as("catalogLabels"));
        fieldList.add(EP_FILE.FILE_URL.as("fileUrl"));
        List<OrganBo> pList = dslContext.select(fieldList)
                .from(EP_ORGAN)
                .leftJoin(EP_FILE)
                .on(EP_FILE.BIZ_TYPE_CODE.eq(BizConstant.FILE_BIZ_TYPE_CODE_ORGAN_MAIN_PIC))
                .and(EP_FILE.SOURCE_ID.eq(EP_ORGAN.ID))
                .and(EP_FILE.DEL_FLAG.eq(false))
                .leftJoin(EP_ORGAN_CATALOG)
                .on(EP_ORGAN_CATALOG.OGN_ID.eq(EP_ORGAN.ID))
                .and(EP_ORGAN_CATALOG.DEL_FLAG.eq(false))
                .leftJoin(EP_CONSTANT_CATALOG)
                .on(EP_CONSTANT_CATALOG.ID.eq(EP_ORGAN_CATALOG.COURSE_CATALOG_ID))
                .and(EP_CONSTANT_CATALOG.DEL_FLAG.eq(false))
                .where(EP_ORGAN.STATUS.eq(EpOrganStatus.normal))
                .groupBy(EP_ORGAN.ID)
                .orderBy(EP_ORGAN.MARKET_WEIGHT.desc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetchInto(OrganBo.class);
        return new PageImpl<>(pList, pageable, count);
    }
}

