package com.keyue.common.enums;

/**
 * 接口返回码
 * @author luolp 2019年05月16日 16:04
 */
public enum ResultCode {

    SUCCESS(0, "成功"),
    FAILED(1, "失败");

    private Integer code;
    private String msg;

    ResultCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }
}
