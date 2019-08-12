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
}
