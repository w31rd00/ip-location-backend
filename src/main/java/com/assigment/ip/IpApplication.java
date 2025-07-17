package com.assigment.ip;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class IpApplication {

	public static void main(String[] args) {
		SpringApplication.run(IpApplication.class, args);
	}

}
