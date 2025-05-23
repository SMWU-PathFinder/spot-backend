package com.pathfinder.spot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SpotApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpotApplication.class, args);
	}

}
