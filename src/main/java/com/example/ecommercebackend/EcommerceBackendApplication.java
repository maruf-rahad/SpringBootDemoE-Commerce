package com.example.ecommercebackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class EcommerceBackendApplication {

	public static void main(String[] args) {

		ApplicationContext context = SpringApplication.run(EcommerceBackendApplication.class, args);
	}

}
