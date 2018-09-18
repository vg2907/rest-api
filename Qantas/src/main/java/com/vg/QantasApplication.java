package com.vg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@SpringBootApplication
@ComponentScan(basePackages = "com.vg.*")
public class QantasApplication {

	public static void main(String[] args) {
		SpringApplication.run(QantasApplication.class, args);
	}
	
	@GetMapping("/ping")
	public String get() {
		return "OK";
	}
		
}
