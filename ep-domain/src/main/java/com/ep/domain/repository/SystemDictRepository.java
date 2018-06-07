package com.ep.domain.repository;

import com.ep.domain.pojo.po.EpSystemDictPo;
import com.ep.domain.repository.domain.enums.EpSystemDictStatus;
import com.ep.domain.repository.domain.tables.records.EpSystemDictRecord;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static com.ep.domain.repository.domain.Tables.EP_SYSTEM_DICT;

/**
 * @Description: 系统字典Repository
 * @Author: CC.F
 * @Date: 15:34 2018/1/26
 */
@Repository
public class SystemDictRepository extends AbstractCRUDRepository<EpSystemDictRecord, Long, EpSystemDictPo> {

    @Autowired
    public SystemDictRepository(DSLContext dslContext) {
        super(dslContext, EP_SYSTEM_DICT, EP_SYSTEM_DICT.ID, EpSystemDictPo.class);
    }

    public Optional<EpSystemDictPo> findById(Long id) {
        EpSystemDictPo data = dslContext
                .selectFrom(EP_SYSTEM_DICT)
                .where(EP_SYSTEM_DICT.ID.eq(id))
                .and(EP_SYSTEM_DICT.DEL_FLAG.eq(false))
                .fetchOneInto(EpSystemDictPo.class);
        return Optional.ofNullable(data);
    }

    public List<EpSystemDictPo> findByCondition(Collection<? extends Condition> condition) {
        return dslContext.selectFrom(EP_SYSTEM_DICT).where(condition).fetchInto(EpSystemDictPo.class);
    }

    /**
     * 根据id逻辑删除字典
     *
     * @param id
     * @return
     */
    public int deleteLogical(Long id) {
        return dslContext
                .update(EP_SYSTEM_DICT)
                .set(EP_SYSTEM_DICT.DEL_FLAG, true)
                .where(EP_SYSTEM_DICT.ID.eq(id))
                .and(EP_SYSTEM_DICT.DEL_FLAG.eq(false))
                .execute();
    }

    public void enableSysDict(Long id) {
        dslContext.update(EP_SYSTEM_DICT).set(EP_SYSTEM_DICT.STATUS, EpSystemDictStatus.enable)
                  .where(EP_SYSTEM_DICT.DEL_FLAG.eq(false)).and(EP_SYSTEM_DICT.ID.eq(id))
                  .execute();
    }

    public void disabledSysDict(Long id) {
        dslContext.update(EP_SYSTEM_DICT).set(EP_SYSTEM_DICT.STATUS, EpSystemDictStatus.disabled)
                  .where(EP_SYSTEM_DICT.DEL_FLAG.eq(false)).and(EP_SYSTEM_DICT.ID.eq(id))
                  .execute();
    }

    /**
     * @param groupName
     * @param key       return EpSystemDictPojo
     */
    public EpSystemDictPo findByGroupNameAndKey(String groupName, String key) {
        return dslContext.selectFrom(EP_SYSTEM_DICT)
                         .where(EP_SYSTEM_DICT.GROUP_NAME.eq(groupName)).and(EP_SYSTEM_DICT.KEY.eq(key))
                         .and(EP_SYSTEM_DICT.STATUS.eq(EpSystemDictStatus.enable))
                         .and(EP_SYSTEM_DICT.DEL_FLAG.eq(false))
                         .fetchOneInto(EpSystemDictPo.class);
    }

    public List<EpSystemDictPo> findByGroupNameAndKeyFuzzy(String groupName, String key) {
        return dslContext.selectFrom(EP_SYSTEM_DICT)
                         .where(EP_SYSTEM_DICT.GROUP_NAME.eq(groupName)).and(EP_SYSTEM_DICT.KEY.like("%" + key + "%"))
                         .and(EP_SYSTEM_DICT.STATUS.eq(EpSystemDictStatus.enable))
                         .and(EP_SYSTEM_DICT.DEL_FLAG.eq(false))
                         .fetchInto(EpSystemDictPo.class);
    }

    /**
     * 根据groupName查找数据
     *
     * @param groupName
     * @Return List<EpSystemDictPojo></>
     */
    public List<EpSystemDictPo> findByGroupName(String groupName) {
        return dslContext.selectFrom(EP_SYSTEM_DICT)
                         .where(EP_SYSTEM_DICT.GROUP_NAME.eq(groupName))
                         .and(EP_SYSTEM_DICT.STATUS.eq(EpSystemDictStatus.enable))
                         .and(EP_SYSTEM_DICT.DEL_FLAG.eq(false))
                         .orderBy(EP_SYSTEM_DICT.SORT.desc())
                         .fetchInto(EpSystemDictPo.class);
    }

    /**
     * 根据key查找数据
     *
     * @param key
     * @return List<EpSystemDictPojo></>
     */
    public List<EpSystemDictPo> findByKey(String key) {
        return dslContext.selectFrom(EP_SYSTEM_DICT)
                         .where(EP_SYSTEM_DICT.KEY.eq(key))
                         .and(EP_SYSTEM_DICT.STATUS.eq(EpSystemDictStatus.enable))
                         .and(EP_SYSTEM_DICT.DEL_FLAG.eq(false))
                         .orderBy(EP_SYSTEM_DICT.SORT.desc())
                         .fetchInto(EpSystemDictPo.class);
    }

    public EpSystemDictPo findOneByKey(String key) {
        return dslContext.selectFrom(EP_SYSTEM_DICT)
                         .where(EP_SYSTEM_DICT.KEY.eq(key))
                         .and(EP_SYSTEM_DICT.STATUS.eq(EpSystemDictStatus.enable))
                         .and(EP_SYSTEM_DICT.DEL_FLAG.eq(false))
                         .orderBy(EP_SYSTEM_DICT.SORT.desc())
                         .limit(1)
                         .fetchOneInto(EpSystemDictPo.class);
    }

    public EpSystemDictPo findGroupNameAndKeyById(Long id) {
        return dslContext.select(EP_SYSTEM_DICT.GROUP_NAME, EP_SYSTEM_DICT.KEY).from(EP_SYSTEM_DICT)
                         .where(EP_SYSTEM_DICT.ID.eq(id))
                         .and(EP_SYSTEM_DICT.STATUS.eq(EpSystemDictStatus.enable))
                         .and(EP_SYSTEM_DICT.DEL_FLAG.eq(false))
                         .fetchOneInto(EpSystemDictPo.class);
    }

    public int updateByGroupName(String groupName, String key, String value) {
        return dslContext.update(EP_SYSTEM_DICT)
                .set(EP_SYSTEM_DICT.VALUE, value)
                .where(EP_SYSTEM_DICT.GROUP_NAME.eq(groupName))
                .and(EP_SYSTEM_DICT.KEY.eq(key))
                .and(EP_SYSTEM_DICT.STATUS.eq(EpSystemDictStatus.enable))
                .and(EP_SYSTEM_DICT.DEL_FLAG.eq(false))
                .execute();
    }

    /**
     * 根据id修改字典
     *
     * @param po
     * @return
     */
    public int updateById(EpSystemDictPo po) {
        return dslContext.update(EP_SYSTEM_DICT)
                .set(EP_SYSTEM_DICT.LABEL, po.getLabel())
                .set(EP_SYSTEM_DICT.GROUP_NAME, po.getGroupName())
                .set(EP_SYSTEM_DICT.KEY, po.getKey())
                .set(EP_SYSTEM_DICT.VALUE, po.getValue())
                .set(EP_SYSTEM_DICT.DESCRIPTION, po.getDescription())
                .set(EP_SYSTEM_DICT.SORT, po.getSort())
                .set(EP_SYSTEM_DICT.STATUS, po.getStatus())
                .where(EP_SYSTEM_DICT.ID.eq(po.getId()))
                .and(EP_SYSTEM_DICT.DEL_FLAG.eq(false))
                .execute();
    }

}
