package com.ziyu.admin.modules.system.dto;

import lombok.Data;

import java.util.Map;

/**
 * \* User: ziyu
 * \* Date: 2019/8/3
 * \* Time: 11:20
 * \* Description: 班级信息
 * \
 */
@Data
public class ClassInfo {
    private String className;//班级名称
    private Map<SubjectInfoDto, String> subjectTeachers;//班级任课信息
}
