package com.keyue.dao;

import com.keyue.dao.model.UserReadHistory;

import java.util.List;

public interface UserReadHistoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserReadHistory record);

    int insertSelective(UserReadHistory record);

    UserReadHistory selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserReadHistory record);

    int updateByPrimaryKey(UserReadHistory record);

    int deleteByRecord(UserReadHistory record);

    List<UserReadHistory> selectList(UserReadHistory userReadHistory);
}