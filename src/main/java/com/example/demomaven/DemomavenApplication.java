package com.example.demomaven;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class DemomavenApplication {

	public static void main(String[] args) {

		ApplicationContext context = SpringApplication.run(DemomavenApplication.class, args);
	}

}
