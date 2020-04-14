package com.keyue.dao;

import com.keyue.dao.model.Book;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BookMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Book record);

    int insertSelective(Book record);

    Book selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Book record);

    int updateByPrimaryKey(Book record);

    List<Book> queryBooksByCateId(@Param("cateId") Integer cateId, @Param("limitCount") Integer limitCount);

    List<Book> searchBooks(@Param("cateId") Integer cateId,@Param("keyword") String keyword);

    int queryBooksCountByCateId(Integer cateId);

    Book queryBookByBookNo(@Param("bookNo") String bookNo);
}