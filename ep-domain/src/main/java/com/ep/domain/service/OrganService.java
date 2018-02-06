package com.ep.domain.service;

import com.ep.domain.constant.BizConstant;
import com.ep.domain.constant.MessageCode;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.OrganBo;
import com.ep.domain.pojo.dto.OrganInfoDto;
import com.ep.domain.pojo.po.EpFilePo;
import com.ep.domain.pojo.po.EpOrganPo;
import com.ep.domain.repository.FileRepository;
import com.ep.domain.repository.OrganCourseRepository;
import com.ep.domain.repository.OrganRepository;
import com.ep.domain.repository.domain.enums.EpOrganStatus;
import org.jooq.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @Description: 机构服务类
 * @Author: J.W
 * @Date: 上午9:30 2018/1/14
 */
@Service
public class OrganService {

    @Autowired
    private OrganRepository organRepository;
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private OrganCourseRepository ognCourseRepository;

    /**
     * 机构详情(基本信息＋banner列表＋课程列表)
     *
     * @param id
     * @return
     */
    public ResultDo<OrganInfoDto> getOgnDetail(Long id) {
        ResultDo<OrganInfoDto> resultDo = ResultDo.build();
        // 机构详情
        Optional<EpOrganPo> ognInfoPojo = this.getById(id);
        if (!ognInfoPojo.isPresent() || !EpOrganStatus.normal.equals(ognInfoPojo.get().getStatus())) {
            return ResultDo.build(MessageCode.ERROR_DATA_MISS);
        }
        // 机构主图
        Optional<EpFilePo> mainPicOpt = fileRepository.getOneByBizTypeAndSourceId(BizConstant.FILE_BIZ_TYPE_CODE_ORGAN_MAIN_PIC, id);
        String mainPic = mainPicOpt.isPresent() ? mainPicOpt.get().getFileUrl() : null;
        // 机构Logo
        Optional<EpFilePo> logoOpt = fileRepository.getOneByBizTypeAndSourceId(BizConstant.FILE_BIZ_TYPE_CODE_ORGAN_LOGO, id);
        String logo = logoOpt.isPresent() ? logoOpt.get().getFileUrl() : null;
        OrganInfoDto ognInfoDto = new OrganInfoDto(ognInfoPojo.get(), mainPic, logo);
        return resultDo.setResult(ognInfoDto);
    }

    /**
     * 根据主键获取机构信息
     *
     * @param id
     * @return
     */
    public Optional<EpOrganPo> getById(Long id) {
        return Optional.ofNullable(organRepository.getById(id));
    }

    /**
     * 分页查询机构列表
     *
     * @param pageable
     * @return
     */
    public Page<OrganBo> queryOgnPage(Pageable pageable) {
        return organRepository.queryOgnPage(pageable);
    }


    public Page<EpOrganPo> findByPageAndCondition(Pageable pageable, Collection<? extends Condition> condition) {
        return organRepository.findByPageable(pageable, condition);
    }

    /**
     * 系统后台新增商家
     * @param po
     * @return
     */
    public EpOrganPo insertSystemOrgan(EpOrganPo po){
        return organRepository.insertNew(po);
    }

    /**
     * 系统后台修改商家
     * @param po
     * @return
     */
    public int updateSystemOrgan(EpOrganPo po){
        return organRepository.updateSystemOrgan(po);
    }

    /**
     * 删除商家
     * @param id
     */
    public void delete(Long id){
        organRepository.deleteLogical(id);
    }

    /**
     *
     * @param status
     * @return
     */
    public List<EpOrganPo> getByStatus(EpOrganStatus status){
        return organRepository.getByStatus(status);
    }
}
