package com.keyue.dao;

import com.keyue.dao.model.BookBooklists;

public interface BookBooklistsMapper {
    int deleteByPrimaryKey(Integer listsId);

    int insert(BookBooklists record);

    int insertSelective(BookBooklists record);

    BookBooklists selectByPrimaryKey(Integer listsId);

    int updateByPrimaryKeySelective(BookBooklists record);

    int updateByPrimaryKey(BookBooklists record);
}