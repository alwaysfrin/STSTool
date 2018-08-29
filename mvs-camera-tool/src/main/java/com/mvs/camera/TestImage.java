package com.mvs.camera;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.mvs.camera.pojo.MvsCamera;
import com.mvs.camera.pojo.ResultBean;
import com.mvs.camera.util.CameraTool;
import com.mvs.camera.util.HttpRequestProxy;

import net.coobird.thumbnailator.Thumbnails;

public class TestImage {

    public static final Logger LOGGER = LoggerFactory.getLogger(TestImage.class);

	public static void main(String[] args) {
		//请求拍照
//		takePicture();
		
		//拍照后返回的图片名称，生成本地文件
		downPicture();
	}

	private static void takePicture() {
		LOGGER.info("请求拍照");
		Map<String,String> postMap = new HashMap<String,String>();
		postMap.put("clientUrl","http://192.168.1.148/getimage");
        postMap.put("imageType", "1");
        postMap.put("imageSize", "1");
        
		HttpRequestProxy proxy = new HttpRequestProxy();
		String result = proxy.doGet("http://localhost:8890/mvs/take-picture",postMap);
		LOGGER.info("返回结果：" + result);
	}

	private static void downPicture() {
		LOGGER.info("请求下载照片");
		String reqUrl = "http://localhost:8890/mvs/get-picture";
		String pictureName = "1535539922466.jpg";
		int needDelete = 1;
		String newFilePath = "D:\\" + pictureName;
		File file = downPictureTool(reqUrl,pictureName,needDelete,newFilePath);
		LOGGER.info(file.isFile() + "，大小：" + file.length() + "，路径：" + file.getAbsolutePath());
	}
	
	private static File downPictureTool(String reqUrl,String pictureName,int needDelete,String newFilePath) {
		int connectTimeOut = 1000;
		int readTimeOut = 20000;
		
		HttpURLConnection urlCon = null;
        try {
            URL url = new URL(reqUrl + "?pictureName=" + pictureName + "&needDelete=" + needDelete);
            urlCon = (HttpURLConnection) url.openConnection();
            urlCon.setRequestMethod("GET");
            
            //主机连接超时
            urlCon.setConnectTimeout(connectTimeOut);
            //数据读取超时
            urlCon.setReadTimeout(readTimeOut);
            urlCon.setConnectTimeout(connectTimeOut);
            urlCon.setReadTimeout(readTimeOut);
            urlCon.setDoInput(true);
            
            if(urlCon.getResponseCode() == 200) {
            	//如果响应为“200”，表示成功响应，则返回一个输入流
                InputStream is = urlCon.getInputStream();
                LOGGER.info("正常返回图片流大小：" + is.available());
                
                // 将文件输出流与文件myavatar.jpg关联
                // 这里是输出到工程根目录下
                File file = new File(newFilePath);
                FileOutputStream fos = new FileOutputStream(file);
                LOGGER.info("图片保存绝对路径： " + file.getAbsolutePath());

                // 将输入流循环写到关联文件的输出流
                // 为了提高效率, 定义缓冲buffer来缓存输入流
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }

                // 释放资源
                is.close();
                fos.close();
                
	            return file;
            }else {
            	LOGGER.error("无法获取照片，请检查路径：" + reqUrl);
            	return null;
            }
        } catch (IOException e) {
            LOGGER.error("网络故障请求：" + e.getMessage() + "，reqUrl=" + reqUrl);
        } finally {
            if (urlCon != null) {
                urlCon.disconnect();
            }
        }
    	return null;
	}
}
