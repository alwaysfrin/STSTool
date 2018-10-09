package com.opc.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.opc.pojo.ResultBean;
import com.opc.controller.OpcService;
import com.opc.pojo.OpcModel;

import javafish.clients.opc.JOpc;
import javafish.clients.opc.component.OpcGroup;
import javafish.clients.opc.component.OpcItem;
import javafish.clients.opc.variant.Variant;

@Service
public class OpcConnectTool {
	public static final Logger LOGGER = LoggerFactory.getLogger(OpcConnectTool.class);
	
	/**
	 * 查询opcserver数据
	 * @param server
	 * @param model
	 */
	public ResultBean queryOpcServer(OpcModel server) {
		ResultBean resultBean = new ResultBean(false,"");
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
	        OpcGroup queryGroup = new OpcGroup("opcquerygroup", true, OpcModel.UPDATE_RATE, 0.0f);
	        queryGroup.addItem(queryItem);
	        jopc.addGroup(queryGroup);
	        jopc.connect();
	        LOGGER.info("JOPC client is connected...");
	
	        jopc.registerGroups();
	        jopc.registerItem(queryGroup, queryItem);
	        LOGGER.info("OPCGroup are registered...");
	        
	        //此处需要延迟，不然可能获取的数据不准确
	        //Thread.sleep(50);
	        synchronized(this) {
	            this.wait(50);
	        }
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
	        if(responseItem.isQuality()) {
	        	//准确获取数据
	        	resultBean.setSuccess(true);
	        	resultBean.setResult(paramValue);
	        	resultBean.setMsg("获取数据成功。");
	        }else {
	        	//无法准确获取数据
	        	resultBean.setSuccess(false);
	        	resultBean.setResult("");
	        	resultBean.setMsg("无法获取数据。");
	        }
		}catch(Exception e) {
			LOGGER.error(e.getMessage(),e);
        	resultBean.setSuccess(false);
        	resultBean.setResult("");
        	resultBean.setMsg(e.getMessage());
		}finally {
	        JOpc.coUninitialize();
		}
		return resultBean;
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
	        OpcGroup editGroup = new OpcGroup("opcquerygroup", true, OpcModel.UPDATE_RATE, 0.0f);
	        editGroup.addItem(editItem);
	        jopc.addGroup(editGroup);
	        jopc.connect();
	        LOGGER.info("JOPC client is connected...");
	
	        jopc.registerGroups();
	        jopc.registerItem(editGroup, editItem);
	        LOGGER.info("OPCGroup are registered...");
	        
	        OpcItem responseItem = jopc.synchReadItem(editGroup, editItem);
	        responseItem.setValue(new Variant(editValue));
            jopc.synchWriteItem(editGroup,responseItem);
            
            result.setSuccess(true);
            result.setMsg("信息修改成功。");
		}catch(Exception e) {
			LOGGER.error(e.getMessage(),e);
            result.setSuccess(false);
            result.setMsg("修改失败：" + e.getMessage());
		}finally {
	        JOpc.coUninitialize();
		}
		return result;
	}
}
