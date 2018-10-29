package com.jco.service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.jco.pojo.JCoParam;
import com.jco.pojo.JCoTableParam;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoField;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoMetaData;
import com.sap.conn.jco.JCoParameterList;
import com.sap.conn.jco.JCoRecordMetaData;
import com.sap.conn.jco.JCoTable;
import com.sap.conn.jco.ext.DestinationDataProvider;

/**
 * jco调用核心方法
 * @author frin
 *
 */
@Service
public class SAPService {

    public static final Logger LOGGER = Logger.getLogger(SAPService.class);
    private static final String PROP_NAME = "MES";
    private static final String LANG = "ZH";	//登录语言
    private static final String POOL_CAPACITY = "5"; //最大连接数
    private static final String PEAK_LIMIT = "10"; //最大连接线程
    
    /**
     * 获取与SAP的连接（直接通过描述文件）
     * @return
     */
    public JCoDestination getConnection(){
        JCoDestination destination = null;
        try {
            /**
             * 	从ECC.jcodestination文件中获取连接参数
             * 	SAP此处约定，项目的根目录，查找MES.jcodestination文件
             */
            destination = JCoDestinationManager.getDestination(PROP_NAME);
        } catch (JCoException e) {
            LOGGER.error("无法获取jco连接：" + e.getCause());
        }
        return destination;
    }

    /**
     * 获取SAP连接（若配置文件不存在则自动生成）
     * @return	SAP连接对象
     */
    public JCoDestination getConnectionByProp(JCoParam param){
        JCoDestination destination = null;

        try {
            Properties connectProperties = new Properties();
            connectProperties.setProperty(DestinationDataProvider.JCO_ASHOST, param.getAshost());//服务器
            connectProperties.setProperty(DestinationDataProvider.JCO_SYSNR,  param.getSysnr());        //系统编号
            connectProperties.setProperty(DestinationDataProvider.JCO_CLIENT, param.getClient());       //SAP集团
            connectProperties.setProperty(DestinationDataProvider.JCO_USER,   param.getUser());  //SAP用户名
            connectProperties.setProperty(DestinationDataProvider.JCO_PASSWD, param.getPasswd());     //密码
            connectProperties.setProperty(DestinationDataProvider.JCO_LANG,   LANG);        //登录语言
            connectProperties.setProperty(DestinationDataProvider.JCO_POOL_CAPACITY, POOL_CAPACITY);  //最大连接数
            connectProperties.setProperty(DestinationDataProvider.JCO_PEAK_LIMIT, PEAK_LIMIT);     //最大连接线程

            createDataFile(PROP_NAME, "jcoDestination", connectProperties);

            destination = JCoDestinationManager.getDestination(PROP_NAME);
        } catch (JCoException e) {
            LOGGER.error("Connect SAP fault, error msg: " + e.toString());
        }
        return destination;
    }

    /**
     * 创建SAP接口属性文件。
     * @param name	ABAP管道名称
     * @param suffix	属性文件后缀
     * @param properties	属性文件内容
     */
    private static void createDataFile(String name, String suffix, Properties properties){
        File cfg = new File(name+"."+suffix);
        if(cfg.exists()){
            cfg.deleteOnExit();
        }
        try{
            FileOutputStream fos = new FileOutputStream(cfg, false);
            properties.store(fos, "sap connection information!");
            fos.close();
        }catch (Exception e){
            LOGGER.error("Create Data file fault, error msg: " + e.toString());
            throw new RuntimeException("Unable to create SAP destination file " + cfg.getName(), e);
        }
    }

    /**
     * 打印jcoTable的详细结构信息
     * Table参数作为export parameter
     * JCoTable每一行都是一个JCoStructure，可以通过setRow()设置指针的位置，然后再遍历各个field：
     * @param jcoTable
     */
    public void printJCoTableInfo(JCoTable jcoTable){
        JCoRecordMetaData tableMeta = jcoTable.getRecordMetaData();
        LOGGER.info("===列名===");
        for(int i = 0; i < tableMeta.getFieldCount(); i++){
            LOGGER.info(String.format("%s\t", tableMeta.getName(i)));
        }

        for(int i = 0; i < jcoTable.getNumRows(); i++){
            LOGGER.info("===" + i +"行===");
            jcoTable.setRow(i);

            for(JCoField fld : jcoTable){
                LOGGER.info(String.format("%s\t", fld.getValue()));
            }
        }
    }

    /**
     * 通过paran，可以参数、table入参，并返回参数或table
     * @param jcoParam
     * @return
     * @throws JCoException
     */
    public JCoParam excuteMethod(JCoParam jcoParam) throws JCoException {
    	JCoDestination jCoDestination = getConnectionByProp(jcoParam);
        JCoFunction function = jCoDestination.getRepository().getFunction(jcoParam.getFunctionName());
        
        //普通入参
        if(!StringUtils.isEmpty(jcoParam.getParameterInputName())) {
        	// 传递rfc参数，放入对应的参数名和值
            JCoParameterList importParam = function.getImportParameterList();
            String[] paramInArr = jcoParam.getParameterInputName().split(",");
            String[] paramInValArr = jcoParam.getParameterInputVal().split(",");
            for(int i=0; i<paramInArr.length; i++) {
            	importParam.setValue(paramInArr[i], paramInValArr[i]);
            }
        }
        
        //table入参
        if(!CollectionUtils.isEmpty(jcoParam.getTableInList())) {
        	for(JCoTableParam table : jcoParam.getTableInList()) {
        		JCoTable tableIn = function.getTableParameterList().getTable(table.getTableName());
        		for(Map<String,String> cellMap: table.getCell()) {
        			tableIn.appendRow();
        			for(String keySet : cellMap.keySet()) {
        				//放入每列的值
        				tableIn.setValue(keySet, cellMap.get(keySet));
        			}
        		}
        	}
        }
        
        // 执行RFC
        function.execute(jCoDestination);
        
        //返回普通参数
        if(!StringUtils.isEmpty(jcoParam.getParameterOutputName())) {
            JCoParameterList paramList = function.getExportParameterList();
        	//有参数返回
        	jcoParam.setParameterOutputVal(new HashMap<String,String>());
        	for(String returnName : jcoParam.getParameterOutputName().split(",")) {
            	//执行方法后返回的值
        		jcoParam.getParameterOutputVal().put(returnName,paramList.getString(returnName));
            }
        }

        //返回table参数
        if(!CollectionUtils.isEmpty(jcoParam.getTableOutList())) {
            JCoParameterList tableReturnList = function.getTableParameterList();
            for(JCoTableParam tableOut : jcoParam.getTableOutList()) {
	            JCoTable jcoTable = tableReturnList.getTable(tableOut.getTableName());
		        //打印jcotable详细信息
		        //printJCoTableInfo(jcoTable);
		
	            tableOut.setReturnCell(new ArrayList<Map<String,String>>());
		        //返回结果
		        for (int i = 0; i < jcoTable.getNumRows(); i++) {
		        	jcoTable.setRow(i);
		        	Map<String,String> cellMap = new HashMap<String,String>();
		        	for(String returnName : tableOut.getReturnCellName()) {
		        		cellMap.put(returnName, jcoTable.getString(returnName));
		        	}
		        	tableOut.getReturnCell().add(cellMap);
		        }
            }
        }
        return jcoParam;
    }
}
