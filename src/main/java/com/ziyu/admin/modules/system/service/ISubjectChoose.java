package com.ziyu.admin.modules.system.service;

import java.util.List;

public interface ISubjectChoose {
    public List<String> unSelectSubject(Integer grade, Integer classNo, Integer weekDay, Integer lesssonNo);
}
