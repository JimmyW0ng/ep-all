package com.ep.backend.controller;

import com.ep.common.component.SpringComponent;
import com.ep.common.tool.StringTools;
import com.ep.domain.constant.MessageCode;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.OrganClassCommentBo;
import com.ep.domain.pojo.po.EpOrganClassCommentPo;
import com.ep.domain.service.OrganClassCommentService;
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

import static com.ep.domain.repository.domain.Tables.*;

/**
 * @Description: 班次评价控制器
 * @Author: CC.F
 * @Date: 0:47 2018/3/18
 */
@Slf4j
@Controller
@RequestMapping("auth/classComment")
public class OrganClassCommentController extends BackendController {
    @Autowired
    private OrganClassCommentService organClassCommentService;

    @GetMapping("index")
    @PreAuthorize("hasAnyAuthority('merchant:classComment:index')")
    public String index(Model model, @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                        @RequestParam(value = "ognName", required = false) String ognName,
                        @RequestParam(value = "courseName", required = false) String courseName,
                        @RequestParam(value = "className", required = false) String className,
                        @RequestParam(value = "crStartTime", required = false) Timestamp crStartTime,
                        @RequestParam(value = "crEndTime", required = false) Timestamp crEndTime
    ) {
        Map<String, Object> searchMap = Maps.newHashMap();
        Collection<Condition> conditions = Lists.newArrayList();
        if (StringTools.isNotBlank(ognName)) {
            conditions.add(EP_ORGAN.OGN_NAME.like("%" + ognName + "%"));
        }
        searchMap.put("ognName", ognName);
        if (StringTools.isNotBlank(courseName)) {
            conditions.add(EP_ORGAN_COURSE.COURSE_NAME.like("%" + courseName + "%"));
        }
        searchMap.put("courseName", courseName);
        if (StringTools.isNotBlank(className)) {
            conditions.add(EP_ORGAN_CLASS.CLASS_NAME.like("%" + className + "%"));
        }
        searchMap.put("className", className);
        if (null != crStartTime) {
            conditions.add(EP_ORGAN_CLASS_COMMENT.CREATE_AT.greaterOrEqual(crStartTime));
        }
        searchMap.put("crStartTime", crStartTime);
        if (null != crEndTime) {
            conditions.add(EP_ORGAN_CLASS_COMMENT.CREATE_AT.lessOrEqual(crEndTime));
        }
        searchMap.put("crEndTime", crEndTime);
        conditions.add(EP_ORGAN_CLASS_COMMENT.DEL_FLAG.eq(false));
        Long ognId = super.getCurrentUser().get().getOgnId();
        conditions.add(EP_ORGAN_CLASS_COMMENT.OGN_ID.eq(ognId));
        Page<OrganClassCommentBo> page = organClassCommentService.findbyPageAndCondition(pageable, conditions);
        model.addAttribute("page", page);
        model.addAttribute("searchMap", searchMap);
        return "classComment/index";
    }

    /**
     * 评论设为精选
     *
     * @param id
     * @return
     */
    @GetMapping("chosen/{id}")
    @PreAuthorize("hasAnyAuthority('merchant:classComment:index')")
    @ResponseBody
    public ResultDo chosen(@PathVariable("id") Long id) {
        if (null == this.innerOgnOrPlatformReq(id, super.getCurrentUserOgnId())) {
            return ResultDo.build(MessageCode.ERROR_ILLEGAL_RESOURCE);
        }
        return organClassCommentService.chosen(id);
    }

    /**
     * 评论取消精选
     *
     * @param id
     * @return
     */
    @GetMapping("unchosen/{id}")
    @PreAuthorize("hasAnyAuthority('merchant:classComment:index')")
    @ResponseBody
    public ResultDo unchosen(@PathVariable("id") Long id) {
        if (null == this.innerOgnOrPlatformReq(id, super.getCurrentUserOgnId())) {
            return ResultDo.build(MessageCode.ERROR_ILLEGAL_RESOURCE);
        }
        return organClassCommentService.unchosen(id);
    }

    /**
     * 校验业务对象是否属于该机构，是：返回po;否：返回null
     *
     * @param sourceId
     * @param ognId
     * @return
     */
    private EpOrganClassCommentPo innerOgnOrPlatformReq(Long sourceId, Long ognId) {
        if (sourceId == null) {
            return null;
        }
        Optional<EpOrganClassCommentPo> optional = organClassCommentService.findById(sourceId);
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
