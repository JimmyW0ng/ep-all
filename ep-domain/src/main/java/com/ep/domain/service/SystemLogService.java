package com.ep.domain.service;

//import com.changfu.pojo.SysLogPojo;
//import com.changfu.business.repositories.SysLogRepository;
//import org.springframework.beans.factory.annotation.Autowired;

import com.ep.domain.pojo.po.EpSystemLogPo;
import com.ep.domain.repository.SystemLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description: 操作日志服务
 * @Author: CC.F
 * @Date: 上午9:30 2018/4/26
 */
@Service
public class SystemLogService {

    @Autowired
    private SystemLogRepository systemLogRepository;

    public void insert(EpSystemLogPo po) {
        systemLogRepository.insert(po);
    }

}
