package com.ziyu.admin.modules.system.po;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * \* User: ziyu
 * \* Date: 2019/8/3
 * \* Time: 9:40
 * \* Description: 课程表
 * \
 */
@Data
@Table(name = "class_curriculum_info")
public class CurriculumInfo {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="grade_id")
    private String gradeId;//年级

    @Column(name="class_id")
    private String classId;//班级

    private Integer weekday;//周几

    @Column(name="pitch_num")
    private Integer pitchNum;//第几节

    @Column(name="subjectInfo_id")
    private Long subjectInfoId; //课程id

    @Column(name="user_id")
    private Long userId;//教师id

    @Column(name="user_name")
    private String userName;//教师名称

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGradeId() {
        return gradeId;
    }

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public Integer getWeekday() {
        return weekday;
    }

    public void setWeekday(Integer weekday) {
        this.weekday = weekday;
    }

    public Integer getPitchNum() {
        return pitchNum;
    }

    public void setPitchNum(Integer pitchNum) {
        this.pitchNum = pitchNum;
    }

    public Long getSubjectInfoId() {
        return subjectInfoId;
    }

    public void setSubjectInfoId(Long subjectInfoId) {
        this.subjectInfoId = subjectInfoId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
