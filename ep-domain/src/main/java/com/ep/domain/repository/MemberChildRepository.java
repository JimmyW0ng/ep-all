package com.ep.domain.repository;

import com.ep.common.tool.DateTools;
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

import static com.ep.domain.repository.domain.Tables.*;

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
                .set(EP_MEMBER_CHILD.SHOW_AT, DateTools.getCurrentDateTime())
                .set(EP_MEMBER_CHILD.CHILD_NICK_NAME, updatePo.getChildNickName())
                .set(EP_MEMBER_CHILD.CHILD_IDENTITY, updatePo.getChildIdentity())
                .set(EP_MEMBER_CHILD.CURRENT_SCHOOL, updatePo.getCurrentSchool())
                .set(EP_MEMBER_CHILD.CURRENT_CLASS, updatePo.getCurrentClass());
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
     * 根据会员ID和孩子昵称查询
     *
     * @param memberId
     * @param childNickName
     * @return
     */
    public Optional<EpMemberChildPo> getByMemberIdAndNickName(Long memberId, String childNickName) {
        EpMemberChildPo childPo = dslContext.selectFrom(EP_MEMBER_CHILD)
                .where(EP_MEMBER_CHILD.MEMBER_ID.eq(memberId))
                .and(EP_MEMBER_CHILD.CHILD_NICK_NAME.eq(childNickName))
                .and(EP_MEMBER_CHILD.DEL_FLAG.eq(false))
                .fetchOneInto(EpMemberChildPo.class);
        return Optional.ofNullable(childPo);
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
     * 查询用户下其他重复昵称的孩子
     *
     * @param id
     * @param memberId
     * @param childNickName
     * @return
     */
    public Optional<EpMemberChildPo> getOtherSameNickNameChild(Long id, Long memberId, String childNickName) {
        EpMemberChildPo childPo = dslContext.selectFrom(EP_MEMBER_CHILD)
                .where(EP_MEMBER_CHILD.MEMBER_ID.eq(memberId))
                .and(EP_MEMBER_CHILD.CHILD_NICK_NAME.eq(childNickName))
                .and(EP_MEMBER_CHILD.ID.notEqual(id))
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
    public Optional<EpMemberChildPo> getOtherSameTrueNameChild(Long id, Long memberId, String childTrueName) {
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
     * 查询会员的所有孩子综合信息
     *
     * @param memberId
     * @return
     */
    public List<MemberChildBo> queryAllByMemberId(Long memberId) {
        List<Field<?>> fieldList = Lists.newArrayList(EP_MEMBER_CHILD.ID);
        fieldList.add(EP_MEMBER_CHILD.CHILD_NICK_NAME);
        return dslContext.select(fieldList)
                         .from(EP_MEMBER_CHILD)
                         .where(EP_MEMBER_CHILD.MEMBER_ID.eq(memberId))
                         .and(EP_MEMBER_CHILD.DEL_FLAG.eq(false))
                         .groupBy(EP_MEMBER_CHILD.ID)
                         .orderBy(EP_MEMBER_CHILD.SHOW_AT.desc())
                         .fetchInto(MemberChildBo.class);
    }

    /**
     * 查询会员孩子综合信息
     *
     * @param memberId
     * @param id
     * @return
     */
    public Optional<MemberChildBo> getAllByMemberIdAndChildId(Long memberId, Long id) {
        List<Field<?>> fieldList = Lists.newArrayList(EP_MEMBER_CHILD.fields());
        fieldList.add(EP_MEMBER_CHILD_SIGN.CONTENT.as("sign"));
        MemberChildBo childBo = dslContext.select(fieldList)
                .from(EP_MEMBER_CHILD)
                .leftJoin(EP_MEMBER_CHILD_SIGN)
                .on(EP_MEMBER_CHILD.ID.eq(EP_MEMBER_CHILD_SIGN.CHILD_ID))
                .and(EP_MEMBER_CHILD_SIGN.DEL_FLAG.eq(false))
                .where(EP_MEMBER_CHILD.ID.eq(id))
                .and(EP_MEMBER_CHILD.MEMBER_ID.eq(memberId))
                .and(EP_MEMBER_CHILD.DEL_FLAG.eq(false))
                .fetchOneInto(MemberChildBo.class);
        return Optional.ofNullable(childBo);
    }

    /**
     * 获取班次孩子信息
     *
     * @param classId
     * @return
     */
    public List<MemberChildBo> queryAllByClassId(Long classId) {
        List<Field<?>> fieldList = Lists.newArrayList(EP_MEMBER_CHILD.ID);
        fieldList.add(EP_MEMBER_CHILD.CHILD_NICK_NAME);
        fieldList.add(EP_ORGAN_CLASS_CHILD.SCHEDULE_COMMENT_NUM);
        return dslContext.select(fieldList)
                         .from(EP_MEMBER_CHILD)
                         .innerJoin(EP_ORGAN_CLASS_CHILD)
                         .on(EP_MEMBER_CHILD.ID.eq(EP_ORGAN_CLASS_CHILD.CHILD_ID))
                         .and(EP_ORGAN_CLASS_CHILD.CLASS_ID.eq(classId))
                         .and(EP_ORGAN_CLASS_CHILD.DEL_FLAG.eq(false))
                         .where(EP_MEMBER_CHILD.DEL_FLAG.eq(false))
                         .fetchInto(MemberChildBo.class);
    }
}

