package com.ep.domain.service;

import com.ep.common.tool.CollectionsTools;
import com.ep.domain.constant.BizConstant;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.MemberChildHonorBo;
import com.ep.domain.pojo.po.EpFilePo;
import com.ep.domain.repository.FileRepository;
import com.ep.domain.repository.MemberChildHonorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
}