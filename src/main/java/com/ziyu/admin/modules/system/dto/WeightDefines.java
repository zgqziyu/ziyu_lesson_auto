package com.ziyu.admin.modules.system.dto;

/**
 * \* User: ziyu
 * \* Date: 2019/7/20
 * \* Description:
 * \
 */
public class WeightDefines {
    public static Integer WEIGHT_BASE = 100;//基础权重
    public static Integer SUBJECT_ATTRIBUTES = 5;//主副科权重
    public static Integer SUBJECT_FIRST_LESSON_WEIGHT_BASE = 15;//第一节课主课优先级更高
    public static Integer SUBJECT_FIRST_LESSON_WEIGHT_MICRO = 1;//第一节课主课优先级更高,第一节主课平均安排
    public static Integer SUBJECT_FREE_SPACE = 1;//可排课节数阶梯权重
    public static Integer SUBJECT_SEQNO_DIFF = 3;//同日上下午第一节课程尽量不同，
    public static Integer SUBJECT_CONTINUE = 30;//连堂绝对优先
    public static Integer SUBJECT_PER_DAY = 3;//平均天课节阶梯权重
    public static Integer SUBJECT_FIRST_FIXED = 2;//从未安排过的科目优先

    public static Integer SUBJECT_NULL_POSITION = 80;//无课位置优先安排权重
    public static Integer SUBJECT_NULL_OTHER = 100;//其他位置不安排权重

}
