package com.ep.domain.component;

import com.ep.domain.pojo.po.EpConstantRegionPo;
import com.ep.domain.repository.AbstractCRUDRepository;
import com.ep.domain.repository.domain.enums.EpConstantRegionRegionType;
import com.ep.domain.repository.domain.tables.EpConstantRegion;
import com.ep.domain.repository.domain.tables.records.EpConstantRegionRecord;
import com.google.common.collect.Maps;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static com.ep.domain.repository.domain.Tables.EP_CONSTANT_REGION;

/**
 * @Description: 地区Component
 * @Author: CC.F
 * @Date: 23:14 2018/1/31
 */
@Component
public class ConstantRegionComponent extends AbstractCRUDRepository<EpConstantRegionRecord, Long, EpConstantRegionPo> {

    @Autowired
    public ConstantRegionComponent(DSLContext dslContext) {
        super(dslContext, EP_CONSTANT_REGION, EP_CONSTANT_REGION.ID, EpConstantRegionPo.class);
    }

    public Map<Long,String> getMapByType(EpConstantRegionRegionType type){
        List<EpConstantRegionPo> list=dslContext.select(EP_CONSTANT_REGION.ID,EP_CONSTANT_REGION.REGION_NAME)
                .from(EP_CONSTANT_REGION)
                .where(EP_CONSTANT_REGION.REGION_TYPE.eq(type))
                .and(EP_CONSTANT_REGION.DEL_FLAG.eq(false))
                .fetchInto(EpConstantRegionPo.class);
        Map<Long,String> map= Maps.newHashMap();
        list.forEach(p->{
            map.put(p.getId(),p.getRegionName());
        });
        return map;
    }
}
