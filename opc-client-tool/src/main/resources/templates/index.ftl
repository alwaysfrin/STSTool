<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8"></meta>
<title>Insert title here</title>
</head>
<body>
<h1>请确保JCustomOpc.dll在JavaLib下</h1>
当前JavaLib ： ${javalib!''}<br/>
<hr>
<table width="100%" border="1">
<tr>
	<td>
		<h2>查询tag数据</h2>
		<form action="queryparam" method="POST">
			OPCServer地址：<input type="text" name="host" value="localhost">&nbsp;&nbsp;
			ServerProgId（opc服务器名称）：<input type="text" name="serverProgId" value="Kepware.KEPServerEX.V5"><br/>
			accessPath（Channel层）：<input type="text" name="accessPath" value="Channel1">&nbsp;&nbsp;
			itemName（device.tag）：<input type="text" name="itemName" value="Device1.Tag1"><br/>
			<input type="submit" value="立即查询"/>
		</form>
	</td>
	<td rowSpan="2" width="300px" align="left"><h3>返回结果：${result!''}</h3></td>
</tr>
<tr>
	<td>
		<h2>修改tag数据</h2>
		<form action="editparam" method="POST">
			OPCServer地址：<input type="text" name="host" value="localhost">&nbsp;&nbsp;
			ServerProgId（opc服务器名称）：<input type="text" name="serverProgId" value="Kepware.KEPServerEX.V5"><br/>
			accessPath（Channel层）：<input type="text" name="accessPath" value="Channel1">&nbsp;&nbsp;
			itemName（device.tag）：<input type="text" name="itemName" value="Device1.Tag1">&nbsp;&nbsp;<br/>
			修改为：<input type="text" name="editValue" value="">&nbsp;&nbsp;<input type="submit" value="立即修改"/>
		</form>
	</td>
</tr>
</table>
</body>
</html>