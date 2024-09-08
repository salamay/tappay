package com.tappay.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import java.util.TimeZone;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.tappay.service.jparepository")
@EnableR2dbcRepositories(basePackages = "com.tappay.service.reactiverepository")
public class ServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceApplication.class, args);
	}

}
