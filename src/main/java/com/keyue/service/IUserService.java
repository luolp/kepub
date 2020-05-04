package com.keyue.service;

import com.keyue.dao.model.Book;
import com.keyue.dao.model.User;
import com.keyue.entity.ResultModel;
import com.keyue.entity.res.UserRes;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

public interface IUserService {

    ResultModel<String> login(HttpSession session, String username, String password);

    ResultModel<String> register(String email, String password, String nickname);

    ResultModel<String> activate(String code);

    ResultModel<String> getCaptcha(String username);

    ResultModel<String> updatePwd(String username, String code, String password);

    UserRes getUser(int userId);
}
