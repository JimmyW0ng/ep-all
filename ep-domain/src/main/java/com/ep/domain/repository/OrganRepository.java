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
                .set(EP_ORGAN.OGN_NAME, updatePo.getOgnName())
                .set(EP_ORGAN.OGN_ADDRESS, updatePo.getOgnAddress())
                .set(EP_ORGAN.OGN_REGION, updatePo.getOgnRegion())
                .set(EP_ORGAN.OGN_LNG, updatePo.getOgnLng())
                .set(EP_ORGAN.OGN_LAT, updatePo.getOgnLat())
                .set(EP_ORGAN.OGN_SHORT_INTRODUCE, updatePo.getOgnShortIntroduce())
                .set(EP_ORGAN.OGN_INTRODUCE, updatePo.getOgnIntroduce())
                .set(EP_ORGAN.OGN_CREATE_DATE, updatePo.getOgnCreateDate())
                .set(EP_ORGAN.OGN_PHONE, updatePo.getOgnPhone())
                .set(EP_ORGAN.OGN_EMAIL, updatePo.getOgnEmail())
                .set(EP_ORGAN.OGN_URL, updatePo.getOgnUrl())
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
                .and(EP_ORGAN.OGN_NAME.eq(name))
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
    public Page<OrganBo> queryOrganForPage(Pageable pageable) {
        long count = dslContext.selectCount().from(EP_ORGAN)
                .where(EP_ORGAN.STATUS.eq(EpOrganStatus.online))
                .and(EP_ORGAN.DEL_FLAG.eq(false))
                .fetchOneInto(Long.class);
        if (count == BizConstant.DB_NUM_ZERO) {
            return new PageImpl<>(Lists.newArrayList(), pageable, count);
        }
        List<Field<?>> fieldList = Lists.newArrayList(EP_ORGAN.fields());
        fieldList.add(DSL.groupConcat(EP_CONSTANT_CATALOG.ID).as("catalogIds"));
        fieldList.add(DSL.groupConcat(EP_CONSTANT_CATALOG.LABEL).as("catalogLabels"));
        List<OrganBo> pList = dslContext.select(fieldList)
                .from(EP_ORGAN)
                .leftJoin(EP_ORGAN_CATALOG)
                .on(EP_ORGAN_CATALOG.OGN_ID.eq(EP_ORGAN.ID))
                .and(EP_ORGAN_CATALOG.DEL_FLAG.eq(false))
                .leftJoin(EP_CONSTANT_CATALOG)
                .on(EP_CONSTANT_CATALOG.ID.eq(EP_ORGAN_CATALOG.COURSE_CATALOG_ID))
                .and(EP_CONSTANT_CATALOG.DEL_FLAG.eq(false))
                .where(EP_ORGAN.STATUS.eq(EpOrganStatus.online))
                .groupBy(EP_ORGAN.ID)
                .orderBy(EP_ORGAN.MARKET_WEIGHT.desc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetchInto(OrganBo.class);
        return new PageImpl<>(pList, pageable, count);
    }


    /**
     * 系统后台修改商家
     * @param po
     * @return
     */
    public int updateSystemOrgan(EpOrganPo po){
        return dslContext.update(EP_ORGAN)
                .set(EP_ORGAN.OGN_NAME, po.getOgnName())
                .set(EP_ORGAN.OGN_ADDRESS, po.getOgnAddress())
                .set(EP_ORGAN.OGN_REGION, po.getOgnRegion())
                .set(EP_ORGAN.OGN_LNG, po.getOgnLng())
                .set(EP_ORGAN.OGN_LAT, po.getOgnLat())
                .set(EP_ORGAN.OGN_SHORT_INTRODUCE, po.getOgnShortIntroduce())
                .set(EP_ORGAN.OGN_CREATE_DATE, po.getOgnCreateDate())
                .set(EP_ORGAN.OGN_PHONE, po.getOgnPhone())
                .set(EP_ORGAN.OGN_EMAIL, po.getOgnEmail())
                .set(EP_ORGAN.OGN_URL, po.getOgnUrl())
                .set(EP_ORGAN.OGN_INTRODUCE, po.getOgnIntroduce())
            .set(EP_ORGAN.MARKET_WEIGHT, po.getMarketWeight())
            .set(EP_ORGAN.TOGETHER_SCORE, po.getTogetherScore())
            .set(EP_ORGAN.TOTAL_PARTICIPATE, po.getTotalParticipate())
            .set(EP_ORGAN.STATUS, po.getStatus())
            .set(EP_ORGAN.REMARK, po.getRemark())
            .set(EP_ORGAN.UPDATE_AT, DSL.currentTimestamp())
            .where(EP_ORGAN.ID.eq(po.getId())).and(EP_ORGAN.DEL_FLAG.eq(false)).execute();
    }

    /**
     * 删除商家
     * @param id
     * @return
     */
    public int deleteLogical(Long id){
        return dslContext.update(EP_ORGAN)
                .set(EP_ORGAN.DEL_FLAG,true)
                .where(EP_ORGAN.ID.eq(id))
                .execute();
    }

    public List<EpOrganPo> getByStatus(EpOrganStatus status){
        return dslContext.selectFrom(EP_ORGAN)
                .where(EP_ORGAN.DEL_FLAG.eq(false))
                .and(EP_ORGAN.STATUS.eq(status))
                .fetchInto(EpOrganPo.class);
    }

    /**
     * 更新课程平均分
     *
     * @param ognId
     * @param avgScore
     * @return
     */
    public int updateTogetherById(Long ognId, Byte avgScore) {
        return dslContext.update(EP_ORGAN)
                .set(EP_ORGAN.TOGETHER_SCORE, avgScore)
                .where(EP_ORGAN.ID.eq(ognId))
                .and(EP_ORGAN.DEL_FLAG.eq(false))
                .execute();
    }
}

