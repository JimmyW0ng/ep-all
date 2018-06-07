package com.ep.domain.service;

import com.ep.common.tool.StringTools;
import com.ep.domain.constant.BizConstant;
import com.ep.domain.constant.MessageCode;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.po.EpSystemDictPo;
import com.ep.domain.repository.SystemDictRepository;
import lombok.extern.slf4j.Slf4j;
import org.jooq.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

/**
 * @Description: 字典服务
 * @Author: CC.F
 * @Date: 20:18 2018/6/5
 */
@Slf4j
@Service
public class SystemDictService {
    @Autowired
    private SystemDictRepository systemDictRepository;

    public Page<EpSystemDictPo> findbyPageAndCondition(Pageable pageable, Collection<? extends Condition> condition) {
        return systemDictRepository.findByPageable(pageable, condition);
    }

    public Optional<EpSystemDictPo> findById(Long id) {
        return systemDictRepository.findById(id);
    }

    /**
     * 新增字典
     *
     * @param po
     * @return
     */
    public ResultDo createDict(EpSystemDictPo po) {
        log.info("[字典]新增字典开始，po={}。", po);
        if (!checkPoParams(po) || po.getId() != null) {
            return ResultDo.build(MessageCode.ERROR_SYSTEM_PARAM_FORMAT);
        }
        EpSystemDictPo insertPo = new EpSystemDictPo();
        insertPo.setLabel(po.getLabel());
        insertPo.setGroupName(po.getGroupName());
        insertPo.setKey(po.getKey());
        insertPo.setValue(po.getValue());
        insertPo.setSort(po.getSort());
        insertPo.setDescription(StringTools.isBlank(po.getDescription()) ? null : po.getDescription());
        insertPo.setStatus(po.getStatus());
        systemDictRepository.insert(insertPo);
        log.info("[字典]新增字典成功，id={}。", insertPo.getId());
        return ResultDo.build();
    }

    /**
     * 修改字典
     *
     * @param po
     * @return
     */
    public ResultDo updateDict(EpSystemDictPo po) {
        log.info("[字典]修改字典开始，po={}。", po);
        if (!checkPoParams(po)) {
            return ResultDo.build(MessageCode.ERROR_SYSTEM_PARAM_FORMAT);
        }
        if (systemDictRepository.updateById(po) == BizConstant.DB_NUM_ONE) {
            log.info("[字典]修改字典成功，id={}。", po.getId());
            return ResultDo.build();
        }
        log.info("[字典]修改字典失败，id={}。", po.getId());
        return ResultDo.build(MessageCode.ERROR_OPERATE_FAIL);
    }

    /**
     * 删除字典
     *
     * @param id
     * @return
     */
    public ResultDo deleteDict(Long id) {
        log.info("[字典]删除字典开始，id={}。", id);
        if (systemDictRepository.deleteLogical(id) == BizConstant.DB_NUM_ONE) {
            log.info("[字典]修改字典成功，id={}。", id);
            return ResultDo.build();
        }
        log.info("[字典]删除字典失败，id={}。", id);
        return ResultDo.build(MessageCode.ERROR_OPERATE_FAIL);
    }

    /**
     * 校验po对象属性
     *
     * @param po
     * @return
     */
    private boolean checkPoParams(EpSystemDictPo po) {
        if (null == po.getLabel()) {
            log.error("接受参数异常，label=null。");
            return false;
        }
        if (null == po.getGroupName()) {
            log.error("接受参数异常，groupName=null。");
            return false;
        }
        if (null == po.getKey()) {
            log.error("接受参数异常，key=null。");
            return false;
        }
        if (null == po.getValue()) {
            log.error("接受参数异常，value=null。");
            return false;
        }
        if (null == po.getSort()) {
            log.error("接受参数异常，sort=null。");
            return false;
        }
        if (null == po.getStatus()) {
            log.error("接受参数异常，status=null。");
            return false;
        }
        return true;
    }
}
