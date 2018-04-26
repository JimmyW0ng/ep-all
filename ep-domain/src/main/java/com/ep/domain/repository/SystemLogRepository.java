package com.ep.domain.repository;

import com.ep.domain.pojo.po.EpSystemLogPo;
import com.ep.domain.repository.domain.tables.records.EpSystemLogRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.ep.domain.repository.domain.Tables.EP_SYSTEM_LOG;


/**
 * @Description: 操作日志仓库
 * @Author: CC.F
 * @Date: 上午9:30 2018/4/26
 */
@Repository
public class SystemLogRepository extends AbstractCRUDRepository<EpSystemLogRecord, Long, EpSystemLogPo> {

    @Autowired
    public SystemLogRepository(DSLContext dslContext) {
        super(dslContext, EP_SYSTEM_LOG, EP_SYSTEM_LOG.ID, EpSystemLogPo.class);
    }
}
