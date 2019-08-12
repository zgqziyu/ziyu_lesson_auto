package com.ziyu.admin.modules.system.service;

public interface IWeightCalc {
    public Integer weightCalc(Integer grade, Integer classNo, Integer weekDay, Integer lesssonNo, String subject);
}
