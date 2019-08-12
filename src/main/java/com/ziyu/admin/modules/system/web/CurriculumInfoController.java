package com.ziyu.admin.modules.system.web;

import com.ziyu.admin.core.utils.Result;
import com.ziyu.admin.modules.base.web.BaseCrudController;
import com.ziyu.admin.modules.system.service.CurriculumInfoService;
import com.ziyu.admin.modules.system.service.LessonScheduleService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.ziyu.admin.core.annotation.SystemLog;
import com.ziyu.admin.modules.system.po.CurriculumInfo;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * \* User: ziyu
 * \* Date: 2019/8/8
 * \* Time: 14:16
 * \* Description: 课程表
 * \
 */
@Controller
@RequestMapping("/curriculumInfo")
public class CurriculumInfoController extends BaseCrudController<CurriculumInfo> {

    @Autowired
    private CurriculumInfoService curriculumInfoService;

    @Autowired
    private LessonScheduleService lessonScheduleService;

    @GetMapping
    public String subjectInfoPage(Model model) {
//        String grade = "1";
//        String classId = "1";
//        List<List<Map<String, Object>>> resultList = curriculumInfoService.getCurriculumInfoByParams(grade, classId);
//        model.addAttribute("curriculumInfoList", resultList);
//        System.err.println("resultList : "+resultList);
        return "lesson/curriculumInfo";
    }

    @ResponseBody
    @PostMapping("/table/{grade}/{classId}")
    public List<List<Map<String, Object>>> curriculumInfoTable(@NotNull @PathVariable("grade")String grade,
                                      @NotNull @PathVariable("classId")String classId,
                                      Model model) {
        List<List<Map<String, Object>>> resultList = curriculumInfoService.getCurriculumInfoByParams(grade, classId);
        model.addAttribute("curriculumInfoList", resultList);
        return resultList;
    }

    @ResponseBody
    @PostMapping("/createTable")
    @RequiresPermissions("curriculumInfo:createTable")
    @SystemLog("智能排课")
    public Result createTable() {
        //先清空数据库
        curriculumInfoService.truncate();
        //开始执行智能排课
        lessonScheduleService.lessonScheduleAuto();
        return Result.success();
    }
}
