package com.ep.domain.service;

import com.ep.common.tool.CollectionsTools;
import com.ep.domain.constant.BizConstant;
import com.ep.domain.constant.MessageCode;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.MemberChildBo;
import com.ep.domain.pojo.po.EpFilePo;
import com.ep.domain.pojo.po.EpOrganAccountPo;
import com.ep.domain.pojo.po.EpOrganClassPo;
import com.ep.domain.repository.FileRepository;
import com.ep.domain.repository.MemberChildRepository;
import com.ep.domain.repository.OrganAccountRepository;
import com.ep.domain.repository.OrganClassRepository;
import com.ep.domain.repository.domain.enums.EpOrganClassStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @Description: 机构班次孩子接口服务类
 * @Author: J.W
 * @Date: 上午11:15 2018/3/5
 */
@Slf4j
@Service
public class OrganClassChildService {

    @Autowired
    private OrganClassRepository organClassRepository;
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private MemberChildRepository memberChildRepository;
    @Autowired
    private OrganAccountRepository organAccountRepository;

    /**
     * 查看班次学员
     *
     * @param classId
     * @param mobile
     * @return
     */
    public ResultDo<List<MemberChildBo>> findChildrenByClassId(Long classId, Long mobile) {
        ResultDo<List<MemberChildBo>> resultDo = ResultDo.build();
        // 校验课程
        EpOrganClassPo classPo = organClassRepository.getById(classId);
        if (classPo == null || classPo.getDelFlag()) {
            log.error("班次不存在, classId={}", classId);
            return resultDo.setError(MessageCode.ERROR_CLASS_NOT_EXIST);
        }
        if (classPo.getStatus().equals(EpOrganClassStatus.save)) {
            log.error("课程未上线, classId={}, status={}", classId, classPo.getStatus().getName());
            return resultDo.setError(MessageCode.ERROR_COURSE_NOT_ONLINE);
        }
        // 校验班次负责人
        Optional<EpOrganAccountPo> existAccount = organAccountRepository.getByMobileAndOgnId(mobile, classPo.getOgnId());
        if (!existAccount.isPresent()) {
            log.error("当前用户无机构账户数据, mobile={}", mobile);
            return resultDo.setError(MessageCode.ERROR_ORGAN_ACCOUNT_NOT_EXISTS);
        }
        // 孩子信息
        List<MemberChildBo> childList = memberChildRepository.queryAllByClassId(classId);
        if (CollectionsTools.isNotEmpty(childList)) {
            for (MemberChildBo childBo : childList) {
                Optional<EpFilePo> optional = fileRepository.getOneByBizTypeAndSourceId(BizConstant.FILE_BIZ_TYPE_CODE_CHILD_AVATAR, childBo.getId());
                String avatar = optional.isPresent() ? optional.get().getFileUrl() : null;
                childBo.setAvatar(avatar);
            }
        }
        return resultDo.setResult(childList);
    }
}
