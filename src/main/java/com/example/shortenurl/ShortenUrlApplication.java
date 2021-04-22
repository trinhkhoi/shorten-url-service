package com.example.shortenurl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.example.shortenurl.repository")
@EntityScan("com.example.shortenurl.entity")
@ComponentScan("com.example.shortenurl.*")
public class ShortenUrlApplication {

	public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(ShortenUrlApplication.class);
        springApplication.run(args);
	}

}
