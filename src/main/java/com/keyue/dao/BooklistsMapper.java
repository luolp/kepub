package com.keyue.dao;

import com.keyue.dao.model.Booklists;

public interface BooklistsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Booklists record);

    int insertSelective(Booklists record);

    Booklists selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Booklists record);

    int updateByPrimaryKey(Booklists record);
}