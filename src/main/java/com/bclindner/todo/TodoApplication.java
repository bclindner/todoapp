package com.bclindner.todo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@SpringBootApplication
@EnableJpaRepositories // needed for @Entity
@EnableJpaAuditing     // needed for @CreatedDate, @LastModifiedDate, etc.
@EnableScheduling      // needed for scheduled jobs
public class TodoApplication {

	public static void main(String[] args) {
		SpringApplication.run(TodoApplication.class, args);
	}
	
	/**
	 * Spec for the OpenAPI documentation.
	 * @param appVersion
	 * @return
	 */
	@Bean
	public OpenAPI openAPISpec(@Value("${springdoc.version}") String appVersion) {
		return new OpenAPI()
			.info(
				new Info()
					.title("To-Do App")	
					.description("Primitive to-do list API.")
					.version(appVersion)
			);
	}

}
