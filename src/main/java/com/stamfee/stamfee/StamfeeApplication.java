package com.stamfee.stamfee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class StamfeeApplication {

	public static void main(String[] args) {
		SpringApplication.run(StamfeeApplication.class, args);
	}

}
