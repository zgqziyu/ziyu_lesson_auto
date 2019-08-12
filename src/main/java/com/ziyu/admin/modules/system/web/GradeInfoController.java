package com.ziyu.admin.modules.system.web;

import com.ziyu.admin.core.utils.Result;
import com.ziyu.admin.modules.base.query.PageQuery;
import com.ziyu.admin.modules.base.web.BaseCrudController;
import com.ziyu.admin.modules.system.po.GradeInfo;
import com.ziyu.admin.modules.system.service.GradeInfoService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.ziyu.admin.core.annotation.SystemLog;

import javax.validation.constraints.NotNull;

/**
 * \* User: ziyu
 * \* Date: 2019/8/2
 * \* Time: 14:08
 * \* Description: 年级上课情况管理
 * \
 */
@Controller
@RequestMapping("/gradeInfo")
public class GradeInfoController extends BaseCrudController<GradeInfo> {

    @Autowired
    private GradeInfoService gradeInfoService;

    @GetMapping
    public String subjectInfoPage(Model model) {
        model.addAttribute("subjectInfoList", gradeInfoService.queryAll());
        return "lesson/gradeInfo";
    }

    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("gradeInfo:view")
    @Override
    public Result queryList(GradeInfo gradeInfo, PageQuery pageQuery){
        return super.queryList(gradeInfo, pageQuery);
    }

    @ResponseBody
    @RequiresPermissions("gradeInfo:create")
    @SystemLog("创建上课情况")
    @PostMapping("/create")
    @Override
    public Result create(@Validated GradeInfo gradeInfo) {
        gradeInfoService.create(gradeInfo);
        return Result.success();
    }

    @ResponseBody
    @RequiresPermissions("gradeInfo:update")
    @SystemLog("更新上课情况")
    @PostMapping("/update")
    @Override
    public Result update(@Validated GradeInfo gradeInfo) {
        gradeInfoService.updateNotNull(gradeInfo);
        return Result.success();
    }

    @ResponseBody
    @RequiresPermissions("gradeInfo:delete")
    @SystemLog("删除上课情况")
    @PostMapping("/delete-batch")
    @Override
    public Result deleteBatchByIds(@NotNull @RequestParam("id")  Object[] ids) {
        super.deleteBatchByIds(ids);
        return Result.success();
    }

}
