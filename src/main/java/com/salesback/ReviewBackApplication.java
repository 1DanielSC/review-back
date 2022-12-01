package com.salesback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ReviewBackApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReviewBackApplication.class, args);
	}

}
