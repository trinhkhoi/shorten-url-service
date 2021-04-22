package com.example.shortenurl.test;

import com.example.shortenurl.ShortenUrlApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.example.shortenurl.repository")
@EntityScan("com.example.shortenurl.entity")
@ComponentScan("com.example.shortenurl.*")
public class ShortenUrlApplicationTests {
	@Bean
	public TruncateDatabaseService truncateDatabaseService() {
		return new TruncateDatabaseService();
	}

	public static void main(final String[] args) {
		SpringApplication.run(ShortenUrlApplication.class, args);
	}
}
