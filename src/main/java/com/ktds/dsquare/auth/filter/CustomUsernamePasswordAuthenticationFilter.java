package com.ktds.dsquare.auth.filter;

import com.ktds.dsquare.auth.dto.request.LoginRequest;
import com.ktds.dsquare.auth.dto.response.LoginResponse;
import com.ktds.dsquare.auth.jwt.JwtProperties;
import com.ktds.dsquare.auth.jwt.JwtService;
import com.ktds.dsquare.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
public class CustomUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

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
            return LoginRequest.convert(request);
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

        response.setHeader(JwtProperties.HEADER(), JwtProperties.PREFIX() + authTokens.get(JwtProperties.ACCESS_KEY()));
        ResponseUtil.writeJsonValue(response, LoginResponse.toDto(authTokens));
    }
    private Map<String, String> generateAuthToken(UserDetails principal) {
        Map<String, String> authTokens = jwtService.generateTokens(principal);
        log.info("Tokens are generated :: {}", authTokens);
        return authTokens;
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws ServletException, IOException {
        log.info("Authentication Failed ({})", failed.getMessage());
        super.unsuccessfulAuthentication(request, response, failed); // 이를 수행하면 이후 trace에서 상태 코드가 403으로 바뀌어버림
    }

}
