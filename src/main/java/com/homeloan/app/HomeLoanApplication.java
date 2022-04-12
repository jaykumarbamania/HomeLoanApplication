package com.homeloan.app;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import com.homeloan.app.model.LoanAccount;
import com.homeloan.app.service.StorageService;

@SpringBootApplication()

public class HomeLoanApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomeLoanApplication.class, args);
	}
	
//	@Bean
//	CommandLineRunner init(StorageService storageService) {
//		return (args) -> {
//			storageService.deleteAll();
//			storageService.init();
//		};
//	}

}
