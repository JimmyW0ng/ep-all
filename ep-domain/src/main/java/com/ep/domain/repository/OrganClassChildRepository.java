package com.ep.domain.repository;

import com.ep.domain.pojo.po.EpOrganClassChildPo;
import com.ep.domain.repository.domain.tables.records.EpOrganClassChildRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.ep.domain.repository.domain.Tables.EP_ORGAN_CLASS_CHILD;

/**
 * @Description:机构班级孩子表Repository
 * @Author: J.W
 * @Date: 上午10:35 2017/11/27
 */
@Repository
public class OrganClassChildRepository extends AbstractCRUDRepository<EpOrganClassChildRecord, Long, EpOrganClassChildPo> {

    @Autowired
    public OrganClassChildRepository(DSLContext dslContext) {
        super(dslContext, EP_ORGAN_CLASS_CHILD, EP_ORGAN_CLASS_CHILD.ID, EpOrganClassChildPo.class);
    }

    /**
     * 根据订单获取班级孩子
     *
     * @param orderId
     * @return
     */
    public Optional<EpOrganClassChildPo> getByOrderId(Long orderId) {
        EpOrganClassChildPo classChildPo = dslContext.selectFrom(EP_ORGAN_CLASS_CHILD)
                .where(EP_ORGAN_CLASS_CHILD.ORDER_ID.eq(orderId))
                .and(EP_ORGAN_CLASS_CHILD.DEL_FLAG.eq(false))
                .fetchOneInto(EpOrganClassChildPo.class);
        return Optional.ofNullable(classChildPo);
    }

    /**
     * 更新
     *
     * @param orderId
     * @return
     */
    public int updateCourseCommentFlagByOrderId(Long orderId) {
        return dslContext.update(EP_ORGAN_CLASS_CHILD)
                .set(EP_ORGAN_CLASS_CHILD.COURSE_COMMENT_FLAG, true)
                .where(EP_ORGAN_CLASS_CHILD.ORDER_ID.eq(orderId))
                .and(EP_ORGAN_CLASS_CHILD.DEL_FLAG.eq(false))
                .execute();
    }

}

