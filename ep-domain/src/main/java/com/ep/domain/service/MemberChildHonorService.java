package com.ep.domain.service;

import com.ep.common.tool.CollectionsTools;
import com.ep.domain.constant.BizConstant;
import com.ep.domain.constant.MessageCode;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.MemberChildHonorBo;
import com.ep.domain.pojo.po.EpFilePo;
import com.ep.domain.pojo.po.EpMemberChildHonorPo;
import com.ep.domain.pojo.po.EpOrganClassPo;
import com.ep.domain.pojo.po.EpOrganCoursePo;
import com.ep.domain.repository.FileRepository;
import com.ep.domain.repository.MemberChildHonorRepository;
import com.ep.domain.repository.OrganClassRepository;
import com.ep.domain.repository.OrganCourseRepository;
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
 * @Description: 孩子荣誉服务接口
 * @Author J.W
 * @Date: 下午 5:32 2018/2/23 0023
 */
@Slf4j
@Service
public class MemberChildHonorService {

    @Autowired
    private MemberChildHonorRepository memberChildHonorRepository;
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private OrganCourseRepository organCourseRepository;
    @Autowired
    private OrganClassRepository organClassRepository;

    public Optional<EpMemberChildHonorPo> findById(Long id) {
        return memberChildHonorRepository.findById(id);
    }

    /**
     * 查询孩子获得的最新荣誉-分页
     *
     * @param pageable
     * @param childId
     * @return
     */
    public ResultDo<Page<MemberChildHonorBo>> queryRecentForPage(Pageable pageable, Long childId) {
        ResultDo<Page<MemberChildHonorBo>> resultDo = ResultDo.build();
        Page<MemberChildHonorBo> page = memberChildHonorRepository.queryRecentForPage(pageable, childId);
        List<MemberChildHonorBo> data = page.getContent();
        if (CollectionsTools.isNotEmpty(data)) {
            for (MemberChildHonorBo honorBo : data) {
                Optional<EpFilePo> optAvatar = fileRepository.getOneByBizTypeAndSourceId(BizConstant.FILE_BIZ_TYPE_CODE_COURSE_MAIN_PIC, honorBo.getCourseId());
                String coursePic = optAvatar.isPresent() ? optAvatar.get().getFileUrl() : null;
                honorBo.setCoursePic(coursePic);
            }
        }
        return resultDo.setResult(page);
    }

    /**
     * 查询孩子班次内获得的荣誉
     *
     * @param childId
     * @param classId
     * @return
     */
    public ResultDo<List<MemberChildHonorBo>> queryByClassId(Long childId, Long classId) {
        ResultDo<List<MemberChildHonorBo>> resultDo = ResultDo.build();
        List<MemberChildHonorBo> data = memberChildHonorRepository.queryByClassId(childId, classId);
        return resultDo.setResult(data);
    }

    /**
     * 后台获取孩子荣誉分页
     *
     * @param pageable
     * @param condition
     * @return
     */
    public Page<MemberChildHonorBo> findbyPageAndCondition(Pageable pageable, Collection<? extends Condition> condition) {
        return memberChildHonorRepository.findbyPageAndCondition(pageable, condition);
    }

    /**
     * 新增孩子荣誉
     *
     * @param po
     */
    public ResultDo createHonor(EpMemberChildHonorPo po) {
        Long ognId = po.getOgnId();
        Long courseId = po.getCourseId();
        Long classId = po.getClassId();
        log.info("[荣誉]，新增孩子荣誉开始，荣誉对象po={}。", po);
        Optional<EpOrganCoursePo> courseOptional = organCourseRepository.findById(courseId);
        if (!courseOptional.isPresent()) {
            log.error("[荣誉]，新增孩子荣誉失败，产品id不存在,courseId={}。", courseId);
            ResultDo.build(MessageCode.ERROR_COURSE_NOT_EXIST);
        }
        //产品必须为本机构的
        if (!courseOptional.get().getOgnId().equals(ognId)) {
            ResultDo.build(MessageCode.ERROR_ILLEGAL_RESOURCE);
        }
        Optional<EpOrganClassPo> classOptional = organClassRepository.findById(classId);
        if (!classOptional.isPresent()) {
            log.error("[荣誉]，新增孩子荣誉失败，班次id不存在,classId={}。", classId);
            ResultDo.build(MessageCode.ERROR_CLASS_NOT_EXIST);
        }
        //班次必须为本机构的
        if (!classOptional.get().getOgnId().equals(ognId)) {
            ResultDo.build(MessageCode.ERROR_ILLEGAL_RESOURCE);
        }
        memberChildHonorRepository.insert(po);
        log.info("[荣誉]，新增孩子荣誉成功，id={}。", po.getId());
        return ResultDo.build();
    }

    /**
     * 根据id获取孩子荣誉
     *
     * @param id
     * @return
     */
    public MemberChildHonorBo findBoById(Long id) {
        return memberChildHonorRepository.findBoById(id);
    }

    /**
     * 更新孩子的荣誉记录
     *
     * @param po
     */
    public ResultDo updateHonor(EpMemberChildHonorPo po) {
        log.info("[荣誉]，修改孩子荣誉开始，荣誉对象po={}。", po);
        if (memberChildHonorRepository.updateChildHonor(po) == BizConstant.DB_NUM_ONE) {
            log.info("[荣誉]，修改孩子荣誉成功，id={}。", po.getId());
            return ResultDo.build();
        } else {
            log.error("[荣誉]，修改孩子荣誉失败，id={}。", po.getId());
            return ResultDo.build(MessageCode.ERROR_OPERATE_FAIL);
        }
    }

    /**
     * 删除孩子荣誉
     *
     * @param id
     */
    public ResultDo deleteById(Long id) {
        log.info("[荣誉]，删除孩子荣誉开始，荣誉id={}。", id);
        if (memberChildHonorRepository.deleteLogicById(id) == BizConstant.DB_NUM_ONE) {
            log.info("[荣誉]，删除孩子荣誉成功，id={}。", id);
            return ResultDo.build();
        } else {
            log.error("[荣誉]，删除孩子荣誉失败，id={}。", id);
            return ResultDo.build(MessageCode.ERROR_OPERATE_FAIL);
        }
    }
}
