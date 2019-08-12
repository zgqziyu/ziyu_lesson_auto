package com.ziyu.admin.modules.system.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ziyu.admin.modules.base.service.impl.BaseService;
import com.ziyu.admin.modules.system.mapper.SubjectInfoMapper;
import com.ziyu.admin.modules.system.po.SubjectInfo;
import com.ziyu.admin.modules.system.service.SubjectInfoService;

import java.util.List;

/**
 * \* User: ziyu
 * \* Date: 2019/8/1
 * \* Time: 13:04
 * \* Description:
 * \
 */
@Service
public class SubjectInfoServiceImpl extends BaseService<SubjectInfo> implements SubjectInfoService {

    @Autowired
    private SubjectInfoMapper subjectInfoMapper;

    @Override
    public List<SubjectInfo> getQuerySubjectInfoList(Integer subjectGrade){
        List<SubjectInfo> subjectInfoList = subjectInfoMapper.getQuerySubjectInfoList(subjectGrade);
        return subjectInfoList;
    }

    @Override
    public SubjectInfo getEntityById(Long id) {
        return subjectInfoMapper.selectByPrimaryKey(id);
    }
}
