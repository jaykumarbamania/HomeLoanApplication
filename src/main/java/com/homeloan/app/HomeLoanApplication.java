package com.homeloan.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages={"com.homeloan.app"})
public class HomeLoanApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomeLoanApplication.class, args);
	}

}
