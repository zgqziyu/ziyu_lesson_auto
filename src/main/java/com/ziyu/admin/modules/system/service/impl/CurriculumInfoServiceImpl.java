package com.ziyu.admin.modules.system.service.impl;

import com.ziyu.admin.modules.system.service.CurriculumInfoService;
import com.ziyu.admin.modules.system.service.GradeInfoService;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ziyu.admin.modules.base.service.impl.BaseService;
import com.ziyu.admin.modules.system.mapper.CurriculumInfoMapper;
import com.ziyu.admin.modules.system.po.CurriculumInfo;
import com.ziyu.admin.modules.system.po.GradeInfo;
import com.ziyu.admin.modules.system.service.SubjectInfoService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * \* User: ziyu
 * \* Date: 2019/8/3
 * \* Time: 10:47
 * \* Description:
 * \
 */
@Service
public class CurriculumInfoServiceImpl extends BaseService<CurriculumInfo> implements CurriculumInfoService {

    @Autowired
    private CurriculumInfoMapper curriculumInfoMapper;

    @Autowired
    private GradeInfoService gradeInfoService;

    @Autowired
    private SubjectInfoService subjectInfoService;

    @Override
    public List<List<Map<String, Object>>>  getCurriculumInfoByParams(String gradeId, String classId){
        List<List<Map<String, Object>>> mapListTwo = new ArrayList<List<Map<String, Object>>>();
        GradeInfo gradeInfo = gradeInfoService.selectByGrade(Integer.valueOf(gradeId));
        Integer dayPerWeek = gradeInfo.getDayPerWeek();
        //循环每一天
        for (int i = 0; i < dayPerWeek; i++){
            List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
            //某一天的课程安排
            List<CurriculumInfo> curriculumInfoList = curriculumInfoMapper.getCurriculumInfoByParams(gradeId,classId,i);
            for (CurriculumInfo curriculumInfo : curriculumInfoList) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", curriculumInfo.getId());
                if(curriculumInfo.getSubjectInfoId() != null){
                    map.put("subjectInfoName", subjectInfoService.getEntityById(curriculumInfo.getSubjectInfoId()).getSubjectName());
                }else{
                    map.put("subjectInfoName", "無課");
                }
                map.put("subjectInfoId", curriculumInfo.getSubjectInfoId());
                map.put("userName", curriculumInfo.getUserName());
                mapList.add(map);
            }
            mapListTwo.add(mapList);
        }
        return mapListTwo;
    }

    @Override
    public void truncate(){
        curriculumInfoMapper.truncate();
    }

}
