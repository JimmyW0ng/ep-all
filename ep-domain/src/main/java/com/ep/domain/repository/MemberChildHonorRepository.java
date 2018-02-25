package com.ep.domain.repository;

import com.ep.domain.pojo.po.EpMemberChildHonorPo;
import com.ep.domain.repository.domain.tables.records.EpMemberChildHonorRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.ep.domain.repository.domain.Tables.EP_MEMBER_CHILD_HONOR;

/**
 * @Description: 孩子荣誉表Repository
 * @Author: J.W
 * @Date: 上午10:35 2017/11/27
 */
@Repository
public class MemberChildHonorRepository extends AbstractCRUDRepository<EpMemberChildHonorRecord, Long, EpMemberChildHonorPo> {

    @Autowired
    public MemberChildHonorRepository(DSLContext dslContext) {
        super(dslContext, EP_MEMBER_CHILD_HONOR, EP_MEMBER_CHILD_HONOR.ID, EpMemberChildHonorPo.class);
    }


    /**
     * 统计孩子的荣誉数
     *
     * @param childId
     * @return
     */
    public Long countByChildId(Long childId) {
        return dslContext.selectCount()
                .from(EP_MEMBER_CHILD_HONOR)
                .where(EP_MEMBER_CHILD_HONOR.CHILD_ID.eq(childId))
                .and(EP_MEMBER_CHILD_HONOR.DEL_FLAG.eq(false))
                .fetchOneInto(Long.class);
    }
}

