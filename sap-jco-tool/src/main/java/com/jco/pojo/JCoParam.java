package com.jco.pojo;

import java.util.List;
import java.util.Map;

/**
 * jco参数类
 * @author HP
 *
 */
public class JCoParam {
	
	private String jcoClientUrl;	//jco工具的路径(http://localhost:8889/jco)

	private String ashost; 	// 服务器ip 10.86.95.121
	private String sysnr; 	// 系统编号 00
	private String client; 	// SAP集团号 220
	private String user;	//用户名
	private String passwd;	//密码
	//private String lang; 	// 语言，ZH
	
	private String functionName;	//调用函数名
	private String parameterInputName;	//执行方法前放入，函数入参名，逗号分隔
	private String parameterInputVal;	//执行方法前放入，函数入参值，逗号分隔

	//执行方法后可获取的函数以及table
	private String parameterOutputName;	//执行方法前放入，函数出参名，逗号分隔
	private Map<String,String> parameterOutputVal;	//执行方法后返回，函数出参值
	
	private List<JCoTableParam> tableInList;	//入参table
	private List<JCoTableParam> tableOutList;	//出参table

	public JCoParam() {
		super();
	}

	public JCoParam(String ashost, String sysnr, String client, String user, String passwd) {
		super();
		this.ashost = ashost;
		this.sysnr = sysnr;
		this.client = client;
		this.user = user;
		this.passwd = passwd;
	}

	public String getAshost() {
		return ashost;
	}

	public void setAshost(String ashost) {
		this.ashost = ashost;
	}

	public String getSysnr() {
		return sysnr;
	}

	public void setSysnr(String sysnr) {
		this.sysnr = sysnr;
	}

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public String getFunctionName() {
		return functionName;
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	public String getParameterInputName() {
		return parameterInputName;
	}

	public void setParameterInputName(String parameterInputName) {
		this.parameterInputName = parameterInputName;
	}

	public String getParameterInputVal() {
		return parameterInputVal;
	}

	public void setParameterInputVal(String parameterInputVal) {
		this.parameterInputVal = parameterInputVal;
	}

	public String getParameterOutputName() {
		return parameterOutputName;
	}

	public void setParameterOutputName(String parameterOutputName) {
		this.parameterOutputName = parameterOutputName;
	}

	public Map<String, String> getParameterOutputVal() {
		return parameterOutputVal;
	}

	public void setParameterOutputVal(Map<String, String> parameterOutputVal) {
		this.parameterOutputVal = parameterOutputVal;
	}

	public List<JCoTableParam> getTableInList() {
		return tableInList;
	}

	public void setTableInList(List<JCoTableParam> tableInList) {
		this.tableInList = tableInList;
	}

	public List<JCoTableParam> getTableOutList() {
		return tableOutList;
	}

	public void setTableOutList(List<JCoTableParam> tableOutList) {
		this.tableOutList = tableOutList;
	}

	public String getJcoClientUrl() {
		return jcoClientUrl;
	}

	public void setJcoClientUrl(String jcoClientUrl) {
		this.jcoClientUrl = jcoClientUrl;
	}

	@Override
	public String toString() {
		return "JCoParam [jcoClientUrl=" + jcoClientUrl + ", ashost=" + ashost + ", sysnr=" + sysnr + ", client="
				+ client + ", user=" + user + ", passwd=" + passwd + ",functionName=" + functionName
				+ ", parameterInputName=" + parameterInputName + ", parameterInputVal=" + parameterInputVal
				+ ", parameterOutputName=" + parameterOutputName + ", tableInList=" + tableInList
				+ ", parameterOutputVal=" + (parameterOutputVal != null ? parameterOutputVal.size() + "" : 0)
				+ ", tableOutList=" + tableOutList + "]";
	}
}
