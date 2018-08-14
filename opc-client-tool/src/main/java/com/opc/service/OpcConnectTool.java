package com.opc.service;

import org.springframework.stereotype.Service;

import com.opc.pojo.ResultBean;
import com.opc.pojo.OpcModel;

import javafish.clients.opc.JOpc;
import javafish.clients.opc.component.OpcGroup;
import javafish.clients.opc.component.OpcItem;
import javafish.clients.opc.variant.Variant;

@Service
public class OpcConnectTool {

	/**
	 * 查询opcserver数据
	 * @param server
	 * @param model
	 */
	public String queryOpcServer(OpcModel server) {
		String paramValue = null;
		JOpc.coInitialize();
		try {
	        JOpc jopc = new JOpc(server.getHost(), server.getServerProgId(), "JOPCQuery");
	        /**
	         * param1 : item Name
	         * param2 : 是否激活查询
	         * param3 : 路径
	         */
	        OpcItem queryItem = new OpcItem(server.getItemName(), true, server.getAccessPath());
	        /**
	         * 实例化分组，添加item
	         * param1 : 组名
	         * param2 : 是否激活查询
	         * param3 : 查询频率（异步使用）
	         * param4 : 不工作占比
	         */
	        OpcGroup queryGroup = new OpcGroup("opcquerygroup", true, 1000, 0.0f);
	        queryGroup.addItem(queryItem);
	        jopc.addGroup(queryGroup);
	        jopc.connect();
	        //System.out.println("JOPC client is connected...");
	
	        jopc.registerGroups();
	        //System.out.println("OPCGroup are registered...");
	        
	        //此处需要延迟，不然可能获取的数据不准确
	        Thread.sleep(50);
	        OpcItem responseItem = jopc.synchReadItem(queryGroup, queryItem);
	        paramValue = responseItem.getValue().toString();
	        
	        /*SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar cal = responseItem.getTimeStamp();
            Date date = cal.getTime();
            
			model.addAttribute("result",responseItem.getItemName() + " : " + responseItem.getValue()
						            + " : " + responseItem.getAccessPath() 
						            + " : " + responseItem.getDataType() 
						            + " : " + Variant.getVariantName(responseItem.getDataType()) 
						            + " : " + sdf.format(date));*/
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
	        JOpc.coUninitialize();
		}
		return paramValue;
	}

	/**
	 * 修改opcserver数据
	 * @param server
	 * @param editValue
	 * @param model
	 */
	public ResultBean editOpcParam(OpcModel server, String editValue) {
		ResultBean result = new ResultBean(false,"");
		JOpc.coInitialize();
		try {
	        JOpc jopc = new JOpc(server.getHost(), server.getServerProgId(), "JOPCQuery");
	        /**
	         * param1 : item Name
	         * param2 : 是否激活查询
	         * param3 : 路径
	         */
	        OpcItem editItem = new OpcItem(server.getItemName(), true, server.getAccessPath());
	        /**
	         * 实例化分组，添加item
	         * param1 : 组名
	         * param2 : 是否激活查询
	         * param3 : 查询频率（异步使用）
	         * param4 : 不工作占比
	         */
	        OpcGroup editGroup = new OpcGroup("opcquerygroup", true, 1000, 0.0f);
	        editGroup.addItem(editItem);
	        jopc.addGroup(editGroup);
	        jopc.connect();
	        //System.out.println("JOPC client is connected...");
	
	        jopc.registerGroups();
	        //System.out.println("OPCGroup are registered...");
	        
	        OpcItem responseItem = jopc.synchReadItem(editGroup, editItem);
	        responseItem.setValue(new Variant(editValue));
            jopc.synchWriteItem(editGroup,responseItem);
            
            result.setSuccess(true);
            result.setMsg("信息修改成功。");
		}catch(Exception e) {
			e.printStackTrace();
            result.setSuccess(true);
            result.setMsg("信息无法修改：" + e.getMessage());
		}finally {
	        JOpc.coUninitialize();
		}
		return result;
	}
}
