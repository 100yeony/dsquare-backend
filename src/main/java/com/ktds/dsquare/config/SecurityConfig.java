package com.ktds.dsquare.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // CSRF
        http.csrf().disable();
        // URL Authentication/Authorization
        http
                .authorizeRequests()
                .antMatchers("*")
                .permitAll();

        return http.build();
    }

}
