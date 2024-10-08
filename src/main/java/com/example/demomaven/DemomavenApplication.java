package com.example.demomaven;

import com.example.demomaven.models.Dev;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class DemomavenApplication {

	public static void main(String[] args) {

		ApplicationContext context = SpringApplication.run(DemomavenApplication.class, args);

		Dev obj = context.getBean(Dev.class);
		obj.getTestString();
	}

}
