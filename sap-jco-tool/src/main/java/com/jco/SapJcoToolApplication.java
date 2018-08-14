package com.jco;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(value = {"com.jco"})
public class SapJcoToolApplication {

	public static void main(String[] args) {
		SpringApplication.run(SapJcoToolApplication.class, args);
	}
}
