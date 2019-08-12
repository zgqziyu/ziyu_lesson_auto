package com.ziyu.admin.modules.system.web;

import com.ziyu.admin.core.annotation.SystemLog;
import com.ziyu.admin.core.utils.Result;
import com.ziyu.admin.modules.base.query.PageQuery;
import com.ziyu.admin.modules.base.web.BaseCrudController;
import com.ziyu.admin.modules.system.po.SubjectInfo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.ziyu.admin.modules.system.service.SubjectInfoService;

import javax.validation.constraints.NotNull;

/**
 * \* User: ziyu
 * \* Date: 2019/8/1
 * \* Time: 15:44
 * \* Description: 课程管理
 * \
 */
@Controller
@RequestMapping("/subjectInfo")
public class SubjectInfoController extends BaseCrudController<SubjectInfo> {

    @Autowired
    private SubjectInfoService subjectInfoService;


//    @RequiresPermissions("subjectInfo:view")
    @GetMapping
    public String subjectInfoPage(Model model) {
        model.addAttribute("subjectInfoList", subjectInfoService.queryAll());
        return "lesson/subjectInfo";
    }

    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("subjectInfo:view")
    @Override
    public Result queryList(SubjectInfo subjectInfo, PageQuery pageQuery){
        return super.queryList(subjectInfo, pageQuery);
    }

    @ResponseBody
    @RequiresPermissions("subjectInfo:create")
    @SystemLog("创建课程")
    @PostMapping("/create")
    @Override
    public Result create(@Validated SubjectInfo subjectInfo) {
        subjectInfoService.create(subjectInfo);
        return Result.success();
    }

    @ResponseBody
    @RequiresPermissions("subjectInfo:update")
    @SystemLog("更新课程")
    @PostMapping("/update")
    @Override
    public Result update(@Validated SubjectInfo subjectInfo) {
        subjectInfoService.updateNotNull(subjectInfo);
        return Result.success();
    }

    @ResponseBody
    @RequiresPermissions("subjectInfo:delete")
    @SystemLog("删除课程")
    @PostMapping("/delete-batch")
    @Override
    public Result deleteBatchByIds(@NotNull @RequestParam("id")  Object[] ids) {
        super.deleteBatchByIds(ids);
        return Result.success();
    }


}
