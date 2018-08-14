package com.opc.pojo;

import io.swagger.annotations.ApiParam;

public class ResultBean {

	@ApiParam("是否成功")
	private boolean success;
	
	@ApiParam("处理信息")
	private String msg;
	
	@ApiParam("处理结果")
	private String result;

	public ResultBean(boolean success, String msg) {
		super();
		this.success = success;
		this.msg = msg;
	}

	public ResultBean(boolean success, String msg, String result) {
		super();
		this.success = success;
		this.msg = msg;
		this.result = result;
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

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

}
