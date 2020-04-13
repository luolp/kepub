package com.keyue.controller;

import com.keyue.dao.model.Book;
import com.keyue.service.IBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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

    @RequestMapping(value = "/get_search_result",method = RequestMethod.GET)
    @ResponseBody
    public List<Book> searchJsonInfo(@RequestParam(value = "page", defaultValue = "1") Integer pageNum,
                                                   @RequestParam(value = "keyword",defaultValue = "") String keyword,
                                                  @RequestParam(value = "categoryid",required = false) Integer categoryId)
    {
        return bookService.searchJsonInfo(pageNum,keyword,categoryId);
    }
}
