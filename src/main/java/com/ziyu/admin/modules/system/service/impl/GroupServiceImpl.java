package com.ziyu.admin.modules.system.service.impl;

import com.ziyu.admin.modules.system.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ziyu.admin.modules.base.service.impl.BaseService;
import com.ziyu.admin.modules.system.mapper.GroupMapper;
import com.ziyu.admin.modules.system.po.Group;

@Service
public class GroupServiceImpl extends BaseService<Group> implements GroupService {

    @Autowired
    private GroupMapper groupMapper;
}
