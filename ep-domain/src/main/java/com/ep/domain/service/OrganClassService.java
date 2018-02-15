package com.ep.domain.service;

import com.ep.domain.pojo.po.EpOrganClassPo;
import com.ep.domain.repository.OrganClassRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 机构课程班次Service
 * @Author: CC.F
 * @Date: 17:12 2018/2/11
 */
@Service
@Slf4j
public class OrganClassService {
    @Autowired
    private OrganClassRepository organClassRepository;

    public List<EpOrganClassPo> findByCourseId(Long courseId){
        return organClassRepository.getByCourseId(courseId);
    }

    public EpOrganClassPo insertOrganClassPo(EpOrganClassPo organClassPo){
        return organClassRepository.insertNew(organClassPo);
    }

}
