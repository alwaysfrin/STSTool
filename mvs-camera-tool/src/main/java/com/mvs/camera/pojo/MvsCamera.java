package com.mvs.camera.pojo;

import org.springframework.stereotype.Component;

import io.swagger.annotations.ApiParam;

/**
 * 相机对象
 * @author frin
 *
 */
@Component
public class MvsCamera {
	
	@ApiParam("请求图片的路径，默认http://localhost/getimage")
	private String clientUrl;	//http://localhost/getimage
	
	@ApiParam("图片格式，1 jpg，2 bmp")
	private int imageType = 1;	
	
	@ApiParam("图片压缩程度，1 不压缩，2，3，5 最大化压缩")
	private int imageSize = 1;

	public MvsCamera() {
		super();
	}

	public MvsCamera(String clientUrl) {
		super();
		this.clientUrl = clientUrl;
	}

	public MvsCamera(String clientUrl, int imageSize) {
		super();
		this.clientUrl = clientUrl;
		this.imageSize = imageSize;
	}

	public MvsCamera(String clientUrl, int imageType, int imageSize) {
		super();
		this.clientUrl = clientUrl;
		this.imageType = imageType;
		this.imageSize = imageSize;
	}

	public String getClientUrl() {
		return clientUrl;
	}

	public void setClientUrl(String clientUrl) {
		this.clientUrl = clientUrl;
	}

	public int getImageType() {
		return imageType;
	}

	public void setImageType(int imageType) {
		this.imageType = imageType;
	}

	public int getImageSize() {
		return imageSize;
	}

	public void setImageSize(int imageSize) {
		this.imageSize = imageSize;
	}

	@Override
	public String toString() {
		return "MvsCamera [clientUrl=" + clientUrl + ", imageType=" + imageType + ", imageSize=" + imageSize + "]";
	}
}
