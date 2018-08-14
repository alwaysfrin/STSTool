package com.jco.pojo;

import java.util.List;
import java.util.Map;

public class JCoTableParam {

	private String tableName;	//执行方法前放入，表名，入参table或出参table
	
	//用于tableInList
	private List<Map<String,String>> cell;	//执行方法前放入，所有入参列的名称和值
	
	//用于tableOutList
	private String[] returnCellName;	//执行方法前放入，返回的表列名
	private List<Map<String,String>> returnCell;	//执行方法后返回，所有表列的名称和值

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public List<Map<String, String>> getCell() {
		return cell;
	}

	public void setCell(List<Map<String, String>> cell) {
		this.cell = cell;
	}

	public String[] getReturnCellName() {
		return returnCellName;
	}

	public void setReturnCellName(String[] returnCellName) {
		this.returnCellName = returnCellName;
	}

	public List<Map<String, String>> getReturnCell() {
		return returnCell;
	}

	public void setReturnCell(List<Map<String, String>> returnCell) {
		this.returnCell = returnCell;
	}

	@Override
	public String toString() {
		return "获取table记录数：" + returnCell;
	}
}
