package com.ep.domain.service;

import com.ep.domain.repository.OrganClassScheduleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
