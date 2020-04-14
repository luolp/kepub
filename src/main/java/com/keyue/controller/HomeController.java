package com.keyue.controller;

import com.keyue.dao.model.Book;
import com.keyue.entity.RequestParams4Reader;
import com.keyue.service.IBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@Controller
public class HomeController {

    @Autowired
    private IBookService bookService;

    @RequestMapping(value = "/index.html",method = RequestMethod.GET)
    public String index(Model model)
    {
        Map<String, Object> attrs = bookService.queryBooks4IndexPage();
        model.addAllAttributes(attrs);
        return "index.php";
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

    @RequestMapping(value = "/discover.html",method = RequestMethod.GET)
    public String discover(Model model)
    {
        Book book = bookService.getRandomBook();
        model.addAttribute("book",book);
        return "discover";
    }

    @RequestMapping(value = "/reader.html",method = RequestMethod.GET)
    public String reader(Model model,
                         @RequestParam(value = "bookNo") String bookNo,
                         @RequestParam(value = "chaptherNum", defaultValue = "1") Integer chaptherNum)
    {
        Map<String, Object> attrs = bookService.queryInfo4Reader(bookNo, chaptherNum);
        model.addAllAttributes(attrs);
        return "reader";
    }

    @RequestMapping(value = "/book/{bookNo}",method = RequestMethod.GET)
    public String chapter(@PathVariable(value = "bookNo") String bookNo, Model model)
    {
        Map<String, Object> attrs = bookService.queryInfo4Chapter(bookNo);
        model.addAllAttributes(attrs);
        return "chapter";
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
