package com.ziyu.admin.modules.system.dto;

import lombok.Data;

/**
 * \* User: ziyu
 * \* Date: 2019/7/13
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

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public Integer getClassNo() {
        return classNo;
    }

    public void setClassNo(Integer classNo) {
        this.classNo = classNo;
    }

    public Integer getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(Integer weekDay) {
        this.weekDay = weekDay;
    }

    public Integer getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }

    public SubjectInfoDto getSubjectInfoDto() {
        return subjectInfoDto;
    }

    public void setSubjectInfoDto(SubjectInfoDto subjectInfoDto) {
        this.subjectInfoDto = subjectInfoDto;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

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
