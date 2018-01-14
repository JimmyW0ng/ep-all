package com.ep.domain.repository;

import com.ep.domain.constant.BizConstant;
import com.ep.domain.pojo.po.EpOrganInfoPo;
import com.ep.domain.repository.domain.tables.records.EpOrganInfoRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.ep.domain.repository.domain.Tables.EP_ORGAN_INFO;


/**
 * @Description:机构表Repository
 * @Author: J.W
 * @Date: 上午10:35 2017/11/27
 */
@Repository
public class OrganInfoRepository extends AbstractCRUDRepository<EpOrganInfoRecord, Long, EpOrganInfoPo> {

    @Autowired
    public OrganInfoRepository(DSLContext dslContext) {
        super(dslContext, EP_ORGAN_INFO, EP_ORGAN_INFO.ID, EpOrganInfoPo.class);
    }

    /**
     * 更新机构信息
     *
     * @param updatePo
     * @return
     */
    public int editOrgan(EpOrganInfoPo updatePo) {
        return dslContext.update(EP_ORGAN_INFO)
                .set(EP_ORGAN_INFO.ORGAN_NAME, updatePo.getOrganName())
                .set(EP_ORGAN_INFO.ORGAN_ADDRESS, updatePo.getOrganAddress())
                .set(EP_ORGAN_INFO.ORGAN_REGION, updatePo.getOrganRegion())
                .set(EP_ORGAN_INFO.ORGAN_LNG, updatePo.getOrganLng())
                .set(EP_ORGAN_INFO.ORGAN_LAT, updatePo.getOrganLat())
                .set(EP_ORGAN_INFO.ORGAN_SHORT_INTRODUCE, updatePo.getOrganShortIntroduce())
                .set(EP_ORGAN_INFO.ORGAN_INTRODUCE, updatePo.getOrganIntroduce())
                .set(EP_ORGAN_INFO.ORGAN_CREATE_DATE, updatePo.getOrganCreateDate())
                .set(EP_ORGAN_INFO.ORGAN_PHONE, updatePo.getOrganPhone())
                .set(EP_ORGAN_INFO.ORGAN_EMAIL, updatePo.getOrganEmail())
                .set(EP_ORGAN_INFO.ORGAN_URL, updatePo.getOrganUrl())
                .set(EP_ORGAN_INFO.STATUS, updatePo.getStatus())
                .where(EP_ORGAN_INFO.ID.eq(updatePo.getId())).and(EP_ORGAN_INFO.DEL_FLAG.eq(false)).execute();
    }

    /**
     * 逻辑删除
     *
     * @param id
     * @return
     */
    public int delOrgan(Long id) {
        return dslContext.update(EP_ORGAN_INFO)
                .set(EP_ORGAN_INFO.DEL_FLAG, true)
                .where(EP_ORGAN_INFO.ID.eq(id))
                .and(EP_ORGAN_INFO.DEL_FLAG.eq(false))
                .execute();
    }

    /**
     * 按机构名称查重
     *
     * @param name
     * @return
     */
    public Optional<EpOrganInfoPo> findByName(String name) {
        EpOrganInfoPo ognInfoPo = dslContext.selectFrom(EP_ORGAN_INFO)
                .where(EP_ORGAN_INFO.DEL_FLAG.eq(false))
                .and(EP_ORGAN_INFO.ORGAN_NAME.eq(name))
                .limit(BizConstant.DB_NUM_ONE)
                .fetchOneInto(EpOrganInfoPo.class);
        return Optional.ofNullable(ognInfoPo);
    }
}

