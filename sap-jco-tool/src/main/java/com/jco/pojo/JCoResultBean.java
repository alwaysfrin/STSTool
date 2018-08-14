package com.jco.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.swagger.annotations.ApiParam;

public class JCoResultBean {

	@ApiParam("是否成功")
	private boolean success;
	
	@ApiParam("处理信息")
	private String msg = "";
	
	@ApiParam("JCO执行后返回")
	private JCoParam jcoParam;

	public JCoResultBean(boolean success, String msg) {
		super();
		this.success = success;
		this.msg = msg;
	}

	public JCoResultBean(boolean success, String msg, JCoParam jcoParam) {
		super();
		this.success = success;
		this.msg = msg;
		this.jcoParam = jcoParam;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public JCoParam getJcoParam() {
		return jcoParam;
	}

	public void setJcoParam(JCoParam jcoParam) {
		this.jcoParam = jcoParam;
	}

	@Override
	public String toString() {
		return "JCoResultBean [success=" + success + ", msg=" + msg + ", jcoParam=" + jcoParam + "]";
	}
}
