package com.fundoo.notes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling 
@SpringBootApplication
public class FundooApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(FundooApiApplication.class, args);
	}

}
