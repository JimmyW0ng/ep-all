package com.ep.backend.controller;

import com.ep.common.tool.BeanTools;
import com.ep.common.tool.CollectionsTools;
import com.ep.common.tool.DateTools;
import com.ep.domain.component.ConstantRegionComponent;
import com.ep.domain.component.DictComponent;
import com.ep.domain.constant.BizConstant;
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

import javax.servlet.http.HttpServletRequest;
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
@Controller
@RequestMapping("auth/systemOrgan")
public class SystemOrganController extends BackendController {

    @Autowired
    private OrganService organService;
    @Autowired
    private ConstantRegionComponent constantRegionComponent;

    @Autowired
    private DictComponent dictComponent;
    @Autowired
    private FileService fileService;


    @GetMapping("index")
    public String index(Model model,
                        @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                        @RequestParam(value = "crStartTime", required = false) Timestamp crStartTime,
                        @RequestParam(value = "crEndTime", required = false) Timestamp crEndTime
    ) {
        Map map = Maps.newHashMap();
        Collection<Condition> conditions = Lists.newArrayList();
//        if (StringTools.isNotBlank(mobile)) {
//            conditions.add(EP.EP_SYSTEM_USER.MOBILE.eq(Long.parseLong(mobile)));
//        }
//        map.put("mobile", mobile);
//        if (StringTools.isNotBlank(type)) {
//            conditions.add(EP.EP_SYSTEM_USER.TYPE.eq(EpSystemUserType.valueOf(type)));
//        }
//        map.put("type", type);

        if (null != crStartTime) {
            conditions.add(EP.EP_ORGAN.CREATE_AT.greaterOrEqual(crStartTime));
        }
        map.put("crStartTime", crStartTime);
        if (null != crEndTime) {
            conditions.add(EP.EP_ORGAN.CREATE_AT.lessOrEqual(crEndTime));
        }
        map.put("crEndTime", crEndTime);
        conditions.add(EP.EP_ORGAN.DEL_FLAG.eq(false));
        Page<EpOrganPo> page = organService.findByPageAndCondition(pageable, conditions);
        model.addAttribute("page", page);
        model.addAttribute("map", map);
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
        model.addAttribute("district",null);
        model.addAttribute("city",null);
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
     * @param request
     * @return
     */
    @PostMapping("create")
    @ResponseBody
    public ResultDo create(HttpServletRequest request, SystemOrganBo bo
    ) {
        ResultDo resultDo = ResultDo.build();
        EpOrganPo po = new EpOrganPo();
        BeanTools.copyPropertiesIgnoreNull(bo, po);
        po.setOgnCreateDate(DateTools.stringToTimestamp(bo.getOrganCreateDateStr(), "yyyy-MM-dd"));
        organService.createSystemOrgan(po,bo.getMainpicUrlPreCode(),bo.getLogoUrlPreCode());
        return resultDo;
    }

    /**
     * 修改机构
     *
     * @param bo
     * @param request
     * @return
     */
    @PostMapping("update")
    @ResponseBody
    public ResultDo update(HttpServletRequest request, SystemOrganBo bo
    ) {
        ResultDo resultDo = ResultDo.build();
        EpOrganPo po = new EpOrganPo();
        BeanTools.copyPropertiesIgnoreNull(bo, po);
        po.setOgnCreateDate(DateTools.stringToTimestamp(bo.getOrganCreateDateStr(), "yyyy-MM-dd"));
        organService.updateSystemOrgan(po,bo.getMainpicUrlPreCode(),bo.getLogoUrlPreCode());
        return resultDo;
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
        Long cityId=constantRegionPoDistrict.getParentId();
        EpConstantRegionPo constantRegionPoCity = constantRegionComponent.getById(cityId);
        Long provinceId=constantRegionPoCity.getParentId();
        model.addAttribute("district",constantRegionComponent.getMapByParentId(cityId));
        model.addAttribute("districtId",constantRegionPoDistrict.getId());
        model.addAttribute("city",constantRegionComponent.getMapByParentId(provinceId));
        model.addAttribute("cityId",cityId);
        model.addAttribute("province", constantRegionComponent.getMapByType(EpConstantRegionRegionType.province));
        model.addAttribute("provinceId", provinceId);
        model.addAttribute("organPo", po);
        List<EpFilePo> mainpics=fileService.getByBizTypeAndSourceId(BizConstant.FILE_BIZ_TYPE_CODE_ORGAN_MAIN_PIC,po.getId());
        String mainpicImgUrl =null;
        if(CollectionsTools.isNotEmpty(mainpics)){
            mainpicImgUrl = mainpics.get(mainpics.size()-1).getFileUrl();
        }
        List<EpFilePo> logos=fileService.getByBizTypeAndSourceId(BizConstant.FILE_BIZ_TYPE_CODE_ORGAN_LOGO,po.getId());
        String logoImgUrl =null;
        if(CollectionsTools.isNotEmpty(logos)){
            logoImgUrl = logos.get(logos.size()-1).getFileUrl();
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
        EpConstantRegionPo constantRegionPoDistrict = constantRegionComponent.getById(po.getOgnRegion());
        model.addAttribute("district", constantRegionPoDistrict.getRegionName());
        Long cityId=constantRegionPoDistrict.getParentId();

        EpConstantRegionPo constantRegionPoCity = constantRegionComponent.getById(cityId);
        model.addAttribute("city", constantRegionPoCity.getRegionName());
        Long provinceId=constantRegionPoCity.getParentId();
        EpConstantRegionPo constantRegionPoProvince = constantRegionComponent.getById(provinceId);
        model.addAttribute("province", constantRegionPoProvince.getRegionName());
        model.addAttribute("organPo", po);
        List<EpFilePo> mainpics=fileService.getByBizTypeAndSourceId(BizConstant.FILE_BIZ_TYPE_CODE_ORGAN_MAIN_PIC,po.getId());
        String mainpicImgUrl =null;
        if(CollectionsTools.isNotEmpty(mainpics)){
            mainpicImgUrl = mainpics.get(mainpics.size()-1).getFileUrl();
        }
        List<EpFilePo> logos=fileService.getByBizTypeAndSourceId(BizConstant.FILE_BIZ_TYPE_CODE_ORGAN_LOGO,po.getId());
        String logoImgUrl =null;
        if(CollectionsTools.isNotEmpty(logos)){
            logoImgUrl = logos.get(logos.size()-1).getFileUrl();
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
    public ResultDo delete( @PathVariable("id") Long id
    ) {
        ResultDo resultDo=ResultDo.build();
        organService.delete(id);
        return resultDo;
    }

    /**
     * 上传商家主图
     * @param file
     * @return
     */
    @PostMapping("uploadMainpic")
    @ResponseBody
    public ResultDo uploadMainpic(@RequestParam("file") MultipartFile file){
        ResultDo resultDo=ResultDo.build();
        try{
             resultDo=fileService.addFileByBizType(file.getName(),file.getBytes(), BizConstant.FILE_BIZ_TYPE_CODE_ORGAN_MAIN_PIC,null);
        }catch(Exception e){
            resultDo.setSuccess(false);
            return resultDo;
        }

        return resultDo;
    }

    /**
     * 上传商家logo
     * @param file
     * @return
     */
    @PostMapping("uploadLogo")
    @ResponseBody
    public ResultDo uploadLogo(@RequestParam("file") MultipartFile file){
        ResultDo resultDo=ResultDo.build();
        try{
            // resultDo=fileService.replaceFileByBizTypeAndSourceId(file.getName(),file.getBytes(),BizConstant.FILE_BIZ_TYPE_CODE_ORGAN_LOGO,sourceId,null);
            resultDo=fileService.addFileByBizType(file.getName(),file.getBytes(), BizConstant.FILE_BIZ_TYPE_CODE_ORGAN_LOGO,null);
        }catch(Exception e){
            resultDo.setSuccess(false);
            return resultDo;
        }

        return resultDo;
    }

    /**
     * 设置图片模态框初始化
     * @param id
     * @return
     */
    @GetMapping("uploadInit/{id}")
    @ResponseBody
    public ResultDo<Map<String,String>> uploadInit(@PathVariable("id") Long id){
        ResultDo<Map<String,String>> resultDo=ResultDo.build();
        Map<String,String> map=Maps.newHashMap();
        //商家主图
        List<EpFilePo> mainpics=fileService.getByBizTypeAndSourceId(BizConstant.FILE_BIZ_TYPE_CODE_COURSE_MAIN_PIC,id);
        if(CollectionsTools.isNotEmpty(mainpics)){
            String mainpicImgUrl=mainpics.get(0).getFileUrl();
            map.put("mainpicImgUrl", mainpicImgUrl);
        }

        //商家logo
        List<EpFilePo> logos=fileService.getByBizTypeAndSourceId(BizConstant.FILE_BIZ_TYPE_CODE_ORGAN_LOGO,id);

        if(CollectionsTools.isNotEmpty(logos)){
            String logoImgUrl=logos.get(0).getFileUrl();
            map.put("logoImgUrl",logoImgUrl);
        }

        resultDo.setResult(map);
        return resultDo;
    }
}
