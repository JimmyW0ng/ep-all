package com.ep.domain.service;

import com.ep.domain.constant.MessageCode;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.OrganAccountClassBo;
import com.ep.domain.pojo.bo.RectifyOrganClassCatalogBo;
import com.ep.domain.pojo.po.EpOrganAccountPo;
import com.ep.domain.pojo.po.EpOrganClassCatalogPo;
import com.ep.domain.pojo.po.EpOrganClassPo;
import com.ep.domain.repository.OrganClassCatalogRepository;
import com.ep.domain.repository.OrganClassRepository;
import com.ep.domain.repository.OrganClassScheduleRepository;
import com.ep.domain.repository.domain.enums.EpOrganClassStatus;
import com.ep.domain.repository.domain.enums.EpOrganClassType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 课程目录接口服务类
 * @Author: J.W
 * @Date: 下午5:54 2018/2/12
 */
@Slf4j
@Service
public class OrganClassCatalogService {

    @Autowired
    private OrganClassCatalogRepository organClassCatalogRepository;
    @Autowired
    private OrganClassRepository organClassRepository;
    @Autowired
    private OrganClassScheduleRepository organClassScheduleRepository;

    /**
     * 根据班次获取
     *
     * @param classId
     * @return
     */
    public List<EpOrganClassCatalogPo> findByClassId(Long classId){
        return organClassCatalogRepository.findByClassId(classId);
    }

    /**
     * 根据班次获取
     *
     * @param classId
     * @return
     */
    public List<RectifyOrganClassCatalogBo> findRectifyBoByClassId(Long classId) {
        return organClassCatalogRepository.findRectifyBoByClassId(classId);
    }

    /**
     * 查看班次全部课时
     *
     * @param classId
     * @param organAccountPo
     * @return
     */
    public ResultDo<List<OrganAccountClassBo>> getNomalClassAllCatalog(Long classId, EpOrganAccountPo organAccountPo) {
        ResultDo<List<OrganAccountClassBo>> resultDo = ResultDo.build();
        // 校验课程
        EpOrganClassPo classPo = organClassRepository.getById(classId);
        if (classPo == null || classPo.getDelFlag() || !EpOrganClassType.normal.equals(classPo.getType())) {
            log.error("班次不存在, classId={}", classId);
            return resultDo.setError(MessageCode.ERROR_CLASS_NOT_EXIST);
        }
        if (classPo.getStatus().equals(EpOrganClassStatus.save)) {
            log.error("课程未上线, classId={}, status={}", classId, classPo.getStatus().getName());
            return resultDo.setError(MessageCode.ERROR_COURSE_NOT_ONLINE);
        }
        // 校验班次负责人
        if (!organAccountPo.getId().equals(classPo.getOgnAccountId())) {
            log.error("当前机构账户不是班次负责人, accountId={}, classId={}", organAccountPo.getId(), classId);
            return resultDo.setError(MessageCode.ERROR_ORGAN_ACCOUNT_NOT_MATCH_CLASS);
        }
        List<OrganAccountClassBo> classCatalogs = organClassScheduleRepository.findNomalClassScheduleByClassId(classId);
        return resultDo.setResult(classCatalogs);
    }
}
