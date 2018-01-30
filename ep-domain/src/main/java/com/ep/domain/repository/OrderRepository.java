package com.ep.domain.repository;

import com.ep.domain.pojo.po.EpOrderPo;
import com.ep.domain.repository.domain.enums.EpOrderStatus;
import com.ep.domain.repository.domain.tables.records.EpOrderRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    /**
     * 根据孩子id和班次id查询订单
     *
     * @param childId
     * @param classId
     * @return
     */
    public List<EpOrderPo> findByChildIdAndClassId(Long childId, Long classId) {
        return dslContext.selectFrom(EP_ORDER)
                .where(EP_ORDER.CHILD_ID.eq(childId))
                .and(EP_ORDER.CLASS_ID.eq(classId))
                .and(EP_ORDER.STATUS.in(EpOrderStatus.save, EpOrderStatus.success))
                .and(EP_ORDER.DEL_FLAG.eq(false))
                .orderBy(EP_ORDER.ID.desc())
                .fetchInto(EpOrderPo.class);
    }

    /**
     * 根据孩子查询成功的
     *
     * @param childId
     * @return
     */
    public List<EpOrderPo> getSuccessByChildId(Long childId) {
        return dslContext.selectFrom(EP_ORDER)
                .where(EP_ORDER.CHILD_ID.eq(childId))
                .and(EP_ORDER.STATUS.in(EpOrderStatus.success))
                .and(EP_ORDER.DEL_FLAG.eq(false))
                .orderBy(EP_ORDER.ID.desc())
                .fetchInto(EpOrderPo.class);
    }
}

