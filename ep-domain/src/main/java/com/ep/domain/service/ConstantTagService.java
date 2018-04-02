package com.ep.domain.service;

import com.ep.common.tool.StringTools;
import com.ep.domain.constant.BizConstant;
import com.ep.domain.constant.MessageCode;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.ConstantTagBo;
import com.ep.domain.pojo.po.EpConstantTagPo;
import com.ep.domain.repository.ConstantTagRepository;
import com.ep.domain.repository.OrganCourseTagRepository;
import com.ep.domain.repository.domain.enums.EpConstantTagStatus;
import lombok.extern.slf4j.Slf4j;
import org.jooq.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

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
     * 根据状态和机构id获取标签
     *
     * @param ognId
     * @return
     */
    public List<EpConstantTagPo> findByOgnIdAndStatus(Long ognId, EpConstantTagStatus[] status) {
        return constantTagRepository.findByOgnIdAndStatus(ognId, status);
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
     * 根据id获取标签
     *
     * @param ognId
     * @return
     */
    public Optional<EpConstantTagPo> findById(Long id, Long ognId) {
        return constantTagRepository.findById(id, ognId);
    }

    /**
     * 新增EpConstantTagPo
     *
     * @param po
     */
    public ResultDo createConstantTag(EpConstantTagPo po) {
        log.info("[标签]新增标签开始，标签对象={}。", po);
        if (StringTools.isBlank(po.getTagName()) || po.getSort() == null || po.getTagLevel() == null) {
            log.error("[标签]新增标签失败，请求参数异常。");
            ResultDo.build(MessageCode.ERROR_OPERATE_FAIL);
        }
        po.setStatus(EpConstantTagStatus.save);
        constantTagRepository.insert(po);
        log.info("[标签]新增标签成功，id={}。", po.getId());
        return ResultDo.build();
    }


    /**
     * 修改EpConstantTagPo
     *
     * @param po
     */
    public ResultDo updateConstantTag(EpConstantTagPo po) {
        log.info("[标签]修改标签开始，标签对象={}。", po);
        if (StringTools.isBlank(po.getTagName()) || po.getSort() == null || po.getTagLevel() == null || po.getId() == null) {
            log.error("[标签]修改标签失败，请求参数异常。");
            ResultDo.build(MessageCode.ERROR_OPERATE_FAIL);
        }
        if (constantTagRepository.updatePo(po) == BizConstant.DB_NUM_ONE) {
            log.info("[标签]修改标签成功，id={}。", po.getId());
            return ResultDo.build();
        } else {
            log.info("[标签]修改标签失败，id={}。", po.getId());
            return ResultDo.build(MessageCode.ERROR_OPERATE_FAIL);
        }
    }

    /**
     * 根据id逻辑删除
     *
     * @param id
     */
    public ResultDo deleteById(Long id, Long ognId) {
        log.info("[标签]删除标签开始，id={}。", id);

        if (constantTagRepository.deleteById(id, ognId) == BizConstant.DB_NUM_ONE) {
            log.info("[标签]删除标签成功，id={}。", id);
            return ResultDo.build();
        } else {
            log.error("[标签]删除标签失败，id={}。", id);
            return ResultDo.build(MessageCode.ERROR_OPERATE_FAIL);
        }
    }

    /**
     * 机构标签分页
     *
     * @param pageable
     * @param conditions
     * @return
     */
    public Page<ConstantTagBo> findOgnTagbyPageAndCondition(Pageable pageable, Collection<? extends Condition> conditions) {
        return constantTagRepository.findbyPageAndCondition(pageable, conditions);
    }

    /**
     * 机构获取公用标签分页
     *
     * @param pageable
     * @param conditions
     * @return
     */
    public Page<EpConstantTagPo> findConstantTagbyPageAndConditionForOgn(Pageable pageable, Collection<? extends Condition> conditions) {
        return constantTagRepository.findByPageable(pageable, conditions);
    }

    /**
     * 后台公用标签分页
     *
     * @param pageable
     * @param conditions
     * @return
     */
    public Page<ConstantTagBo> findConstantTagbyPageAndConditionForBackend(Pageable pageable, Collection<? extends Condition> conditions) {
        return constantTagRepository.findbyPageAndCondition(pageable, conditions);
    }

    /**
     * 标签上线
     *
     * @param id
     * @param ognId
     * @return
     */
    public ResultDo onlineById(Long id, Long ognId) {
        log.info("[标签]标签上线开始，标签id={},机构id={}。", id, ognId);
        if (constantTagRepository.onlineById(id, ognId) == BizConstant.DB_NUM_ONE) {
            log.info("[标签]标签上线成功，标签id={},机构id={}。", id, ognId);
            return ResultDo.build();
        } else {
            log.error("[标签]标签上线失败，标签id={},机构id={}。", id, ognId);
            return ResultDo.build(MessageCode.ERROR_OPERATE_FAIL);
        }
    }

    /**
     * 标签下线
     *
     * @param id
     * @param ognId
     * @return
     */
    public ResultDo offlineById(Long id, Long ognId) {
        log.info("[标签]标签下线开始，标签id={},机构id={}。", id, ognId);
        if (constantTagRepository.offlineById(id, ognId) == BizConstant.DB_NUM_ONE) {
            log.info("[标签]标签下线成功，标签id={},机构id={}。", id, ognId);
            return ResultDo.build();
        } else {
            log.error("[标签]标签下线失败，标签id={},机构id={}。", id, ognId);
            return ResultDo.build(MessageCode.ERROR_OPERATE_FAIL);
        }
    }
}
