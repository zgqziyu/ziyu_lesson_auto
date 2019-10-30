package com.ziyu.admin.modules.system.po;

import lombok.Data;

import javax.persistence.*;

/**
 * 课程信息
 */
@Data
@Table(name = "class_subject_info")
public class SubjectInfo {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sub_code")
    private String subjectCode;//课程代号

    @Column(name = "sub_name")
    private String subjectName;//课程名称

    @Column(name = "sub_attributes")
    private Integer subjectAttributes;//主副科 1主课，2副科，3其他

    @Column(name = "sub_count")
    private Integer subjectCount;//课程节数

    @Column(name = "sub_grade")
    private Integer subjectGrade;//所属年级

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public Integer getSubjectAttributes() {
        return subjectAttributes;
    }

    public void setSubjectAttributes(Integer subjectAttributes) {
        this.subjectAttributes = subjectAttributes;
    }

    public Integer getSubjectCount() {
        return subjectCount;
    }

    public void setSubjectCount(Integer subjectCount) {
        this.subjectCount = subjectCount;
    }

    public Integer getSubjectGrade() {
        return subjectGrade;
    }

    public void setSubjectGrade(Integer subjectGrade) {
        this.subjectGrade = subjectGrade;
    }
}