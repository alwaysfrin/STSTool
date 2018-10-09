package com.opc.pojo;

import io.swagger.annotations.ApiParam;

public class OpcModel {
	
	public static int UPDATE_RATE = 100;	//查询频率

	@ApiParam("opcserver地址-ip")
	private String host;	//通常为localhost
	
	@ApiParam("opcserver名称")
	private String serverProgId;	//opcserver的名称

	@ApiParam("第一层路径")
	private String accessPath;	//一般为channel

	@ApiParam("剩余路径名称")
	private String itemName;	//一般为device.tag

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getServerProgId() {
		return serverProgId;
	}

	public void setServerProgId(String serverProgId) {
		this.serverProgId = serverProgId;
	}

	public String getAccessPath() {
		return accessPath;
	}

	public void setAccessPath(String accessPath) {
		this.accessPath = accessPath;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

}
