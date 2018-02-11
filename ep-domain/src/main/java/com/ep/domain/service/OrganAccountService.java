package com.ep.domain.service;

import com.ep.common.tool.CollectionsTools;
import com.ep.common.tool.DateTools;
import com.ep.domain.constant.BizConstant;
import com.ep.domain.constant.MessageCode;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.OrganAccountBo;
import com.ep.domain.pojo.bo.OrganAccountClassBo;
import com.ep.domain.pojo.po.EpFilePo;
import com.ep.domain.pojo.po.EpOrganAccountPo;
import com.ep.domain.pojo.po.EpOrganPo;
import com.ep.domain.repository.FileRepository;
import com.ep.domain.repository.OrganAccountRepository;
import com.ep.domain.repository.OrganClassRepository;
import com.ep.domain.repository.OrganRepository;
import lombok.extern.slf4j.Slf4j;
import org.jooq.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @Description: 机构账户关联信息服务
 * @Author: CC.F
 * @Date: 9:45 2018/2/6
 */
@Service
@Slf4j
public class OrganAccountService {

    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private OrganRepository organRepository;
    @Autowired
    private OrganAccountRepository organAccountRepository;
    @Autowired
    private OrganClassRepository organClassRepository;

    /**
     * 根据手机号获取详情
     *
     * @param mobile
     * @return
     */
    public ResultDo<OrganAccountBo> getOrganAccountInfo(Long mobile) {
        ResultDo<OrganAccountBo> resultDo = ResultDo.build();
        List<EpOrganAccountPo> accountList = organAccountRepository.getByMobile(mobile);
        if (CollectionsTools.isEmpty(accountList)) {
            return resultDo.setError(MessageCode.ERROR_ORGAN_ACCOUNT_NOT_EXISTS);
        }
        EpOrganAccountPo accountPo = accountList.get(BizConstant.DB_NUM_ZERO);
        OrganAccountBo accountBo = new OrganAccountBo();
        // 认证编号
        accountBo.setId(accountPo.getId());
        // 昵称
        accountBo.setNickName(accountPo.getNickName());
        // 机构名
        EpOrganPo organPo = organRepository.getById(accountPo.getOgnId());
        accountBo.setOgnName(organPo.getOgnName());
        // 头像
        Optional<EpFilePo> optAvatar = fileRepository.getOneByBizTypeAndSourceId(BizConstant.FILE_BIZ_TYPE_CODE_TEACHER_AVATAR, accountBo.getId());
        String avatar = optAvatar.isPresent() ? optAvatar.get().getFileUrl() : null;
        accountBo.setAvatar(avatar);
        return resultDo.setResult(accountBo);
    }

    public EpOrganAccountPo getById(Long id) {
        return organAccountRepository.getById(id);

    }

    public Page<OrganAccountBo> findbyPageAndCondition(Pageable pageable, Collection<? extends Condition> condition) {
        return organAccountRepository.findbyPageAndCondition(pageable, condition);
    }

    /**
     * 新增机构账户关联信息
     * @param po
     * @return
     */
    public EpOrganAccountPo create(EpOrganAccountPo po){
        return organAccountRepository.insertNew(po);
    }

    /**
     * 修改机构账户关联信息
     * @param po
     * @return
     */
    public int update(EpOrganAccountPo po){
        return organAccountRepository.updateById(po);
    }

    /**
     * 删除机构账户关联信息
     * @param id
     * @return
     */
    public int delete(Long id){
        return organAccountRepository.deleteLogical(id);
    }

    public List<EpOrganAccountPo> findByOgnId(Long ognId) {
        return organAccountRepository.findByOgnId(ognId);
    }

    /**
     * 根据账户id获取今日课程
     *
     * @param id
     * @return
     */
    public ResultDo<List<OrganAccountClassBo>> findClassByOrganAccount(Long id) {
        ResultDo<List<OrganAccountClassBo>> resultDo = ResultDo.build();
        Timestamp now = DateTools.getCurrentDateTime();
        Timestamp startTime = DateTools.zerolizedTime(now);
        Timestamp endTime = DateTools.getEndTime(now);
        List<OrganAccountClassBo> todayClasses = organClassRepository.findClassByOgnAccountId(id, startTime, endTime);
        if (CollectionsTools.isNotEmpty(todayClasses)) {
            for (OrganAccountClassBo classBo : todayClasses) {
                // 加载课程图片
                Optional<EpFilePo> optional = fileRepository.getOneByBizTypeAndSourceId(BizConstant.FILE_BIZ_TYPE_CODE_COURSE_MAIN_PIC, classBo.getCourseId());
                String mainPicUrl = optional.isPresent() ? optional.get().getFileUrl() : null;
                classBo.setMainPicUrl(mainPicUrl);
            }
        }
        resultDo.setResult(todayClasses);
        return resultDo;
    }

}
