package com.opc.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.opc.pojo.ResultBean;
import com.opc.pojo.OpcModel;
import com.opc.service.OpcConnectTool;

import io.swagger.annotations.Api;

@Api(value="/",tags="远端restful请求")
@RestController
public class OpcRestfulService {
    public static final Logger LOGGER = LoggerFactory.getLogger(OpcRestfulService.class);
	
	@Autowired
	private OpcConnectTool opcConnectTool;
	
	@PostMapping(value="/query")
	public ResultBean query(OpcModel server, HttpServletResponse response) throws IOException {
		if(server == null || StringUtils.isEmpty(server.getAccessPath().trim()) 
				|| StringUtils.isEmpty(server.getHost().trim()) || StringUtils.isEmpty(server.getItemName().trim()) 
				|| StringUtils.isEmpty(server.getServerProgId().trim())) {
			return new ResultBean(false,"请求数据不完整");
		}else {
			long startTime = System.currentTimeMillis();
			ResultBean result = opcConnectTool.queryOpcServer(server);
			
			LOGGER.info("共耗时：" + (System.currentTimeMillis() - startTime)+ "，获取：" + result);
			return result;
		}
	}
	
	@PostMapping(value="/edit")
	public ResultBean edit(OpcModel server, String editValue, HttpServletResponse response) throws IOException {
		if(StringUtils.isEmpty(server.getAccessPath().trim()) || StringUtils.isEmpty(server.getHost().trim())
				|| StringUtils.isEmpty(server.getItemName().trim()) || StringUtils.isEmpty(server.getServerProgId().trim())
				|| StringUtils.isEmpty(editValue.trim())) {
			response.getWriter().println("请求数据不完整");
			return new ResultBean(false,"请求数据不完整");
		}else {
			return opcConnectTool.editOpcParam(server, editValue);
		}
	}
}
