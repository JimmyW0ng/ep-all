package com.ep.domain.service;

import com.ep.domain.pojo.bo.OrganCourseTagBo;
import com.ep.domain.repository.OrganCourseTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 课程标签service
 * @Author: CC.F
 * @Date: 21:09 2018/2/15
 */
@Service
public class OrganCourseTagService {
    @Autowired
    private OrganCourseTagRepository organCourseTagRepository;

    /**
     * 根据课程id获取课程标签
     * @param id
     * @return
     */
    public List<OrganCourseTagBo> findBosByCourseId(Long id){
        return organCourseTagRepository.findBosByCourseId(id);
    }
}
