package com.keyue.dao;

import com.keyue.dao.model.BookChapter;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BookChapterMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BookChapter record);

    int insertSelective(BookChapter record);

    BookChapter selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BookChapter record);

    int updateByPrimaryKey(BookChapter record);

    List<BookChapter> queryChapterListByBookNo(@Param("bookNo") String bookNo);
}