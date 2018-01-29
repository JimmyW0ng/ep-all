package com.ep.domain.repository;

import com.ep.domain.pojo.po.EpMemberChildSignPo;
import com.ep.domain.repository.domain.tables.records.EpMemberChildSignRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.ep.domain.repository.domain.Tables.EP_MEMBER_CHILD_SIGN;

/**
 * @Description:孩子签名表Repository
 * @Author: J.W
 * @Date: 上午10:35 2017/11/27
 */
@Repository
public class MemberChildSignRepository extends AbstractCRUDRepository<EpMemberChildSignRecord, Long, EpMemberChildSignPo> {

    @Autowired
    public MemberChildSignRepository(DSLContext dslContext) {
        super(dslContext, EP_MEMBER_CHILD_SIGN, EP_MEMBER_CHILD_SIGN.ID, EpMemberChildSignPo.class);
    }

    /**
     * 根据孩子id获取签名
     *
     * @param childId
     * @return
     */
    public EpMemberChildSignPo getByChildId(Long childId) {
        return dslContext.selectFrom(EP_MEMBER_CHILD_SIGN)
                .where(EP_MEMBER_CHILD_SIGN.CHILD_ID.eq(childId))
                .and(EP_MEMBER_CHILD_SIGN.DEL_FLAG.eq(false))
                .fetchOneInto(EpMemberChildSignPo.class);
    }

    /**
     * 根据孩子id更新签名
     *
     * @param childId
     * @param content
     * @return
     */
    public int updateSign(Long childId, String content) {
        return dslContext.update(EP_MEMBER_CHILD_SIGN)
                .set(EP_MEMBER_CHILD_SIGN.CONTENT, content)
                .where(EP_MEMBER_CHILD_SIGN.CHILD_ID.eq(childId))
                .and(EP_MEMBER_CHILD_SIGN.DEL_FLAG.eq(false))
                .execute();
    }

}

