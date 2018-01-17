package com.ep.domain.service;

import com.ep.common.tool.DateTools;
import com.ep.domain.constant.BizConstant;
import com.ep.domain.constant.MessageCode;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.MemberChildBo;
import com.ep.domain.pojo.po.EpMemberChildPo;
import com.ep.domain.repository.EpFileRepository;
import com.ep.domain.repository.MemberChildRepository;
import com.ep.domain.repository.domain.enums.EpMemberChildChildSex;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @Description:孩子业务接口
 * @Author: J.W
 * @Date: 上午10:33 2017/11/27
 */
@Slf4j
@Service
public class MemberChildService {

    @Autowired
    private MemberChildRepository memberChildRepository;
    @Autowired
    private EpFileRepository fileRepository;

    /**
     * 新增孩子信息
     *
     * @param memberId
     * @param childNickName
     * @param childTrueName
     * @param childSex
     * @param childBirthday
     * @param childIdentity
     * @param currentSchool
     * @param currentClass
     * @return
     */
    public ResultDo addChild(Long memberId,
                             String childNickName,
                             String childTrueName,
                             EpMemberChildChildSex childSex,
                             Date childBirthday,
                             String childIdentity,
                             String currentSchool,
                             String currentClass) {
        // 前置校验
        Optional<?> optional = this.getByMemberIdAndTrueName(memberId, childTrueName);
        if (optional.isPresent()) {
            return ResultDo.build(MessageCode.ERROR_CHILD_TRUE_NAME_EXISTS);
        }
        EpMemberChildPo addPo = new EpMemberChildPo();
        addPo.setMemberId(memberId);
        addPo.setChildNickName(childNickName);
        addPo.setChildTrueName(childTrueName);
        addPo.setChildSex(childSex);
        addPo.setChildBirthday(DateTools.dateToTimestamp(childBirthday));
        addPo.setChildIdentity(childIdentity);
        addPo.setCurrentSchool(currentSchool);
        addPo.setCurrentClass(currentClass);
        memberChildRepository.insert(addPo);
        return ResultDo.build();
    }

    /**
     * 更新孩子信息
     *
     * @param childId
     * @param childNickName
     * @param childTrueName
     * @param childSex
     * @param childBirthday
     * @param childIdentity
     * @param currentSchool
     * @param currentClass
     * @return
     */
    public ResultDo editChild(Long memberId,
                              Long childId,
                              String childNickName,
                              String childTrueName,
                              EpMemberChildChildSex childSex,
                              Date childBirthday,
                              String childIdentity,
                              String currentSchool,
                              String currentClass) {
        // 前置校验
        Optional<?> optional = memberChildRepository.getOtherSameNameChild(childId, memberId, childTrueName);
        if (optional.isPresent()) {
            return ResultDo.build(MessageCode.ERROR_CHILD_TRUE_NAME_EXISTS);
        }
        EpMemberChildPo updatePo = new EpMemberChildPo();
        updatePo.setId(childId);
        updatePo.setMemberId(memberId);
        updatePo.setChildNickName(childNickName);
        updatePo.setChildTrueName(childTrueName);
        updatePo.setChildSex(childSex);
        updatePo.setChildBirthday(DateTools.dateToTimestamp(childBirthday));
        updatePo.setChildIdentity(childIdentity);
        updatePo.setCurrentSchool(currentSchool);
        updatePo.setCurrentClass(currentClass);
        int updateNum = memberChildRepository.editChild(updatePo);
        if (updateNum == BizConstant.DB_NUM_ONE) {
            return ResultDo.build();
        } else {
            return ResultDo.build(MessageCode.ERROR_SAVE_MISS);
        }
    }

    /**
     * 逻辑删除
     *
     * @param memberId
     * @param id
     * @return
     */
    public int delChild(Long memberId, Long id) {
        int delNum = memberChildRepository.delChild(memberId, id);
        if (delNum == BizConstant.DB_NUM_ONE) {
            // 删除孩子头像
            fileRepository.logicDelByBizTypeAndSourceId(BizConstant.FILE_BIZ_TYPE_CODE_CHILD_AVATAR, id);
        }
        return delNum;
    }

    /**
     * 根据会员ID和孩子姓名查询
     *
     * @param memberId
     * @param childTrueName
     * @return
     */
    public Optional<EpMemberChildPo> getByMemberIdAndTrueName(Long memberId, String childTrueName) {
        return memberChildRepository.getByMemberIdAndTrueName(memberId, childTrueName);
    }

    /**
     * 查询会员的所有孩子的综合信息
     *
     * @param memberId
     * @return
     */
    public List<EpMemberChildPo> getChildrenByMemberId(Long memberId) {
        return memberChildRepository.getChildrenByMemberId(memberId);
    }

    /**
     * 查询会员的所有孩子的综合信息
     *
     * @param memberId
     * @return
     */
    public List<MemberChildBo> queryAllByMemberId(Long memberId) {
        return memberChildRepository.queryAllByMemberId(memberId);
    }

    /**
     * 根据主键获取孩子信息
     *
     * @param memberId
     * @param childId
     * @return
     */
    public ResultDo<EpMemberChildPo> getById(Long memberId, Long childId) {
        EpMemberChildPo child = memberChildRepository.getById(childId);
        if (child == null || !child.getMemberId().equals(memberId)) {
            return ResultDo.build(MessageCode.ERROR_DATA_MISS);
        }
        ResultDo<EpMemberChildPo> resultDo = ResultDo.build();
        return resultDo.setResult(child);
    }
}