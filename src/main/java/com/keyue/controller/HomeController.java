package com.keyue.controller;

import com.keyue.common.util.CommonUtil;
import com.keyue.dao.model.Book;
import com.keyue.dao.model.UserReadHistory;
import com.keyue.entity.ResultModel;
import com.keyue.service.IBookService;
import com.keyue.service.IUserService;
import com.keyue.utils.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Controller
public class HomeController {

    @Autowired
    private IBookService bookService;
    @Autowired
    private IUserService userService;

    @RequestMapping(value = {"/", "/index.html"}, method = RequestMethod.GET)
    public String index(Model model)
    {
        Map<String, Object> attrs = bookService.queryBooks4IndexPage();
        model.addAllAttributes(attrs);
        return "index";
    }

    @RequestMapping(value = "/search.html",method = RequestMethod.GET)
    public String search(Model model,@RequestParam(value = "keyword",defaultValue = "") String keyword)
    {
        Map<String, Object> attrs = bookService.queryBooks4Search(keyword);
        model.addAllAttributes(attrs);
        return "search";
    }

    @RequestMapping(value = "/categoryIndex.html",method = RequestMethod.GET)
    public String categoryIndex(Model model, @RequestParam(value = "cateId", defaultValue = "1") Integer cateId)
    {
        Map<String, Object> attrs = bookService.queryBooksByCateId(cateId);
        model.addAllAttributes(attrs);
        return "categoryIndex";
    }

    @RequestMapping(value = "/queryBookByPage",method = RequestMethod.GET)
    @ResponseBody
    public ResultModel<List<Book>> queryBookByPage(Book book,
                                                   @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                   @RequestParam(value = "limit", defaultValue = "15") Integer limit)
    {
        return bookService.queryBookByPage(book, page, limit);
    }


    @RequestMapping(value = "/discover.html",method = RequestMethod.GET)
    public String discover(Model model, String time)
    {
        Map<String, Object> attrs = bookService.getRandomChapter(time);
        model.addAllAttributes(attrs);
        return "discover";
    }

    @RequestMapping(value = "/reader.html",method = RequestMethod.GET)
    public String reader(Model model,
                         @RequestParam(value = "bookNo") String bookNo,
                         @RequestParam(value = "chaptherNum", defaultValue = "1") Integer chaptherNum)
    {
        Map<String, Object> attrs = bookService.queryInfo4Reader(bookNo, chaptherNum);
        model.addAllAttributes(attrs);

        // 保存阅读记录
        int userId = CommonUtil.getUserId();
        if (userId > 0) {
            userService.saveReadHistory(userId, bookNo, chaptherNum);
        }

        return "reader";
    }

    @RequestMapping(value = "/book/{bookNo}",method = RequestMethod.GET)
    public String chapter(@PathVariable(value = "bookNo") String bookNo, Model model)
    {
        Map<String, Object> attrs = bookService.queryInfo4Chapter(bookNo);
        model.addAllAttributes(attrs);
        return "chapter";
    }

    @RequestMapping(value = "/author/{authorId}",method = RequestMethod.GET)
    public String chapter(@PathVariable(value = "authorId") Integer authorId, Model model)
    {
        Map<String, Object> attrs = bookService.queryBooks4AuthorPage(authorId);
        model.addAllAttributes(attrs);
        return "author";
    }

    @RequestMapping(value = "/get_search_result",method = RequestMethod.GET)
    @ResponseBody
    public List<Book> searchJsonInfo(@RequestParam(value = "page", defaultValue = "1") Integer pageNum,
                                                   @RequestParam(value = "keyword",defaultValue = "") String keyword,
                                                  @RequestParam(value = "categoryid",required = false) Integer categoryId)
    {
        return bookService.searchJsonInfo(pageNum,keyword,categoryId);
    }


}
