package com.mvs.camera;

import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(value = {"com.mvs.camera.pojo","com.mvs.camera.controller","com.mvs.camera.util"})
public class MvsCameraToolApplication {
	private static final Logger LOGGER = Logger.getLogger(MvsCameraToolApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(MvsCameraToolApplication.class, args);
		LOGGER.info("【mvs-海康威视相机获取工具成功启动...】");
	}
}
