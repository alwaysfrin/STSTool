package com.mvs.camera;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(value = {"com.mvs.camera.pojo","com.mvs.camera.controller","com.mvs.camera.util"})
public class MvsCameraToolApplication {

	public static void main(String[] args) {
		SpringApplication.run(MvsCameraToolApplication.class, args);
	}
}
