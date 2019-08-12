package com.ziyu.admin.modules.system.mapper;

import com.ziyu.admin.core.utils.MyMapper;
import com.ziyu.admin.modules.system.po.GradeInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GradeInfoMapper extends MyMapper<GradeInfo> {
//    int deleteByPrimaryKey(Long id);
//
//    int insert(GradeInfo record);
//
//    int insertSelective(GradeInfo record);
//
//    GradeInfo selectByPrimaryKey(Long id);
//
//    int updateByPrimaryKeySelective(GradeInfo record);
//
//    int updateByPrimaryKey(GradeInfo record);

    GradeInfo selectByGrade(Integer grade);
}