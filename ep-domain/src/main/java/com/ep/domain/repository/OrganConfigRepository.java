package com.ep.domain.repository;

import com.ep.domain.pojo.po.EpOrganConfigPo;
import com.ep.domain.repository.domain.tables.records.EpOrganConfigRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.ep.domain.repository.domain.Tables.EP_ORGAN_CONFIG;

/**
 * @Description:
 * @Author: CC.F
 * @Date: 22:41 2018/3/20
 */
@Repository
public class OrganConfigRepository extends AbstractCRUDRepository<EpOrganConfigRecord, Long, EpOrganConfigPo> {

    @Autowired
    public OrganConfigRepository(DSLContext dslContext) {
        super(dslContext, EP_ORGAN_CONFIG, EP_ORGAN_CONFIG.ID, EpOrganConfigPo.class);
    }

    /**
     * 根据机构id获取配置
     *
     * @param ognId
     * @return
     */
    public Optional<EpOrganConfigPo> getByOgnId(Long ognId) {
        EpOrganConfigPo data = dslContext.selectFrom(EP_ORGAN_CONFIG)
                .where(EP_ORGAN_CONFIG.OGN_ID.eq(ognId))
                .and(EP_ORGAN_CONFIG.DEL_FLAG.eq(false))
                .fetchOneInto(EpOrganConfigPo.class);
        return Optional.ofNullable(data);
    }

    /**
     * 更改 是否支持标签功能 配置
     *
     * @param supportTag
     * @param ognId
     * @return
     */
    public int updateSupportTagByOgnId(Boolean supportTag, Long ognId) {
        return dslContext.update(EP_ORGAN_CONFIG)
                .set(EP_ORGAN_CONFIG.SUPPORT_TAG, supportTag)
                .where(EP_ORGAN_CONFIG.OGN_ID.eq(ognId))
                .and(EP_ORGAN_CONFIG.DEL_FLAG.eq(false))
                .execute();
    }
}
