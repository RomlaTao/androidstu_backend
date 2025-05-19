package com.example.mealservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MealserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MealserviceApplication.class, args);
	}

}
