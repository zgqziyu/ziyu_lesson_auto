package com.ziyu.admin.modules.system.service.impl;

import com.ziyu.admin.modules.system.dto.*;
import com.ziyu.admin.modules.system.service.ISubjectChoose;
import com.ziyu.admin.modules.system.service.IWeightCalc;
import lombok.Data;

import java.util.*;

/**
 * \* User: ziyu
 * \* Date: 2019/8/1
 * \* Time: 13:09
 * \* Description:
 * \
 */
@Data
public class ClassScheduling {
    private Map<Integer, GradeInfoDto> gradeInfoMap;//基础信息           //输入信息
    private List<SchedulePositionInfo> udfFixedSubjectList;          //外部初始化
    private Map<Integer, Map<Integer, Map<Integer, Map<Integer, SchedulePositionInfo>>>> scheduleInfos;//课表信息，年级：班级：周天：课节：课程  //最终输出
    private Map<String, Map<Integer, Map<Integer, SchedulePositionInfo>>> teacherSchedule;//老师课程安排  //中间数据
    private Map<Integer, Map<Integer, List<ScheduleClassInfo>>> scheduleClassInfo;//班级课程信息
    private Map<Integer, Map<Integer, Map<Integer, Map<Integer, List<SchedulingProcessInfo>>>>> scheduledInfo;//已经排课位置信息       //中间数据
    private Map<Integer, Integer> scheduledLesson;//已经安排的位置
    private Map<Integer, Map<Integer, Integer>> classFreeSpace;//班级剩余课节数
    private List<String> teacherList;//老师列表
    private Integer gradeCount = 0;//多少个年级
    private Integer classCount = 0;//多少个班级
    private Integer maxDayPerWeek = 0;//做大周天数
    private Integer maxLessonPerDay = 0;//最大节数
    private Integer maxWeight = 0;
    private List<SchedulePositionInfo> onlyOneChoice;

    public ISubjectChoose iSubjectChoose;//科目自定义规则筛选
    public IWeightCalc iWeightCalc;//科目权重外部因素计算

    //开始制作课程表
    public boolean makeLessonTable() {
        //检查gradeInfo
        if (!this.checkInputGradeInfo(this.gradeInfoMap)) {
            return false;
        }
        //检查课程是否可以排满
        if (!this.processNullPos(this.gradeInfoMap)) {
            return false;
        }
        //c初始化老师课程安排信息
        if (!initTeacherList(this.gradeInfoMap)) {
            return false;
        }
        //初始化老师任课信息
        if (!this.initTeacherLesson()) {
            return false;
        }
        //初始化课表信息
        if (!this.initLessonTableInfo(this.gradeInfoMap)) {
            return false;
        }
        //初始化中间排课过程信息
        if (!this.initScheduldInfo(this.gradeInfoMap)) {
            return false;
        }

        this.scheduledLesson = new HashMap<Integer, Integer>();
        this.onlyOneChoice = new ArrayList<SchedulePositionInfo>();
        //优先安排外部设定唯一的科目
        this.firstSetUdfOnlyOneSubject(this.udfFixedSubjectList);

        this.firstSetOnlyChoice();
        //遍历所有课节
        for (Map.Entry<Integer, GradeInfoDto> item : this.gradeInfoMap.entrySet()) {
            for (Map.Entry<Integer, ClassInfo> itemItem : item.getValue().getClassInfos().entrySet()) {
                for (Integer weekDay = 0; weekDay < item.getValue().getDayPerWeek(); weekDay++) {
                    for (Integer lessonNo = 0; lessonNo < item.getValue().getLessonPerDay(); lessonNo++) {
                        //排课
                        /*
                        1、如果已经安排则跳过
                        2、筛选可排科目
                        3、处理科目权重
                        4、安排课程
                        5、预处理后续课程信息
                         */
                        if (this.scheduledLesson.get(this.generateKey(item.getKey(), itemItem.getKey(), weekDay, lessonNo)) != null) {
                            continue;//已经排课
                        }
                        //获取可选科目列表
                        List<SchedulingProcessInfo> lessonValidSubjects = this.fetchValidSubject(item.getKey(), itemItem.getKey(), weekDay, lessonNo);
                        if (iSubjectChoose != null) {
                            List<String> udfSubjects = iSubjectChoose.unSelectSubject(item.getKey(), itemItem.getKey(), weekDay, lessonNo);
                            //合并两个科目列表
                            this.mergeValidSubject(lessonValidSubjects, udfSubjects);
                        }
                        if (lessonValidSubjects.size() == 0) {
                            continue;
                        }

                        this.addScheduldPosition(item.getKey(), itemItem.getKey(), weekDay, lessonNo);
                        this.classFreeSpace.get(item.getKey()).put(itemItem.getKey(), this.classFreeSpace.get(item.getKey()).get(itemItem.getKey()).intValue() - 1);
                        //处理权重
                        this.calcWeights(item.getKey(), itemItem.getKey(), weekDay, lessonNo, lessonValidSubjects);
                        //选择一门课
                        SchedulingProcessInfo selSubject = this.chooseOneSubject(lessonValidSubjects);
                        String subjectTeacher = this.fetchTeacherByKemuAndLesson(item.getKey(), itemItem.getKey(), selSubject.getScheduleClassInfo().getSubjectInfoDto().getSubjectName());
                        //设置课程
                        this.setScheduleTable(item.getKey(), itemItem.getKey(), weekDay, lessonNo, selSubject);
                        //设置预排课信息
                        this.setPreScheduleInfo(item.getKey(), itemItem.getKey(), weekDay, lessonNo, subjectTeacher, selSubject);
                        //如果存在某个位置只有一门课可以安排，则直接安排
                        //this.firstSetOnlyChoice();
                    }
                }
                //班级课程安排完成后，如果有未排课区域，则做自动调课，保证在没有极端冲突的情况下，把课排满
                this.autoMicroChangeLesson(item.getKey(), itemItem.getKey());
            }
        }
        return true;
    }

    //安排外部设置只有一门可选科目的位置
    private void firstSetUdfOnlyOneSubject(List<SchedulePositionInfo> positionInfos) {
        if (positionInfos == null) {
            return;
        }
        for (SchedulePositionInfo info : positionInfos) {
            //设置对应位置的科目
            for (SchedulingProcessInfo item : this.scheduledInfo.get(info.getGrade()).get(info.getClassNo())
                    .get(info.getWeekDay()).get(info.getSeqNo())) {
                if (!info.getSubjectInfoDto().equals(item.getScheduleClassInfo().getSubjectInfoDto())) {
                    item.setCannotSelReason(4);//外部设置
                    item.setCanSelect(false);
                }
            }
            if (this.scheduledLesson.get(this.generateKey(info.getGrade(), info.getClassNo(), info.getWeekDay(), info.getSeqNo())) == null) {
                this.setOnlyOneSubjectChoice(info.getGrade(), info.getClassNo(), info.getWeekDay(), info.getSeqNo());
            }
        }
    }

    //未安排完的科目
    private List<SchedulingProcessInfo> fetchMicroChangeSubjectCanSelect(Integer grade, Integer classNo, Integer weekDay, Integer lessonNo) {
        List<SchedulingProcessInfo> result = new ArrayList<SchedulingProcessInfo>();
        for (SchedulingProcessInfo item : this.scheduledInfo.get(grade).get(classNo).get(weekDay).get(lessonNo)) {
            if (item.getScheduleClassInfo().getUnUsedCount() > 0) {
                result.add(item);
            }
        }
        return result;
    }

    private boolean isSubjectInScheduleProcessList(SubjectInfoDto info, List<SchedulingProcessInfo> infos) {
        for (SchedulingProcessInfo item : infos) {
            if (item.getScheduleClassInfo().getSubjectInfoDto().equals(info)) {
                return true;
            }
        }
        return false;
    }

    private boolean isInfoInScheduleProcessList(SchedulingProcessInfo info, List<SchedulingProcessInfo> infos) {
        for (SchedulingProcessInfo item : infos) {
            if (item.equals(info)) {
                return true;
            }
        }
        return false;
    }

    //此位置可以安排的科目
    //考虑一个老师不给两个半上课则exceptReason=false   不考虑则exceptReason=true
    private List<SchedulingProcessInfo> fetchMicroChangeSubjectValids(Integer grade, Integer classNo, Integer weekDay, Integer lessonNo, boolean exceptReason) {
        List<SchedulingProcessInfo> result = new ArrayList<SchedulingProcessInfo>();
        for (SchedulingProcessInfo item : this.scheduledInfo.get(grade).get(classNo).get(weekDay).get(lessonNo)) {
            if (item.getScheduleClassInfo().getSubjectInfoDto().getSubjectCode() == "wk") {
                continue;
            }
            Integer reason = item.getCannotSelReason();
            if (reason != 1 || exceptReason) {
                int cntDayLesson = 0;
                for (Integer idx = 0; idx < this.gradeInfoMap.get(grade).getLessonPerDay(); idx++) {
                    if (idx == lessonNo) {
                        continue;
                    }
                    if (this.scheduleInfos.get(grade).get(classNo).get(weekDay).get(idx).getSubjectInfoDto() == null) {
                        continue;
                    }
                    if (this.scheduleInfos.get(grade).get(classNo).get(weekDay).get(idx).getSubjectInfoDto().equals(item.getScheduleClassInfo().getSubjectInfoDto())) {
                        cntDayLesson++;
                    }
                }
                if (cntDayLesson >= item.getScheduleClassInfo().getTotalCount() / 1.0 / this.gradeInfoMap.get(grade).getDayPerWeek()) {
                    continue;
                }
                result.add(item);
            }
        }
        return result;
    }


    //调换同一班级两个位置的课程
    private boolean changeLesson(Integer grade, Integer classNo, Integer SourceWeekDay, Integer SourceLessonNo, Integer DestWeekDay, Integer DestLessonNo,
                                 SchedulePositionInfo source, SchedulePositionInfo dest) {
        if (source.getSubjectInfoDto().equals(dest.getSubjectInfoDto())) {
            return false;
        }
        //获取源位置可以安排的科目列表
        List<SchedulingProcessInfo> sourceCanSelectSubjects = this.fetchMicroChangeSubjectValids(grade, classNo, SourceWeekDay, SourceLessonNo, false);
        //获取目标位置可以安排的科目列表
        List<SchedulingProcessInfo> destCanSelectSubjects = this.fetchMicroChangeSubjectValids(grade, classNo, DestWeekDay, DestLessonNo, false);
        if (SourceWeekDay == DestWeekDay) {
            for (SchedulingProcessInfo item : this.scheduledInfo.get(grade).get(classNo).get(SourceWeekDay).get(SourceLessonNo)) {
                if (item.getScheduleClassInfo().getSubjectInfoDto().equals(dest.getSubjectInfoDto())) {
                    sourceCanSelectSubjects.add(item);
                    break;
                }
            }
        }
        if (sourceCanSelectSubjects.size() <= 0 || destCanSelectSubjects.size() <= 0) {
            return false;
        }
        boolean canChangeDestPos = false;
        boolean canChangeSourcePos = false;
        for (SchedulingProcessInfo subjectS : sourceCanSelectSubjects) {//判断目标位置科目是否可以放在源位置
            if (subjectS.getScheduleClassInfo().getSubjectInfoDto().equals(dest.getSubjectInfoDto())) {
                canChangeSourcePos = true;
                break;
            }
        }
        //canChangeSubjects包含item,已经排好的位置可以安排没有安排满的课程
        for (SchedulingProcessInfo subjectD : destCanSelectSubjects) {
            if (subjectD.getScheduleClassInfo().getSubjectInfoDto().equals(source.getSubjectInfoDto())) {
                canChangeDestPos = true;
                break;
            }
        }
        if (!canChangeDestPos || !canChangeSourcePos) {
            return false;
        }
        return true;
    }

    //自动微调
    private void autoMicroChangeLesson(Integer grade, Integer classNo) {
        boolean canChangeDestPos = false;
        boolean canChangeSourcePos = false;
        for (Integer weekDay = 0; weekDay < this.gradeInfoMap.get(grade).getDayPerWeek(); weekDay++) {
            for (Integer lessonNo = 0; lessonNo < this.gradeInfoMap.get(grade).getLessonPerDay(); lessonNo++) {
                if (this.scheduledLesson.get(this.generateKey(grade, classNo, weekDay, lessonNo)) != null) {
                    continue;
                }
                //获取可以安排的科目列表
                List<SchedulingProcessInfo> unFixedSubjects = this.fetchMicroChangeSubjectCanSelect(grade, classNo, weekDay, lessonNo);

                //遍历科目
                boolean canBreak = false;
                for (SchedulingProcessInfo subjectC : unFixedSubjects) {
                    //遍历预备调换位置（调换下午的课程）
                    for (Integer weekDay1 = 0; weekDay1 < this.gradeInfoMap.get(grade).getDayPerWeek(); weekDay1++) {//遍历待替换的位置
                        //for (Integer lessonNo1 = this.gradeInfoMap.get(grade).getLessonAtAM(); lessonNo1 < this.gradeInfoMap.get(grade).getLessonPerDay(); lessonNo1++) {
                        for (Integer lessonNo1 = 1; lessonNo1 < this.gradeInfoMap.get(grade).getLessonPerDay(); lessonNo1++) {
                            boolean isUdfFixedPos = false;
                            for (SchedulePositionInfo sp : this.udfFixedSubjectList) {
                                if (sp.getGrade() == grade && sp.getClassNo() == classNo && sp.getWeekDay() == weekDay1 && sp.getSeqNo() == lessonNo1) {
                                    isUdfFixedPos = true;
                                    break;
                                }
                            }
                            if (isUdfFixedPos) {
                                continue;
                            }
                            SchedulePositionInfo source = new SchedulePositionInfo(grade, classNo, weekDay, lessonNo, subjectC.getScheduleClassInfo().getSubjectInfoDto(),
                                    this.fetchTeacherByKemuAndLesson(grade, classNo, subjectC.getScheduleClassInfo().getSubjectInfoDto().getSubjectName()));
                            //调换同一班级两个位置的课程
                            if (this.changeLesson(grade, classNo, weekDay, lessonNo, weekDay1, lessonNo1,
                                    source,
                                    this.scheduleInfos.get(grade).get(classNo).get(weekDay1).get(lessonNo1))) {

                                this.setScheduleTable(grade, classNo, weekDay, lessonNo, this.scheduleInfos.get(grade).get(classNo).get(weekDay1).get(lessonNo1));
                                this.setScheduleTable(grade, classNo, weekDay1, lessonNo1, source);

                                this.reduceSelfCanUseCount(grade, classNo, weekDay, lessonNo,
                                        this.fetchTeacherByKemuAndLesson(grade, classNo, subjectC.getScheduleClassInfo().getSubjectInfoDto().getSubjectName())
                                        , subjectC);

                                canBreak = true;
                                break;
                            }
                        }
                        if (canBreak) {
                            break;
                        }
                    }
                }
            }
        }
    }

    //优先安排只有一门可排的情况
    private void firstSetOnlyChoice() {
        /*
        1、遍历只有一个可选的位置列表
        2、设置课程
         */
        if (this.onlyOneChoice.size() <= 0) {
            return;
        }
        //for (SchedulePositionInfo item : this.onlyOneChoice) {
        SchedulePositionInfo item = this.onlyOneChoice.get(0);
        this.addScheduldPosition(item.getGrade(), item.getClassNo(), item.getWeekDay(), item.getSeqNo());
        SchedulingProcessInfo subject = null;

        String subjectTeacher = this.fetchTeacherByKemuAndLesson(item.getGrade(), item.getClassNo(), item.getSubjectInfoDto().getSubjectName());
        //设置课程
        for (SchedulingProcessInfo info : this.scheduledInfo.get(item.getGrade()).get(item.getClassNo()).get(item.getWeekDay())
                .get(item.getSeqNo())) {
            if (info.getScheduleClassInfo().getSubjectInfoDto().equals(item.getSubjectInfoDto())) {
                subject = info;
                this.setScheduleTable(item.getGrade(), item.getClassNo(), item.getWeekDay(), item.getSeqNo(), info);
                break;
            }
        }
        //设置预排课信息
        this.onlyOneChoice.remove(item);
        this.setPreScheduleInfo(item.getGrade(), item.getClassNo(), item.getWeekDay(), item.getSeqNo(), subjectTeacher, subject);
        this.firstSetOnlyChoice();
    }

    //设置预排课过程信息
    private void setPreScheduleInfo(Integer grade, Integer classNo, Integer weekDay, Integer lesssonNo, String teacher, SchedulingProcessInfo schedulingProcessInfo) {
        /*
        1、减少可排课程的课节数
        2、安排老师课程
        3、处理其他位置可排课程信息
        4、计算每个科目剩余可排课节数
        5、增加已排位置记录
         */
        for (Map.Entry<Integer, Map<Integer, Map<Integer, Map<Integer, SchedulePositionInfo>>>> item : this.scheduleInfos.entrySet()) {
            for (Map.Entry<Integer, Map<Integer, Map<Integer, SchedulePositionInfo>>> itemItem : item.getValue().entrySet()) {
                //1、减少其他班级本位置同老师科目的freeSpace
                if ((item.getKey() > grade) || (item.getKey() == grade && itemItem.getKey() > classNo)) {
                    this.reduceFreeSpaceCount(item.getKey(), itemItem.getKey(), weekDay, lesssonNo, teacher, schedulingProcessInfo);
                }
                //记录不能排课的原因（同老师其他位置上课，导致不能在本节安排）
            }
        }

        //减少本班剩余课节的freeSpace
        this.reduceSelfFreeSpaceCount(grade, classNo, weekDay, lesssonNo, teacher, schedulingProcessInfo);
        //减少可排课节数
        this.reduceSelfCanUseCount(grade, classNo, weekDay, lesssonNo, teacher, schedulingProcessInfo);
        //处理老师课表
        this.recordTeacherSchedule(grade, classNo, weekDay, lesssonNo, teacher, schedulingProcessInfo);
        //处理各个位置科目可以选择情况
        this.processScheduleSubjectInfo(grade, classNo, weekDay, lesssonNo, teacher);
    }

    //处理单个位置可选科目情况
    private void processScheduleSubjectInfo(Integer grade, Integer classNo, Integer weekDay, Integer lesssonNo, String teacher) {
        for (Map.Entry<Integer, Map<Integer, Map<Integer, Map<Integer, SchedulePositionInfo>>>> item : this.scheduleInfos.entrySet()) {
            for (Map.Entry<Integer, Map<Integer, Map<Integer, SchedulePositionInfo>>> itemItem : item.getValue().entrySet()) {
                for (Map.Entry<Integer, Map<Integer, SchedulePositionInfo>> itemItemItem : itemItem.getValue().entrySet()) {
                    for (Map.Entry<Integer, SchedulePositionInfo> itemItemItemItem : itemItemItem.getValue().entrySet()) {
                        //处理本节课以外的所有位置
                        if (item.getKey() == grade && itemItem.getKey() == classNo &&
                                itemItemItem.getKey() == weekDay && itemItemItemItem.getKey() == lesssonNo) {
                            continue;
                        }
                        //检查科目列表
                        for (SchedulingProcessInfo info : this.scheduledInfo.get(item.getKey()).get(itemItem.getKey())
                                .get(itemItemItem.getKey()).get(itemItemItemItem.getKey())) {
                            //如果本门课此位置不可排 则设置为1.如果可已经拍完，则设置为2
                            if (info.getScheduleClassInfo().getCannotSetPosMap().get(this.generateKey(
                                    item.getKey(), itemItem.getKey(), itemItemItem.getKey(), itemItemItemItem.getKey()
                            )) != null) {
                                info.setCannotSelReason(1);
                                info.setCanSelect(false);
                            }

                            //如果此科目的老师和已经安排的科目一直，则设置position
                            if (weekDay == itemItemItem.getKey() && lesssonNo == itemItemItemItem.getKey()) {
                                if (teacher.equals(this.fetchTeacherByKemuAndLesson(item.getKey(), itemItem.getKey(), info.getScheduleClassInfo().getSubjectInfoDto().getSubjectName()))) {
                                    info.setPositionInfo(new SchedulePositionInfo(grade, classNo, weekDay, lesssonNo, info.getScheduleClassInfo().getSubjectInfoDto(), teacher));
                                }
                            }

                            if (info.getCannotSelReason() == 0) {
                                if (info.getScheduleClassInfo().getUnUsedCount() == 0) {
                                    info.setCannotSelReason(2);
                                    info.setCanSelect(false);
                                }
                                if (info.getScheduleClassInfo().getFreeSpace() <= 0) {//无可排位置
                                    info.setCannotSelReason(3);
                                    info.setCanSelect(false);
                                }
                            }
                        }
                        //如果此位置只有一门课可以选择
                        if (this.scheduledLesson.get(this.generateKey(item.getKey(), itemItem.getKey(), itemItemItem.getKey(), itemItemItemItem.getKey())) == null) {
                            this.setOnlyOneSubjectChoice(item.getKey(), itemItem.getKey(), itemItemItem.getKey(), itemItemItemItem.getKey());
                        }
                    }
                }
            }
        }
    }

    //减少科目可安排课节数
    private void reduceSelfCanUseCount(Integer grade, Integer classNo, Integer weekDay, Integer lesssonNo, String teacher, SchedulingProcessInfo schedulingProcessInfo) {
        for (ScheduleClassInfo subject : this.scheduleClassInfo.get(grade).get(classNo)) {
            //同老师，则减少可排位置数
            if (subject.getSubjectInfoDto().equals(schedulingProcessInfo.getScheduleClassInfo().getSubjectInfoDto())) {
                subject.setUnUsedCount(subject.getUnUsedCount() - 1);
            }
        }
    }

    //减少本班可排区间数
    private void reduceSelfFreeSpaceCount(Integer grade, Integer classNo, Integer weekDay, Integer lesssonNo, String teacher, SchedulingProcessInfo schedulingProcessInfo) {
        for (ScheduleClassInfo subject : this.scheduleClassInfo.get(grade).get(classNo)) {
            //同老师，则减少可排位置数
            if (subject.getCannotSetPosMap().get(this.generateKey(grade, classNo, weekDay, lesssonNo)) == null) {
                subject.setFreeSpace(subject.getFreeSpace() - 1);
            }
        }
    }


    //减少其他班可排区间数
    private void reduceFreeSpaceCount(Integer grade, Integer classNo, Integer weekDay, Integer lesssonNo, String teacher, SchedulingProcessInfo schedulingProcessInfo) {
        for (ScheduleClassInfo subject : this.scheduleClassInfo.get(grade).get(classNo)) {
            //同老师，则减少可排位置数
            if (teacher.equals(this.fetchTeacherByKemuAndLesson(grade, classNo, subject.getSubjectInfoDto().getSubjectName()))) {
                subject.setFreeSpace(subject.getFreeSpace() - 1);
                subject.getCannotSetPosMap().put(this.generateKey(grade, classNo, weekDay, lesssonNo),
                        new SchedulePositionInfo(grade, classNo, weekDay, lesssonNo, null, null));
            }
        }
    }

    //设置只有一门可选的位置
    private void setOnlyOneSubjectChoice(Integer grade, Integer classNo, Integer weekDay, Integer lesssonNo) {
        Integer subjectCount = 0;
        SubjectInfoDto choice = null;
        for (SchedulingProcessInfo subject : this.scheduledInfo.get(grade).get(classNo).get(weekDay).get(lesssonNo)) {
            if (subject.getCannotSelReason() == 0) {
                choice = subject.getScheduleClassInfo().getSubjectInfoDto();
                subjectCount++;
            }
        }
        if (subjectCount > 1) {
            return;
        }
        for (SchedulePositionInfo item : this.onlyOneChoice) {
            if (item.getGrade().equals(grade) && item.getClassNo().equals(classNo) && item.getWeekDay().equals(weekDay) && item.getSeqNo().equals(lesssonNo)) {
                //无课可选，则从中移除
                if (subjectCount == 0) {
                    this.onlyOneChoice.remove(item);
                }
                return;
            }
        }
        if (subjectCount == 1) {
            //检查是否已经在列表
            this.onlyOneChoice.add(new SchedulePositionInfo(grade, classNo, weekDay, lesssonNo, choice,
                    this.fetchTeacherByKemuAndLesson(grade, classNo, choice.getSubjectName())));
        }
    }

    //老师课表
    private void recordTeacherSchedule(Integer grade, Integer classNo, Integer weekDay, Integer lesssonNo, String teacher, SchedulingProcessInfo schedulingProcessInfo) {
        this.teacherSchedule.get(teacher).get(weekDay).get(lesssonNo).setGrade(grade);
        this.teacherSchedule.get(teacher).get(weekDay).get(lesssonNo).setClassNo(classNo);
        this.teacherSchedule.get(teacher).get(weekDay).get(lesssonNo).setWeekDay(weekDay);
        this.teacherSchedule.get(teacher).get(weekDay).get(lesssonNo).setSeqNo(lesssonNo);
        this.teacherSchedule.get(teacher).get(weekDay).get(lesssonNo).setSubjectInfoDto(schedulingProcessInfo.getScheduleClassInfo().getSubjectInfoDto());
    }

    private void addScheduldPosition(Integer grade, Integer classNo, Integer weekDay, Integer lesssonNo) {
        this.scheduledLesson.put(this.generateKey(grade, classNo, weekDay, lesssonNo), 1);
    }

    //设置课程
    private void setScheduleTable(Integer grade, Integer classNo, Integer weekDay, Integer lesssonNo, SchedulingProcessInfo schedulingProcessInfo) {
        this.scheduleInfos.get(grade).get(classNo).get(weekDay).get(lesssonNo).setGrade(grade);
        this.scheduleInfos.get(grade).get(classNo).get(weekDay).get(lesssonNo).setClassNo(classNo);
        this.scheduleInfos.get(grade).get(classNo).get(weekDay).get(lesssonNo).setWeekDay(weekDay);
        this.scheduleInfos.get(grade).get(classNo).get(weekDay).get(lesssonNo).setSeqNo(lesssonNo);
        this.scheduleInfos.get(grade).get(classNo).get(weekDay).get(lesssonNo).setSubjectInfoDto(schedulingProcessInfo.getScheduleClassInfo().getSubjectInfoDto());
        this.scheduleInfos.get(grade).get(classNo).get(weekDay).get(lesssonNo).setTeacher(this.fetchTeacherByKemuAndLesson(grade, classNo,
                schedulingProcessInfo.getScheduleClassInfo().getSubjectInfoDto().getSubjectName()));
    }

    //设置课程 override
    private void setScheduleTable(Integer grade, Integer classNo, Integer weekDay, Integer lesssonNo, SchedulePositionInfo schedulePositionInfo) {
        this.scheduleInfos.get(grade).get(classNo).get(weekDay).get(lesssonNo).setGrade(grade);
        this.scheduleInfos.get(grade).get(classNo).get(weekDay).get(lesssonNo).setClassNo(classNo);
        this.scheduleInfos.get(grade).get(classNo).get(weekDay).get(lesssonNo).setWeekDay(weekDay);
        this.scheduleInfos.get(grade).get(classNo).get(weekDay).get(lesssonNo).setSeqNo(lesssonNo);
        this.scheduleInfos.get(grade).get(classNo).get(weekDay).get(lesssonNo).setSubjectInfoDto(schedulePositionInfo.getSubjectInfoDto());
        this.scheduleInfos.get(grade).get(classNo).get(weekDay).get(lesssonNo).setTeacher(schedulePositionInfo.getTeacher());
    }

    //根据权重选择一门课
    private SchedulingProcessInfo chooseOneSubject(List<SchedulingProcessInfo> schedulingProcessInfos) {
        /*
        1、选择最大的权重
        2、获取最大的权重列表
        3、从其中选择一门课
         */
        List<SchedulingProcessInfo> canChooseSubjects = new ArrayList<SchedulingProcessInfo>();
        for (SchedulingProcessInfo item : schedulingProcessInfos) {
            if (item.getWeights().equals(this.maxWeight)) {
                canChooseSubjects.add(item);
            }
        }
        return canChooseSubjects.get(this.fetchRandomInt(canChooseSubjects.size()));
    }

    //获取随机数
    private Integer fetchRandomInt(Integer maxValue) {
        Random r = new Random();
        return r.nextInt(maxValue);
    }

    //计算所有科目的权重
    private void calcWeights(Integer grade, Integer classNo, Integer weekDay, Integer lesssonNo, List<SchedulingProcessInfo> schedulingProcessInfos) {
        this.maxWeight = 0;
        for (SchedulingProcessInfo item : schedulingProcessInfos) {
            Integer wPlus = this.calcWeight(grade, classNo, weekDay, lesssonNo, schedulingProcessInfos.size(), item);
            item.setWeights(item.getWeights() + wPlus);
            if (item.getWeights() > this.maxWeight) {
                this.maxWeight = item.getWeights();
            }
        }
    }

    //计算每个科目的权重
    private Integer calcWeight(Integer grade, Integer classNo, Integer weekDay, Integer lesssonNo, Integer subjectCount, SchedulingProcessInfo schedulingProcessInfo) {
        Integer result = 0;
        /*
         * 1、计算上下午主副课权重，课程位置、科目、上下午
         * 2、计算连堂权重          尚未安排的科目数、当前天数、已经安排的科目数
         * 3、计算平均分配规则权重  平均天课节  未安排的平均天课节
         * 4、计算可排位置比重权重             可排位置数量、阶梯权重
         * 5、计算从未安排过的科目权重         科目已经安排的数量
         * 6、计算老师平均安排权重             当天老师课节数、平均课节数
         * 7、计算自定义规则权重
         * */
        //第一节主课权重统一
        if (lesssonNo < 3) {
            if (schedulingProcessInfo.getScheduleClassInfo().getSubjectInfoDto().getSubjectAttributes() == 1) {
                result = WeightDefines.SUBJECT_FIRST_LESSON_WEIGHT_BASE;
                result += this.mainSubjectAvgFirstLessson(grade, classNo, weekDay, lesssonNo, schedulingProcessInfo);
            }
        } else {
            result += this.amPmWeight(grade, lesssonNo, schedulingProcessInfo);//上下午权重
            result += this.avgWeekDayWeight(grade, classNo, weekDay, lesssonNo, schedulingProcessInfo);
            result += this.spacePlaceWeight(grade, classNo, weekDay, lesssonNo, subjectCount, schedulingProcessInfo);
            result += this.unFixedSubjectWeight(schedulingProcessInfo);
            result += this.nullSubjectWeight(grade, classNo, weekDay, lesssonNo, schedulingProcessInfo);
        }
        result += this.notSameSubjectWeight(grade, classNo, weekDay, lesssonNo, schedulingProcessInfo);
        result += this.lessonContinueWeight(grade, classNo, weekDay, lesssonNo, schedulingProcessInfo);
        if (result < 0 || result > 1000) result = 0;
        if (iWeightCalc != null) {
            Integer udfWeight = iWeightCalc.weightCalc(grade, classNo, weekDay, lesssonNo, schedulingProcessInfo.getScheduleClassInfo().getSubjectInfoDto().getSubjectName());
            result += udfWeight;
        }
        return result;
    }

    private Integer mainSubjectAvgFirstLessson(Integer grade, Integer classNo, Integer weekDay, Integer lesssonNo, SchedulingProcessInfo schedulingProcessInfo) {
        Integer result = 0;
        Integer firstLessonCount = 0;
        for (Integer w = 0; w < weekDay; w++) {
            if (this.scheduleInfos.get(grade).get(classNo).get(w).get(lesssonNo).getSubjectInfoDto().equals(schedulingProcessInfo.getScheduleClassInfo().getSubjectInfoDto())) {
                firstLessonCount++;
            }
        }
        result -= firstLessonCount * WeightDefines.SUBJECT_FIRST_LESSON_WEIGHT_MICRO;
        return result;

    }

    private Integer nullSubjectWeight(Integer grade, Integer classNo, Integer weekDay, Integer lesssonNo, SchedulingProcessInfo schedulingProcessInfo) {
        Integer result = 0;
        if (schedulingProcessInfo.getScheduleClassInfo().getSubjectInfoDto().getSubjectCode().equals("wk")) {
            if (lesssonNo == this.gradeInfoMap.get(grade).getNullPosition() - 1) {
                result += WeightDefines.SUBJECT_NULL_POSITION;
            } else {
                result -= WeightDefines.SUBJECT_NULL_OTHER;
            }
        }
        return result;
    }

    //同一天不安排同样的课
    private Integer notSameSubjectWeight(Integer grade, Integer classNo, Integer weekDay, Integer lesssonNo, SchedulingProcessInfo schedulingProcessInfo) {
        Integer result = 0;
        if (weekDay == 0) {
            return result;
        }
        if (this.scheduleInfos.get(grade).get(classNo).get(weekDay - 1).get(lesssonNo).getSubjectInfoDto() == null) {
            return 0;
        }
        if (lesssonNo == 0 || lesssonNo == this.gradeInfoMap.get(grade).getLessonAtAM()) {
            if (this.scheduleInfos.get(grade).get(classNo).get(weekDay - 1).get(lesssonNo).getSubjectInfoDto().equals(schedulingProcessInfo.getScheduleClassInfo().getSubjectInfoDto())) {
                result -= WeightDefines.SUBJECT_SEQNO_DIFF;
            }
        }
        return result;
    }

    //从未安排过的科目优先
    private Integer unFixedSubjectWeight(SchedulingProcessInfo schedulingProcessInfo) {
        Integer result = 0;
        if (schedulingProcessInfo.getScheduleClassInfo().getUnUsedCount() == schedulingProcessInfo.getScheduleClassInfo().getTotalCount()) {
            result += WeightDefines.SUBJECT_FIRST_FIXED;
        }
        return result;
    }

    //可排位置权重
    private Integer spacePlaceWeight(Integer grade, Integer classNo, Integer weekDay, Integer lesssonNo, Integer subjectCount, SchedulingProcessInfo schedulingProcessInfo) {
        Integer result = 0;
        Integer unFixedPlaceCount = this.classFreeSpace.get(grade).get(classNo).intValue();
        if (unFixedPlaceCount <= 0) {
            return result;
        }
        double freeSpaceRate = schedulingProcessInfo.getScheduleClassInfo().getFreeSpace() / unFixedPlaceCount;
        if (freeSpaceRate <= 0.8) {
            result += WeightDefines.SUBJECT_FREE_SPACE;
        }
        //剩余课程数大于平均课节数
        if (schedulingProcessInfo.getScheduleClassInfo().getUnUsedCount() > unFixedPlaceCount / 1.0 / this.gradeInfoMap.get(grade).getSubjectInfos().size()) {
            result += WeightDefines.SUBJECT_FREE_SPACE;
        }
        if (schedulingProcessInfo.getScheduleClassInfo().getUnUsedCount() > this.gradeInfoMap.get(grade).getDayPerWeek() - weekDay) {
            result += WeightDefines.SUBJECT_FREE_SPACE * 2;
        }
        return result;
    }

    //平均课节权重
    private Integer avgWeekDayWeight(Integer grade, Integer classNo, Integer weekDay, Integer lesssonNo, SchedulingProcessInfo schedulingProcessInfo) {
        //最后一天 此权重已经无意义
        if (weekDay + 1 == this.gradeInfoMap.get(grade).getDayPerWeek()) {
            return 0;
        }
        //无课科目
        if (schedulingProcessInfo.getScheduleClassInfo().getSubjectInfoDto() == null) {
            return 0;
        }
        Integer result = 0;
        //查看当天是否已经安排
        Integer cntDayLesson = 0;
        Integer cntFreeSpaceInDay = 0;
        for (Integer l = 0; l < this.gradeInfoMap.get(grade).getLessonPerDay(); l++) {
            if (this.scheduleInfos.get(grade).get(classNo).get(weekDay).get(l).getSubjectInfoDto() == null) {
//                for (SchedulingProcessInfo info : this.scheduledInfo.get(grade).get(classNo).get(weekDay).get(l)) {
//                    if (info.getScheduleClassInfo().getSubjectInfo().equals(schedulingProcessInfo.getScheduleClassInfo().getSubjectInfo())) {
//                        if (info.getCannotSelReason() == 0) {
//                            cntFreeSpaceInDay++;
//                        }
//                    }
//                }
                continue;
            }
            if (this.scheduleInfos.get(grade).get(classNo).get(weekDay).get(l).getSubjectInfoDto().equals(schedulingProcessInfo.getScheduleClassInfo().getSubjectInfoDto())) {
                cntDayLesson++;
            }
        }
        double avgFixed = schedulingProcessInfo.getScheduleClassInfo().getTotalCount() / 1.0 / this.gradeInfoMap.get(grade).getDayPerWeek();
        //double avgUnFixed = (schedulingProcessInfo.getScheduleClassInfo().getUnUsedCount() - cntDayLesson) / 1.0 / (this.gradeInfoMap.get(grade).getDayPerWeek() - weekDay);

        //今天已安排节数和平均节数对比
        if (cntDayLesson < avgFixed) {
            result += WeightDefines.SUBJECT_PER_DAY;
        } else {
            result -= WeightDefines.SUBJECT_PER_DAY;
        }
        //todo
        //如果剩余的天数已经不够安排,即剩余课节除以剩余天数大于等于平均课节向上取整

        //if (cntFreeSpaceInDay <= 2)
        {
            if (schedulingProcessInfo.getScheduleClassInfo().getUnUsedCount() / 1.0 / (this.gradeInfoMap.get(grade).getDayPerWeek() - weekDay - 1) > Math.ceil(avgFixed)) {
                result += WeightDefines.SUBJECT_PER_DAY;
            }
        }
        return result;
    }

    private Integer lessonContinueWeight(Integer grade, Integer classNo, Integer weekDay, Integer lesssonNo, SchedulingProcessInfo schedulingProcessInfo) {
        /*
        1、检查未安排的平均课节数
        2、如果大于等于1，且上一节这一门课，则增加权重
         */
        Integer result = 0;
        if (lesssonNo == 0) {
            return result;
        }
        //计算当天本科目已排课节数，如果一排课节数大于等于平均课节数则不处理连堂规则


        double avgFixed = schedulingProcessInfo.getScheduleClassInfo().getTotalCount() / 1.0 / this.gradeInfoMap.get(grade).getDayPerWeek();
        if (avgFixed <= 1) {
            return result;
        }
        Integer cntDayLesson = 0;
        for (Integer l = 0; l < lesssonNo; l++) {
            if (this.scheduleInfos.get(grade).get(classNo).get(weekDay).get(l).getSubjectInfoDto() == null) {
                return 0;
            }
            if (this.scheduleInfos.get(grade).get(classNo).get(weekDay).get(l).getSubjectInfoDto().equals(schedulingProcessInfo.getScheduleClassInfo().getSubjectInfoDto())) {
                cntDayLesson++;
            }
        }
        if (cntDayLesson >= avgFixed) {
            return result;
        }
        double avgUnFix = schedulingProcessInfo.getScheduleClassInfo().getUnUsedCount() / 1.0 / (this.gradeInfoMap.get(grade).getDayPerWeek() - weekDay);
        if (avgUnFix >= 1) {
            //看上一节是否是这门课
            if (this.scheduleInfos.get(grade).get(classNo).get(weekDay).get(lesssonNo - 1).getSubjectInfoDto().equals(schedulingProcessInfo.getScheduleClassInfo().getSubjectInfoDto())) {
                result += WeightDefines.SUBJECT_CONTINUE;
            }
        }
        return result;
    }

    //计算上下午主副课权重
    private Integer amPmWeight(Integer grade, Integer lesssonNo, SchedulingProcessInfo schedulingProcessInfo) {
        Integer result = 0;
        switch (schedulingProcessInfo.getScheduleClassInfo().getSubjectInfoDto().getSubjectAttributes()) {
            case 1:
                if (lesssonNo < this.gradeInfoMap.get(grade).getLessonAtAM()) {
                    result += WeightDefines.SUBJECT_ATTRIBUTES;
                }
                break;
            case 2:
            case 3:
                if (lesssonNo >= this.gradeInfoMap.get(grade).getLessonAtAM()) {
                    result += WeightDefines.SUBJECT_ATTRIBUTES;
                }
                break;
        }
        return result;
    }

    //获取可选科目列表
    private List<SchedulingProcessInfo> fetchValidSubject(Integer grade, Integer classNo, Integer weekDay, Integer lesssonNo) {
        List<SchedulingProcessInfo> result = new ArrayList<SchedulingProcessInfo>();
        for (SchedulingProcessInfo si : this.scheduledInfo.get(grade).get(classNo).get(weekDay).get(lesssonNo)) {
            //检查老师是否在其他班任课
            if (si.getCannotSelReason() == 0) {
                //检查当天次节课已经上了的次数，如果次数大于平均数  则不拍此科目
                Integer cntDayLesson = 0;
                for (Integer idx = 0; idx < lesssonNo; idx++) {
                    if (this.scheduleInfos.get(grade).get(classNo).get(weekDay).get(idx).getSubjectInfoDto() != null) {
                        if (this.scheduleInfos.get(grade).get(classNo).get(weekDay).get(idx).getSubjectInfoDto().equals(si.getScheduleClassInfo().getSubjectInfoDto())) {
                            cntDayLesson++;
                        }
                    }
                }

                if (cntDayLesson / 1.0 >= si.getScheduleClassInfo().getTotalCount() / 1.0 / this.gradeInfoMap.get(grade).getDayPerWeek()) {
                    continue;
                }
                result.add(si);
            }
        }
        return result;
    }

    private List<SchedulingProcessInfo> mergeValidSubject(List<SchedulingProcessInfo> s1, List<String> s2) {
        for (SchedulingProcessInfo item : s1) {
            for (String kk : s2) {
                if (item.getScheduleClassInfo().getSubjectInfoDto().equals(kk)) {
                    s1.remove(item);
                }
            }
        }
        return s1;
    }

    private String fetchTeacherByKemuAndLesson(Integer grade, Integer classNo, String subject) {
        for (Map.Entry<SubjectInfoDto, String> item : this.gradeInfoMap.get(grade).getClassInfos().get(classNo).getSubjectTeachers().entrySet()) {
            if (item.getKey().getSubjectName().equals(subject)) {
                return item.getValue();
            }
        }
        return null;
    }

    //生成位置唯一key，为方便搜索
    private Integer generateKey(Integer grade, Integer classs, Integer weekDay, Integer lessonNo) {
        return (int) ((grade + 1) * Math.pow(10, 6) + (classs + 1) * Math.pow(10, 4) + (weekDay + 1) * Math.pow(10, 2) + lessonNo + 1);
    }

    //检查gradeInfo的信息
    private boolean checkInputGradeInfo(Map<Integer, GradeInfoDto> gradeInfo) {
        if (gradeInfo.size() <= 0) {
            return false;
        }
        for (Map.Entry<Integer, GradeInfoDto> item : gradeInfo.entrySet()) {
            this.gradeCount++;
            this.classCount += item.getValue().getClassCount();
            if (item.getValue().getDayPerWeek() > this.maxDayPerWeek) {
                this.maxDayPerWeek = item.getValue().getDayPerWeek();
            }
            if (item.getValue().getLessonPerDay() > this.maxLessonPerDay) {
                this.maxLessonPerDay = item.getValue().getLessonPerDay();
            }
        }
        return true;
    }

    //检查课程是否可以排满
    private boolean processNullPos(Map<Integer, GradeInfoDto> gradeInfo) {
        if (gradeInfo.size() <= 0) {
            return false;
        }

        for (Map.Entry<Integer, GradeInfoDto> item : gradeInfo.entrySet()) {
            Integer TotalCount = 0;
            for (SubjectInfoDto info : item.getValue().getSubjectInfos()) {
                TotalCount += info.getSubjectCount();
            }
            SubjectInfoDto si = new SubjectInfoDto("wk", "无课", 4,
                    item.getValue().getDayPerWeek() * item.getValue().getLessonPerDay() - TotalCount);
            if (TotalCount < item.getValue().getDayPerWeek() * item.getValue().getLessonPerDay()) {
                item.getValue().getSubjectInfos().add(si);//添加科目名为无课的科目
            }
            //为无课科目添加老师，无课科目所有老师不同，规则为 “wk01010101”
            for (Map.Entry<Integer, ClassInfo> entry : item.getValue().getClassInfos().entrySet()) {
                entry.getValue().getSubjectTeachers().put(si, si.getSubjectCode() + item.getKey().toString() + entry.getKey());
            }
        }
        return true;
    }

    //获取老师列表
    private boolean initTeacherList(Map<Integer, GradeInfoDto> gradeInfo) {
        this.teacherList = new ArrayList<String>();
        for (Map.Entry<Integer, GradeInfoDto> item : gradeInfo.entrySet()) {
            if (item.getValue().getClassInfos().size() <= 0) {
                return false;
            }
            for (Map.Entry<Integer, ClassInfo> cc : item.getValue().getClassInfos().entrySet()) {
                if (cc.getValue().getSubjectTeachers().size() <= 0) {
                    return false;
                }
                for (Map.Entry<SubjectInfoDto, String> ss : cc.getValue().getSubjectTeachers().entrySet()) {
                    if (!this.teacherList.contains(ss.getValue())) {
                        this.teacherList.add(ss.getValue());
                    }
                }
            }
        }
        return true;
    }

    //初始化老师课程安排信息
    private boolean initTeacherLesson() {
        //教师：周几：课程：SchedulePositionInfo
        teacherSchedule = new HashMap<String, Map<Integer, Map<Integer, SchedulePositionInfo>>>();
        //先遍历老师
        for (String teacher : this.teacherList) {
            Map<Integer, Map<Integer, SchedulePositionInfo>> item = new HashMap<Integer, Map<Integer, SchedulePositionInfo>>();
            for (Integer weekDay = 0; weekDay < this.maxDayPerWeek; weekDay++) {
                Map<Integer, SchedulePositionInfo> itemItem = new HashMap<Integer, SchedulePositionInfo>();
                for (Integer lessonNo = 0; lessonNo < this.maxLessonPerDay; lessonNo++) {
                    SchedulePositionInfo itemItemItem = new SchedulePositionInfo(0, 0, weekDay, lessonNo, null, teacher);
                    itemItem.put(lessonNo, itemItemItem);
                }
                item.put(weekDay, itemItem);
            }
            this.teacherSchedule.put(teacher, item);
        }
        return true;
    }


    //初始化课表信息
    private boolean initLessonTableInfo(Map<Integer, GradeInfoDto> gradeInfo) {
        this.scheduleInfos = new HashMap<Integer, Map<Integer, Map<Integer, Map<Integer, SchedulePositionInfo>>>>();
        this.classFreeSpace = new HashMap<Integer, Map<Integer, Integer>>();
        for (Map.Entry<Integer, GradeInfoDto> item : gradeInfo.entrySet()) {
            Map<Integer, Map<Integer, Map<Integer, SchedulePositionInfo>>> grade = new HashMap<Integer, Map<Integer, Map<Integer, SchedulePositionInfo>>>();
            Map<Integer, Integer> classFS = new HashMap<Integer, Integer>();
            for (Map.Entry<Integer, ClassInfo> itemItem : item.getValue().getClassInfos().entrySet()) {
                Map<Integer, Map<Integer, SchedulePositionInfo>> classs = new HashMap<Integer, Map<Integer, SchedulePositionInfo>>();
                classFS.put(itemItem.getKey(), item.getValue().getDayPerWeek() * item.getValue().getLessonPerDay());
                for (Integer weekDay = 0; weekDay < item.getValue().getDayPerWeek(); weekDay++) {
                    Map<Integer, SchedulePositionInfo> weekLesson = new HashMap<Integer, SchedulePositionInfo>();
                    for (Integer lessonNo = 0; lessonNo < item.getValue().getLessonPerDay(); lessonNo++) {
                        SchedulePositionInfo schedulePositionInfo = new SchedulePositionInfo(item.getKey(), itemItem.getKey(), weekDay, lessonNo, null, null);
                        weekLesson.put(lessonNo, schedulePositionInfo);
                    }
                    classs.put(weekDay, weekLesson);
                }
                grade.put(itemItem.getKey(), classs);
            }
            this.classFreeSpace.put(item.getKey(), classFS);
            this.scheduleInfos.put(item.getKey(), grade);
        }
        return true;
    }

    //初始化排课中间过程数据
    private boolean initScheduldInfo(Map<Integer, GradeInfoDto> gradeInfo) {
        this.scheduledInfo = new HashMap<Integer, Map<Integer, Map<Integer, Map<Integer, List<SchedulingProcessInfo>>>>>();
        this.scheduleClassInfo = new HashMap<Integer, Map<Integer, List<ScheduleClassInfo>>>();
        for (Map.Entry<Integer, GradeInfoDto> item : gradeInfo.entrySet()) {
            Map<Integer, Map<Integer, Map<Integer, List<SchedulingProcessInfo>>>> grade = new HashMap<Integer, Map<Integer, Map<Integer, List<SchedulingProcessInfo>>>>();//课节信息
            Map<Integer, List<ScheduleClassInfo>> gradeClass = new HashMap<Integer, List<ScheduleClassInfo>>();//班级信息
            for (Map.Entry<Integer, ClassInfo> itemItem : item.getValue().getClassInfos().entrySet()) {
                Map<Integer, Map<Integer, List<SchedulingProcessInfo>>> classs = new HashMap<Integer, Map<Integer, List<SchedulingProcessInfo>>>();//课节信息
                List<ScheduleClassInfo> classInfo = new ArrayList<ScheduleClassInfo>();//班级信息
                Integer idx = 0;
                for (Map.Entry<SubjectInfoDto, String> subject : itemItem.getValue().getSubjectTeachers().entrySet()) {
                    Map<Integer, SchedulePositionInfo> positionInfoMap = new HashMap<Integer, SchedulePositionInfo>();
                    ScheduleClassInfo classSubject = new ScheduleClassInfo(subject.getKey(), subject.getKey().getSubjectCount(), subject.getKey().getSubjectCount(),
                            item.getValue().getDayPerWeek() * item.getValue().getLessonPerDay(), positionInfoMap);//班级科目列表
                    classInfo.add(idx, classSubject);
                    idx++;
                }
                gradeClass.put(itemItem.getKey(), classInfo);
                for (Integer weekDay = 0; weekDay < item.getValue().getDayPerWeek(); weekDay++) {
                    Map<Integer, List<SchedulingProcessInfo>> weekLesson = new HashMap<Integer, List<SchedulingProcessInfo>>();
                    for (Integer lessonNo = 0; lessonNo < item.getValue().getLessonPerDay(); lessonNo++) {
                        List<SchedulingProcessInfo> schedulingProcessInfos = new ArrayList<SchedulingProcessInfo>();
                        Integer idx1 = 0;
                        for (Map.Entry<SubjectInfoDto, String> subject : itemItem.getValue().getSubjectTeachers().entrySet()) {
                            SchedulingProcessInfo schedulingProcessInfo = new SchedulingProcessInfo(classInfo.get(idx1), WeightDefines.WEIGHT_BASE, true, 0);
                            schedulingProcessInfos.add(schedulingProcessInfo);
                            idx1++;
                        }
                        weekLesson.put(lessonNo, schedulingProcessInfos);
                    }
                    classs.put(weekDay, weekLesson);
                }
                grade.put(itemItem.getKey(), classs);
            }
            this.scheduledInfo.put(item.getKey(), grade);
            this.scheduleClassInfo.put(item.getKey(), gradeClass);
        }
        return true;
    }
}
