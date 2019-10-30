package com.ziyu.admin.modules.system.dto;

import com.ziyu.admin.modules.system.po.SubjectInfo;
import lombok.Data;


/**
 * \* User: ziyu
 * \* Date: 2019/7/2
 * \* Time: 16:26
 * \* Description:
 * \
 */
@Data
public class SubjectInfoDto {
    private Long id;

    private String subjectCode;//课程代号

    private String subjectName;//课程名称

    private Integer subjectAttributes;//主副科 1主课，2副科，3其他

    private Integer subjectCount;//课程节数

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

    public SubjectInfoDto(String sc, String sn, Integer a, Integer s) {
        this.subjectCode = sc;
        this.subjectName = sn;
        this.subjectAttributes = a;
        this.subjectCount = s;
    }

    public SubjectInfoDto(SubjectInfo subjectInfo){
        this.id = subjectInfo.getId();
        this.subjectCode = subjectInfo.getSubjectCode();
        this.subjectName = subjectInfo.getSubjectName();
        this.subjectAttributes = subjectInfo.getSubjectAttributes();
        this.subjectCount = subjectInfo.getSubjectCount();
    }

}
