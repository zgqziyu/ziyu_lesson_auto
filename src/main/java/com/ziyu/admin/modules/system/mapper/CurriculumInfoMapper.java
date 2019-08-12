package com.ziyu.admin.modules.system.mapper;

import com.ziyu.admin.core.utils.MyMapper;
import com.ziyu.admin.modules.system.po.CurriculumInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * \* User: ziyu
 * \* Date: 2019/8/3
 * \* Time: 9:50
 * \* Description:
 * \
 */
@Mapper
public interface CurriculumInfoMapper extends MyMapper<CurriculumInfo> {
    List<CurriculumInfo> getCurriculumInfoByParams(String gradeId, String classId, Integer weekday);

    @Update("truncate table class_curriculum_info")
    void truncate();
}
