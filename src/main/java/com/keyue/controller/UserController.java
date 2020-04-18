package com.keyue.controller;

import com.keyue.common.util.RegexUtil;
import com.keyue.entity.ResultModel;
import com.keyue.service.IBookService;
import com.keyue.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
public class UserController {

    @Autowired
    private IUserService userServiceImpl;

    @RequestMapping(value = "/login.html",method = RequestMethod.GET)
    public String login()
    {
        return "login";
    }

    @RequestMapping(value = "/register.html",method = RequestMethod.GET)
    public String register()
    {
        return "register";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    private ResultModel<String> login(HttpSession session, String username, String password) {
        return userServiceImpl.login(session, username.trim(), password.trim());
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    private ResultModel<String> register(String email, String password, String nickname) {
        return userServiceImpl.register(email.trim(), password, nickname);
    }


}
