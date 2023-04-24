package com.ktds.dsquare.auth.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.ktds.dsquare.auth.CustomUserDetails;
import com.ktds.dsquare.auth.jwt.JwtProperties;
import com.ktds.dsquare.auth.jwt.JwtService;
import com.ktds.dsquare.common.ErrorResponse;
import com.ktds.dsquare.member.Member;
import com.ktds.dsquare.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.ObjectUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class AuthenticationAuthorizationFilter extends BasicAuthenticationFilter {

    private final JwtService jwtService;

    public AuthenticationAuthorizationFilter(
            AuthenticationManager authenticationManager,
            JwtService jwtService
    ) {
        super(authenticationManager);
        this.jwtService = jwtService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.warn("Trying authorization");

        try {
            UserDetails principal = authenticate(request);
            authorize(principal);
        } catch (TokenExpiredException e) {
            handleTokenExpiration(response);
        } catch (UsernameNotFoundException e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            log.error("Token was for a user who doesn't exist.", e);
        } catch (JWTVerificationException e) {
            handleTokenVerificationException(response, e);
        } catch (Exception e) {
            log.error("Error while authentication", e);
//            response.sendError(HttpStatus.BAD_REQUEST.value(), e.getMessage()); // 적용되지 않음. Spring Security Flow 검토 필요
//            return;
        }

        filterChain.doFilter(request, response);
    }
    private UserDetails authenticate(HttpServletRequest request) throws JWTVerificationException, UsernameNotFoundException {
        String authHeader = request.getHeader(JwtProperties.HEADER());
//        if (!StringUtils.hasText(authHeader))
//            return null;

        Member authenticatedUser = jwtService.authenticate(authHeader);
        return new CustomUserDetails(authenticatedUser);
    }
    private void authorize(UserDetails principal) {
        AbstractAuthenticationToken authToken = ObjectUtils.isEmpty(principal)
//                ? UsernamePasswordAuthenticationToken.unauthenticated(null, null)
                ? null
                : new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

    private void handleTokenExpiration(HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        ResponseUtil.writeJsonValue(
                response,
                ErrorResponse.builder()
                        .code("401001")
                        .message("Token has expired.")
                        .build()
        );
    }
    private void handleTokenVerificationException(HttpServletResponse response, Exception e) throws IOException {
        log.error("Token verification error - {}", e.getMessage());
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        ResponseUtil.writeJsonValue(
                response,
                ErrorResponse.builder()
                        .code("400001")
                        .message(e.getMessage())
                        .build()
        );
    }

}
