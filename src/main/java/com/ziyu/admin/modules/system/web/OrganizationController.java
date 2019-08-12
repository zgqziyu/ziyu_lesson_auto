package com.ziyu.admin.modules.system.web;

import com.ziyu.admin.core.utils.Result;
import com.ziyu.admin.modules.base.web.BaseCrudController;
import com.ziyu.admin.modules.system.po.Organization;
import com.ziyu.admin.modules.system.service.OrganizationService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.ziyu.admin.core.annotation.SystemLog;
import com.ziyu.admin.modules.system.dto.TreeDto;
import tk.mybatis.mapper.weekend.Weekend;

import javax.validation.constraints.NotNull;
import java.util.List;

@Controller
@RequestMapping("/organization")
public class OrganizationController extends BaseCrudController<Organization> {

    @Autowired
    private OrganizationService organizationService;

    @GetMapping
    @RequiresPermissions("organization:view")
    public String organizationPage() {
        Weekend example = Weekend.of(Organization.class);
        example.setOrderByClause("priority");
        return "system/organization";
    }

    @ResponseBody
    @RequestMapping("/tree")
    public List<TreeDto> findOrgTree(Long pId) {
        return organizationService.queryOrgTree(pId);
    }

    @ResponseBody
    @RequiresPermissions("organization:view")
    @RequestMapping(value = "{id}/load", method = RequestMethod.POST)
    public Result load(@PathVariable Long id) {
        Organization organization = organizationService.queryOne(new Organization().setId(id));
        return Result.success(organization);
    }

    @ResponseBody
    @RequiresPermissions("organization:create")
    @SystemLog("组织管理创建组织")
    @PostMapping("/create")
    @Override
    public Result create(@Validated Organization organization) {
        organizationService.createOrganization(organization);
        return Result.success();
    }

    @ResponseBody
    @RequiresPermissions("organization:update")
    @SystemLog("组织管理更新组织")
    @PostMapping("/update")
    @Override
    public Result update(@Validated Organization organization) {
        organizationService.updateNotNull(organization);
        return Result.success();
    }

    @ResponseBody
    @RequiresPermissions("organization:delete")
    @SystemLog("组织管理删除组织")
    @PostMapping("/delete")
    @Override
    public Result delete(@NotNull @RequestParam("id") Object id) {
        super.delete(id);
        return Result.success();
    }

}
