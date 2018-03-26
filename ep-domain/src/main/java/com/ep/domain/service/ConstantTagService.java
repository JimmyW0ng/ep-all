package com.ep.domain.service;

import com.ep.common.tool.StringTools;
import com.ep.domain.constant.BizConstant;
import com.ep.domain.constant.MessageCode;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.ConstantTagBo;
import com.ep.domain.pojo.po.EpConstantTagPo;
import com.ep.domain.repository.ConstantTagRepository;
import com.ep.domain.repository.OrganCourseTagRepository;
import lombok.extern.slf4j.Slf4j;
import org.jooq.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * @Description: 标签服务类
 * @Author: CC.F
 * @Date: 11:22 2018/2/9
 */
@Slf4j
@Service
public class ConstantTagService {
    @Autowired
    private ConstantTagRepository constantTagRepository;
    @Autowired
    private OrganCourseTagRepository organCourseTagRepository;

    /**
     * 根据课程类目id和机构id获取标签
     *
     * @param catalogId
     * @param ognId
     * @return
     */
    public List<EpConstantTagPo> findByCatalogIdAndOgnId(Long catalogId, Long ognId) {
        return constantTagRepository.findByCatalogIdAndOgnId(catalogId, ognId);
    }

    /**
     * 根据课程类目id和机构id获取标签Bo
     *
     * @param catalogId
     * @param ognId
     * @return
     */
    public List<ConstantTagBo> findBosByCatalogIdAndOgnId(Long catalogId, Long ognId) {
        return constantTagRepository.findBosByCatalogIdAndOgnId(catalogId, ognId);
    }

    /**
     * 根据机构id获取标签
     *
     * @param ognId
     * @return
     */
    public List<EpConstantTagPo> findByOgnId(Long ognId) {
        return constantTagRepository.findByOgnId(ognId);
    }

    /**
     * 新增EpConstantTagPo
     *
     * @param po
     */
    public ResultDo createConstantTag(EpConstantTagPo po) {
        log.info("[标签]新增标签开始，标签对象={}。", po);
        if (StringTools.isBlank(po.getTagName())) {
            log.error("[标签]新增标签失败，请求参数异常。");
            ResultDo.build(MessageCode.ERROR_OPERATE_FAIL);
        }
        constantTagRepository.insert(po);
        log.info("[标签]新增标签成功，id={}。", po.getId());
        return ResultDo.build().setResult(po.getTagName());
    }

    /**
     * 根据id逻辑删除
     *
     * @param id
     */
    public ResultDo deleteById(Long id) {
        log.info("[标签]删除标签开始，id={}。", id);
        if (organCourseTagRepository.constantTagIsUesd(id)) {
            log.error("[标签]删除标签失败，标签正在被使用。");
            return ResultDo.build(MessageCode.ERROR_CONSTANT_TAG_DELETE_WHEN_USED);
        }
        if (constantTagRepository.deleteById(id) == BizConstant.DB_NUM_ONE) {
            log.info("[标签]删除标签成功，id={}。", id);
            return ResultDo.build();
        } else {
            log.error("[标签]删除标签失败，id={}。", id);
            return ResultDo.build(MessageCode.ERROR_OPERATE_FAIL);
        }
    }

    /**
     * 分页
     *
     * @param pageable
     * @param conditions
     * @return
     */
    public Page<ConstantTagBo> findbyPageAndCondition(Pageable pageable, Collection<? extends Condition> conditions) {
        return constantTagRepository.findbyPageAndCondition(pageable, conditions);
    }
}
