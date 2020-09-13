package com.keyue.dao;

import com.keyue.dao.model.UserVisitTimeLog;
import com.keyue.entity.VisitLogData;

import java.util.List;

public interface UserVisitTimeLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserVisitTimeLog record);

    int insertSelective(UserVisitTimeLog record);

    UserVisitTimeLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserVisitTimeLog record);

    int updateByPrimaryKey(UserVisitTimeLog record);

    List<VisitLogData> queryVisitLog(int userId);
}