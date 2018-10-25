<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8"></meta>
<title>Insert title here</title>
</head>
<body>
<h1>本工具用于相机拍照获取照片并进行传输</h1>
<hr>
<table width="100%" border="1">
<tr>
	<td>
		<h2>拍照保存照片</h2>
		<form action="take-picture" method="GET">
			clientUrl地址：<input type="text" name="clientUrl" value="http://localhost/getimage">&nbsp;&nbsp;
			imageType（图片类型，1 jpg，2 bmp）：<input type="text" name="imageType" value="1"><br/>
			imageSize（图片压缩程度，1 不压缩，5 最大压缩）：<input type="text" name="imageSize" value="1"><br/>
			<input type="submit" value="立即拍照"/>
		</form>
	</td>
</tr>
<tr>
	<td>
		<h2>下载照片</h2>
		<form action="get-picture" method="GET">
			照片名称（保存照片返回）：<input type="text" name="pictureName" value="pictureName">&nbsp;&nbsp;
			<input type="submit" value="立即下载"/>
		</form>
	</td>
</tr>
</table>
</body>
</html>