package com.bookstore.helloworldserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class HelloworldServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(HelloworldServerApplication.class, args);
	}

}

