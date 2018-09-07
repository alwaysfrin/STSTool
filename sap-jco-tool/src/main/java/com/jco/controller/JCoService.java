package com.jco.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jco.pojo.JCoParam;
import com.jco.pojo.JCoResultBean;
import com.jco.pojo.JCoTableParam;
import com.jco.service.SAPService;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoParameterList;

import io.swagger.annotations.Api;

@Api(value="/", tags="jco请求")
@RestController
public class JCoService {
    public static final Logger LOGGER = LoggerFactory.getLogger(JCoService.class);

	@Autowired
	private SAPService sapService;

	/**
	 * 执行SAP方法，可使用参数入参，table入参，可返回参数和table
	 * @param param
	 * @param tableInNames	入参表名，逗号分隔
	 * @param tableInValues	入参值，|分隔不同表，&分隔多条记录，逗号分隔不同字段
	 * 		例：key1:value1,key2:value2&key3:value3,key4:value4|key21:value21,key22:value22
	 * @param tableOutNames	出参表名，逗号分隔
	 * @param tableOutCellNames	出参值，|分隔不同表，逗号分隔不同字段，例：cell1,cell2&cell21,cell22
	 * @return
	 */
	@PostMapping(value="/execute")
	public JCoResultBean paramQuery(JCoParam jcoParam,String tableInNames,String tableInValues,
													String tableOutNames,String tableOutCellNames) {
		long startTime = System.currentTimeMillis();
		JCoResultBean result = new JCoResultBean(false,"执行方法出错。");
        try {
    		if(!StringUtils.isEmpty(tableInNames) && !StringUtils.isEmpty(tableInValues) ) {
    			//有table入参
    			List<JCoTableParam> tableInList = new ArrayList<JCoTableParam>();
    			JCoTableParam tableIn = null;
    			List<Map<String,String>> cellList = null;
    			Map<String,String> cellMap = null;
    			String[] cellTmp = null;
    			
    			String[] tableNameArr = tableInNames.split(",");
    			//竖杠|为特殊符号，需要添加斜杠
    			String[] tableValueArr = tableInValues.split("\\|");
    			for(int i=0; i<tableNameArr.length; i++) {
    				tableIn = new JCoTableParam();
    	            cellList = new ArrayList<Map<String,String>>();
    				
    	            tableIn.setTableName(tableNameArr[i]);
    	            for(String rowCountStr : tableValueArr[i].split("&")) {
	    	            cellMap = new HashMap<String,String>();
	    	            for(String cellStr : rowCountStr.split(",")) {
	    	            	cellTmp = cellStr.split("=");
	    	            	cellMap.put(cellTmp[0], cellTmp[1]);
	    	            }
	    	            cellList.add(cellMap);	//放入列集合
    	            }
    	            tableIn.setCell(cellList);	//放入入参表
    	            tableInList.add(tableIn);	//放入入参表集合
    			}
    			jcoParam.setTableInList(tableInList);
    		}
    		
    		if(!StringUtils.isEmpty(tableOutNames) && !StringUtils.isEmpty(tableOutCellNames) ) {
    			//有table出参
    			List<JCoTableParam> tableOutList = new ArrayList<JCoTableParam>();
    			JCoTableParam tableOut = null;
    			
    			String[] tableOutNameArr = tableOutNames.split(",");
    			String[] tableOutCellArr = tableOutCellNames.split("\\|");
    			for(int i=0; i<tableOutNameArr.length; i++) {
    				tableOut = new JCoTableParam();
    				
    				tableOut.setTableName(tableOutNameArr[i]);
    				tableOut.setReturnCellName(tableOutCellArr[i].split(","));
    				tableOutList.add(tableOut);	//放入入参表集合
    			}
    			jcoParam.setTableOutList(tableOutList);
    		}
            
    		jcoParam = sapService.excuteMethod(jcoParam);
            
            result = new JCoResultBean(true,"执行方法成功。");
            result.setJcoParam(jcoParam);
			
            LOGGER.info("共耗时：" + (System.currentTimeMillis() - startTime) + "，返回：" + jcoParam.toString());
        }catch(Exception e) {
			result = new JCoResultBean(false,"执行sap方法出错：" + e.getMessage());
			LOGGER.error(e.getMessage());
			// e.printStackTrace();
        }
		return result;
	}
}
