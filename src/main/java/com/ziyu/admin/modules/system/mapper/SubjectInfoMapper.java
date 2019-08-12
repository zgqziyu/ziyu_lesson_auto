package com.ziyu.admin.modules.system.mapper;

import com.ziyu.admin.core.utils.MyMapper;
import com.ziyu.admin.modules.system.po.SubjectInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SubjectInfoMapper extends MyMapper<SubjectInfo> {
//    int deleteByPrimaryKey(Long id);
//
//    int insert(SubjectInfo record);
//
//    int insertSelective(SubjectInfo record);

//    SubjectInfo selectByPrimaryKey(Long id);

//    int updateByPrimaryKeySelective(SubjectInfo record);
//
//    int updateByPrimaryKey(SubjectInfo record);

    List<SubjectInfo> getQuerySubjectInfoList(Integer subjectGrade);
}