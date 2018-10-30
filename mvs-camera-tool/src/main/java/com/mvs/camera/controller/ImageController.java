package com.mvs.camera.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.mvs.camera.pojo.MvsCamera;
import com.mvs.camera.pojo.ResultBean;
import com.mvs.camera.util.CameraTool;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;

@Api(value="/",tags="相机接口服务")
@Controller
public class ImageController {

	public static final Logger LOGGER = Logger.getLogger(ImageController.class);
	
	@GetMapping(value="/index")
	public String index() {
		return "/index";
	}

	@ResponseBody
	@RequestMapping(value="/take-picture")
	public ResultBean takePicture(HttpServletResponse response,String clientUrl, String pressText,int imageType,int imageSize) throws IOException {
		ResultBean result = new CameraTool(new MvsCamera(clientUrl,pressText,imageType,imageSize)).getImage();
		LOGGER.info("返回结果：" + result.toString());
		
		return result;
	}
	
	/**
	 * 下载图片
	 * @param response
	 * @param pictureName	图片名称
	 * @param needDelete 下载后是否要删除，1 删除，2 保留
	 * @throws IOException
	 */
	@RequestMapping(value="/get-picture")
	public void getPicture(HttpServletResponse response,String pictureName,int needDelete) throws IOException {
		ResultBean result;
		File file = new File(pictureName);
		
		OutputStream out = null;
        FileInputStream fis = null;
        try {
    		//输出流
            out = response.getOutputStream();
			if(file.isFile()) {
				//设置response响应头
                response.setHeader("content-disposition", "attachment;filename="+ pictureName);
                //输出流到response中
                byte[] data = new byte[1024];
                int len = 0;
                fis = new FileInputStream(file);
                while((len = fis.read(data)) > 0){
                    out.write(data, 0, len);
                }
                
                if(needDelete == 1) {
                	file.delete();
                	LOGGER.info("图片"+pictureName+"已删除。");
                }
			}else {
				result = new ResultBean(false,"图片不存在，请重新拍照。");
				out.write(result.toString().getBytes("UTF-8"));
			}
        }catch(IOException e) {
        	LOGGER.error("获取图片出错：" + e.getMessage(),e);
        }finally {
        	if(fis != null) {
        		fis.close();
        	}
        	if(out != null) {
        		out.close();
        	}
        }
	}
}
