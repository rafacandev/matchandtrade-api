package com.matchandtrade;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ImportResource;

@ImportResource("classpath:application-context-spring-boot.xml")
@ServletComponentScan
@SpringBootApplication
public class WebserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebserviceApplication.class, args);
	}
}
