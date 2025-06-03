package com.example.analystservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class AnalystserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AnalystserviceApplication.class, args);
	}

}