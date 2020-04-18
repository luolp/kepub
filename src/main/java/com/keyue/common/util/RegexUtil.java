package com.keyue.common.util;

import java.util.regex.Pattern;

public class RegexUtil {
    // 正则表达式: 验证手机号
    public static final String REGEX_MOBILE = "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|166|198|199|(147))\\d{8}$";
    // 验证邮箱
    public static final String REGEX_EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
    // 验证汉字
    public static final String REGEX_CHINESE = "^[\u4e00-\u9fa5],{0,}$";
    // 验证身份证
    public static final String REGEX_ID_CARD = "(^\\d{18}$)|(^\\d{15}$)";
    // 验证URL
    public static final String REGEX_URL = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";
    // 验证IP地址
    public static final String REGEX_IP_ADDR = "(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)";
    // 昵称（"a-z", "A-Z", "0-9", "_", "Chinese character",can't end with "_",ength is between 6 to 20）
    // public static final String REGEX_NICKNAME = "^[\\w\\u4e00-\\u9fa5]{6,20}(?<!_)$";
    // 昵称（小于20位的任意字符）
    public static final String REGEX_NICKNAME = "^.{1,20}$";
    // 密码（字母、数字、特殊符号，6~18位)
    public static final String REGEX_PASSWORD = "^[0-9A-Za-z._~!@#$%^&*?\\(\\)\\-+=,]{6,18}$";


    public static boolean isMatch(final String regex, final CharSequence input) {
        return input != null && input.length() > 0 && Pattern.matches(regex, input);
    }

    /**
     * 手机号
     * @param mobile
     * @return
     */
    public static boolean isMobile(String mobile) {
        return isMatch(REGEX_MOBILE, mobile);
    }

    /**
     * 邮箱
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        return isMatch(REGEX_EMAIL, email);
    }

    /**
     * 汉字
     * @param chinese
     * @return
     */
    public static boolean isChinese(String chinese) {
        return isMatch(REGEX_CHINESE, chinese);
    }

    /**
     * 身份证
     * @param idCard
     * @return
     */
    public static boolean isIDCard(String idCard) {
        return isMatch(REGEX_ID_CARD, idCard);
    }

    /**
     * URL
     * @param url
     * @return
     */
    public static boolean isUrl(String url) {
        return isMatch(REGEX_URL, url);
    }

    /**
     * IP地址
     * @param ipAddr
     * @return
     */
    public static boolean isIPAddr(String ipAddr) {
        return isMatch(REGEX_IP_ADDR, ipAddr);
    }

    /**
     * 昵称
     * @param nickname
     * @return
     */
    public static boolean isNickname(String nickname) {
        return isMatch(REGEX_NICKNAME, nickname);
    }

    /**
     * 密码
     * @param password
     * @return
     */
    public static boolean isPassword(String password) {
        return isMatch(REGEX_PASSWORD, password);
    }
}
