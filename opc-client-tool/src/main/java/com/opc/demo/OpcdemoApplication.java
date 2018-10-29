package com.opc.demo;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.ContextStoppedEvent;


import javafish.clients.opc.JOpc;

@SpringBootApplication
@ComponentScan(value = { "com.opc" })
public class OpcdemoApplication {
	public static final Logger LOGGER = Logger.getLogger(OpcdemoApplication.class);

	public static void main(String[] args) {
		// SpringApplication.run(OpcdemoApplication.class, args);
		SpringApplication springApplication = new SpringApplication(OpcdemoApplication.class);

		springApplication.addListeners((ApplicationListener<ApplicationEvent>) event -> {
			if (event instanceof ApplicationStartedEvent) {
				LOGGER.info("系统开始，启动opc...");
			} else if (event instanceof ContextStoppedEvent) {
				// 应用停止
				LOGGER.info("系统停止，结束opc...");
			} else if (event instanceof ContextClosedEvent) {
				// 应用关闭
				LOGGER.info("系统关闭，结束opc...");
			} else {
				//LOGGER.info("系统监听到其他事件：{}",event);
			}
		});

		springApplication.run(args);
		LOGGER.info("【opc工具成功启动...】");
	}
}
