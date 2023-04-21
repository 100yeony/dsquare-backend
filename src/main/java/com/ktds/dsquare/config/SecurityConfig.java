package com.ktds.dsquare.config;

import com.ktds.dsquare.auth.filter.AuthenticationAuthorizationFilter;
import com.ktds.dsquare.auth.filter.CustomUsernamePasswordAuthenticationFilter;
import com.ktds.dsquare.auth.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@EnableWebSecurity(debug = false)
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

    private final JwtService jwtService;

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
        /* Exception Handling */
        configureExceptionHandling(http);

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
                .antMatchers("/auth/refresh").permitAll()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated();
    }
    private void configureLogin(HttpSecurity http) throws Exception {
        http.formLogin().disable();
    }
    private void configureFilters(HttpSecurity http) throws Exception {
        http.apply(new MyCustomDsl());
    }
    private void configureExceptionHandling(HttpSecurity http) throws Exception {
        http.exceptionHandling()
                .authenticationEntryPoint(new AuthenticationEntryPoint() {
                    @Override
                    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
                        log.error("-- Exception while authentication -- :: {}", String.valueOf(authException));
                    }
                })
                .accessDeniedHandler(new AccessDeniedHandler() {
                    @Override
                    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
                        log.error("-- Access denied -- :: {}", String.valueOf(accessDeniedException));
                    }
                });
    }

    public class MyCustomDsl extends AbstractHttpConfigurer<MyCustomDsl, HttpSecurity> {
        @Override
        public void configure(HttpSecurity http) {
            AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
            http.addFilter(new CustomUsernamePasswordAuthenticationFilter(authenticationManager, jwtService));
            http.addFilter(new AuthenticationAuthorizationFilter(authenticationManager, jwtService));
        }
    }


    // **************************************************


    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> {
            web.ignoring()
                    .antMatchers("/v2/api-docs",
                            "/configuration/ui",
                            "/swagger-resources/**",
                            "/configuration/security",
                            "/swagger-ui.html",
                            "/webjars/**");
        };
    }

}
