package com.company.ngspringchat;

import org.hibernate.Session;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class NgSpringChatApplication {

	public static void main(String[] args) {
		SpringApplication.run(NgSpringChatApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

}
