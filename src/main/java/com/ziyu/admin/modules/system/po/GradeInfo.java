package com.ziyu.admin.modules.system.po;

import lombok.Data;

import javax.persistence.*;

/**
 * \* User: ziyu
 * \* Date: 2019/8/2
 * \* Time: 14:00
 * \* Description: 年级上课情况表
 * \
 */
@Data
@Table(name = "class_grade_info")
public class GradeInfo {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer grade;//年级

    @Column(name = "grade_name")
    private String gradeName;//年级描述名称

    @Column(name = "class_count")
    private Integer classCount;//班级数

    @Column(name = "day_per_week")
    private Integer dayPerWeek;//每周几天课

    @Column(name = "lesson_per_day")
    private Integer lessonPerDay;//每天几节课

    @Column(name = "lesson_at_am")
    private Integer lessonAtAM;//上午课节

    @Column(name = "lesson_at_pm")
    private Integer lessonAtPM;//下午课节

    @Column(name = "null_position")
    private Integer nullPosition;//无课优先安排的课节，默认每天最后一节

}
