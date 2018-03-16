package com.ep.domain.repository;

import com.ep.common.tool.CollectionsTools;
import com.ep.domain.pojo.po.EpOrganVipPo;
import com.ep.domain.repository.domain.tables.records.EpOrganVipRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.ep.domain.repository.domain.Tables.EP_ORGAN_VIP;

/**
 * @Description: 机构会员信息表
 * @Author J.W
 * @Date: 下午 3:29 2018/3/16 0016
 */
@Repository
public class OrganVipRepository extends AbstractCRUDRepository<EpOrganVipRecord, Long, EpOrganVipPo> {

    @Autowired
    public OrganVipRepository(DSLContext dslContext) {
        super(dslContext, EP_ORGAN_VIP, EP_ORGAN_VIP.ID, EpOrganVipPo.class);
    }

    /**
     * 根据机构和孩子判断是否是会员
     *
     * @param ognId
     * @param childId
     * @return
     */
    public Boolean existVipByOgnIdAndChildId(Long ognId, Long childId) {
        List<EpOrganVipPo> data = dslContext.selectCount().from(EP_ORGAN_VIP)
                                            .where(EP_ORGAN_VIP.OGN_ID.eq(ognId))
                                            .and(EP_ORGAN_VIP.CHILD_ID.eq(childId))
                                            .and(EP_ORGAN_VIP.DEL_FLAG.eq(false))
                                            .fetchInto(EpOrganVipPo.class);
        return CollectionsTools.isNotEmpty(data);
    }

}
