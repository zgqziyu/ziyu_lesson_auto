package com.ziyu.admin.modules.system.service.impl;

import com.ziyu.admin.modules.system.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ziyu.admin.modules.base.service.impl.BaseService;
import com.ziyu.admin.modules.system.mapper.LogMapper;
import com.ziyu.admin.modules.system.po.Log;

/**
 * 日志服务
 */
@Service
public class LogServiceImpl extends BaseService<Log> implements LogService {

    @Autowired
    private LogMapper logMapper;
}
