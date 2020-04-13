package com.keyue.dao;

import com.keyue.dao.model.BookCate;

import java.util.List;

public interface BookCateMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BookCate record);

    int insertSelective(BookCate record);

    BookCate selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BookCate record);

    int updateByPrimaryKey(BookCate record);

    List<BookCate> queryAll();
}