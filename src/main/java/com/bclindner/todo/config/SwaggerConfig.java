package com.bclindner.todo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.OAuthScope;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@SecurityScheme(
        name = "auth0",
        type = SecuritySchemeType.OAUTH2,
        flows = @OAuthFlows(
            authorizationCode = @OAuthFlow(
            authorizationUrl = "${springdoc.swagger-ui.oauth2RedirectUrl}",
            tokenUrl = "${springdoc.oAuthFlow.tokenUrl}",
            scopes = {
                @OAuthScope(name = "todoitem:read", description = "Read TodoItem resources"),
                @OAuthScope(name = "todoitem:modify", description = "Modify TodoItem resources")
            }
        )
    )
)
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
			);
	}
}
