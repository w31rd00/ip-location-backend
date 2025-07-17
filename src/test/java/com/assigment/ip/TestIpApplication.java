package com.assigment.ip;

import org.springframework.boot.SpringApplication;

public class TestIpApplication {

	public static void main(String[] args) {
		SpringApplication.from(IpApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
