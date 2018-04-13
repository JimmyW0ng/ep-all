package com.ep.domain.service;

import com.ep.domain.constant.BizConstant;
import com.ep.domain.constant.MessageCode;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.po.EpConstantCatalogPo;
import com.ep.domain.repository.ConstantCatalogRepository;
import com.ep.domain.repository.OrganCourseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @Description:课程类目服务
 * @Author: CC.F
 * @Date: 21:59 2018/2/5
 */
@Slf4j
@Service
public class ConstantCatalogService {
    @Autowired
    private ConstantCatalogRepository constantCatalogRepository;
    @Autowired
    private OrganCourseRepository organCourseRepository;

    public List<EpConstantCatalogPo> getAll(){
        return constantCatalogRepository.getAll();
    }

    /**
     * 新增产品类目
     *
     * @param po
     * @return
     */
    public ResultDo createConstantCatalog(EpConstantCatalogPo po) {
        log.info("[课程类目]，新增课程类目开始，po={}。", po);
        if (po.getId() != null || po.getParentId() == null || po.getLabel() == null) {
            log.error("[课程类目]，新增课程类目失败，接受参数异常。");
            return ResultDo.build(MessageCode.ERROR_SYSTEM_PARAM_FORMAT);
        }
        constantCatalogRepository.insert(po);
        log.info("[课程类目]，新增课程类目成功，id={}。", po.getId());
        return ResultDo.build();
    }

    /**
     * 修改产品类目
     *
     * @param po
     * @return
     */
    public ResultDo updateConstantCatalog(EpConstantCatalogPo po) {
        log.info("[课程类目]，修改课程类目开始，po={}。", po);
        if (po.getId() == null || po.getParentId() == null || po.getLabel() == null) {
            log.error("[课程类目]，修改课程类目失败，接受参数异常。");
            return ResultDo.build(MessageCode.ERROR_SYSTEM_PARAM_FORMAT);
        }
        if (constantCatalogRepository.updateConstantCatalogPo(po) == BizConstant.DB_NUM_ONE) {
            log.info("[课程类目]，修改课程类目成功，id={}。", po.getId());
            return ResultDo.build();
        } else {
            log.error("[课程类目]，修改课程类目失败，id={}。", po.getId());
            return ResultDo.build(MessageCode.ERROR_OPERATE_FAIL);
        }
    }


    /**
     * 删除课程类目
     *
     * @param ids
     * @return
     */
    public ResultDo delete(Long[] ids) {
        log.info("[课程类目]，删除课程类目开始，ids={}。", ids);
        if (organCourseRepository.countByCatalogIds(ids) > BizConstant.DB_NUM_ZERO) {
            log.info("[课程类目]，删除课程类目失败，存在课程类目正在使用中。", ids);
            return ResultDo.build(MessageCode.ERROR_CONSTANT_CATALOG_DELETE_WHEN_USED);
        }
        if (constantCatalogRepository.deleteByIds(ids) == ids.length) {
            log.info("[课程类目]，删除课程类目成功，ids={}。", ids);
            return ResultDo.build();
        } else {
            log.info("[课程类目]，删除课程类目失败，ids={}。", ids);
            return ResultDo.build(MessageCode.ERROR_OPERATE_FAIL);
        }
    }

    /**
     * 获得二级目录
     *
     * @return
     */
    public List<EpConstantCatalogPo> findSecondCatalog(){
        return constantCatalogRepository.findSecondCatalog();
    }

    /**
     * 获得一级目录下拉框
     *
     * @return
     */
    public List<EpConstantCatalogPo> findFirstCatalogSelectModel() {
        return constantCatalogRepository.findFirstCatalogSelectModel();
    }

    /**
     * 根据父级id获取二级目录下拉框
     *
     * @param pid
     * @return
     */
    public List<EpConstantCatalogPo> findSecondCatalogSelectModelByPid(Long pid) {
        return constantCatalogRepository.findSecondCatalogSelectModelByPid(pid);
    }

    /**
     * 根据id获取po
     *
     * @param id
     * @return
     */
    public Optional<EpConstantCatalogPo> findById(Long id) {
        return constantCatalogRepository.findById(id);
    }

}
