package com.ep.domain.repository;

import com.ep.domain.constant.BizConstant;
import com.ep.domain.pojo.po.EpOrganClassPo;
import com.ep.domain.repository.domain.tables.records.EpOrganClassRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.ep.domain.repository.domain.Tables.EP_ORGAN_CLASS;

/**
 * @Description:机构课程班次表Repository
 * @Author: J.W
 * @Date: 上午10:35 2017/11/27
 */
@Repository
public class OrganClassRepository extends AbstractCRUDRepository<EpOrganClassRecord, Long, EpOrganClassPo> {

    @Autowired
    public OrganClassRepository(DSLContext dslContext) {
        super(dslContext, EP_ORGAN_CLASS, EP_ORGAN_CLASS.ID, EpOrganClassPo.class);
    }

    /**
     * 获取课程班次
     *
     * @param courseId
     * @return
     */
    public List<EpOrganClassPo> getByCourseId(Long courseId) {
        return dslContext.selectFrom(EP_ORGAN_CLASS)
                .where(EP_ORGAN_CLASS.COURSE_ID.eq(courseId))
                .and(EP_ORGAN_CLASS.DEL_FLAG.eq(false))
                .orderBy(EP_ORGAN_CLASS.SORT.desc(), EP_ORGAN_CLASS.ID.asc())
                .fetchInto(EpOrganClassPo.class);
    }

    /**
     * 下单（没有报名限制）
     *
     * @param classId
     * @return
     */
    public int orderWithNoLimit(Long classId) {
        return dslContext.update(EP_ORGAN_CLASS)
                .set(EP_ORGAN_CLASS.ORDERED_NUM, EP_ORGAN_CLASS.ORDERED_NUM.add(BizConstant.DB_NUM_ONE))
                .where(EP_ORGAN_CLASS.ID.eq(classId))
                .and(EP_ORGAN_CLASS.DEL_FLAG.eq(false))
                .execute();
    }

    /**
     * 下单（有报名限制）
     *
     * @param classId
     * @return
     */
    public int orderWithLimit(Long classId) {
        return dslContext.update(EP_ORGAN_CLASS)
                .set(EP_ORGAN_CLASS.ORDERED_NUM, EP_ORGAN_CLASS.ORDERED_NUM.add(BizConstant.DB_NUM_ONE))
                .where(EP_ORGAN_CLASS.ID.eq(classId))
                .and(EP_ORGAN_CLASS.ENTER_LIMIT_FLAG.eq(true))
                .and(EP_ORGAN_CLASS.ENTERED_NUM.lessThan(EP_ORGAN_CLASS.ENTER_REQUIRE_NUM))
                .and(EP_ORGAN_CLASS.ORDERED_NUM.lessThan(BizConstant.ORDER_BEYOND_NUM))
                .and(EP_ORGAN_CLASS.DEL_FLAG.eq(false))
                .execute();
    }

}

