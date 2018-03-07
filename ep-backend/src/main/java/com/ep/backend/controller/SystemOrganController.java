package com.ep.backend.controller;

import com.ep.common.tool.CollectionsTools;
import com.ep.common.tool.StringTools;
import com.ep.domain.component.ConstantRegionComponent;
import com.ep.domain.constant.BizConstant;
import com.ep.domain.constant.MessageCode;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.SystemOrganBo;
import com.ep.domain.pojo.po.EpConstantRegionPo;
import com.ep.domain.pojo.po.EpFilePo;
import com.ep.domain.pojo.po.EpOrganPo;
import com.ep.domain.repository.domain.enums.EpConstantRegionRegionType;
import com.ep.domain.service.FileService;
import com.ep.domain.service.OrganService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.jooq.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.ep.domain.repository.domain.Ep.EP;

/**
 * @Description: 后台商家机构控制器
 * @Author: CC.F
 * @Date: 11:19 2018/1/29
 */
@Slf4j
@Controller
@RequestMapping("auth/systemOrgan")
public class SystemOrganController extends BackendController {

    @Autowired
    private OrganService organService;
    @Autowired
    private ConstantRegionComponent constantRegionComponent;
    @Autowired
    private FileService fileService;


    @GetMapping("index")
    public String index(Model model,
                        @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                        @RequestParam(value = "ognName", required = false) String ognName,
                        @RequestParam(value = "crStartTime", required = false) Timestamp crStartTime,
                        @RequestParam(value = "crEndTime", required = false) Timestamp crEndTime
    ) {
        Map<String, Object> searchMap = Maps.newHashMap();
        Collection<Condition> conditions = Lists.newArrayList();
        if (StringTools.isNotBlank(ognName)) {
            conditions.add(EP.EP_ORGAN.OGN_NAME.like("%" + ognName + "%"));
        }
        searchMap.put("ognName", ognName);
        if (null != crStartTime) {
            conditions.add(EP.EP_ORGAN.CREATE_AT.greaterOrEqual(crStartTime));
        }
        searchMap.put("crStartTime", crStartTime);
        if (null != crEndTime) {
            conditions.add(EP.EP_ORGAN.CREATE_AT.lessOrEqual(crEndTime));
        }
        searchMap.put("crEndTime", crEndTime);
        conditions.add(EP.EP_ORGAN.DEL_FLAG.eq(false));
        Page<EpOrganPo> page = organService.findByPageAndCondition(pageable, conditions);
        model.addAttribute("page", page);
        model.addAttribute("searchMap", searchMap);
        return "/systemOrgan/index";
    }

    /**
     * 注册初始化
     *
     * @param model
     * @return
     */
    @GetMapping("createInit")
    public String createInit(Model model
    ) {
        model.addAttribute("organPo", new EpOrganPo());
        model.addAttribute("province", constantRegionComponent.getMapByType(EpConstantRegionRegionType.province));
        model.addAttribute("district", null);
        model.addAttribute("city", null);
        return "systemOrgan/form";
    }

    /**
     * 根据省获取市
     *
     * @param model
     * @return
     */
    @GetMapping("getCityByProvince/{provinceId}")
    @ResponseBody
    public ResultDo<Map<Long, String>> getCityByProvince(Model model, @PathVariable("provinceId") Long provinceId
    ) {
        ResultDo<Map<Long, String>> resultDo = ResultDo.build();
        resultDo.setResult(constantRegionComponent.getMapByParentId(provinceId));
        return resultDo;
    }

    /**
     * 根据市获取区
     *
     * @param
     * @return
     */
    @GetMapping("getDistrictByCity/{cityId}")
    @ResponseBody
    public ResultDo<Map<Long, String>> getDistrictByCity(@PathVariable("cityId") Long cityId
    ) {
        ResultDo<Map<Long, String>> resultDo = ResultDo.build();
        resultDo.setResult(constantRegionComponent.getMapByParentId(cityId));
        return resultDo;
    }

    /**
     * 新增机构
     *
     * @param bo
     * @return
     */
    @PostMapping("create")
    @ResponseBody
    public ResultDo create(SystemOrganBo bo) {
        try {
            return organService.createSystemOrgan(bo);
        } catch (Exception e) {
            log.error("[机构]机构新增失败。", e);
            return ResultDo.build(MessageCode.ERROR_SYSTEM);
        }
    }

    /**
     * 修改机构
     *
     * @param bo
     * @return
     */
    @PostMapping("update")
    @ResponseBody
    public ResultDo update(SystemOrganBo bo) {
        try {
            return organService.updateSystemOrgan(bo);
        } catch (Exception e) {
            log.error("[机构]修改机构失败。", e);
            return ResultDo.build(MessageCode.ERROR_SYSTEM);
        }
    }

    /**
     * 修改机构初始化
     *
     * @param model
     * @param id
     * @return
     */
    @GetMapping("updateInit/{id}")
    public String updateInit(Model model, @PathVariable("id") Long id
    ) {
        EpOrganPo po = organService.getById(id).get();
        EpConstantRegionPo constantRegionPoDistrict = constantRegionComponent.getById(po.getOgnRegion());
        Long cityId = constantRegionPoDistrict.getParentId();
        EpConstantRegionPo constantRegionPoCity = constantRegionComponent.getById(cityId);
        Long provinceId = constantRegionPoCity.getParentId();
        model.addAttribute("district", constantRegionComponent.getMapByParentId(cityId));
        model.addAttribute("districtId", constantRegionPoDistrict.getId());
        model.addAttribute("city", constantRegionComponent.getMapByParentId(provinceId));
        model.addAttribute("cityId", cityId);
        model.addAttribute("province", constantRegionComponent.getMapByType(EpConstantRegionRegionType.province));
        model.addAttribute("provinceId", provinceId);
        model.addAttribute("organPo", po);
        List<EpFilePo> mainpics = fileService.getByBizTypeAndSourceId(BizConstant.FILE_BIZ_TYPE_CODE_ORGAN_MAIN_PIC, po.getId());
        String mainpicImgUrl = null;
        if (CollectionsTools.isNotEmpty(mainpics)) {
            mainpicImgUrl = mainpics.get(mainpics.size() - 1).getFileUrl();
        }
        List<EpFilePo> logos = fileService.getByBizTypeAndSourceId(BizConstant.FILE_BIZ_TYPE_CODE_ORGAN_LOGO, po.getId());
        String logoImgUrl = null;
        if (CollectionsTools.isNotEmpty(logos)) {
            logoImgUrl = logos.get(logos.size() - 1).getFileUrl();
        }
        model.addAttribute("mainpicImgUrl", mainpicImgUrl);
        model.addAttribute("logoImgUrl", logoImgUrl);
        return "systemOrgan/form";
    }

    /**
     * 查看机构
     *
     * @param model
     * @param id
     * @return
     */
    @GetMapping("view/{id}")
    public String view(Model model, @PathVariable("id") Long id
    ) {
        EpOrganPo po = organService.getById(id).get();
        //区
        EpConstantRegionPo constantRegionPoDistrict = constantRegionComponent.getById(po.getOgnRegion());
        model.addAttribute("district", constantRegionPoDistrict.getRegionName());
        Long cityId = constantRegionPoDistrict.getParentId();
        //市
        EpConstantRegionPo constantRegionPoCity = constantRegionComponent.getById(cityId);
        model.addAttribute("city", constantRegionPoCity.getRegionName());
        Long provinceId = constantRegionPoCity.getParentId();
        //省
        EpConstantRegionPo constantRegionPoProvince = constantRegionComponent.getById(provinceId);
        model.addAttribute("province", constantRegionPoProvince.getRegionName());
        model.addAttribute("organPo", po);
        //主图
        List<EpFilePo> mainpics = fileService.getByBizTypeAndSourceId(BizConstant.FILE_BIZ_TYPE_CODE_ORGAN_MAIN_PIC, po.getId());
        String mainpicImgUrl = null;
        if (CollectionsTools.isNotEmpty(mainpics)) {
            mainpicImgUrl = mainpics.get(mainpics.size() - 1).getFileUrl();
        }
        //logo
        List<EpFilePo> logos = fileService.getByBizTypeAndSourceId(BizConstant.FILE_BIZ_TYPE_CODE_ORGAN_LOGO, po.getId());
        String logoImgUrl = null;
        if (CollectionsTools.isNotEmpty(logos)) {
            logoImgUrl = logos.get(logos.size() - 1).getFileUrl();
        }
        model.addAttribute("mainpicImgUrl", mainpicImgUrl);
        model.addAttribute("logoImgUrl", logoImgUrl);
        return "systemOrgan/view";
    }

    /**
     * 删除机构
     *
     * @return
     */
    @GetMapping("delete/{id}")
    @ResponseBody
    public ResultDo delete(@PathVariable("id") Long id
    ) {
        ResultDo resultDo = ResultDo.build();
        organService.delete(id);
        return resultDo;
    }

    /**
     * 上传商家主图
     *
     * @param file
     * @return
     */
    @PostMapping("uploadMainpic")
    @ResponseBody
    public ResultDo uploadMainpic(@RequestParam("file") MultipartFile file) {
        ResultDo resultDo = ResultDo.build();
        try {
            resultDo = fileService.addFileByBizType(file.getName(), file.getBytes(), BizConstant.FILE_BIZ_TYPE_CODE_ORGAN_MAIN_PIC, null);
        } catch (Exception e) {
            resultDo.setSuccess(false);
            return resultDo;
        }

        return resultDo;
    }

    /**
     * 上传商家logo
     *
     * @param file
     * @return
     */
    @PostMapping("uploadLogo")
    @ResponseBody
    public ResultDo uploadLogo(@RequestParam("file") MultipartFile file) {
        ResultDo resultDo = ResultDo.build();
        try {
            resultDo = fileService.addFileByBizType(file.getName(), file.getBytes(), BizConstant.FILE_BIZ_TYPE_CODE_ORGAN_LOGO, null);
        } catch (Exception e) {
            resultDo.setSuccess(false);
            return resultDo;
        }

        return resultDo;
    }

    /**
     * 设置图片模态框初始化
     *
     * @param id
     * @return
     */
    @GetMapping("uploadInit/{id}")
    @ResponseBody
    public ResultDo<Map<String, String>> uploadInit(@PathVariable("id") Long id) {
        ResultDo<Map<String, String>> resultDo = ResultDo.build();
        Map<String, String> map = Maps.newHashMap();
        //商家主图
        List<EpFilePo> mainpics = fileService.getByBizTypeAndSourceId(BizConstant.FILE_BIZ_TYPE_CODE_COURSE_MAIN_PIC, id);
        if (CollectionsTools.isNotEmpty(mainpics)) {
            String mainpicImgUrl = mainpics.get(0).getFileUrl();
            map.put("mainpicImgUrl", mainpicImgUrl);
        }

        //商家logo
        List<EpFilePo> logos = fileService.getByBizTypeAndSourceId(BizConstant.FILE_BIZ_TYPE_CODE_ORGAN_LOGO, id);

        if (CollectionsTools.isNotEmpty(logos)) {
            String logoImgUrl = logos.get(0).getFileUrl();
            map.put("logoImgUrl", logoImgUrl);
        }

        resultDo.setResult(map);
        return resultDo;
    }

    /**
     * 机构下线
     *
     * @param id
     * @return
     */
    @GetMapping("offline/{id}")
    @ResponseBody
    public ResultDo offline(@PathVariable("id") Long id) {

        return organService.offlineById(id);
    }

    /**
     * 机构上线
     *
     * @param id
     * @return
     */
    @GetMapping("online/{id}")
    @ResponseBody
    public ResultDo onlineById(@PathVariable("id") Long id) {
        return organService.onlineById(id);
    }

}
