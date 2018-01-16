package com.ep.domain.repository;

import com.ep.common.tool.DateTools;
import com.ep.common.tool.StringTools;
import com.ep.domain.pojo.bo.MemberChildBo;
import com.ep.domain.pojo.po.EpMemberChildPo;
import com.ep.domain.repository.domain.tables.records.EpMemberChildRecord;
import com.google.common.collect.Lists;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.UpdateSetMoreStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.ep.domain.repository.domain.Tables.EP_MEMBER_CHILD;
import static com.ep.domain.repository.domain.Tables.EP_MEMBER_CHILD_SIGN;

/**
 * @Description:孩子信息表Repository
 * @Author: J.W
 * @Date: 上午10:35 2017/11/27
 */
@Repository
public class MemberChildRepository extends AbstractCRUDRepository<EpMemberChildRecord, Long, EpMemberChildPo> {

    @Autowired
    public MemberChildRepository(DSLContext dslContext) {
        super(dslContext, EP_MEMBER_CHILD, EP_MEMBER_CHILD.ID, EpMemberChildPo.class);
    }

    /**
     * 更新孩子信息
     *
     * @param updatePo
     * @return
     */
    public int editChild(EpMemberChildPo updatePo) {
        UpdateSetMoreStep step = dslContext.update(EP_MEMBER_CHILD)
                .set(EP_MEMBER_CHILD.CHILD_TRUE_NAME, updatePo.getChildTrueName())
                .set(EP_MEMBER_CHILD.CHILD_SEX, updatePo.getChildSex())
                .set(EP_MEMBER_CHILD.CHILD_BIRTHDAY, updatePo.getChildBirthday())
                .set(EP_MEMBER_CHILD.SHOW_AT, DateTools.getCurrentDateTime());
        if (StringTools.isNotBlank(updatePo.getChildNickName())) {
            step = step.set(EP_MEMBER_CHILD.CHILD_NICK_NAME, updatePo.getChildNickName());
        }
        if (StringTools.isNotBlank(updatePo.getChildIdentity())) {
            step = step.set(EP_MEMBER_CHILD.CHILD_IDENTITY, updatePo.getChildIdentity());
        }
        if (StringTools.isNotBlank(updatePo.getCurrentSchool())) {
            step = step.set(EP_MEMBER_CHILD.CURRENT_SCHOOL, updatePo.getCurrentSchool());
        }
        if (StringTools.isNotBlank(updatePo.getCurrentClass())) {
            step = step.set(EP_MEMBER_CHILD.CURRENT_CLASS, updatePo.getCurrentClass());
        }
        return step.where(EP_MEMBER_CHILD.ID.eq(updatePo.getId()))
                .and(EP_MEMBER_CHILD.MEMBER_ID.eq(updatePo.getMemberId()))
                .and(EP_MEMBER_CHILD.DEL_FLAG.eq(false)).execute();
    }

    /**
     * 逻辑删除
     *
     * @param memberId
     * @param id
     * @return
     */
    public int delChild(Long memberId, Long id) {
        return dslContext.update(EP_MEMBER_CHILD)
                .set(EP_MEMBER_CHILD.DEL_FLAG, true)
                .where(EP_MEMBER_CHILD.ID.eq(id))
                .and(EP_MEMBER_CHILD.MEMBER_ID.eq(memberId))
                .and(EP_MEMBER_CHILD.DEL_FLAG.eq(false))
                .execute();
    }

    /**
     * 根据会员ID和孩子姓名查询
     *
     * @param memberId
     * @param childTrueName
     * @return
     */
    public Optional<EpMemberChildPo> getByMemberIdAndTrueName(Long memberId, String childTrueName) {
        EpMemberChildPo childPo = dslContext.selectFrom(EP_MEMBER_CHILD)
                .where(EP_MEMBER_CHILD.MEMBER_ID.eq(memberId))
                .and(EP_MEMBER_CHILD.CHILD_TRUE_NAME.eq(childTrueName))
                .and(EP_MEMBER_CHILD.DEL_FLAG.eq(false))
                .fetchOneInto(EpMemberChildPo.class);
        return Optional.ofNullable(childPo);
    }

    /**
     * 查询用户下其他重名的孩子
     *
     * @param id
     * @param memberId
     * @param childTrueName
     * @return
     */
    public Optional<EpMemberChildPo> getOtherSameNameChild(Long id, Long memberId, String childTrueName) {
        EpMemberChildPo childPo = dslContext.selectFrom(EP_MEMBER_CHILD)
                .where(EP_MEMBER_CHILD.MEMBER_ID.eq(memberId))
                .and(EP_MEMBER_CHILD.CHILD_TRUE_NAME.eq(childTrueName))
                .and(EP_MEMBER_CHILD.ID.notEqual(id))
                .and(EP_MEMBER_CHILD.DEL_FLAG.eq(false))
                .fetchOneInto(EpMemberChildPo.class);
        return Optional.ofNullable(childPo);
    }

    /**
     * 统计会员孩子数量
     *
     * @param memberId
     * @return
     */
    public int countChildNum(Long memberId) {
        return dslContext.selectCount().from(EP_MEMBER_CHILD)
                .where(EP_MEMBER_CHILD.MEMBER_ID.eq(memberId))
                .and(EP_MEMBER_CHILD.DEL_FLAG.eq(false))
                .fetchOneInto(Integer.class);
    }

    /**
     * 查询会员的所有孩子
     *
     * @param memberId
     * @return
     */
    public List<EpMemberChildPo> getChildrenByMemberId(Long memberId) {
        return dslContext.selectFrom(EP_MEMBER_CHILD)
                .where(EP_MEMBER_CHILD.MEMBER_ID.eq(memberId))
                .and(EP_MEMBER_CHILD.DEL_FLAG.eq(false))
                .orderBy(EP_MEMBER_CHILD.ID.asc())
                .fetchInto(EpMemberChildPo.class);
    }

    /**
     * 查询会员的所有孩子综合信息
     *
     * @param memberId
     * @return
     */
    public List<MemberChildBo> queryAllByMemberId(Long memberId) {
        List<Field<?>> fieldList = Lists.newArrayList(EP_MEMBER_CHILD.fields());
        fieldList.add(EP_MEMBER_CHILD_SIGN.CONTENT.as("signContent"));
        return dslContext.select(fieldList)
                .from(EP_MEMBER_CHILD)
                .leftJoin(EP_MEMBER_CHILD_SIGN)
                .on(EP_MEMBER_CHILD.ID.eq(EP_MEMBER_CHILD_SIGN.CHILD_ID))
                .and(EP_MEMBER_CHILD_SIGN.DEL_FLAG.eq(false))
                .where(EP_MEMBER_CHILD.MEMBER_ID.eq(memberId))
                .and(EP_MEMBER_CHILD.DEL_FLAG.eq(false))
                .orderBy(EP_MEMBER_CHILD.SHOW_AT.desc())
                .fetchInto(MemberChildBo.class);
    }

    /**
     * 根据会员ID和孩子ID查询
     *
     * @param memberId
     * @param childId
     * @return
     */
    public EpMemberChildPo getByMemberIdAndChildId(Long memberId, Long childId) {
        return dslContext.selectFrom(EP_MEMBER_CHILD)
                .where(EP_MEMBER_CHILD.ID.eq(childId))
                .and(EP_MEMBER_CHILD.MEMBER_ID.eq(memberId))
                .and(EP_MEMBER_CHILD.DEL_FLAG.eq(false))
                .orderBy(EP_MEMBER_CHILD.ID.asc())
                .fetchOneInto(EpMemberChildPo.class);
    }

}

