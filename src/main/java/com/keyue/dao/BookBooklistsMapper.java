package com.keyue.dao;

import com.keyue.dao.model.BookBooklistsKey;

public interface BookBooklistsMapper {
    int deleteByPrimaryKey(BookBooklistsKey key);

    int insert(BookBooklistsKey record);

    int insertSelective(BookBooklistsKey record);
}