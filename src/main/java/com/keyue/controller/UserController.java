package com.keyue.controller;

import com.keyue.common.util.CommonUtil;
import com.keyue.common.util.RegexUtil;
import com.keyue.dao.model.*;
import com.keyue.entity.ResultModel;
import com.keyue.entity.VisitLogRect;
import com.keyue.entity.res.UserRes;
import com.keyue.service.IBookService;
import com.keyue.service.IUserService;
import com.keyue.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

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
        int userId = CommonUtil.getUserId();
        UserRes user = userServiceImpl.getUser(CommonUtil.getUserId());
        List<VisitLogRect> rects = userServiceImpl.queryVisitLog(userId, user.getCreateTime());
        model.addAttribute("user", user);
        model.addAttribute("rects", rects);
        return "center";
    }

    @RequestMapping(value = "/user/userinfo", method = RequestMethod.GET)
    private String userinfo(Model model) {
        UserRes user = userServiceImpl.getUser(CommonUtil.getUserId());
        model.addAttribute("user", user);
        return "userinfo";
    }

    @RequestMapping(value = "/user/update", method = RequestMethod.POST)
    @ResponseBody
    private ResultModel<String> updateUser(User user) {
        user.setId(CommonUtil.getUserId());
        return userServiceImpl.updateUser(user);
    }

    @RequestMapping(value = "/update_nickname.html", method = RequestMethod.GET)
    private String nickname(Model model, String nickname) {
        model.addAttribute("nickname", nickname);
        return "update_nickname";
    }

    @RequestMapping(value = "/update_keno.html", method = RequestMethod.GET)
    private String keno(Model model) {
        UserRes user = userServiceImpl.getUser(CommonUtil.getUserId());
        String keno = user == null ? "" : user.getKeno();
        if(keno != null && keno.startsWith("keid_")){
            user.setCanUpdateKeno(true);
        }

        model.addAttribute("user", user);
        return "update_keno";
    }

    @RequestMapping(value = "/user/saveVisitTimeLog", method = RequestMethod.POST)
    @ResponseBody
    private ResultModel<String> saveVisitTimeLog(UserVisitTimeLog userVisitTimeLog) {
        int userId = CommonUtil.getUserId();
        if (userId > 0) {
            userVisitTimeLog.setUserId(userId);
            return userServiceImpl.saveVisitTimeLog(userVisitTimeLog);
        }
        return ResultModel.success("未登录"); // 用户未登录，不需要保存数据
    }

    @RequestMapping(value = "/user/diary", method = RequestMethod.GET)
    private String diary(Model model,String date) {
        UserDiary userDiary = userServiceImpl.findUserDiary(CommonUtil.getUserId(), date);
        String diaryDateStr = DateUtil.dateToStr(
                DateUtil.strToDate(date, DateUtil.FRONT_DATE_FORMAT_STRING), "yyyy年MM月dd日");
        // 字数
        String content = userDiary.getContent();
        int charNum = 0;
        if (content != null) {
            content = content.replaceAll("\\s", "");
            charNum = content.length();
        }
        // 今天和昨天的才可以编辑
        boolean isEdit = false;
        Date today = new Date();
        Date yesterday = DateUtil.getRelativeDateOfDays(today, -1);
        String todayStr = DateUtil.dateToStr(today, DateUtil.FRONT_DATE_FORMAT_STRING);
        String yesterdayStr = DateUtil.dateToStr(yesterday, DateUtil.FRONT_DATE_FORMAT_STRING);
        if(todayStr.equals(date) || yesterdayStr.equals(date)) {
            isEdit = true;
        }

        model.addAttribute("userDiary", userDiary);
        model.addAttribute("diaryDate", date);
        model.addAttribute("diaryDateStr", diaryDateStr);
        model.addAttribute("charNum", charNum);
        model.addAttribute("isEdit", isEdit);
        return "diary";
    }

    @RequestMapping(value = "/user/saveDiary", method = RequestMethod.POST)
    @ResponseBody
    private ResultModel<String> saveDiary(UserDiary userDiary) {
        userDiary.setUserId(CommonUtil.getUserId());
        return userServiceImpl.saveDiary(userDiary);
    }

    @RequestMapping(value = "/read_history.html", method = RequestMethod.GET)
    private String readHistory(Model model) {
        return "read_history";
    }

    @RequestMapping(value = "/queryReadHistoryByPage",method = RequestMethod.GET)
    @ResponseBody
    public ResultModel<List<UserReadHistory>> queryReadHistoryByPage(UserReadHistory userReadHistory,
                                                                     @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                                     @RequestParam(value = "limit", defaultValue = "15") Integer limit)
    {
        userReadHistory.setUserId(CommonUtil.getUserId());
        return userServiceImpl.queryReadHistoryByPage(userReadHistory, page, limit);
    }

    @RequestMapping(value = "/delReadHistory", method = RequestMethod.POST)
    @ResponseBody
    public ResultModel<String> delReadHistory(Integer id){
        return userServiceImpl.delReadHistory(id);
    }

}
