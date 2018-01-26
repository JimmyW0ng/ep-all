package com.ep.domain.repository;

import com.ep.domain.pojo.po.EpOrderPo;
import com.ep.domain.repository.domain.enums.EpOrderStatus;
import com.ep.domain.repository.domain.tables.records.EpOrderRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.ep.domain.repository.domain.Tables.EP_ORDER;

/**
 * @Description:订单表Repository
 * @Author: J.W
 * @Date: 上午10:35 2017/11/27
 */
@Repository
public class OrderRepository extends AbstractCRUDRepository<EpOrderRecord, Long, EpOrderPo> {

    @Autowired
    public OrderRepository(DSLContext dslContext) {
        super(dslContext, EP_ORDER, EP_ORDER.ID, EpOrderPo.class);
    }

    /**
     * 获取课程总下单成功数
     *
     * @param courseId
     * @return
     */
    public Long getSuccessByCourseId(Long courseId) {
        return dslContext.selectCount()
                .from(EP_ORDER)
                .where(EP_ORDER.COURSE_ID.eq(courseId))
                .and(EP_ORDER.STATUS.eq(EpOrderStatus.success))
                .and(EP_ORDER.DEL_FLAG.eq(false))
                .fetchOneInto(Long.class);
    }

}

