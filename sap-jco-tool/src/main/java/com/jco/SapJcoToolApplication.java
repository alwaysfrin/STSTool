package com.jco;

import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(value = {"com.jco"})
public class SapJcoToolApplication {
	
	private static final Logger LOGGER = Logger.getLogger(SapJcoToolApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(SapJcoToolApplication.class, args);
		LOGGER.info("【SAP-JCO工具成功启动...】");
	}
}
