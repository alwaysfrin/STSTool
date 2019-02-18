package com.mvs.camera.util;

import java.io.File;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.mvs.camera.TestImage;
import com.mvs.camera.pojo.MvsCamera;
import com.mvs.camera.pojo.ResultBean;

/**
 * 图片转换工具类
 * @author HP
 *
 */
@Component
public class CameraTool {
    public static final Logger LOGGER = Logger.getLogger(CameraTool.class);
	private MvsCamera camera;
	private HttpRequestProxy requestProxy;

	public CameraTool(MvsCamera camera) {
		this.camera = camera;
		requestProxy = new HttpRequestProxy();
	}

	public ResultBean getImage() {
		ResultBean result;
		if(camera.getImageType() == 1) {
			result = requestProxy.doImageGetSimple(camera.getClientUrl(),camera.getPressText(), "jpg",camera.getImageSize());
		} else if(camera.getImageType() == 2){
			result = requestProxy.doImageGetSimple(camera.getClientUrl(),camera.getPressText(), "bmp",camera.getImageSize());
		}else {
			result = new ResultBean(false, "暂不支持此图片类型。");
		}
		
		return result;
	}
	
	public static void main(String[] args) {
		//使用默认图片格式，并初步压缩，图片约500k
		ResultBean result = new CameraTool(new MvsCamera("http://192.168.1.148/getimage",2)).getImage();
		//使用默认图片大小和压缩率，图片约5m
		//ResultBean result = new CameraTool(new MvsCamera("http://192.168.1.148/getimage")).getImage();
		//手动控制图片格式和压缩大小
		//ResultBean result = new CameraTool(new MvsCamera("http://192.168.1.148/getimage",1,1)).getImage();
		LOGGER.info("返回生成的图片："+result);
		
		//此处result.getMsg()为图片的名称，需要保存下来
		File file = new File(result.getMsg());
		LOGGER.info("是否存在图片："+file.isFile() + ",大小：" + file.length());
		LOGGER.info("图片的绝对路径："+file.getAbsolutePath());
	}
}
