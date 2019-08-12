package com.ziyu.admin.modules.system.service.impl;

import com.ziyu.admin.core.exception.BizException;
import com.ziyu.admin.core.utils.ResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ziyu.admin.modules.base.service.impl.BaseService;
import com.ziyu.admin.modules.system.mapper.UserMapper;
import com.ziyu.admin.modules.system.po.User;
import com.ziyu.admin.modules.system.service.PasswordHelper;
import com.ziyu.admin.modules.system.service.RoleService;
import com.ziyu.admin.modules.system.service.UserService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl extends BaseService<User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordHelper passwordHelper;

    @Override
    @Transactional
    public void createUser(User user) {
        User u = userMapper.selectOne(new User().setUsername(user.getUsername()));
        if (u != null) {
            throw new BizException(ResultCodeEnum.FAILED_USER_ALREADY_EXIST);
        }
        // 加密密码
        passwordHelper.encryptPassword(user);
        userMapper.insertSelective(user);
    }

    @Override
    @Transactional
    public void changePassword(Long userId, String newPassword) {
        User user = userMapper.selectByPrimaryKey(userId);
        user.setPassword(newPassword);
        passwordHelper.encryptPassword(user);
        userMapper.updateByPrimaryKey(user);
    }

    @Override
    public Set<String> queryRoles(String username) {
        User user = userMapper.selectOne(new User().setUsername(username));
        if (user == null) {
            return Collections.EMPTY_SET;
        }
        return roleService.queryRoles(
                Arrays.asList(user.getRoleIds().split(",")).stream().map(Long::valueOf).collect(Collectors.toList()).toArray(new Long[0])
        );
    }

    @Override
    public Set<String> queryPermissions(String username) {
        User user = userMapper.selectOne(new User().setUsername(username));
        if (user == null) {
            return Collections.EMPTY_SET;
        }
        return roleService.queryPermissions(
                Arrays.asList(user.getRoleIds().split(",")).stream().map(Long::valueOf).collect(Collectors.toList()).toArray(new Long[0])
        );
    }

    @Override
    public List<User> getQueryUserList(Long roleId) {
        List<User> userList = userMapper.getQueryUserList(roleId);
        return userList;
    }

    @Override
    public List<User> getQueryUserListByOrganizationId(Long organizationId) {
        List<User> userList = userMapper.getQueryUserListByOrganizationId(organizationId);
        return userList;
    }


    @Override
    public List<User> getUserListByParams(Long organizationId, String groupIds){
        List<User> userList = userMapper.getUserListByParams(organizationId, groupIds);
        return userList;
    }

    @Override
    public User userByParams(Long organizationId, String groupIds, String classIds){
        List<User> userList = userMapper.userByParams(organizationId, groupIds, classIds);
        if(userList != null && userList.size() > 0){
            return userList.get(0);
        }else {
            return null;
        }
    }

}
