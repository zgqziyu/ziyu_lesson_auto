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
