package com.keyue.dao;

import com.keyue.dao.model.BookChapter;

public interface BookChapterMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BookChapter record);

    int insertSelective(BookChapter record);

    BookChapter selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BookChapter record);

    int updateByPrimaryKey(BookChapter record);
}