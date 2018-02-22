package com.ep.domain.repository;

import com.ep.domain.pojo.po.EpMemberChildTagPo;
import com.ep.domain.repository.domain.tables.records.EpMemberChildTagRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.ep.domain.repository.domain.Tables.EP_MEMBER_CHILD_TAG;

/**
 * @Description: 孩子标签记录表Repository
 * @Author J.W
 * @Date: 下午 5:17 2018/2/22 0022
 */
@Repository
public class MemberChildTagRepository extends AbstractCRUDRepository<EpMemberChildTagRecord, Long, EpMemberChildTagPo> {

    @Autowired
    public MemberChildTagRepository(DSLContext dslContext) {
        super(dslContext, EP_MEMBER_CHILD_TAG, EP_MEMBER_CHILD_TAG.ID, EpMemberChildTagPo.class);
    }

    /**
     * 根据孩子和获取课时标签
     *
     * @param childId
     * @param classCatelogId
     * @return
     */
    public List<EpMemberChildTagPo> findByChildIdAndClassCatelogId(Long childId, Long classCatelogId) {
        return dslContext.selectFrom(EP_MEMBER_CHILD_TAG)
                         .where(EP_MEMBER_CHILD_TAG.CLASS_CATELOG_ID.eq(classCatelogId))
                         .and(EP_MEMBER_CHILD_TAG.CHILD_ID.eq(childId))
                         .and(EP_MEMBER_CHILD_TAG.DEL_FLAG.eq(false))
                         .fetchInto(EpMemberChildTagPo.class);
    }

}
