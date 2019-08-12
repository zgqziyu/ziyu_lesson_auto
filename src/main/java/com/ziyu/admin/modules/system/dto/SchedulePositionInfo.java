package com.ziyu.admin.modules.system.dto;

import lombok.Data;

/**
 * \* User: ziyu
 * \* Date: 2019/8/3
 * \* Time: 11:50
 * \* Description: 优先安排外部设定唯一的科目
 * \
 */
@Data
public class SchedulePositionInfo {
    private Integer grade;//年级
    private Integer classNo;//班级
    private Integer weekDay; //哪一天
    private Integer seqNo; //第几节
    private SubjectInfoDto subjectInfoDto;//科目
    private String teacher;//老师

    public SchedulePositionInfo(Integer g, Integer cn, Integer wd, Integer sn, SubjectInfoDto si, String t) {
        this.grade = g;
        this.classNo = cn;
        this.weekDay = wd;
        this.seqNo = sn;
        this.subjectInfoDto = si;
        this.teacher = t;
    }

    public SchedulePositionInfo(SchedulePositionInfo s) {
        this.grade = s.getGrade();
        this.classNo = s.getClassNo();
        this.weekDay = s.getWeekDay();
        this.seqNo = s.getSeqNo();
        this.subjectInfoDto = s.getSubjectInfoDto();
        this.teacher = s.getTeacher();
    }
}
