package com.ktds.dsquare.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ktds.dsquare.auth.jwt.JwtProperties;
import com.ktds.dsquare.auth.jwt.JwtService;
import com.ktds.dsquare.member.dto.request.LoginRequest;
import com.ktds.dsquare.member.dto.response.LoginResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
public class CustomUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("Attempting authentication");

        LoginRequest loginRequest = getLoginInfo(request);
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPw());
        return authenticationManager.authenticate(authToken);
    }
    private LoginRequest getLoginInfo(HttpServletRequest request) {
        try {
            LoginRequest loginRequest = LoginRequest.convert(request);
            loginRequest.setPw(passwordEncoder.encode(loginRequest.getPw()));
            return loginRequest;
        } catch (IOException e) {
            log.warn("IOException while getting login information", e);
            return LoginRequest.builder()
                    .email("").pw("")
                    .build();
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        log.info("Succeeded authentication");
//        super.successfulAuthentication(request, response, chain, authResult);
        Map<String, String> authTokens = generateAuthToken((UserDetails)authResult.getPrincipal());
        String accessKey = JwtProperties.ACCESS_KEY(), refreshKey = JwtProperties.REFRESH_KEY();

        response.setHeader(JwtProperties.HEADER(), JwtProperties.PREFIX() + authTokens.get(accessKey));
        writeJsonValueToResponse(response, LoginResponse.builder()
                .accessToken(authTokens.get(accessKey))
                .refreshToken(authTokens.get(refreshKey))
                .build());
    }
    private Map<String, String> generateAuthToken(UserDetails principal) {
        Map<String, String> authTokens = JwtService.generateTokens(principal);
        log.info("Tokens are generated :: {}", authTokens);

        String accessKey = JwtProperties.ACCESS_KEY(), refreshKey = JwtProperties.REFRESH_KEY();
        return Map.of(
                accessKey, authTokens.get(accessKey),
                refreshKey, authTokens.get(refreshKey)
        );
    }
    private void writeJsonValueToResponse(HttpServletResponse response, Object value) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        response.getWriter()
                .write(new ObjectMapper().writeValueAsString(value));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        log.info("Authentication Failed ({})", failed.getMessage());
//        super.unsuccessfulAuthentication(request, response, failed); // 이를 수행하면 이후 trace에서 상태 코드가 403으로 바뀌어버림
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
    }

}
