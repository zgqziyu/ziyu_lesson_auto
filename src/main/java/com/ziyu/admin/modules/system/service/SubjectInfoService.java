package com.ziyu.admin.modules.system.service;

import com.ziyu.admin.modules.base.service.IService;
import com.ziyu.admin.modules.system.po.SubjectInfo;

import java.util.List;

/**
 * 课程信息接口
 */
public interface SubjectInfoService extends IService<SubjectInfo> {

    /**
     * 根据年级获取课程信息
     * @param subjectGrade
     * @return
     */
    List<SubjectInfo> getQuerySubjectInfoList(Integer subjectGrade);

    /**
     * 通過id獲取
     * @param id
     * @return
     */
    SubjectInfo getEntityById(Long id);
}
