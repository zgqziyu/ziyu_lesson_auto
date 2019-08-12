package com.ziyu.admin.modules.system.service;

import com.ziyu.admin.modules.base.service.IService;
import com.ziyu.admin.modules.system.dto.TreeDto;
import com.ziyu.admin.modules.system.po.Organization;

import java.util.List;

/**
 * \* User: ziyu
 * \* Date: 2019/7/5
 * \* Description:
 * \
 */
public interface OrganizationService extends IService<Organization> {

    void createOrganization(Organization organization);

    List<TreeDto> queryOrgTree(Long pId);

    List<Organization> queryAllWithExclude(Organization excludeOraganization);

    void move(Organization source, Organization target);
}
