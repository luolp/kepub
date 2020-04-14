package com.keyue.service;

import com.keyue.dao.model.Book;
import java.util.List;
import java.util.Map;

public interface IBookService {

    Map<String,Object> queryBooksByCateId(Integer cateId);
    Map<String,Object> queryBooks4IndexPage();
    Map<String,Object> queryBooks4Search(String keyword);
    List<Book> searchJsonInfo(Integer page, String keyword, Integer categoryId);
    Book getRandomBook();
    Map<String,Object> queryInfo4Chapter(String bookNo);
    Map<String,Object> queryInfo4Reader(String bookNo, Integer chaptherNum);
    Map<String,Object> queryBooks4AuthorPage(Integer authorId);



}
