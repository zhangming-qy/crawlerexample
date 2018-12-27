package com.crawler.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ExampleApplication {
	private final Logger log = LoggerFactory.getLogger(ExampleApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ExampleApplication.class, args);
	}

	//@Bean
	public TaskRunner taskRunner(){
		return new TaskRunner();
	}
}
