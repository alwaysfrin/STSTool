package com.jco;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.jco.pojo.JCoParam;
import com.jco.pojo.JCoResultBean;
import com.jco.pojo.JCoTableParam;
import com.jco.service.SAPService;
import com.jco.tool.HttpRequestProxy;
import com.jco.tool.SAPUrlTool;

/**
 * 参考方法
 * @author HP
 *
 */
public class SAPJCoDemo {
    public static final Logger LOGGER = LoggerFactory.getLogger(SAPJCoDemo.class);
    private static final String IP_CONFIG = ""; //"10.86.95.121";    //测试时请打开，不要提交即可，这部分sonar校验会不通过
    public static void main(String[] args) {
    	//测试直接连接
    	//testConnect();
    	//测试网络服务
    	//testWebServer();
    	//测试方法URL请求调用
    	//testSAPTool();
    }

    /**
     * service调用
     */
	public static void testConnect() {
		SAPService sapService = new SAPService();
        try {
            JCoParam jcoParam = new JCoParam();
            //jcoParam.setJcoClientUrl("http://localhost:8889/jco");	//jco工具的路径
            jcoParam.setAshost(IP_CONFIG);
            jcoParam.setSysnr("00");
            jcoParam.setClient("220");
            jcoParam.setUser("ESBRFC");
            jcoParam.setPasswd("init1234");
        	
            LOGGER.info("开始连接sap……");
            // 连接sap（已经存在配置文件）
            /*jCoDestination = sapService.getConnection();*/
            //第二种，通过配置文件动态加载（若配置文件不存在，则生成）
//            JCoDestination jCoDestination = sapService.getConnectionByProp(jcoParam);
//            LOGGER.info("连接信息：" + jCoDestination);
            
            /****************** 普通入参 ***********************/
            /*jcoParam.setFunctionName("ZFII5002_FPJYFH");
            jcoParam.setParameterInputName("WERKS,ZJSNO,BUKRS,LIFNR,CHECKNO,MATNR,ZSTATE,ZTYPE");
            jcoParam.setParameterInputVal("3310,201808,3310,111006,123321,3310SRM3000YCL8,,0");

            JCoTableParam tableOut = new JCoTableParam();
            tableOut.setTableName("ZOUTPUT");
            tableOut.setReturnCellName(new String[] {"WERKS","ZJSNO","BUKRS","LIFNR","CHECKNO","MATNR","ZSTATE","ZTYPE"});
            
            List<JCoTableParam> tableOutList = new ArrayList<JCoTableParam>();
            tableOutList.add(tableOut);

            jcoParam.setTableOutList(tableOutList);
            
            jcoParam = sapService.excuteMethod(jcoParam);
            //LOGGER.info(jcoParam.toString());
            logJCoDetail(jcoParam);*/
            /******************* table in，返回tabl **********************/
            jcoParam.setFunctionName("ZMM_04_MAT_MES2");
            
            //普通入参
            jcoParam.setParameterInputName("P_WERKS");
            jcoParam.setParameterInputVal("3310");

            //表入参
            List<JCoTableParam> tableInList = new ArrayList<JCoTableParam>();
            JCoTableParam tableIn = new JCoTableParam();
            tableIn.setTableName("R_DATUM");
            
            Map<String,String> cell1 = new HashMap<String,String>();
            cell1.put("SIGN", "I");
            cell1.put("OPTION", "BT");
            cell1.put("LOW", "20180701");
            cell1.put("HIGH", "20180731");
            
            Map<String,String> cell2 = new HashMap<String,String>();
            cell2.put("SIGN", "I");
            cell2.put("OPTION", "BT");
            cell2.put("LOW", "20180801");
            cell2.put("HIGH", "20180810");
            
            List<Map<String,String>> cellList = new ArrayList<Map<String,String>>();
            cellList.add(cell1);
            cellList.add(cell2);
            tableIn.setCell(cellList);
            tableInList.add(tableIn);
            jcoParam.setTableInList(tableInList);
            
            //返回的表名和列
            JCoTableParam tableOut = new JCoTableParam();
            tableOut.setTableName("T_TAB");
            tableOut.setReturnCellName(new String[] {"MATNR","MAKTX","BRGEW","MTART","MEINS"});
            
            List<JCoTableParam> tableOutList = new ArrayList<JCoTableParam>();
            tableOutList.add(tableOut);
            jcoParam.setTableOutList(tableOutList);
            
            jcoParam = sapService.excuteMethod(jcoParam);
            //LOGGER.info(jcoParam.toString());
            //logJCoDetail(jcoParam);

            List<JCoTableParam> paramList = jcoParam.getTableOutList();
            for(JCoTableParam param : paramList) {
            	LOGGER.info("返回行数：" + param.getReturnCell().size());
            	for(Map<String,String> detailMap :  param.getReturnCell()) {
            		LOGGER.info("获取MATNR的值：" + detailMap.get("MATNR"));
            	}
            }

            LOGGER.info("连接SAP执行结束……");
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            // e.printStackTrace();
        }
	}
	
	public static void logJCoDetail(JCoParam jcoParam) {
		if(!CollectionUtils.isEmpty(jcoParam.getParameterOutputVal())) {
			LOGGER.info("==========返回参数，数量："+jcoParam.getParameterOutputVal().size()+"========");
			for(String keySet : jcoParam.getParameterOutputVal().keySet()) {
				LOGGER.info(keySet + ":" + jcoParam.getParameterOutputVal().get(keySet));
			}
		}else {
			LOGGER.info("==========没有返回参数========");
		}
		if(!CollectionUtils.isEmpty(jcoParam.getTableOutList())) {
			LOGGER.info("==========返回table，数量："+jcoParam.getTableOutList().size()+"========");
			for(JCoTableParam tableParam : jcoParam.getTableOutList()) {
				LOGGER.info("==========table："+tableParam.getTableName()+"========");
				int i = 0;
				for(Map<String,String> cellMap : tableParam.getReturnCell()) {
					LOGGER.info("==========第"+ (++i)+"条记录========");
					for(String key : cellMap.keySet()) {
						LOGGER.info(key + ":" + cellMap.get(key));
					}
				}
			}
		}else {
			LOGGER.info("==========没有返回table========");
		}
	}

	/**
	 * 网络请求测试
	 */
	public static void testWebServer() {
        HttpRequestProxy proxy = new HttpRequestProxy();

        String url = "http://localhost:8889/jco";
        if (proxy.urlCheckNet(url, 200, 200)) {
            LOGGER.info("已连接:" + url);

            Map<String, String> paramMap = new HashMap<String, String>();
            paramMap.put("ashost",IP_CONFIG);
            paramMap.put("sysnr", "00");
            paramMap.put("client", "220");
            paramMap.put("user", "ESBRFC");
            paramMap.put("passwd", "init1234");
            paramMap.put("lang", "ZH");
            
            paramMap.put("functionName", "ZMM_04_MAT_MES2");
            //普通入参
            paramMap.put("parameterInputName", "P_WERKS");
            paramMap.put("parameterInputVal", "3310");

            //table入参
            paramMap.put("tableInNames", "R_DATUM");
            paramMap.put("tableInValues", "SIGN=I,OPTION=BT,LOW=20180701,HIGH=20180731&SIGN=I,OPTION=BT,LOW=20180801,HIGH=20180810");
            //table出参
            paramMap.put("tableOutNames", "T_TAB");
            paramMap.put("tableOutCellNames", "MATNR,MAKTX,BRGEW,MTART");

            String temp = proxy.doPost(url + "/execute", paramMap);
            //LOGGER.info("查询返回的消息:" + temp);
            JCoResultBean result = JSON.parseObject(temp,JCoResultBean.class);
            //LOGGER.info(result.toString());
            
            //获取指定数据
            List<JCoTableParam> paramList = result.getJcoParam().getTableOutList();
            for(JCoTableParam param : paramList) {
            	LOGGER.info("返回行数：" + param.getReturnCell().size());
            	for(Map<String,String> detailMap :  param.getReturnCell()) {
            		LOGGER.info("获取MATNR的值：" + detailMap.get("MATNR"));
            	}
            }
        } else {
            LOGGER.info("无法连接:" + url);
        }
	}
    
	/**
	 * 工具类方法（网络服务）
	 */
    public static void testSAPTool() {
    	JCoParam jcoParam = new JCoParam();
        jcoParam.setJcoClientUrl("http://localhost:8889/jco");	//jco工具的路径
        jcoParam.setAshost(IP_CONFIG);
        jcoParam.setSysnr("00");
        jcoParam.setClient("220");
        jcoParam.setUser("ESBRFC");
        jcoParam.setPasswd("init1234");
        
        //组装入参
        jcoParam.setFunctionName("ZMM_04_MAT_MES2");
        
        //普通入参
        jcoParam.setParameterInputName("P_WERKS");
        jcoParam.setParameterInputVal("3310");

        //表入参
        List<JCoTableParam> tableInList = new ArrayList<JCoTableParam>();
        JCoTableParam tableIn = new JCoTableParam();
        tableIn.setTableName("R_DATUM");
        
        Map<String,String> cell1 = new HashMap<String,String>();
        cell1.put("SIGN", "I");
        cell1.put("OPTION", "BT");
        cell1.put("LOW", "20180701");
        cell1.put("HIGH", "20180731");
        
        Map<String,String> cell2 = new HashMap<String,String>();
        cell2.put("SIGN", "I");
        cell2.put("OPTION", "BT");
        cell2.put("LOW", "20180801");
        cell2.put("HIGH", "20180810");
        
        List<Map<String,String>> cellList = new ArrayList<Map<String,String>>();
        cellList.add(cell1);
        cellList.add(cell2);
        tableIn.setCell(cellList);
        tableInList.add(tableIn);
        jcoParam.setTableInList(tableInList);
        
        //返回的表名和列
        JCoTableParam tableOut = new JCoTableParam();
        tableOut.setTableName("T_TAB");
        tableOut.setReturnCellName(new String[] {"MATNR","MAKTX","BRGEW","MTART","MEINS"});
        
        List<JCoTableParam> tableOutList = new ArrayList<JCoTableParam>();
        tableOutList.add(tableOut);
        jcoParam.setTableOutList(tableOutList);
        
        SAPUrlTool sapTool = new SAPUrlTool();
        //String result = sapTool.executeJCoMemthod(jcoParam);
        //LOGGER.info(result);
        JCoResultBean resultBean = sapTool.executeJCoMemthodResult(jcoParam);
        //LOGGER.info(resultBean.toString());
        //获取指定数据
        List<JCoTableParam> paramList = resultBean.getJcoParam().getTableOutList();
        for(JCoTableParam param : paramList) {
        	LOGGER.info("返回行数：" + param.getReturnCell().size());
        	for(Map<String,String> detailMap :  param.getReturnCell()) {
        		LOGGER.info("获取MATNR的值：" + detailMap.get("MATNR"));
        	}
        }
    }
}
