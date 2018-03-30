package com.ep.domain.repository;

import com.ep.domain.constant.BizConstant;
import com.ep.domain.pojo.bo.OrganClassChildBo;
import com.ep.domain.pojo.po.EpOrganClassChildPo;
import com.ep.domain.repository.domain.tables.records.EpOrganClassChildRecord;
import com.google.common.collect.Lists;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.ep.domain.repository.domain.Tables.EP_MEMBER_CHILD;
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
     * 根据班次和孩子id获取数据
     *
     * @param classId
     * @param childId
     * @return
     */
    public Optional<EpOrganClassChildPo> getByClassIdAndChildId(Long classId, Long childId) {
        EpOrganClassChildPo classChildPo = dslContext.selectFrom(EP_ORGAN_CLASS_CHILD)
                .where(EP_ORGAN_CLASS_CHILD.CLASS_ID.eq(classId))
                .and(EP_ORGAN_CLASS_CHILD.CHILD_ID.eq(childId))
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

    /**
     * 孩子班次评价数+1
     *
     * @param orderId
     */
    public int addScheduleCommentNum(Long orderId) {
        return dslContext.update(EP_ORGAN_CLASS_CHILD)
                .set(EP_ORGAN_CLASS_CHILD.SCHEDULE_COMMENT_NUM,
                        EP_ORGAN_CLASS_CHILD.SCHEDULE_COMMENT_NUM.add(BizConstant.DB_NUM_ONE))
                .where(EP_ORGAN_CLASS_CHILD.ORDER_ID.eq(orderId))
                .and(EP_ORGAN_CLASS_CHILD.DEL_FLAG.eq(false))
                .execute();
    }

    /**
     * 孩子班次评价数+1
     *
     * @param orderId
     */
    public int subtractScheduleCommentNum(Long orderId) {
        return dslContext.update(EP_ORGAN_CLASS_CHILD)
                         .set(EP_ORGAN_CLASS_CHILD.SCHEDULE_COMMENT_NUM,
                                 EP_ORGAN_CLASS_CHILD.SCHEDULE_COMMENT_NUM.subtract(BizConstant.DB_NUM_ONE))
                         .where(EP_ORGAN_CLASS_CHILD.ORDER_ID.eq(orderId))
                         .and(EP_ORGAN_CLASS_CHILD.SCHEDULE_COMMENT_NUM.greaterThan(BizConstant.DB_NUM_ZERO))
                         .and(EP_ORGAN_CLASS_CHILD.DEL_FLAG.eq(false))
                         .execute();
    }

    /**
     * 根据班级id获取该班孩子记录
     *
     * @param classId
     * @return
     */
    public List<OrganClassChildBo> findChildByClassId(Long classId) {
        List<Field<?>> fieldList = Lists.newArrayList();
        fieldList.add(EP_ORGAN_CLASS_CHILD.CHILD_ID);
        fieldList.add(EP_ORGAN_CLASS_CHILD.CLASS_ID);
        fieldList.add(EP_MEMBER_CHILD.CHILD_TRUE_NAME);

        return dslContext.select(fieldList)
                .from(EP_ORGAN_CLASS_CHILD)
                .innerJoin(EP_MEMBER_CHILD)
                .on(EP_ORGAN_CLASS_CHILD.CHILD_ID.eq(EP_MEMBER_CHILD.ID))
                .where(EP_ORGAN_CLASS_CHILD.CLASS_ID.eq(classId))
                .and(EP_ORGAN_CLASS_CHILD.DEL_FLAG.eq(false))
                .and(EP_MEMBER_CHILD.DEL_FLAG.eq(false))
                .fetchInto(OrganClassChildBo.class);
    }

    /**
     * 根据班级id获取该班孩子昵称记录
     *
     * @param classId
     * @return
     */
    public List<OrganClassChildBo> findNicknameByClassId(Long classId) {
        List<Field<?>> fieldList = Lists.newArrayList();
        fieldList.add(EP_ORGAN_CLASS_CHILD.CHILD_ID);
        fieldList.add(EP_ORGAN_CLASS_CHILD.CLASS_ID);
        fieldList.add(EP_MEMBER_CHILD.CHILD_NICK_NAME);

        return dslContext.select(fieldList)
                .from(EP_ORGAN_CLASS_CHILD)
                .innerJoin(EP_MEMBER_CHILD)
                .on(EP_ORGAN_CLASS_CHILD.CHILD_ID.eq(EP_MEMBER_CHILD.ID))
                .where(EP_ORGAN_CLASS_CHILD.CLASS_ID.eq(classId))
                .and(EP_ORGAN_CLASS_CHILD.DEL_FLAG.eq(false))
                .and(EP_MEMBER_CHILD.DEL_FLAG.eq(false))
                .fetchInto(OrganClassChildBo.class);
    }
}

