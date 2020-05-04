package com.keyue.dao;

import com.keyue.dao.model.MarkedSymbol;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MarkedSymbolMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MarkedSymbol record);

    int insertSelective(MarkedSymbol record);

    MarkedSymbol selectByPrimaryKey(Integer id);

    List<MarkedSymbol> queryMarkedSymbol(@Param("bookNo") String bookNo, @Param("chapterNum") Integer chapterNum);

}