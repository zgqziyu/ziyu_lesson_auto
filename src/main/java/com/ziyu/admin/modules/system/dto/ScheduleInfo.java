package com.ziyu.admin.modules.system.dto;

import lombok.Data;

import java.util.Map;

/**
 * \* User: ziyu
 * \* Date: 2019/7/20
 * \* Description:
 * \
 */
@Data
public class ScheduleInfo {
    private SubjectInfoDto subjectInfoDto;//安排的科目
    private String teacher;//任教老师
    private Map<SubjectInfoDto, SchedulingProcessInfo> validSubjects;
}
