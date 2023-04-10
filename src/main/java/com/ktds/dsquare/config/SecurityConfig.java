package com.ktds.dsquare.config;

import com.ktds.dsquare.auth.filter.AuthenticationAuthorizationFilter;
import com.ktds.dsquare.auth.filter.CustomUsernamePasswordAuthenticationFilter;
import com.ktds.dsquare.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@EnableWebSecurity(debug = false)
@RequiredArgsConstructor
public class SecurityConfig {

    private final MemberRepository memberRepository;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("*"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "HEAD", "OPTIONS"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        /* CSRF */
        configureCsrf(http);
        /* Session */
        configureSession(http);
        /* CORS */
        configureCors(http);
        /* Http Basic Authentication */
        configureHttpBasic(http);
        /* Request Authorization */
        configureRequestAuthorization(http);
        /* Login */
        configureLogin(http);
        /* Filter Configuration */
        configureFilters(http);

        return http.build();
    }

    private void configureCsrf(HttpSecurity http) throws Exception {
        http.csrf().disable();
    }
    private void configureSession(HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
    private void configureCors(HttpSecurity http) throws Exception {
        http.cors().configurationSource(corsConfigurationSource());
    }
    private void configureHttpBasic(HttpSecurity http) throws Exception {
        http.httpBasic().disable();
    }
    private void configureRequestAuthorization(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/account/signup").permitAll()
//                .antMatchers("/admin/**").hasRole(String)
                .anyRequest().authenticated();
    }
    private void configureLogin(HttpSecurity http) throws Exception {
        http.formLogin().disable();
    }
    private void configureFilters(HttpSecurity http) throws Exception {
        http.apply(new MyCustomDsl());
    }

    public class MyCustomDsl extends AbstractHttpConfigurer<MyCustomDsl, HttpSecurity> {
        @Override
        public void configure(HttpSecurity http) {
            AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
            http.addFilter(new CustomUsernamePasswordAuthenticationFilter(authenticationManager, bCryptPasswordEncoder()));
            http.addFilter(new AuthenticationAuthorizationFilter(authenticationManager, memberRepository));
        }
    }

}
