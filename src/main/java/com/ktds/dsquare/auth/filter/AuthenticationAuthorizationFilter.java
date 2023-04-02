package com.ktds.dsquare.auth.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.ktds.dsquare.auth.CustomUserDetails;
import com.ktds.dsquare.auth.jwt.JwtProperties;
import com.ktds.dsquare.auth.jwt.JwtService;
import com.ktds.dsquare.member.Member;
import com.ktds.dsquare.member.MemberRepository;
import lombok.extern.slf4j.Slf4j;
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

    private final MemberRepository memberRepository;

    public AuthenticationAuthorizationFilter(AuthenticationManager authenticationManager, MemberRepository memberRepository) {
        super(authenticationManager);
        this.memberRepository = memberRepository;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.warn("Trying authorization");

        try {
            UserDetails principal = authenticate(request);
            authorize(principal);
        } catch (Exception e) {
            log.error("Error while authentication", e);
//            response.sendError(HttpStatus.BAD_REQUEST.value(), e.getMessage()); // 적용되지 않음. Spring Security Flow 검토 필요
//            return;
        }

        filterChain.doFilter(request, response);
    }
    private UserDetails authenticate(HttpServletRequest request) throws UsernameNotFoundException {
        DecodedJWT authToken = JwtService.verifyToken(request.getHeader(JwtProperties.HEADER()));
        String username = JwtService.getClaim(authToken, "username");
        Member member = memberRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("No such user with username [ " + username + "]"));

        return new CustomUserDetails(member);
    }
    private void authorize(UserDetails principal) throws RuntimeException {
        if (ObjectUtils.isEmpty(principal))
            throw new RuntimeException("Authorizing principal is null.");

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                principal,
                null,
                principal.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

}
