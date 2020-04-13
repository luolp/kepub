package com.keyue.service;

import com.keyue.dao.model.Book;
import java.util.List;
import java.util.Map;

public interface IBookService {

    Map<String,Object> queryBooksByCateId(Integer cateId);
    Map<String,Object> queryBooks4IndexPage();
    Map<String,Object> queryBooks4Search(String keyword);
    List<Book> searchJsonInfo(Integer page, String keyword, Integer categoryId);


}
