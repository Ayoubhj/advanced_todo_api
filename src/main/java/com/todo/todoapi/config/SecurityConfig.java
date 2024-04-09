package com.todo.todoapi.config;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableElasticsearchRepositories(basePackages = "com.todo.todoapi.elasticRepositories")
@EnableJpaRepositories(basePackages = "com.todo.todoapi.repositories")
public class SecurityConfig {

    @Value("${cors.allowedorigins}")
    String allowedOrgis;
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;


    /**
     * securityFilterChain is a function Bean for initilize security configuration
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

        http.cors().configurationSource((CorsConfigurationSource) request -> {
            CorsConfiguration config = new CorsConfiguration();
            List<String> origins = new ArrayList<>();
            Collections.addAll(origins, 	allowedOrgis.split(","));

            config.setAllowedHeaders(Arrays.asList(HttpHeaders.AUTHORIZATION,
                    HttpHeaders.CONTENT_TYPE,
                    HttpHeaders.ACCEPT,
                    "Authno",
                    "X-XSRF-TOKEN",
                    "X-Requested-With"));
            config.setAllowedMethods(Arrays.asList(
                    HttpMethod.GET.toString(),
                    HttpMethod.POST.toString(),
                    HttpMethod.DELETE.toString(),
                    HttpMethod.PUT.toString(),
                    HttpMethod.PATCH.toString(),
                    HttpMethod.OPTIONS.toString()));
            config.setAllowedOrigins(origins);
            return config;
        });
        http.cors();
        http
                .csrf()
                .disable()
                .authorizeHttpRequests()
                .requestMatchers(
                        "/api/v1/auth/**",
                        "/v2/api-docs",
                        "/v3/api-docs",
                        "/v3/api-docs/**",
                        "/swagger-resources",
                        "/swagger-resources/**",
                        "/configuration/ui",
                        "/configuration/security",
                        "/swagger-ui/**",
                        "/webjars/**",
                        "/swagger-ui.html"
                )
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout()
                .logoutUrl("/api/v1/auth/logout")
                .addLogoutHandler(logoutHandler)
                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext());

        return http.build();
    }



}
