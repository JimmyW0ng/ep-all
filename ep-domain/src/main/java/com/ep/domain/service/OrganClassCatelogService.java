package com.ep.domain.service;

import com.ep.domain.pojo.po.EpOrganClassCatelogPo;
import com.ep.domain.repository.OrganClassCatelogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 班次课程内容目录Service
 * @Author: CC.F
 * @Date: 22:32 2018/2/15
 */
@Service
public class OrganClassCatelogService {
    @Autowired
    private OrganClassCatelogRepository organClassCatelogRepository;

    public List<EpOrganClassCatelogPo> findByCourseId(Long courseId){
        return organClassCatelogRepository.findByCourseId(courseId);
    }

    public List<EpOrganClassCatelogPo> findByClassId(Long classId){
        return organClassCatelogRepository.findByClassId(classId);
    }
}
