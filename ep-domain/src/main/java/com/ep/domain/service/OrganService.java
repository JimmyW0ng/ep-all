package com.ep.domain.service;

import com.ep.common.tool.CollectionsTools;
import com.ep.domain.constant.BizConstant;
import com.ep.domain.constant.MessageCode;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.OrganBo;
import com.ep.domain.pojo.dto.OrganInfoDto;
import com.ep.domain.pojo.po.EpFilePo;
import com.ep.domain.pojo.po.EpOrganPo;
import com.ep.domain.repository.FileRepository;
import com.ep.domain.repository.OrderRepository;
import com.ep.domain.repository.OrganRepository;
import com.ep.domain.repository.domain.enums.EpOrganStatus;
import org.jooq.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private OrderRepository orderRepository;

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
        if (!ognInfoPojo.isPresent() || !EpOrganStatus.online.equals(ognInfoPojo.get().getStatus())) {
            return ResultDo.build(MessageCode.ERROR_DATA_MISS);
        }
        // 机构主图
        Optional<EpFilePo> mainPicOpt = fileRepository.getOneByBizTypeAndSourceId(BizConstant.FILE_BIZ_TYPE_CODE_ORGAN_MAIN_PIC, id);
        String mainPic = mainPicOpt.isPresent() ? mainPicOpt.get().getFileUrl() : null;
        // 机构Logo
        Optional<EpFilePo> logoOpt = fileRepository.getOneByBizTypeAndSourceId(BizConstant.FILE_BIZ_TYPE_CODE_ORGAN_LOGO, id);
        String logo = logoOpt.isPresent() ? logoOpt.get().getFileUrl() : null;
        // 总评论数
        Long totalCommentNum = orderRepository.countByOgnId(id);
        OrganInfoDto ognInfoDto = new OrganInfoDto(ognInfoPojo.get(), logo, mainPic, totalCommentNum);
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
    public ResultDo<Page<OrganBo>> queryOrganForPage(Pageable pageable) {
        Page<OrganBo> page = organRepository.queryOrganForPage(pageable);
        List<OrganBo> data = page.getContent();
        if (CollectionsTools.isNotEmpty(data)) {
            for (OrganBo organ : data) {
                Optional<EpFilePo> organMainPic = fileRepository.getOneByBizTypeAndSourceId(BizConstant.FILE_BIZ_TYPE_CODE_ORGAN_MAIN_PIC, organ.getId());
                String mainPic = organMainPic.isPresent() ? organMainPic.get().getFileUrl() : null;
                organ.setFileUrl(mainPic);
            }
        }
        ResultDo<Page<OrganBo>> resultDo = ResultDo.build();
        resultDo.setResult(page);
        return resultDo;
    }


    public Page<EpOrganPo> findByPageAndCondition(Pageable pageable, Collection<? extends Condition> condition) {
        return organRepository.findByPageable(pageable, condition);
    }

    /**
     * 系统后台新增商家
     * @param po
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void createSystemOrgan(EpOrganPo po,String mainpicUrlPreCode,String logoUrlPreCode){
        EpOrganPo insertPo = organRepository.insertNew(po);
        fileRepository.updateSourceIdByPreCode(mainpicUrlPreCode,insertPo.getId());
        fileRepository.updateSourceIdByPreCode(logoUrlPreCode,insertPo.getId());
    }

    /**
     * 系统后台修改商家
     * @param po
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateSystemOrgan(EpOrganPo po,String mainpicUrlPreCode,String logoUrlPreCode){
        organRepository.updateSystemOrgan(po);
        fileRepository.updateSourceIdByPreCode(mainpicUrlPreCode,po.getId());
        fileRepository.updateSourceIdByPreCode(logoUrlPreCode,po.getId());
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

    public void offlineById(Long id){
        organRepository.offlineById(id);
    }

    public void onlineById(Long id){
        organRepository.onlineById(id);
    }

}
