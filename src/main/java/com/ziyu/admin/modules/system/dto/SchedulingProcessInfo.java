package com.ziyu.admin.modules.system.dto;

import lombok.Data;

/**
 * \* User: ziyu
 * \* Date: 2019/7/18
 * \* Description:
 * \
 */
@Data
public class SchedulingProcessInfo {
    private ScheduleClassInfo scheduleClassInfo;//班级排课情况
    private Integer weights = 100;//权重
    private boolean isCanSelect = true;
    private Integer cannotSelReason;//0:都可以选；1：因同一老师在其他班上课 2，本科目已经排完 3:没有位置可排：4：外部设置不可排
    private SchedulePositionInfo positionInfo;//老师冲突记录

    public ScheduleClassInfo getScheduleClassInfo() {
        return scheduleClassInfo;
    }

    public void setScheduleClassInfo(ScheduleClassInfo scheduleClassInfo) {
        this.scheduleClassInfo = scheduleClassInfo;
    }

    public Integer getWeights() {
        return weights;
    }

    public void setWeights(Integer weights) {
        this.weights = weights;
    }

    public boolean isCanSelect() {
        return isCanSelect;
    }

    public void setCanSelect(boolean canSelect) {
        isCanSelect = canSelect;
    }

    public Integer getCannotSelReason() {
        return cannotSelReason;
    }

    public void setCannotSelReason(Integer cannotSelReason) {
        this.cannotSelReason = cannotSelReason;
    }

    public SchedulePositionInfo getPositionInfo() {
        return positionInfo;
    }

    public void setPositionInfo(SchedulePositionInfo positionInfo) {
        this.positionInfo = positionInfo;
    }

    public SchedulingProcessInfo(ScheduleClassInfo s, Integer ws, boolean b, Integer csr) {
        this.scheduleClassInfo = s;
        this.weights = ws;
        this.isCanSelect = b;
        this.cannotSelReason = csr;
    }
}
