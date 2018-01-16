package com.ep.domain.repository;

import com.ep.domain.constant.BizConstant;
import com.ep.domain.pojo.po.EpOrganPo;
import com.ep.domain.repository.domain.tables.records.EpOrganRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.ep.domain.repository.domain.Tables.EP_ORGAN;


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
}

