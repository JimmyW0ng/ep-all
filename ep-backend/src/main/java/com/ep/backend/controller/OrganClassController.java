package com.ep.backend.controller;

import com.ep.common.component.SpringComponent;
import com.ep.common.tool.StringTools;
import com.ep.domain.constant.MessageCode;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.OrganClassBo;
import com.ep.domain.pojo.po.EpOrganClassPo;
import com.ep.domain.pojo.po.EpSystemUserPo;
import com.ep.domain.repository.domain.enums.EpOrganClassStatus;
import com.ep.domain.service.OrderService;
import com.ep.domain.service.OrganClassService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.jooq.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import static com.ep.domain.repository.domain.Tables.EP_ORGAN_CLASS;

/**
 * @Description: 开班控制器
 * @Author: CC.F
 * @Date: 9:30 2018/3/2
 */
@Slf4j
@Controller
@RequestMapping("auth/organClass")
public class OrganClassController extends BackendController {
    @Autowired
    private OrganClassService organClassService;
    @Autowired
    private OrderService orderService;

    @GetMapping("index")
    @PreAuthorize("hasAnyAuthority('merchant:organClass:index')")
    public String index(Model model,
                        @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                        @RequestParam(value = "className", required = false) String className,
                        @RequestParam(value = "status", required = false) String status,
                        @RequestParam(value = "crStartTime", required = false) Timestamp crStartTime,
                        @RequestParam(value = "crEndTime", required = false) Timestamp crEndTime

    ) {
        Map map = Maps.newHashMap();
        Collection<Condition> conditions = Lists.newArrayList();

        if (StringTools.isNotBlank(className)) {
            conditions.add(EP_ORGAN_CLASS.CLASS_NAME.like("%" + className + "%"));
        }
        map.put("className", className);
        if (StringTools.isNotBlank(status)) {
            conditions.add(EP_ORGAN_CLASS.STATUS.eq(EpOrganClassStatus.valueOf(status)));
        }
        map.put("status", status);

        if (null != crStartTime) {
            conditions.add(EP_ORGAN_CLASS.CREATE_AT.greaterOrEqual(crStartTime));
        }
        map.put("crStartTime", crStartTime);
        if (null != crEndTime) {
            conditions.add(EP_ORGAN_CLASS.CREATE_AT.lessOrEqual(crEndTime));
        }
        map.put("crEndTime", crEndTime);
        Long ognId = super.getCurrentUser().get().getOgnId();
        conditions.add(EP_ORGAN_CLASS.OGN_ID.eq(ognId));
        Page<OrganClassBo> page = organClassService.findbyPageAndCondition(pageable, conditions);
        model.addAttribute("page", page);
        model.addAttribute("map", map);
        return "organClass/index";
    }

    /**
     * 开班
     *
     * @param id
     * @return
     */
    @GetMapping("opening/{id}")
    @PreAuthorize("hasAnyAuthority('merchant:organClass:index')")
    @ResponseBody
    public ResultDo opening(@PathVariable(value = "id") Long id) {
        EpSystemUserPo userPo = super.getCurrentUser().get();
        return organClassService.openById(userPo, id);
    }

    /**
     * 结束正常班次/提前结束预约班次
     *
     * @param id
     * @return
     */
    @GetMapping("end/{id}")
    @PreAuthorize("hasAnyAuthority('merchant:organClass:index')")
    @ResponseBody
    public ResultDo end(@PathVariable(value = "id") Long id) {
        EpSystemUserPo userPo = super.getCurrentUser().get();
        return organClassService.endById(userPo, id);
    }

    /**
     * 查看该班次订单
     *
     * @param id
     * @return
     */
    @GetMapping("findOrders/{id}")
    @PreAuthorize("hasAnyAuthority('merchant:organClass:index')")
    @ResponseBody
    public ResultDo findOrders(@PathVariable("id") Long id) {
        EpSystemUserPo userPo = super.getCurrentUser().get();
        return ResultDo.build().setResult(orderService.findOrdersByClassId(userPo.getOgnId(), id));
    }


    /**
     * 查看该班次成员
     *
     * @param classId
     * @return
     */
    @GetMapping("findClassChild/{classId}")
    @PreAuthorize("hasAnyAuthority('merchant:organClass:index')")
    @ResponseBody
    public ResultDo findClassChild(@PathVariable("classId") Long classId) {
        EpSystemUserPo userPo = super.getCurrentUser().get();
        return ResultDo.build().setResult(organClassService.findClassChildNickName(userPo.getOgnId(), classId));
    }

    /**
     * 根据classId统计正常班次下未结束的班次目录
     *
     * @param classId
     * @return
     */
    @GetMapping("countUnendNormalClassCatalogByClassId/{classId}")
    @PreAuthorize("hasAnyAuthority('merchant:organClass:index')")
    @ResponseBody
    public ResultDo countUnendNormalClassCatalogByClassId(@PathVariable("classId") Long classId) {
        if (null == this.innerOgnOrPlatformReq(classId, super.getCurrentUserOgnId())) {
            return ResultDo.build(MessageCode.ERROR_ILLEGAL_RESOURCE);
        }
        return ResultDo.build().setResult(organClassService.countUnendNormalClassCatalogByClassId(classId));
    }

    /**
     * 根据classId统计预约班次下未结束的预约
     *
     * @param classId
     * @return
     */
    @GetMapping("countUnendBesprakClassScheduleByClassId/{classId}")
    @PreAuthorize("hasAnyAuthority('merchant:organClass:index')")
    @ResponseBody
    public ResultDo countUnendBesprakClassScheduleByClassId(@PathVariable("classId") Long classId) {
        if (null == this.innerOgnOrPlatformReq(classId, super.getCurrentUserOgnId())) {
            return ResultDo.build(MessageCode.ERROR_ILLEGAL_RESOURCE);
        }
        return ResultDo.build().setResult(organClassService.countUnendBesprakClassScheduleByClassId(classId));
    }

    /**
     * 校验业务对象是否属于该机构，是：返回po;否：返回null
     *
     * @param sourceId
     * @param ognId
     * @return
     */
    private EpOrganClassPo innerOgnOrPlatformReq(Long sourceId, Long ognId) {
        if (sourceId == null) {
            return null;
        }
        Optional<EpOrganClassPo> optional = organClassService.findById(sourceId);
        if (!optional.isPresent()) {
            return null;
        }
        if (ognId == null) {
            return optional.get();
        }
        if (optional.get().getOgnId().equals(ognId)) {
            return optional.get();
        } else {
            log.error(SpringComponent.messageSource("ERROR_ILLEGAL_RESOURCE"));
            return null;
        }
    }

}
