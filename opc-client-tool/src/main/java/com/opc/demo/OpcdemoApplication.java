package com.opc.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(value = {"com.opc"})
public class OpcdemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(OpcdemoApplication.class, args);
	}
}
