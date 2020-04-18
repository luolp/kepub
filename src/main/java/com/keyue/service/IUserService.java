package com.keyue.service;

import com.keyue.dao.model.Book;
import com.keyue.entity.ResultModel;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

public interface IUserService {

    ResultModel<String> login(HttpSession session, String username, String password);

    ResultModel<String> register(String email, String password, String nickname);
}
