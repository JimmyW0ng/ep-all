package com.ep.backend.controller;

import com.ep.common.tool.StringTools;
import com.ep.domain.constant.BizConstant;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.OrganAccountBo;
import com.ep.domain.pojo.po.EpFilePo;
import com.ep.domain.pojo.po.EpOrganAccountPo;
import com.ep.domain.pojo.po.EpSystemUserPo;
import com.ep.domain.repository.domain.enums.EpOrganAccountStatus;
import com.ep.domain.service.FileService;
import com.ep.domain.service.OrganAccountService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.ApiOperation;
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

        model.addAttribute("organAccountPo", organAccountService.getById(id));
        //头像
        Optional<EpFilePo> avatarOptional = organAccountService.getTeacherAvatar(id);
        if (avatarOptional.isPresent()) {
            model.addAttribute("avatarImgUrl", avatarOptional.get().getFileUrl());
        }
        model.addAttribute("organAccountPo", organAccountService.getById(id));
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

        model.addAttribute("organAccountPo", organAccountService.getById(id));
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
        EpSystemUserPo currentUser = super.getCurrentUser().get();
        bo.setOgnId(currentUser.getOgnId());
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
        return organAccountService.unfreezeOgnAccount(id);
    }

}
