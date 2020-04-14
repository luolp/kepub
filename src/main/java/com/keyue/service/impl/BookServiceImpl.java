package com.keyue.service.impl;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.keyue.dao.AuthorMapper;
import com.keyue.dao.BookCateMapper;
import com.keyue.dao.BookChapterMapper;
import com.keyue.dao.BookMapper;
import com.keyue.dao.model.Author;
import com.keyue.dao.model.Book;
import com.keyue.dao.model.BookCate;
import com.keyue.dao.model.BookChapter;
import com.keyue.service.IBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class BookServiceImpl implements IBookService {

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private BookCateMapper bookCateMapper;

    @Autowired
    private BookChapterMapper bookChapterMapper;

    @Autowired
    private AuthorMapper authorMapper;

    @Value(value = "${book_file_root_path}")
    private String bookFileRootPath;

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

    @Override
    public Book getRandomBook() {
        return bookMapper.selectByPrimaryKey(1);   //todo
    }

    @Override
    public Map<String, Object> queryInfo4Chapter(String bookNo) {
        Book book = bookMapper.queryBookByBookNo(bookNo);
        List<BookChapter> chapterList = bookChapterMapper.queryChapterListByBookNo(bookNo);

        if(book == null){
            //todo
        }
        Map<String, Object> retMap = new HashMap<>();
        retMap.put("book",book);
        retMap.put("chapterList",chapterList);
        return retMap;
    }

    @Override
    public Map<String, Object> queryInfo4Reader(String bookNo, Integer chaptherNum) {
        Book book = bookMapper.queryBookByBookNo(bookNo);
        List<BookChapter> chapterList = bookChapterMapper.queryChapterListByBookNo(bookNo);

        if(book == null){
            //todo
        }

        BookChapter currentChapter = chapterList.get(chaptherNum - 1);


        String content = "";
        try{
            InputStream is = new FileInputStream(bookFileRootPath + currentChapter.getFileUrl());   //读文件 临时
            int iAvail = is.available();
            byte[] bytes = new byte[iAvail];
            is.read(bytes);
            content = new String(bytes);
            is.close();
        }catch(Exception e){
            e.printStackTrace();
        }

        Map<String, Object> retMap = new HashMap<>();
        retMap.put("book",book);
        retMap.put("preChapter",null);      //todo
        retMap.put("nextChapter",null);     //todo
        retMap.put("currentChapter",currentChapter);
        retMap.put("content",content);
        return retMap;
    }

    @Override
    public Map<String, Object> queryBooks4AuthorPage(Integer authorId) {
        Author author = authorMapper.selectByPrimaryKey(authorId);
        List<Book> books = bookMapper.queryBooksByAuthorId(authorId);
        Map<String,List<Book> > booksMap = books.stream().collect(Collectors.groupingBy(book ->{
            Integer cateId = book.getCateId();
            switch (cateId){
                case 1: case 3: case 5:
                    return "中长篇";
                case 2:
                    return "散文随笔";
                case 4:
                    return "短篇";
                case 6:
                default:
                    return "其他";
            }
        }));

        Map<String, Object> retMap = new HashMap<>();
        retMap.put("author",author);
        retMap.put("booksMap",booksMap);
        return retMap;
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
