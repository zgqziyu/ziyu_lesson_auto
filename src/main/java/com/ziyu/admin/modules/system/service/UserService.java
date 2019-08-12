package com.ziyu.admin.modules.system.service;

import com.ziyu.admin.core.exception.BizException;
import com.ziyu.admin.modules.base.service.IService;
import com.ziyu.admin.modules.system.po.User;

import java.util.List;
import java.util.Set;

public interface UserService extends IService<User> {

    /**
     * 创建用户
     * @param user
     */
    void createUser(User user) throws BizException;

    /**
     * 修改密码
     * @param userId
     * @param newPassword
     */
    void changePassword(Long userId, String newPassword);

    /**
     * 根据用户名查找其角色
     * @param username
     * @return
     */
    Set<String> queryRoles(String username);

    /**
     * 根据用户名查找其权限
     * @param username
     * @return
     */
    Set<String> queryPermissions(String username);

    /**
     * 查询所有教师
     * @param roleId
     * @return
     */
    List<User> getQueryUserList(Long roleId);

    /**
     * 通过所属组id查询
     * @param organizationId
     * @return
     */
    List<User> getQueryUserListByOrganizationId(Long organizationId);

    /**
     * 通过所属和组查询
     * @param organizationId
     * @param groupIds
     * @return
     */
    List<User> getUserListByParams(Long organizationId, String groupIds);

    /**
     * 通过所属年级，组，班级查询教师
     * @param organizationId
     * @param groupIds
     * @param classIds
     * @return
     */
    User userByParams(Long organizationId, String groupIds, String classIds);
}