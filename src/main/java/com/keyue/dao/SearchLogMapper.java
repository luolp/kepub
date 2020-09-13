package com.keyue.dao;

import com.keyue.dao.model.SearchLog;

import java.util.List;

public interface SearchLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SearchLog record);

    int insertSelective(SearchLog record);

    SearchLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SearchLog record);

    int updateByPrimaryKey(SearchLog record);

    List<String> queryHotSearch();
}