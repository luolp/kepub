package com.keyue.controller;

import com.keyue.common.util.CommonUtil;
import com.keyue.common.util.RegexUtil;
import com.keyue.dao.model.User;
import com.keyue.entity.ResultModel;
import com.keyue.entity.res.UserRes;
import com.keyue.service.IBookService;
import com.keyue.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    @RequestMapping(value = "/forget.html",method = RequestMethod.GET)
    public String forget()
    {
        return "forget";
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

    @RequestMapping(value = "/user/activate", method = RequestMethod.GET)
    private String activate(Model model, String code) {
        ResultModel<String> result = userServiceImpl.activate(code);
        model.addAttribute("result", result);
        return "activate";
    }

    @RequestMapping(value = "/forget/getCaptcha", method = RequestMethod.GET)
    @ResponseBody
    private ResultModel<String> getCaptcha(String username) {
        return userServiceImpl.getCaptcha(username.trim());
    }

    @RequestMapping(value = "/forget/updatePwd", method = RequestMethod.POST)
    @ResponseBody
    private ResultModel<String> updatePwd(String username, String code, String password) {
        return userServiceImpl.updatePwd(username.trim(), code, password);
    }


    @RequestMapping(value = "/user/center", method = RequestMethod.GET)
    private String center(Model model) {
        UserRes user = userServiceImpl.getUser(CommonUtil.getUserId());
        model.addAttribute("user", user);
        return "center";
    }

    @RequestMapping(value = "/user/userinfo", method = RequestMethod.GET)
    private String userinfo(Model model) {
        UserRes user = userServiceImpl.getUser(CommonUtil.getUserId());
        model.addAttribute("user", user);
        return "userinfo";
    }

}
