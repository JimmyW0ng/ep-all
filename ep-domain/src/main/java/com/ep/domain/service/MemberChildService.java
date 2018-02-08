package com.ep.domain.service;

import com.ep.common.exception.ServiceRuntimeException;
import com.ep.common.tool.CollectionsTools;
import com.ep.common.tool.DateTools;
import com.ep.common.tool.StringTools;
import com.ep.domain.constant.BizConstant;
import com.ep.domain.constant.MessageCode;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.MemberChildBo;
import com.ep.domain.pojo.po.EpFilePo;
import com.ep.domain.pojo.po.EpMemberChildPo;
import com.ep.domain.pojo.po.EpMemberChildSignPo;
import com.ep.domain.pojo.po.EpOrderPo;
import com.ep.domain.repository.FileRepository;
import com.ep.domain.repository.MemberChildRepository;
import com.ep.domain.repository.MemberChildSignRepository;
import com.ep.domain.repository.OrderRepository;
import com.ep.domain.repository.domain.enums.EpMemberChildChildSex;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @Description: 孩子业务接口
 * @Author: J.W
 * @Date: 上午10:33 2017/11/27
 */
@Slf4j
@Service
public class MemberChildService {

    @Autowired
    private MemberChildRepository memberChildRepository;
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private MemberChildSignRepository memberChildSignRepository;

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
     * @param avatar
     * @param sign
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResultDo addChild(Long memberId,
                             String childNickName,
                             String childTrueName,
                             EpMemberChildChildSex childSex,
                             Date childBirthday,
                             String childIdentity,
                             String currentSchool,
                             String currentClass,
                             String avatar,
                             String sign) {
        // 校验孩子数量
        if (memberChildRepository.countChildNum(memberId) >= BizConstant.CHILD_LIMIT_NUM) {
            return ResultDo.build(MessageCode.ERROR_CHILD_LIMIT_NUM);
        }
        // 前置校验
        Optional<?> optional = this.getByMemberIdAndTrueName(memberId, childTrueName);
        if (optional.isPresent()) {
            return ResultDo.build(MessageCode.ERROR_CHILD_TRUE_NAME_EXISTS);
        }
        EpMemberChildPo addChildPo = new EpMemberChildPo();
        addChildPo.setMemberId(memberId);
        addChildPo.setChildNickName(childNickName);
        addChildPo.setChildTrueName(childTrueName);
        addChildPo.setChildSex(childSex);
        addChildPo.setChildBirthday(DateTools.dateToTimestamp(childBirthday));
        addChildPo.setChildIdentity(childIdentity);
        addChildPo.setCurrentSchool(currentSchool);
        addChildPo.setCurrentClass(currentClass);
        memberChildRepository.insert(addChildPo);
        // 获取头像预授权码
        if (StringTools.isNotEmpty(avatar)) {
            Optional<EpFilePo> optOfFile = fileRepository.getByPreCode(avatar);
            if (!optOfFile.isPresent()) {
                log.error("新增孩子档案保存头像预授权码不能存在，preCode=()", avatar);
                throw new ServiceRuntimeException("新增孩子档案保存头像预授权码不能存在");
            }
            fileRepository.updateSourceIdByPreCode(avatar, addChildPo.getId());
        }
        // 更新孩子签名
        EpMemberChildSignPo signPo = new EpMemberChildSignPo();
        signPo.setChildId(addChildPo.getId());
        signPo.setContent(sign);
        memberChildSignRepository.insert(signPo);
        return ResultDo.build();
    }

    /**
     * 更新孩子信息
     *
     * @param memberId
     * @param childId
     * @param childNickName
     * @param childTrueName
     * @param childSex
     * @param childBirthday
     * @param childIdentity
     * @param currentSchool
     * @param currentClass
     * @param avatar
     * @param sign
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResultDo editChild(Long memberId,
                              Long childId,
                              String childNickName,
                              String childTrueName,
                              EpMemberChildChildSex childSex,
                              Date childBirthday,
                              String childIdentity,
                              String currentSchool,
                              String currentClass,
                              String avatar,
                              String sign) {
        // 前置校验
        ResultDo existResult = this.getCheckedMemberChild(memberId, childId);
        if (existResult.isError()) {
            return existResult;
        }
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
        memberChildRepository.editChild(updatePo);
        // 获取头像预授权码
        if (StringTools.isNotEmpty(avatar)) {
            Optional<EpFilePo> optOfFile = fileRepository.getByPreCode(avatar);
            if (!optOfFile.isPresent()) {
                log.error("新增孩子档案保存头像预授权码不能存在，preCode=()", avatar);
                throw new ServiceRuntimeException("新增孩子档案保存头像预授权码不能存在");
            }
            fileRepository.logicDelByBizTypeAndSourceId(BizConstant.FILE_BIZ_TYPE_CODE_CHILD_AVATAR, childId);
            fileRepository.updateSourceIdByPreCode(avatar, childId);
        }
        // 更新孩子签名
        memberChildSignRepository.updateSign(childId, sign);
        return ResultDo.build();
    }

    /**
     * 逻辑删除
     *
     * @param memberId
     * @param id
     * @return
     */
    public ResultDo delChild(Long memberId, Long id) {
        ResultDo<EpMemberChildPo> checkedChild = getCheckedMemberChild(memberId, id);
        if (checkedChild.isError()) {
            return checkedChild;
        }
        List<EpOrderPo> successOrders = orderRepository.findSuccessByChildId(id);
        if (CollectionsTools.isNotEmpty(successOrders)) {
            return ResultDo.build(MessageCode.ERROR_CHILD_CAN_NOT_DEL);
        }
        memberChildRepository.delChild(memberId, id);
        return ResultDo.build();
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
    public List<MemberChildBo> queryAllByMemberId(Long memberId) {
        List<MemberChildBo> data = memberChildRepository.queryAllByMemberId(memberId);
        if (CollectionsTools.isEmpty(data)) {
            return data;
        }
        for (MemberChildBo childBo : data) {
            Optional<EpFilePo> optional = fileRepository.getOneByBizTypeAndSourceId(BizConstant.FILE_BIZ_TYPE_CODE_CHILD_AVATAR, childBo.getId());
            if (optional.isPresent()) {
                childBo.setAvatar(optional.get().getFileUrl());
            }
        }
        return data;
    }

    /**
     * 判断孩子信息是否为空 && 是否属于某个会员
     *
     * @param memberId
     * @param childId
     * @return
     */
    public ResultDo<EpMemberChildPo> getCheckedMemberChild(Long memberId, Long childId) {
        ResultDo<EpMemberChildPo> resultDo = ResultDo.build();
        EpMemberChildPo child = memberChildRepository.getById(childId);
        if (child == null || !child.getMemberId().equals(memberId)) {
            return resultDo.setError(MessageCode.ERROR_CHILD_NOT_EXISTS);
        }
        resultDo.setResult(child);
        return resultDo;
    }

}
