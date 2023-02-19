package com.bclindner.todo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import com.bclindner.todo.util.AudienceValidator;


/**
 * Spring Security configuration for this app.
 * 
 *
 * Partially referenced from Auth0's guide:
 * https://auth0.com/docs/quickstart/backend/java-spring-security5/01-authorization#configure-the-resource-server
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    JwtDecoder jwtDecoder(
        @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}") String issuer,
        @Value("${auth0.audience}") String audience
        ) {
        NimbusJwtDecoder decoder = JwtDecoders.fromOidcIssuerLocation(issuer);
        OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(issuer);
        OAuth2TokenValidator<Jwt> audienceValidator = new AudienceValidator(audience);
        OAuth2TokenValidator<Jwt> withAudience = new DelegatingOAuth2TokenValidator<Jwt>(withIssuer, audienceValidator);
        decoder.setJwtValidator(withAudience);
        return decoder;
    }
    
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // Ignore Swagger UI
        return (web) -> web.ignoring()
            .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/**");
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/todo-item/**")
                .authenticated()
            )
            .oauth2ResourceServer()
            .jwt()
            .and();
        return http.build();
    }
}
