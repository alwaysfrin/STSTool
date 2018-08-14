<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8"></meta>
<title>Insert title here</title>
</head>
<body>
<table width="100%" border="1">
<tr>
	<td>
		<h2>执行jco</h2>
		<form action="test-connection" method="POST">
			ashost服务器ip：<input type="text" name="ashost" value="10.86.95.121">&nbsp;&nbsp;
			系统编号sysnr：<input type="text" name="sysnr" value="00">&nbsp;&nbsp;
			SAP集团号client：<input type="text" name="client" value="220">&nbsp;&nbsp;<br/>
			用户名user：<input type="text" name="user" value="ESBRFC">&nbsp;&nbsp;
			密码passwd：<input type="text" name="passwd" value="init1234">&nbsp;&nbsp;
			语言lang：<input type="text" name="lang" value="ZH">&nbsp;&nbsp;<br/>
			方法名functionName：<input type="text" name="functionName" value="ZMM_06_MAT_MES">&nbsp;&nbsp;
			<input type="submit" value="立即查询"/>
		</form>
	</td>
</tr>
</table>
<h3>返回结果：${result!''}</h3>
<h4>若提示缺少sapjco3.dll，请确保sapjco3.dll（sapjco3.so）在同级目录或JavaLib下</h4>
当前JavaLib ： ${javalib!''}<br/>
<hr>
</body>
</html>