package com.ep.domain.component;

import com.ep.domain.pojo.po.EpSystemDictPo;
import com.ep.domain.repository.AbstractCRUDRepository;
import com.ep.domain.repository.domain.tables.records.EpSystemDictRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.ep.domain.repository.domain.Tables.EP_SYSTEM_DICT;

/**
 * @Description: 数据字典组件
 * @Author: CC.F
 * @Date: 0:00 2018/2/4
 */
@Component
public class DictComponent extends AbstractCRUDRepository<EpSystemDictRecord, Long, EpSystemDictPo> {

    @Autowired
    public DictComponent(DSLContext dslContext) {
        super(dslContext, EP_SYSTEM_DICT, EP_SYSTEM_DICT.ID, EpSystemDictPo.class);
    }
    public EpSystemDictPo getByGroupNameAndKey(String groupName,String key){
        return dslContext.selectFrom(EP_SYSTEM_DICT)
                .where(EP_SYSTEM_DICT.GROUP_NAME.eq(groupName))
                .and(EP_SYSTEM_DICT.KEY.eq(key))
                .and(EP_SYSTEM_DICT.DEL_FLAG.eq(false))
                .fetchOneInto(EpSystemDictPo.class);
    }
}
