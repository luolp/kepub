package com.keyue.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.keyue.common.enums.ResultCode;

import java.io.Serializable;

public class ResultModel<T> implements Serializable {

	private Integer code;

	private String msg;

	@JsonInclude(Include.NON_NULL)
	private T data;

	public ResultModel() {
		this.code = ResultCode.SUCCESS.getCode();
		this.msg = ResultCode.SUCCESS.getMsg();
	}

	public ResultModel(String msg) {
		this.code = ResultCode.FAILED.getCode();
		this.msg = msg;
	}

	public ResultModel(int code, String msg) {
		this.code = code;
		this.msg=msg;
	}

	/**
	 * 失败返回结果
	 * @param msg 提示信息
	 */
	public static <T> ResultModel<T> failed(String msg) {
		return new ResultModel<T>(ResultCode.FAILED.getCode(), msg);
	}

	/**
	 * 失败返回结果
	 * @param msg 提示信息
	 */
	public static <T> ResultModel<T> failed(Integer code, String msg) {
		return new ResultModel<T>(code, msg);
	}

	/**
	 * 成功返回结果
	 * @param msg 提示信息
	 */
	public static <T> ResultModel<T> success(String msg) {
		return new ResultModel<T>(ResultCode.SUCCESS.getCode(), msg);
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "ResultModel{" +
				"code=" + code +
				", msg='" + msg + '\'' +
				", data=" + data +
				'}';
	}
}
