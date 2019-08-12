package com.ziyu.admin.modules.system.service;

import com.ziyu.admin.modules.base.service.IService;
import com.ziyu.admin.modules.system.po.GradeInfo;

/**
 * 年级上课情况接口
 */
public interface GradeInfoService extends IService<GradeInfo> {
    /**
     * 通过年级获取年级上课情况
     * @param grade
     * @return
     */
    GradeInfo selectByGrade(Integer grade);
}
