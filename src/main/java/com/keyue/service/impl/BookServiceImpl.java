package com.keyue.service.impl;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.keyue.dao.AuthorMapper;
import com.keyue.dao.BookCateMapper;
import com.keyue.dao.BookMapper;
import com.keyue.dao.model.Author;
import com.keyue.dao.model.Book;
import com.keyue.dao.model.BookCate;
import com.keyue.service.IBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class BookServiceImpl implements IBookService {

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private BookCateMapper bookCateMapper;

    @Override
    public Map<String, Object> queryBooksByCateId(Integer cateId) {
        BookCate bookCate = bookCateMapper.selectByPrimaryKey(cateId);
        int countAll = bookMapper.queryBooksCountByCateId(cateId);
        List<Book> books = bookMapper.queryBooksByCateId(cateId, 15);

        Map<String, Object> retMap = new HashMap<>();
        retMap.put("cateId",cateId);
        retMap.put("bookCateName",bookCate.getCateName());
        retMap.put("books",books);
        retMap.put("countAll",countAll);
        return retMap;
    }

    @Override
    public Map<String, Object> queryBooks4IndexPage() {
        List<BookCate> bookCateList = bookCateMapper.queryAll();
        List<List<Book>> bookLists = new ArrayList<>();
        customizeBookCateList(bookCateList);
        for (int i = 0; i < bookCateList.size(); i++) {
            List<Book> books = bookMapper.queryBooksByCateId(bookCateList.get(i).getId(), 6);
            if(null != books){
                bookLists.add(books);
            }else{
                bookLists.add(new ArrayList<>());
            }
        }

        Map<String, Object> retMap = new HashMap<>();
        retMap.put("bookCateList",bookCateList);
        retMap.put("bookLists",bookLists);
        return retMap;
    }

    @Override
    public Map<String, Object> queryBooks4Search(String keyword) {
        int pageSize = 15;
        Page page = PageHelper.startPage(1, pageSize, true);
        List<Book> books = bookMapper.searchBooks(null,keyword);

        Map<String, Object> retMap = new HashMap<>();
        retMap.put("books",books);
        retMap.put("keyword",keyword);
        retMap.put("countAll",page.getTotal());
        retMap.put("hotSearchList",new ArrayList<String>());
        return retMap;
    }

    @Override
    public List<Book> searchJsonInfo(Integer pageNum, String keyword, Integer categoryId) {
        int pageSize = 15;
        Page page = PageHelper.startPage(pageNum, pageSize, true);
        return bookMapper.searchBooks(categoryId,keyword);

    }

    private void customizeBookCateList(List<BookCate> bookCateList){
        bookCateList.clear();
        bookCateList.add(new BookCate(7,"20世纪中文小说100强"));
        bookCateList.add(new BookCate(1,"现当代文学"));
        bookCateList.add(new BookCate(2,"散文随笔"));
        bookCateList.add(new BookCate(3,"外国文学"));
        bookCateList.add(new BookCate(4,"短篇小说"));
        bookCateList.add(new BookCate(5,"古代文学"));
        bookCateList.add(new BookCate(6,"其他"));
    }
}
