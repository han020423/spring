package com.example.study1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
//@EnableJpaAuditing
public class Study1Application {

	public static void main(String[] args) {
		SpringApplication.run(Study1Application.class, args);
	}

}
