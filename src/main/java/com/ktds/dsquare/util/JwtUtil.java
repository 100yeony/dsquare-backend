package com.ktds.dsquare.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.*;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ktds.dsquare.auth.CustomUserDetails;
import com.ktds.dsquare.auth.jwt.JwtProperties;
import com.ktds.dsquare.member.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class JwtUtil {

    private static JWTCreator.Builder createAccessTokenBase() {
        return JWT.create()
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_MILLISECOND_ACCESS()))
                .withSubject("Access-Token");
    }
    private static JWTCreator.Builder createRefreshTokenBase() {
        return JWT.create()
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_MILLISECOND_REFRESH()))
                .withSubject("Refresh-Token");
    }
    //    private JWTCreator.Builder setHeader(JWTCreator.Builder builder) {
//        builder.withHeader()
//    }
//    private static JWTCreator.Builder setPayload(JWTCreator.Builder builder) {
//        Map<String, String> claims = JwtProperties.CLAIMS();
//        for (Map.Entry<String, String> claimEntry : claims.entrySet())
//            builder.withClaim(claimEntry.getKey(), claimEntry.getValue());
//        return builder;
//    }
    private static Algorithm getAlgorithm(String secret) {
        switch (JwtProperties.ALGORITHM()) {
            case HS512:
                return Algorithm.HMAC512(secret);
//            case RS256:
//                return Algorithm.RSA256();
//            case RS512:
//                return Algorithm.RSA512();
            case HS256: default:
                return Algorithm.HMAC256(secret);
        }
    }
    private static String sign(JWTCreator.Builder builder, String secret) {
        return builder.sign(getAlgorithm(secret));
    }

    public static String generateAccessToken(UserDetails principal) {
        JWTCreator.Builder builder = createAccessTokenBase();
        if (principal instanceof CustomUserDetails)
            builder.withClaim("id", ((CustomUserDetails)principal).getMember().getId());
        builder.withClaim("username", principal.getUsername())
                .withClaim(
                        "role",
                        principal.getAuthorities().stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList())
                );

        return sign(builder, JwtProperties.SECRET_ACCESS());
    }
    public static String generateAccessToken(Member member) {
        JWTCreator.Builder builder = createAccessTokenBase();
        builder.withClaim("id", member.getId())
                .withClaim("username", member.getEmail())
                .withClaim("role", List.copyOf(member.getRole()));

        return sign(builder, JwtProperties.SECRET_ACCESS());
    }
    public static String generateRefreshToken(UserDetails principal) {
        JWTCreator.Builder builder = createRefreshTokenBase();
        if (principal instanceof CustomUserDetails)
            builder.withClaim("id", ((CustomUserDetails)principal).getMember().getId());
        builder.withClaim("username", principal.getUsername())
                .withClaim("Refresh-Token-Key", "Refresh-Token-Value");

        return sign(builder, JwtProperties.SECRET_REFRESH());
    }
    public static String generateRefreshToken(Member member) {
        JWTCreator.Builder builder = createRefreshTokenBase();
        builder.withClaim("id", member.getId())
                .withClaim("username", member.getEmail())
                .withClaim("Refresh-Token-Key", "Refresh-Token-Value");

        return sign(builder, JwtProperties.SECRET_REFRESH());
    }

    public static Map<String, String> generateTokens(UserDetails principal) {
        return Map.of(
                JwtProperties.ACCESS_KEY(), generateAccessToken(principal),
                JwtProperties.REFRESH_KEY(), generateRefreshToken(principal)
        );
    }
    public static Map<String, String> generateTokens(Member member) {
        return Map.of(
                JwtProperties.ACCESS_KEY(), generateAccessToken(member),
                JwtProperties.REFRESH_KEY(), generateRefreshToken(member)
        );
    }

    public static DecodedJWT verifyAccessToken(String token) throws JWTVerificationException {
        return verifyToken(token, JwtProperties.SECRET_ACCESS());
    }
    public static DecodedJWT verifyRefreshToken(String token) throws JWTVerificationException {
        return verifyToken(token, JwtProperties.SECRET_REFRESH());
    }
    private static String preprocess(String token) {
        if (!StringUtils.hasText(token)) {
//            throw new RuntimeException("Authentication token is empty.");
            return token;
        }
        if (!token.startsWith(JwtProperties.PREFIX())) {
//            throw new RuntimeException("Invalid token prefix."); // InvalidTokenException? ...Exception? ....
        }
        return token.replace(JwtProperties.PREFIX(), "").strip();
    }
    private static DecodedJWT verifyToken(String token, String secret) throws JWTVerificationException {
        token = preprocess(token);

        try {
            return JWT.require(getAlgorithm(secret)).build()
                    .verify(token);
        } catch (AlgorithmMismatchException ame) {
            log.error("AlgorithmMismatchException : {}", ame.getMessage());
            throw ame;
        } catch (SignatureVerificationException sve) {
            log.error("SignatureVerificationException : {}", sve.getMessage());
            throw sve;
        } catch (TokenExpiredException tee) {
            log.error("TokenExpiredException : {}", tee.getMessage());
            throw tee;
        } catch (MissingClaimException mce) {
            log.error("MissingClaimException : {}", mce.getMessage());
            throw mce;
        } catch (IncorrectClaimException ice) {
            log.error("IncorrectClaimException : {}", ice.getMessage());
            throw ice;
        } catch (JWTVerificationException jve) {
            log.error("JWTVerificationException : {}", jve.getMessage());
            throw jve;
        }
    }

    public static String getClaim(DecodedJWT jwt, String claim) {
        return jwt.getClaim(claim).asString();
    }
    public static String getClaim(String token, String claim) {
        return getClaim(JWT.decode(token), claim);
    }

}
