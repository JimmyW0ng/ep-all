package com.ep.domain.repository;

import com.ep.domain.constant.BizConstant;
import com.ep.domain.pojo.bo.MemberMessageBo;
import com.ep.domain.pojo.po.EpMemberMessagePo;
import com.ep.domain.repository.domain.enums.EpMemberMessageStatus;
import com.ep.domain.repository.domain.enums.EpMemberMessageType;
import com.ep.domain.repository.domain.tables.records.EpMemberMessageRecord;
import com.google.common.collect.Lists;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

import static com.ep.domain.repository.domain.Tables.*;

/**
 * @Description: 会员消息表Repository
 * @Author J.W
 * @Date: 下午 5:17 2018/2/22 0022
 */
@Repository
public class MemberMessageRepository extends AbstractCRUDRepository<EpMemberMessageRecord, Long, EpMemberMessagePo> {

    @Autowired
    public MemberMessageRepository(DSLContext dslContext) {
        super(dslContext, EP_MEMBER_MESSAGE, EP_MEMBER_MESSAGE.ID, EpMemberMessagePo.class);
    }

    /**
     * 孩子评价类消息未读数
     *
     * @param memberId
     * @param type
     * @return
     */
    public Integer getUnreadNumByMemberId(Long memberId, EpMemberMessageType type) {
        return dslContext.selectCount()
                         .from(EP_MEMBER_MESSAGE)
                         .where(EP_MEMBER_MESSAGE.MEMBER_ID.eq(memberId))
                         .and(EP_MEMBER_MESSAGE.TYPE.eq(type))
                         .and(EP_MEMBER_MESSAGE.STATUS.eq(EpMemberMessageStatus.unread))
                         .and(EP_MEMBER_MESSAGE.DEL_FLAG.eq(false))
                         .fetchOneInto(Integer.class);
    }

    /**
     * 孩子消息-分页
     *
     * @param pageable
     * @param memberId
     * @return
     */
    public Page<MemberMessageBo> findClassCatalogCommentByMemberIdForPage(Pageable pageable, Long memberId) {
        Collection<Condition> conditions = Lists.newArrayList();
        conditions.add(EP_MEMBER_MESSAGE.MEMBER_ID.eq(memberId));
        conditions.add(EP_MEMBER_MESSAGE.TYPE.eq(EpMemberMessageType.class_schedule_comment));
        conditions.add(EP_MEMBER_MESSAGE.DEL_FLAG.eq(false));
        Long count = dslContext.selectCount()
                .from(EP_MEMBER_MESSAGE)
                .where(conditions)
                .fetchOneInto(Long.class);
        if (count == BizConstant.DB_NUM_ZERO) {
            return new PageImpl<>(Lists.newArrayList(), pageable, count);
        }
        List<Field<?>> fieldList = Lists.newArrayList(EP_MEMBER_MESSAGE.fields());
        fieldList.add(EP_ORGAN_CLASS_CHILD.ORDER_ID.as("orderId"));
        List<MemberMessageBo> data = dslContext.select(fieldList)
                                               .from(EP_MEMBER_MESSAGE)
                                               .leftJoin(EP_ORGAN_CLASS_CATALOG)
                                               .on(EP_MEMBER_MESSAGE.SOURCE_ID.eq(EP_ORGAN_CLASS_CATALOG.ID))
                                               .and(EP_ORGAN_CLASS_CATALOG.DEL_FLAG.eq(false))
                                               .leftJoin(EP_ORGAN_CLASS_CHILD)
                                               .on(EP_ORGAN_CLASS_CATALOG.CLASS_ID.eq(EP_ORGAN_CLASS_CHILD.CLASS_ID))
                                               .and(EP_ORGAN_CLASS_CHILD.CHILD_ID.eq(EP_MEMBER_MESSAGE.CHILD_ID))
                                               .and(EP_ORGAN_CLASS_CHILD.DEL_FLAG.eq(false))
                                               .where(conditions)
                                               .orderBy(EP_MEMBER_MESSAGE.CREATE_AT.desc())
                                               .limit(pageable.getPageSize())
                                               .offset(pageable.getOffset())
                                               .fetchInto(MemberMessageBo.class);
        return new PageImpl<>(data, pageable, count);
    }

    /**
     * 设置已读
     *
     * @param memberId
     * @param type
     * @return
     */
    public int readAllByMemberId(Long memberId, EpMemberMessageType type) {
        return dslContext.update(EP_MEMBER_MESSAGE)
                         .set(EP_MEMBER_MESSAGE.STATUS, EpMemberMessageStatus.read)
                         .where(EP_MEMBER_MESSAGE.MEMBER_ID.eq(memberId))
                         .and(EP_MEMBER_MESSAGE.TYPE.eq(type))
                         .and(EP_MEMBER_MESSAGE.STATUS.eq(EpMemberMessageStatus.unread))
                         .and(EP_MEMBER_MESSAGE.DEL_FLAG.eq(false))
                         .execute();
    }

    /**
     * 根据sourceId和类型物理删除
     *
     * @param sourceId
     * @param type
     * @return
     */
    public int physicalDeleteBySourceIdAndType(Long sourceId, EpMemberMessageType type) {
        return dslContext.delete(EP_MEMBER_MESSAGE)
                         .where(EP_MEMBER_MESSAGE.SOURCE_ID.eq(sourceId))
                         .and(EP_MEMBER_MESSAGE.TYPE.eq(type))
                         .execute();
    }
}
