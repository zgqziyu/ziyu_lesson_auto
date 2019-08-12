package com.ziyu.admin.modules.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ziyu.admin.modules.system.dto.ClassInfo;
import com.ziyu.admin.modules.system.dto.GradeInfoDto;
import com.ziyu.admin.modules.system.dto.SchedulePositionInfo;
import com.ziyu.admin.modules.system.dto.SubjectInfoDto;
import com.ziyu.admin.modules.system.po.CurriculumInfo;
import com.ziyu.admin.modules.system.po.GradeInfo;
import com.ziyu.admin.modules.system.po.SubjectInfo;
import com.ziyu.admin.modules.system.po.User;
import com.ziyu.admin.modules.system.service.impl.ClassScheduling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * \* User: ziyu
 * \* Date: 2019/8/3
 * \* Time: 11:40
 * \* Description: 自动排课功能
 * \
 */
@Service
public class LessonScheduleService {

    @Autowired
    private GradeInfoService gradeInfoService;

    @Autowired
    private SubjectInfoService subjectInfoService;

    @Autowired
    private UserService userService;

    @Autowired
    private CurriculumInfoService curriculumInfoService;

    public void lessonScheduleAuto(){

        ClassScheduling classScheduling = new ClassScheduling();
        Map<Integer, GradeInfoDto> gradeInfo = new HashMap<Integer, GradeInfoDto>();

        //获取年级
        List<GradeInfo> gradeInfoList = gradeInfoService.queryAll();
        List<GradeInfoDto> gradeInfoDtoList = new ArrayList<>();
        gradeInfoList.forEach(gradeInfo1 -> {
            GradeInfoDto gradeInfoDto = new GradeInfoDto(gradeInfo1);
            gradeInfoDtoList.add(gradeInfoDto);
        });


        //优先安排外部设定唯一的科目
        List<SchedulePositionInfo> fixedInfos = new ArrayList<SchedulePositionInfo>();
        //循环每个年级
        for (GradeInfoDto gradeInfoDto : gradeInfoDtoList ) {
            //年级编号：1,2,3,4,5,6
            Integer grade = gradeInfoDto.getGrade();
            //当前年级上课情况
            GradeInfoDto gradeInfoDto1 = gradeInfoDto;
            //课程信息
            List<SubjectInfoDto> subjectInfos = new ArrayList<SubjectInfoDto>();
            Map<Integer, ClassInfo> classInfos = new HashMap<Integer, ClassInfo>();

            //当前年级课程信息
            List<SubjectInfo> subjectInfoList = subjectInfoService.getQuerySubjectInfoList(gradeInfoDto1.getGrade());
            subjectInfoList.forEach(subjectInfo1 -> {
                SubjectInfoDto subjectInfoDto = new SubjectInfoDto(subjectInfo1);
                subjectInfos.add(subjectInfoDto);
            });
            //年级开设科目列表
            gradeInfoDto1.setSubjectInfos(subjectInfos);

            Integer organizationId;
            //TODO 一年级
            if(grade == 1){
                organizationId = 6;
                //循环每个班级
                classInfos = getClassInfosByGrade(classInfos,gradeInfoDto1,subjectInfos,organizationId);
                gradeInfoDto1.setClassInfos(classInfos);
            }
            if(grade == 2){
                organizationId = 7; //二年级
                classInfos = getClassInfosByGrade(classInfos,gradeInfoDto1,subjectInfos,organizationId);
                gradeInfoDto1.setClassInfos(classInfos);
            }
            if(grade == 3){
                organizationId = 8; //三年级
                classInfos = getClassInfosByGrade(classInfos,gradeInfoDto1,subjectInfos,organizationId);
                gradeInfoDto1.setClassInfos(classInfos);
            }
            if(grade == 4){
                organizationId = 9; //四年级
                classInfos = getClassInfosByGrade(classInfos,gradeInfoDto1,subjectInfos,organizationId);
                gradeInfoDto1.setClassInfos(classInfos);
            }
            if(grade == 5){
                organizationId = 10; //无年级
                classInfos = getClassInfosByGrade(classInfos,gradeInfoDto1,subjectInfos,organizationId);
                gradeInfoDto1.setClassInfos(classInfos);
            }
            if(grade == 6){
                organizationId = 11; //六年级
                classInfos = getClassInfosByGrade(classInfos,gradeInfoDto1,subjectInfos,organizationId);
                gradeInfoDto1.setClassInfos(classInfos);
            }

            gradeInfo.put(grade,gradeInfoDto1);
        }

        //优先安排外部设定唯一的科目
//        for (Integer idx = 1; idx <= gradeInfo.size(); idx++) {
//            for (Integer c = 0; c < gradeInfo.get(idx).getClassCount(); c++) {
//                //设置在最后一天，最后一节
//                fixedInfos.add(new SchedulePositionInfo(idx, c, 4, 6, new SubjectInfoDto("tuanhuo", "团体活动", 3, 1), ""));
//            }
//        }
        classScheduling.setUdfFixedSubjectList(fixedInfos);//外部初始化
        classScheduling.setGradeInfoMap(gradeInfo);
        classScheduling.makeLessonTable();//开始制作课程表
        for (Map.Entry<Integer, Map<Integer, Map<Integer, Map<Integer, SchedulePositionInfo>>>> item : classScheduling.getScheduleInfos().entrySet()) {
            for (Map.Entry<Integer, Map<Integer, Map<Integer, SchedulePositionInfo>>> itemItem : item.getValue().entrySet()) {
//                System.out.println("_________________" + item.getKey().toString() + "年级" + itemItem.getKey().toString() + "班__________");
                for (Map.Entry<Integer, Map<Integer, SchedulePositionInfo>> itemItemItem : itemItem.getValue().entrySet()) {
//                    System.out.print("周" + itemItemItem.getKey().toString() + "  ");
                    Integer i = 0;
                    for (Map.Entry<Integer, SchedulePositionInfo> itemItemItemItem : itemItemItem.getValue().entrySet()) {
//                        if (itemItemItemItem.getValue().getSubjectInfoDto() != null) {
//                            //打印课程名称及教师
//                            System.out.print(itemItemItemItem.getValue().getSubjectInfoDto().getSubjectName() + "(" + fillStringLength(itemItemItemItem.getValue().getTeacher().toString(), 3) + ")   ");
//                        } else {
//                            System.out.print("--  ");
//                        }
                        i++;
                        //TODO 将课表信息保存到数据库
                        CurriculumInfo curriculumInfo = new CurriculumInfo();
                        curriculumInfo.setGradeId(item.getKey().toString());
                        curriculumInfo.setClassId(itemItem.getKey().toString());
                        curriculumInfo.setWeekday(itemItemItem.getKey());
                        curriculumInfo.setPitchNum(i);
                        if (itemItemItemItem.getValue().getSubjectInfoDto() != null) {
                            curriculumInfo.setSubjectInfoId(itemItemItemItem.getValue().getSubjectInfoDto().getId());
                            curriculumInfo.setUserName(itemItemItemItem.getValue().getTeacher());
                        }
                        curriculumInfoService.create(curriculumInfo);
                    }
                }
            }
        }

    }

    /**
     * 控制台打印课表对齐处理
     * @param value
     * @param len
     * @return
     */
    private static String fillStringLength(String value, Integer len) {
        String result = value;
        if (value.length() < len) {
            for (Integer l = 0; l < len - value.length(); l++) {
                result += "  ";
            }
        }
        return result;
    }

    /**
     * 通过年级id，科目组id，班级序号 查询领课教师
     * @param organizationId 年级id
     * @param groupId 科目组id
     * @param classNo 班级序号
     * @return
     */
    public String findTeacherClassInfo(Integer organizationId, String groupId, Integer classNo){
        String teacherName = "待定";
        User user = userService.userByParams(Long.valueOf(organizationId),groupId,String.valueOf(classNo));
        if(user != null){
            teacherName = user.getUsername();
        }
        return teacherName;
    }

    /**
     * //课程-教师映射
     * @param subjectInfos
     * @param organizationId 科目组id
     * @param classNo 班级编号
     * @return
     */
    public Map<SubjectInfoDto, String> getSubjectInfoTeachers(List<SubjectInfoDto> subjectInfos,Integer organizationId,Integer classNo){
        Map<SubjectInfoDto, String> subjectTeachers = new HashMap<SubjectInfoDto, String>();
        for (SubjectInfoDto sid : subjectInfos){
            String subjectName = sid.getSubjectName();//课程名称
            String groupId;
            if("语文".equals(subjectName)){
                groupId = "1";
                subjectTeachers.put(sid,findTeacherClassInfo(organizationId, groupId, classNo));
            }
            if("数学".equals(subjectName)){
                groupId = "2";
                subjectTeachers.put(sid,findTeacherClassInfo(organizationId, groupId, classNo));
            }
            if("英语".equals(subjectName)){
                groupId = "3";
                subjectTeachers.put(sid,findTeacherClassInfo(organizationId, groupId, classNo));
            }
            if("体育".equals(subjectName)){
                groupId = "4";
                subjectTeachers.put(sid,findTeacherClassInfo(organizationId, groupId, classNo));
            }
            if("劳动".equals(subjectName)){
                groupId = "5";
                subjectTeachers.put(sid,findTeacherClassInfo(organizationId, groupId, classNo));
            }
            if("生物".equals(subjectName)){
                groupId = "6";
                subjectTeachers.put(sid,findTeacherClassInfo(organizationId, groupId, classNo));
            }
            if("地理".equals(subjectName)){
                groupId = "7";
                subjectTeachers.put(sid,findTeacherClassInfo(organizationId, groupId, classNo));
            }
            if("研学".equals(subjectName)){
                groupId = "8";
                subjectTeachers.put(sid,findTeacherClassInfo(organizationId, groupId, classNo));
            }
            if("历史".equals(subjectName)){
                groupId = "9";
                subjectTeachers.put(sid,findTeacherClassInfo(organizationId, groupId, classNo));
            }
            if("道德".equals(subjectName)){
                groupId = "10";
                subjectTeachers.put(sid,findTeacherClassInfo(organizationId, groupId, classNo));
            }
            if("国学".equals(subjectName)){
                groupId = "11";
                subjectTeachers.put(sid,findTeacherClassInfo(organizationId, groupId, classNo));
            }
            if("健康".equals(subjectName)){
                groupId = "12";
                subjectTeachers.put(sid,findTeacherClassInfo(organizationId, groupId, classNo));
            }
            if("专题".equals(subjectName)){
                groupId = "13";
                subjectTeachers.put(sid,findTeacherClassInfo(organizationId, groupId, classNo));
            }
            if("实践".equals(subjectName)){
                groupId = "14";
                subjectTeachers.put(sid,findTeacherClassInfo(organizationId, groupId, classNo));
            }
            if("足球".equals(subjectName)){
                groupId = "15";
                subjectTeachers.put(sid,findTeacherClassInfo(organizationId, groupId, classNo));
            }
            if("美术".equals(subjectName)){
                groupId = "16";
                subjectTeachers.put(sid,findTeacherClassInfo(organizationId, groupId, classNo));
            }
            if("信息".equals(subjectName)){
                groupId = "17";
                subjectTeachers.put(sid,findTeacherClassInfo(organizationId, groupId, classNo));
            }
            if("团体活动".equals(subjectName)){
                groupId = "18";
                subjectTeachers.put(sid,findTeacherClassInfo(organizationId, groupId, classNo));
            }
            if("音乐".equals(subjectName)){
                groupId = "19";
                subjectTeachers.put(sid,findTeacherClassInfo(organizationId, groupId, classNo));
            }

        }

        return subjectTeachers;
    }

    /**
     * 循环班级，插入教师
     * @param classInfos
     * @param gradeInfoDto1
     * @param subjectInfos
     * @param organizationId
     * @return
     */
    public  Map<Integer, ClassInfo> getClassInfosByGrade(Map<Integer, ClassInfo> classInfos,GradeInfoDto gradeInfoDto1,
                                                         List<SubjectInfoDto> subjectInfos,Integer organizationId){
        //classNo : 班级编号
        for (Integer classNo = 1; classNo <= gradeInfoDto1.getClassCount(); classNo++) {
            ClassInfo classInfo = new ClassInfo();
            classInfo.setClassName(classNo.toString());
            //课程-教师映射
            //TODO 将String 改为 Long(userId)，先不修改
            Map<SubjectInfoDto, String> subjectTeachers = getSubjectInfoTeachers(subjectInfos, organizationId, classNo);
            classInfo.setSubjectTeachers(subjectTeachers);
            classInfos.put(classNo, classInfo);
        }
        return classInfos;
    }


}
