package com.ziyu.admin.modules.system.service;

import com.ziyu.admin.modules.base.service.IService;
import com.ziyu.admin.modules.system.po.CurriculumInfo;

import java.util.List;
import java.util.Map;

/**
 * \* User: ziyu
 * \* Date: 2019/8/3
 * \* Time: 9:52
 * \* Description:
 * \
 */
public interface CurriculumInfoService extends IService<CurriculumInfo> {

    /**
     * 通过年级，班级 查询课程表
     * @param gradeId
     * @param classId
     * @return
     */
    List<List<Map<String, Object>>>  getCurriculumInfoByParams(String gradeId, String classId);

}
