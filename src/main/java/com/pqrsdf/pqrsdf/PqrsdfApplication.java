package com.pqrsdf.pqrsdf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PqrsdfApplication {

	public static void main(String[] args) {
		SpringApplication.run(PqrsdfApplication.class, args);
	}
}
