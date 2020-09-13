package com.keyue.service.impl;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.keyue.bookcontent.BookContentManager;
import com.keyue.dao.*;
import com.keyue.dao.model.*;
import com.keyue.entity.ResultModel;
import com.keyue.service.IBookService;
import com.keyue.utils.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
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

    @Autowired
    private MarkedSymbolMapper markedSymbolMapper;

    @Autowired
    private SearchLogMapper searchLogMapper;

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
    public ResultModel<List<Book>> queryBookByPage(Book book, Integer page, Integer limit) {
        ResultModel<List<Book>> rm = new ResultModel<>();
        Page<Book> tPage = PageHelper.startPage(page, limit);
        List<Book> tList = bookMapper.selectList(book);
        rm.setData(tList);
        return rm;
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
        long countAll = 0;
        List<Book> books = new ArrayList<>();
        if (!StringUtils.isEmpty(keyword)) {
            int pageSize = 15;
            Page page = PageHelper.startPage(1, pageSize, true);
            books = bookMapper.searchBooks(null,keyword);
            countAll = page.getTotal();
        }

        // 热门搜索
        List<String> hotSearchList = searchLogMapper.queryHotSearch();

        Map<String, Object> retMap = new HashMap<>();
        retMap.put("books",books);
        retMap.put("keyword",keyword);
        retMap.put("countAll",countAll);
        retMap.put("hotSearchList", hotSearchList);
        return retMap;
    }

    @Override
    public List<Book> searchJsonInfo(Integer pageNum, String keyword, Integer categoryId) {
        int pageSize = 15;
        Page page = PageHelper.startPage(pageNum, pageSize, true);
        return bookMapper.searchBooks(categoryId,keyword);
    }

    @Override
    public Map<String, Object> getRandomChapter(String time) {
        String chapterId = CookieUtil.getCookieValue("discover_" + time);
        BookChapter randomChapter;
        if(StringUtils.isEmpty(chapterId)) {
            randomChapter = bookChapterMapper.randomChapter();
            CookieUtil.setCookie("discover_" + time, String.valueOf(randomChapter.getId()));
        } else {
            randomChapter = bookChapterMapper.selectByPrimaryKey(Integer.valueOf(chapterId));
        }

        Book book = bookMapper.queryBookByBookNo(randomChapter.getBookNo());

        List<MarkedSymbol> markedSymbolList = markedSymbolMapper
                .queryMarkedSymbol(randomChapter.getBookNo(), randomChapter.getChapterNo());

        Map<String, Object> retMap = new HashMap<>();
        retMap.put("book",book);
        retMap.put("currentChapter", randomChapter);
        try {
            retMap.put("content", BookContentManager.getContentV2(bookFileRootPath + randomChapter.getFileUrl(),markedSymbolList));
        }catch (Exception e){
            e.printStackTrace();
        }
        return retMap;
    }

    @Override
    public Map<String, Object> queryInfo4Chapter(String bookNo) {
        Book book = bookMapper.queryBookByBookNo(bookNo);
        List<BookChapter> chapterList = bookChapterMapper.queryChapterListByBookNo(bookNo);
        int totalChapterNum = chapterList.size();

        List<BookChapter> supers = chapterList.stream().filter(perm ->
                perm.getParentNo() == 0)
                .collect(Collectors.toList());

        List<BookChapter> chapterTree = new ArrayList<>();
        if(supers.size() == chapterList.size()) {
            // 只有一级目录，则外面嵌套一个“目录”
            if(supers.size() == totalChapterNum){
                BookChapter topChapter = new BookChapter();
                topChapter.setChapterName("目录");
                topChapter.setChildren(chapterList);
                chapterTree.add(topChapter);
            }
        }else{
            chapterList.removeAll(supers);
            for (BookChapter item : supers) {
                List<BookChapter> child = child(item, chapterList);
                item.setChildren(child);
                chapterTree.add(item);
            }
        }

        if(book == null){
            //todo
        }
        Map<String, Object> retMap = new HashMap<>();
        retMap.put("book",book);
        retMap.put("chapterTree",chapterTree);
        return retMap;
    }

    public List<BookChapter> child(BookChapter bookChapter, List<BookChapter> chapterList) {
        List<BookChapter> childChapter = chapterList.stream().filter(s ->
                s.getParentNo().equals(bookChapter.getChapterNo())).collect(Collectors.toList());
        chapterList.removeAll(childChapter);

        List<BookChapter> chapterTree = new ArrayList<>();
        for (BookChapter item : childChapter) {
            List<BookChapter> child = child(item, chapterList);
            item.setChildren(child);
            chapterTree.add(item);
        }
        return chapterTree;
    }

    @Override
    public Map<String, Object> queryInfo4Reader(String bookNo, Integer chaptherNum) {
        Book book = bookMapper.queryBookByBookNo(bookNo);
        List<BookChapter> chapterList = bookChapterMapper.queryChapterListByBookNo(bookNo);

        if(book == null){
            //todo
        }

        List<BookChapter> preChapters = chapterList.stream().filter(perm ->
                perm.getChapterNo() < chaptherNum && !"".equals(perm.getFileUrl())).collect(Collectors.toList());

        List<BookChapter> currOrNextChapters = chapterList.stream().filter(perm ->
                perm.getChapterNo() >= chaptherNum  && !"".equals(perm.getFileUrl())).collect(Collectors.toList());

        BookChapter preChapter =
                preChapters.size() > 0 ? preChapters.get(preChapters.size() - 1) : null;
        BookChapter currentChapter =
                currOrNextChapters.size() > 0 ? currOrNextChapters.get(0) : null;
        BookChapter nextChapter =
                currOrNextChapters.size() > 1 ? currOrNextChapters.get(1) : null;

        List<MarkedSymbol> markedSymbolList = markedSymbolMapper.queryMarkedSymbol(bookNo,chaptherNum);
//        Map<Integer, List<MarkedSymbol>> markedSymbolListMap = markedSymbolList.stream().collect(Collectors.groupingBy(MarkedSymbol::getChapterNum));


        Map<String, Object> retMap = new HashMap<>();
        retMap.put("book",book);
        retMap.put("preChapter", preChapter);
        retMap.put("nextChapter", nextChapter);
        retMap.put("currentChapter", currentChapter);
        try {
            retMap.put("content", BookContentManager.getContentV2(bookFileRootPath + currentChapter.getFileUrl(),markedSymbolList));
        }catch (Exception e){
            e.printStackTrace();
        }
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

        Map<String, List<Book>> keyOrderedBooksMap = new LinkedHashMap<>();
        keyOrderedBooksMap.put("中长篇", booksMap.get("中长篇"));
        keyOrderedBooksMap.put("短篇", booksMap.get("短篇"));
        keyOrderedBooksMap.put("散文随笔", booksMap.get("散文随笔"));
        keyOrderedBooksMap.put("其他", booksMap.get("其他"));

        Map<String, Object> retMap = new HashMap<>();
        retMap.put("author",author);
        retMap.put("booksMap",keyOrderedBooksMap);
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
