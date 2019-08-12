package com.ziyu.admin.modules.system.mapper;

import com.ziyu.admin.core.utils.MyMapper;
import com.ziyu.admin.modules.system.po.Organization;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrganizationMapper extends MyMapper<Organization> {

    int updateSalefParentIds(String makeSelfAsParentIds);

}