package com.fundoo.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;

@SpringBootApplication
@EntityScan ("com.fundoo.notes.*")
public class FundooApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(FundooApiApplication.class, args);
	}

}
