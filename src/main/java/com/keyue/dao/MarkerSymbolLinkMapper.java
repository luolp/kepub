package com.keyue.dao;

import com.keyue.dao.model.MarkerSymbolLink;

public interface MarkerSymbolLinkMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MarkerSymbolLink record);

    int insertSelective(MarkerSymbolLink record);

    MarkerSymbolLink selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MarkerSymbolLink record);

    int updateByPrimaryKey(MarkerSymbolLink record);
}