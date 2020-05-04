package com.keyue.common.util;

import com.keyue.common.constant.SysConst;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class CommonUtil {

    public static int getUserId(){
        HttpSession httpSession = getSession();
        Integer userId = (Integer) httpSession.getAttribute(SysConst.USER_ID);
        return userId == null ? 0 : userId;
    }

    /**
     * 获取request
     * @return
     */
    public static HttpServletRequest getRequest(){
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        return servletRequestAttributes.getRequest();
    }

    /**
     * 获取response
     * @return
     */
    public static HttpServletResponse getResponse(){
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        return servletRequestAttributes.getResponse();
    }

    /**
     * 获取session
     * @return
     */
    public static HttpSession getSession(){
        return getRequest().getSession();
    }
}
