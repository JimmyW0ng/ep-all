package com.ep.backend.controller;

import com.ep.common.component.SpringComponent;
import com.ep.common.tool.StringTools;
import com.ep.domain.constant.BizConstant;
import com.ep.domain.constant.MessageCode;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.OrganAccountBo;
import com.ep.domain.pojo.po.EpFilePo;
import com.ep.domain.pojo.po.EpOrganAccountPo;
import com.ep.domain.repository.domain.enums.EpOrganAccountStatus;
import com.ep.domain.service.FileService;
import com.ep.domain.service.OrganAccountService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.ApiOperation;
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
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import static com.ep.domain.repository.domain.Tables.EP_ORGAN;
import static com.ep.domain.repository.domain.Tables.EP_ORGAN_ACCOUNT;

/**
 * @Description: 机构账户关联信息控制器
 * @Author: CC.F
 * @Date: 9:42 2018/2/6
 */
@Controller
@Slf4j
@RequestMapping("auth/organAccount")
public class OrganAccountController extends BackendController {

    @Autowired
    private OrganAccountService organAccountService;
    @Autowired
    private FileService fileService;


    /**
     * 机构账户关联信息列表
     *
     * @return
     */
    @ApiOperation(value = "列表")
    @GetMapping("/index")
    @PreAuthorize("hasAnyAuthority({'merchant:organAccount:merchantIndex','platform:organAccount:index'})")
    public String index(Model model, @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                        @RequestParam(value = "mobile", required = false) String mobile,
                        @RequestParam(value = "ognName", required = false) String ognName,
                        @RequestParam(value = "status", required = false) String status,
                        @RequestParam(value = "crStartTime", required = false) Timestamp crStartTime,
                        @RequestParam(value = "crEndTime", required = false) Timestamp crEndTime
    ) {
        Map<String, Object> searchMap = Maps.newHashMap();
        Collection<Condition> conditions = Lists.newArrayList();
        if (StringTools.isNotBlank(mobile)) {
            conditions.add(EP_ORGAN_ACCOUNT.REFER_MOBILE.eq(Long.parseLong(mobile)));
        }
        searchMap.put("mobile", mobile);
        if (StringTools.isNotBlank(status)) {
            conditions.add(EP_ORGAN_ACCOUNT.STATUS.eq(EpOrganAccountStatus.valueOf(status)));
        }
        searchMap.put("status", status);

        if (null != crStartTime) {
            conditions.add(EP_ORGAN_ACCOUNT.CREATE_AT.greaterOrEqual(crStartTime));
        }
        searchMap.put("crStartTime", crStartTime);
        if (null != crEndTime) {
            conditions.add(EP_ORGAN_ACCOUNT.CREATE_AT.lessOrEqual(crEndTime));
        }
        searchMap.put("crEndTime", crEndTime);
        conditions.add(EP_ORGAN_ACCOUNT.DEL_FLAG.eq(false));
        Long ognId = super.getCurrentUser().get().getOgnId();
        Page<OrganAccountBo> page;
        if (null != ognId) {
            conditions.add(EP_ORGAN_ACCOUNT.OGN_ID.eq(ognId));
            page = organAccountService.merchantFindbyPageAndCondition(pageable, conditions);

        } else {
            if (StringTools.isNotBlank(ognName)) {
                conditions.add(EP_ORGAN.OGN_NAME.like("%" + ognName + "%"));
            }
            searchMap.put("ognName", ognName);
            page = organAccountService.platformFindbyPageAndCondition(pageable, conditions);
        }

        model.addAttribute("ognId", ognId);
        model.addAttribute("ognName", ognName);
        model.addAttribute("page", page);
        model.addAttribute("searchMap", searchMap);

        return "organAccount/index";
    }

    /**
     * 查看
     *
     * @return
     */
    @GetMapping("/view/{id}")
    @PreAuthorize("hasAnyAuthority({'merchant:organAccount:merchantIndex','platform:organAccount:index'})")
    public String view(Model model, @PathVariable("id") Long id) {
        EpOrganAccountPo organAccountPo = this.innerOgnOrPlatformReq(id, super.getCurrentUserOgnId());
        if (null == organAccountPo) {
            return "noresource";
        }
        model.addAttribute("organAccountPo", organAccountPo);
        //头像
        Optional<EpFilePo> avatarOptional = organAccountService.getTeacherAvatar(id);
        if (avatarOptional.isPresent()) {
            model.addAttribute("avatarImgUrl", avatarOptional.get().getFileUrl());
        }
        return "organAccount/view";
    }

    /**
     * 新增初始化
     *
     * @return
     */
    @GetMapping("/createInit")
    @PreAuthorize("hasAnyAuthority({'merchant:organAccount:merchantIndex','platform:organAccount:index'})")
    public String createInit(Model model) {

        model.addAttribute("organAccountPo", new EpOrganAccountPo());
        return "organAccount/form";
    }

    /**
     * 修改初始化
     *
     * @return
     */
    @ApiOperation(value = "修改初始化")
    @GetMapping("/updateInit/{id}")
    @PreAuthorize("hasAnyAuthority({'merchant:organAccount:merchantIndex','platform:organAccount:index'})")
    public String updateInit(Model model, @PathVariable("id") Long id) {
        EpOrganAccountPo organAccountPo = this.innerOgnOrPlatformReq(id, super.getCurrentUserOgnId());
        if (null == organAccountPo) {
            return "noresource";
        }
        model.addAttribute("organAccountPo", organAccountPo);
        //头像
        Optional<EpFilePo> avatarOptional = organAccountService.getTeacherAvatar(id);
        if (avatarOptional.isPresent()) {
            model.addAttribute("avatarImgUrl", avatarOptional.get().getFileUrl());
        }
        return "organAccount/form";
    }

    /**
     * 新增
     *
     * @return
     */
    @PostMapping("/create")
    @ResponseBody
    @PreAuthorize("hasAnyAuthority({'merchant:organAccount:merchantIndex','platform:organAccount:index'})")
    public ResultDo create(OrganAccountBo bo) {
        bo.setOgnId(super.getCurrentUserOgnId());
        return organAccountService.createOgnAccount(bo);
    }

    /**
     * 修改
     *
     * @return
     */
    @ApiOperation(value = "修改")
    @PostMapping("/update")
    @ResponseBody
    @PreAuthorize("hasAnyAuthority({'merchant:organAccount:merchantIndex','platform:organAccount:index'})")
    public ResultDo update(OrganAccountBo bo) {
        EpOrganAccountPo organAccountPo = this.innerOgnOrPlatformReq(bo.getId(), super.getCurrentUserOgnId());
        if (null == organAccountPo) {
            return ResultDo.build(MessageCode.ERROR_ILLEGAL_RESOURCE);
        }
        return organAccountService.updateOgnAccount(bo);
    }

    /**
     * 删除
     *
     * @return
     */
    @GetMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority({'merchant:organAccount:merchantIndex','platform:organAccount:index'})")
    @ResponseBody
    public ResultDo updateInit(@PathVariable("id") Long id) {
        EpOrganAccountPo organAccountPo = this.innerOgnOrPlatformReq(id, super.getCurrentUserOgnId());
        if (null == organAccountPo) {
            return ResultDo.build(MessageCode.ERROR_ILLEGAL_RESOURCE);
        }
        return organAccountService.deleteOgnAccount(id);
    }


    /**
     * 上传教师头像
     *
     * @param file
     * @return
     */
    @PostMapping("uploadAvatar")
    @PreAuthorize("hasAnyAuthority({'merchant:organAccount:merchantIndex','platform:organAccount:index'})")
    @ResponseBody
    public ResultDo uploadAvatar(@RequestParam("file") MultipartFile file) throws Exception {
        return fileService.addFileByBizType(file.getName(), file.getBytes(), BizConstant.FILE_BIZ_TYPE_CODE_TEACHER_AVATAR, null);
    }

    /**
     * 根据id注销
     *
     * @param id
     * @return
     */
    @GetMapping("cancel/{id}")
    @PreAuthorize("hasAnyAuthority({'merchant:organAccount:merchantIndex','platform:organAccount:index'})")
    @ResponseBody
    public ResultDo cancel(@PathVariable("id") Long id) {
        if (null == this.innerOgnOrPlatformReq(id, super.getCurrentUserOgnId())) {
            return ResultDo.build(MessageCode.ERROR_ILLEGAL_RESOURCE);
        }
        return organAccountService.cancelOgnAccount(id);
    }

    /**
     * 根据id冻结
     *
     * @param id
     * @return
     */
    @GetMapping("freeze/{id}")
    @PreAuthorize("hasAnyAuthority({'merchant:organAccount:merchantIndex','platform:organAccount:index'})")
    @ResponseBody
    public ResultDo freeze(@PathVariable("id") Long id) {
        if (null == this.innerOgnOrPlatformReq(id, super.getCurrentUserOgnId())) {
            return ResultDo.build(MessageCode.ERROR_ILLEGAL_RESOURCE);
        }
        return organAccountService.freezeOgnAccount(id);
    }

    /**
     * 根据id解冻
     *
     * @param id
     * @return
     */
    @GetMapping("unfreeze/{id}")
    @PreAuthorize("hasAnyAuthority({'merchant:organAccount:merchantIndex','platform:organAccount:index'})")
    @ResponseBody
    public ResultDo unfreeze(@PathVariable("id") Long id) {
        if (null == this.innerOgnOrPlatformReq(id, super.getCurrentUserOgnId())) {
            return ResultDo.build(MessageCode.ERROR_ILLEGAL_RESOURCE);
        }
        return organAccountService.unfreezeOgnAccount(id);
    }

    /**
     * 校验业务对象是否属于该机构，是：返回po;否：返回null
     *
     * @param sourceId
     * @param ognId
     * @return
     */
    private EpOrganAccountPo innerOgnOrPlatformReq(Long sourceId, Long ognId) {
        if (sourceId == null) {
            return null;
        }
        Optional<EpOrganAccountPo> optional = organAccountService.findById(sourceId);
        if (!optional.isPresent()) {
            return null;
        }
        if (optional.get().getOgnId().equals(ognId)) {
            return optional.get();
        } else {
            log.error(SpringComponent.messageSource("ERROR_ILLEGAL_RESOURCE"));
            return null;
        }
    }

}
