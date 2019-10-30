package com.ziyu.admin.modules.system.dto;

import com.ziyu.admin.modules.system.po.GradeInfo;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * \* User: ziyu
 * \* Date: 2019/8/2
 * \* Time: 16:20
 * \* Description:
 * \
 */
@Data
public class GradeInfoDto {
    private Long id;

    private Integer grade;//年级

    private String gradeName;//年级描述名称

    private Integer classCount;//班级数

    private Integer dayPerWeek;//每周几天课

    private Integer lessonPerDay;//每天几节课

    private Integer lessonAtAM;//上午课节

    private Integer lessonAtPM;//下午课节

    private Integer nullPosition;//无课优先安排的课节，默认每天最后一节
    private List<SubjectInfoDto> subjectInfos;//年级开设科目列表
    private Map<Integer, ClassInfo> classInfos;//班级信息，以及班级任课信息

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public Integer getClassCount() {
        return classCount;
    }

    public void setClassCount(Integer classCount) {
        this.classCount = classCount;
    }

    public Integer getDayPerWeek() {
        return dayPerWeek;
    }

    public void setDayPerWeek(Integer dayPerWeek) {
        this.dayPerWeek = dayPerWeek;
    }

    public Integer getLessonPerDay() {
        return lessonPerDay;
    }

    public void setLessonPerDay(Integer lessonPerDay) {
        this.lessonPerDay = lessonPerDay;
    }

    public Integer getLessonAtAM() {
        return lessonAtAM;
    }

    public void setLessonAtAM(Integer lessonAtAM) {
        this.lessonAtAM = lessonAtAM;
    }

    public Integer getLessonAtPM() {
        return lessonAtPM;
    }

    public void setLessonAtPM(Integer lessonAtPM) {
        this.lessonAtPM = lessonAtPM;
    }

    public Integer getNullPosition() {
        return nullPosition;
    }

    public void setNullPosition(Integer nullPosition) {
        this.nullPosition = nullPosition;
    }

    public List<SubjectInfoDto> getSubjectInfos() {
        return subjectInfos;
    }

    public void setSubjectInfos(List<SubjectInfoDto> subjectInfos) {
        this.subjectInfos = subjectInfos;
    }

    public Map<Integer, ClassInfo> getClassInfos() {
        return classInfos;
    }

    public void setClassInfos(Map<Integer, ClassInfo> classInfos) {
        this.classInfos = classInfos;
    }

    public GradeInfoDto(GradeInfo gi){
        this.grade = gi.getGrade();
        this.gradeName = gi.getGradeName();
        this.classCount = gi.getClassCount();
        this.dayPerWeek = gi.getDayPerWeek();
        this.lessonPerDay = gi.getLessonPerDay();
        this.lessonAtAM = gi.getLessonAtAM();
        this.lessonAtPM = gi.getLessonAtPM();
        this.nullPosition = gi.getNullPosition();
    }

    public GradeInfoDto(Integer g, String gn, Integer c, Integer d, Integer l, Integer lam, Integer lpm, Integer np) {
        this.grade = d;
        this.gradeName = gn;
        this.classCount = c;
        this.dayPerWeek = d;
        this.lessonPerDay = l;
        this.lessonAtAM = lam;
        this.lessonAtPM = lpm;
        if (np < 1) {
            this.nullPosition = l;
        } else {
            this.nullPosition = np;
        }

    }
}
