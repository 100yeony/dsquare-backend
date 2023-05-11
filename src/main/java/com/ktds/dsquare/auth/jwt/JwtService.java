package com.ktds.dsquare.auth.jwt;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ktds.dsquare.auth.AuthToken;
import com.ktds.dsquare.auth.AuthTokenRepository;
import com.ktds.dsquare.auth.dto.request.TokenRefreshRequest;
import com.ktds.dsquare.auth.dto.response.LoginResponse;
import com.ktds.dsquare.common.exception.AccessTokenStillValidException;
import com.ktds.dsquare.common.exception.RefreshTokenMismatchException;
import com.ktds.dsquare.member.Member;
import com.ktds.dsquare.member.MemberRepository;
import com.ktds.dsquare.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtService {

    private final AuthTokenRepository authTokenRepository;
    private final MemberRepository memberRepository;


    @Transactional
    public Map<String, String> generateTokens(UserDetails principal) {
        Map<String, String> tokens = JwtUtil.generateTokens(principal);

        Member member = memberRepository.findByEmail(principal.getUsername()).orElse(null);
        AuthToken record = authTokenRepository.findByMember(member).orElse(null);

        if (ObjectUtils.isEmpty(record))
            authTokenRepository.save(AuthToken.toEntity(principal, tokens));
        else
            record.refresh(tokens);
        return tokens;
    }

    public Map<String, String> generateTokens(Member member, AuthToken authToken) {
        Map<String, String> tokens = JwtUtil.generateTokens(member);
        authToken.refresh(tokens);
        return tokens;
    }

    public Member authenticate(String authHeader) throws JWTVerificationException {
        DecodedJWT jwt = verifyToken(authHeader);
        String username = getClaim(jwt, "username");
        return memberRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("No such user with username [ " + username + "]"));
    }
    public DecodedJWT verifyToken(String token) throws JWTVerificationException {
        return JwtUtil.verifyAccessToken(token);
    }

    public String getClaim(DecodedJWT jwt, String claim) {
        return JwtUtil.getClaim(jwt, claim);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public LoginResponse refreshAccessToken(TokenRefreshRequest request) throws Exception {
        String refreshToken = request.getRefreshToken();
        try {
            DecodedJWT jwt = JwtUtil.verifyRefreshToken(refreshToken);
            Member member = memberRepository.findByEmail(getClaim(jwt, "username"))
                    .orElseThrow(() -> new UsernameNotFoundException(""));
            AuthToken authToken = authTokenRepository.findByMember(member)
                    .orElseThrow(() -> new RuntimeException("Please log in."));

            // 올바르지 않은 토큰
            if (!authToken.getRefreshToken().equals(refreshToken)) {
                log.warn("Illegal request received. Refresh token mismatched.");
                throw new RefreshTokenMismatchException();
            }
            // Access token이 아직 만료되지 않음 (비정상 Refresh 요청)
            if (!isAccessTokenExpired(authToken.getAccessToken())) {
                log.warn("Illegal request received. Probably refresh token has hijacked.");
                throw new AccessTokenStillValidException();
            }

            Map<String, String> freshTokens = generateTokens(member, authToken);
            return LoginResponse.toDto(freshTokens);
        } catch (TokenExpiredException e) {
            throw new RuntimeException("Refresh token is expired.");
        } catch (UsernameNotFoundException e) {
            throw new RuntimeException("Invalid token.");
        }
    }
    public boolean isAccessTokenExpired(String accessToken) {
        try {
            verifyToken(accessToken);
        } catch (TokenExpiredException e) {
            return true;
        } catch (Exception e) {
            return false;
        }
        return false;
    }

}
