package com.ep.domain.service;

import com.ep.domain.pojo.dto.OrganClassScheduleDto;
import com.ep.domain.repository.OrganClassScheduleRepository;
import lombok.extern.slf4j.Slf4j;
import org.jooq.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * @Description: 班次行程服务
 * @Author: CC.F
 * @Date: 16:08 2018/3/28/028
 */
@Slf4j
@Service
public class OrganClassScheduleService {
    @Autowired
    private OrganClassScheduleRepository organClassScheduleRepository;

    /**
     * 商户后台获取分页
     *
     * @param pageable
     * @param conditions
     * @return
     */
    public Page<OrganClassScheduleDto> findbyPageAndCondition(Long courseId, Pageable pageable, Collection<Condition> conditions) {
        Page<OrganClassScheduleDto> page = organClassScheduleRepository.findbyPageAndCondition(pageable, conditions);
        page.getContent().forEach(p -> {
            if (p.getCourseId() == null) {
                p.setCourseId(courseId);
            }
        });
        return page;
    }
}
