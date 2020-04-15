package com.keyue.dao;

import com.keyue.dao.model.Marker;

public interface MarkerMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Marker record);

    int insertSelective(Marker record);

    Marker selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Marker record);

    int updateByPrimaryKeyWithBLOBs(Marker record);

    int updateByPrimaryKey(Marker record);
}