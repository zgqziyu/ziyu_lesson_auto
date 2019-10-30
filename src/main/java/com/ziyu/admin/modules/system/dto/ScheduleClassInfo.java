package com.ziyu.admin.modules.system.dto;

import lombok.Data;

import java.util.Map;
/**
 * \* User: ziyu
 * \* Date: 2019/7/20
 * \* Description:
 * \
 */
@Data
public class ScheduleClassInfo {
    private SubjectInfoDto subjectInfoDto;//科目
    private Integer totalCount;//总课节数
    private Integer unUsedCount;//未排课节数
    private Integer freeSpace;//可排位置数
    private Map<Integer, SchedulePositionInfo> cannotSetPosMap;//本门课不能安排位置的列表

    public SubjectInfoDto getSubjectInfoDto() {
        return subjectInfoDto;
    }

    public void setSubjectInfoDto(SubjectInfoDto subjectInfoDto) {
        this.subjectInfoDto = subjectInfoDto;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getUnUsedCount() {
        return unUsedCount;
    }

    public void setUnUsedCount(Integer unUsedCount) {
        this.unUsedCount = unUsedCount;
    }

    public Integer getFreeSpace() {
        return freeSpace;
    }

    public void setFreeSpace(Integer freeSpace) {
        this.freeSpace = freeSpace;
    }

    public Map<Integer, SchedulePositionInfo> getCannotSetPosMap() {
        return cannotSetPosMap;
    }

    public void setCannotSetPosMap(Map<Integer, SchedulePositionInfo> cannotSetPosMap) {
        this.cannotSetPosMap = cannotSetPosMap;
    }

    public ScheduleClassInfo(SubjectInfoDto si, Integer tc, Integer uuc, Integer fs, Map<Integer, SchedulePositionInfo> csp) {
        this.subjectInfoDto = si;
        this.totalCount = tc;
        this.unUsedCount = uuc;
        this.freeSpace = fs;
        this.cannotSetPosMap = csp;
    }
}
