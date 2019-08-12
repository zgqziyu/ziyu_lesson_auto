package com.ziyu.admin.modules.system.mapper;

import com.ziyu.admin.core.utils.MyMapper;
import com.ziyu.admin.modules.system.po.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper extends MyMapper<User> {

    List<User> getQueryUserList(Long roleId);

    List<User> getQueryUserListByOrganizationId(Long organizationId);

    List<User> getUserListByParams(@Param("organizationId") Long organizationId, @Param("groupIds") String groupIds);

    List<User> userByParams(@Param("organizationId") Long organizationId, @Param("groupIds") String groupIds, @Param("classIds") String classIds);

}