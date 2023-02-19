package com.bclindner.todo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@SecurityScheme(
        name = "OAuth2",
        type = SecuritySchemeType.OAUTH2,
        flows = @OAuthFlows(
            authorizationCode = @OAuthFlow(
            authorizationUrl = "${springdoc.swagger-ui.authorizeUrl}",
            tokenUrl = "${springdoc.swagger-ui.tokenUrl}",
            scopes = {}
        )
    )
)
@SecurityRequirement(name = "OAuth2")
@Configuration
public class SwaggerConfig {

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
                    .contact(
                        new Contact()
                        .name("Brian Lindner")
                        .email("brian@bclindner.com")
                        .url("https://bclindner.com")
                    )
			);
	}
}
