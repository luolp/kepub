package com.keyue.dao;

import com.keyue.dao.model.UserDiary;

public interface UserDiaryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserDiary record);

    int insertSelective(UserDiary record);

    UserDiary selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserDiary record);

    int updateByPrimaryKeyWithBLOBs(UserDiary record);

    int updateByPrimaryKey(UserDiary record);

    UserDiary selectByRecord(UserDiary record);

    UserDiary findByRecord(UserDiary record);
}