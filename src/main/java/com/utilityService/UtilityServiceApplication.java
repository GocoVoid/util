package com.utilityService;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class UtilityServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UtilityServiceApplication.class, args);
	}
	
	@Bean
	ModelMapper getMapper() {
		return new ModelMapper();
	}

}
