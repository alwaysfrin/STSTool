package com.jco.tool;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Joiner;
import com.jco.pojo.JCoParam;
import com.jco.pojo.JCoResultBean;
import com.jco.pojo.JCoTableParam;
import com.sap.conn.jco.JCoTable;

/**
 * 工具类，可直接使用
 * @author HP
 *
 */
public class SAPUrlTool {
    public static final Logger LOGGER = LoggerFactory.getLogger(SAPUrlTool.class);
    
    //请求路径
	public static final String EXECUTE_URL = "/execute";
	
	private HttpRequestProxy proxy = new HttpRequestProxy();
	
	/**
	 * 返回JSON字符串
	 * @param postMap
	 * @return
	 */
	public String executeJCoMemthod(JCoParam jcoParam) {
		JCoResultBean resultBean = new JCoResultBean(false,"");
		if (proxy.urlCheckNet(jcoParam.getJcoClientUrl(), 100, 100)) {
			Map<String,String> postMap = new HashMap<String,String>();
			postMap.put("ashost",jcoParam.getAshost());
	        postMap.put("sysnr", jcoParam.getSysnr());
	        postMap.put("client", jcoParam.getClient());
	        postMap.put("user", jcoParam.getUser());
	        postMap.put("passwd", jcoParam.getPasswd());
	        
	        postMap.put("functionName", jcoParam.getFunctionName());
	        
	        if(!StringUtils.isEmpty(jcoParam.getParameterInputName()) && !StringUtils.isEmpty(jcoParam.getParameterInputVal())) {
		        //普通入参
		        postMap.put("parameterInputName", jcoParam.getParameterInputName());
		        postMap.put("parameterInputVal", jcoParam.getParameterInputVal());
	        }
	        
	        if(!StringUtils.isEmpty(jcoParam.getParameterOutputName())) {
		        //普通出参
		        postMap.put("parameterOutputName", jcoParam.getParameterOutputName());
	        }

	        if(!CollectionUtils.isEmpty(jcoParam.getTableInList())) {
		        //table入参
	        	//将list转换成逗号分隔的字符串
	        	StringBuilder sb = new StringBuilder();
	        	int i = jcoParam.getTableInList().size();
	        	for(JCoTableParam param : jcoParam.getTableInList()) {
	        		sb.append(param.getTableName());
	        		if(--i > 0) {
	        			sb.append(",");
	        		}
	        	}
		        postMap.put("tableInNames", sb.toString());
		        
		        //组合列
		        sb = new StringBuilder();
	        	i = jcoParam.getTableInList().size();
	        	int j = 0;
	        	int k = 0;
		        for(JCoTableParam tableParam : jcoParam.getTableInList()) {
		        	j = tableParam.getCell().size();
		        	for(Map<String,String> cellMap : tableParam.getCell()) {
		        		k = cellMap.keySet().size();
		        		for(String keySet : cellMap.keySet()) {
		        			sb.append(keySet + "=" + cellMap.get(keySet));
		        			if(--k > 0) {
		        				sb.append(",");
		        			}
		        		}
		        		if(--j > 0) {
		        			sb.append("&");
		        		}
		        	}
		        	if(--i > 0) {
		        		sb.append("|");
		        	}
		        }
		        postMap.put("tableInValues", sb.toString());
	        }
	        if(!CollectionUtils.isEmpty(jcoParam.getTableOutList())) {
		        //table出参
	        	//将list转换成逗号分隔的字符串
	        	StringBuilder names = new StringBuilder();
	        	StringBuilder returnCell = new StringBuilder();
	        	int i = jcoParam.getTableOutList().size();
	        	for(JCoTableParam param : jcoParam.getTableOutList()) {
	        		names.append(param.getTableName());
	        		returnCell.append(String.join(",", param.getReturnCellName()));
	        		
	        		if(--i > 0) {
	        			names.append(",");
	        			returnCell.append("|");
	        		}
	        	}
		        postMap.put("tableOutNames", names.toString());
		        postMap.put("tableOutCellNames", returnCell.toString());
	        }
	        
	        for(String ks : postMap.keySet()) {
	        	System.out.println(ks + ":" + postMap.get(ks));
	        }
	        
			return proxy.doPost(jcoParam.getJcoClientUrl() + EXECUTE_URL, postMap);
		}else {
			resultBean = new JCoResultBean(false,"网络【"+jcoParam.getJcoClientUrl()+"】无法连接。");
			return JSON.toJSONString(resultBean);
		}
	}
	
	/**
	 * 返回result对象
	 * @param jcoParam
	 * @return
	 */
	public JCoResultBean executeJCoMemthodResult(JCoParam jcoParam) {
		return JSON.parseObject(executeJCoMemthod(jcoParam),JCoResultBean.class);
	}
}
