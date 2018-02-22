package com.ep.domain.service;

import com.ep.common.tool.CollectionsTools;
import com.ep.domain.constant.BizConstant;
import com.ep.domain.constant.MessageCode;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.dto.OrganClassCatelogCommentDto;
import com.ep.domain.pojo.po.EpOrganAccountPo;
import com.ep.domain.pojo.po.EpOrganClassCatelogPo;
import com.ep.domain.repository.OrganAccountRepository;
import com.ep.domain.repository.OrganClassCatelogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 课程目录接口服务类
 * @Author: J.W
 * @Date: 下午5:54 2018/2/12
 */
@Service
public class OrganClassCatelogService {

    @Autowired
    private OrganClassCatelogRepository organClassCatelogRepository;
    @Autowired
    private OrganAccountRepository organAccountRepository;

    /**
     * 课时评价初始化
     *
     * @param mobile
     * @param classCatelogId
     * @return
     */
    public ResultDo<OrganClassCatelogCommentDto> getClassCatelogCommentView(Long mobile, Long classCatelogId) {
        ResultDo<OrganClassCatelogCommentDto> resultDo = ResultDo.build();
        // 前置校验
        List<EpOrganAccountPo> accountList = organAccountRepository.getByMobile(mobile);
        if (CollectionsTools.isEmpty(accountList)) {
            return resultDo.setError(MessageCode.ERROR_ORGAN_ACCOUNT_NOT_EXISTS);
        }
        EpOrganAccountPo accountPo = accountList.get(BizConstant.DB_NUM_ZERO);
        return resultDo;
    }

    public List<EpOrganClassCatelogPo> findByCourseId(Long courseId){
        return organClassCatelogRepository.findByCourseId(courseId);
    }

    public List<EpOrganClassCatelogPo> findByClassId(Long classId){
        return organClassCatelogRepository.findByClassId(classId);
    }
}
