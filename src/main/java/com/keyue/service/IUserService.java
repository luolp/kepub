package com.keyue.service;

import com.keyue.dao.model.*;
import com.keyue.entity.ResultModel;
import com.keyue.entity.VisitLogRect;
import com.keyue.entity.res.UserRes;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface IUserService {

    ResultModel<String> login(HttpSession session, String username, String password);

    ResultModel<String> register(String email, String password, String nickname);

    ResultModel<String> activate(String code);

    ResultModel<String> getCaptcha(String username);

    ResultModel<String> updatePwd(String username, String code, String password);

    UserRes getUser(int userId);

    ResultModel<String> updateUser(User user);

    List<VisitLogRect> queryVisitLog(int userId, Date regTime);

    ResultModel<String> saveVisitTimeLog(UserVisitTimeLog userVisitTimeLog);

    ResultModel<String> saveDiary(UserDiary userDiary);

    UserDiary findUserDiary(int userId, String date);

    void saveReadHistory(int userId, String bookNo, Integer chaptherNo);

    ResultModel<List<UserReadHistory>> queryReadHistoryByPage(UserReadHistory userReadHistory, Integer page, Integer limit);

    ResultModel<String> delReadHistory(Integer id);
}
