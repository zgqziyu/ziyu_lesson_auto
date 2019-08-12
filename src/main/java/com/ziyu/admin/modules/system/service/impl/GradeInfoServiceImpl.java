package com.ziyu.admin.modules.system.service.impl;

import com.ziyu.admin.modules.system.service.GradeInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ziyu.admin.modules.base.service.impl.BaseService;
import com.ziyu.admin.modules.system.mapper.GradeInfoMapper;
import com.ziyu.admin.modules.system.po.GradeInfo;

/**
 * \* User: ziyu
 * \* Date: 2019/8/1
 * \* Time: 13:04
 * \* Description:
 * \
 */
@Service
public class GradeInfoServiceImpl extends BaseService<GradeInfo> implements GradeInfoService {

    @Autowired
    private GradeInfoMapper gradeInfoMapper;

    @Override
    public GradeInfo selectByGrade(Integer grade){
        GradeInfo gradeInfo = gradeInfoMapper.selectByGrade(grade);
        if(gradeInfo != null){
            return gradeInfo;
        }else{
            return null;
        }
    }
}
