package com.jco.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.jco.pojo.JCoParam;
import com.jco.service.SAPService;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoFunctionTemplate;

@Controller
public class JCoServiceFront {
    public static final Logger LOGGER = LoggerFactory.getLogger(JCoServiceFront.class);

	@Autowired
	private SAPService sapService;

	@GetMapping(value="/index")
	public String index(Model model) {
		model.addAttribute("javalib", "java lib : "+System.getProperty("java.library.path"));
		model.addAttribute("result","");
		
		return "index";
	}

	@PostMapping(value="/test-connection")
	public String testConnection(JCoParam param,String functionName,Model model) {
		long startTime = System.currentTimeMillis();
        
        try {
    		StringBuffer sb = new StringBuffer();
    		JCoDestination jCoDestination = sapService.getConnectionByProp(param);
    		if(jCoDestination != null) {
    			sb.append("已连接sap："+jCoDestination+"，");
	    		JCoFunctionTemplate funTemplate = jCoDestination.getRepository().getFunctionTemplate(functionName);
	    		if(funTemplate != null) {
	    			sb.append("已获取方法，");
	    			JCoFunction function = funTemplate.getFunction();
	    			if(function != null) {
	    				sb.append("方法正常：" + function);
	    			}else {
	    				sb.append("方法不存在：" + functionName);
	    			}
	    		}else {
	    			sb.append("无法获取方法，请检查方法名:" + functionName);
	    		}
    		}else {
    			sb.append("无法连接sap。");
    		}
    		
			model.addAttribute("result", "共耗时：" + (System.currentTimeMillis() - startTime) + "，" + sb.toString());
        }catch(Exception e) {
			model.addAttribute("result", "无法连接SAP：" + e.getMessage());
			LOGGER.error(e.getMessage());
			// e.printStackTrace();
        }
        
        model.addAttribute("javalib", "java lib : "+System.getProperty("java.library.path"));
		return "index";
	}
}
