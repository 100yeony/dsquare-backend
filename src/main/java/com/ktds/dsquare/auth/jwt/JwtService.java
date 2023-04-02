package com.ktds.dsquare.auth.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
//@RequiredArgsConstructor
@Slf4j
public class JwtService {

    private static JWTCreator.Builder create() {
        return JWT.create()
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_MILLISECOND()));
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
    private static Algorithm getAlgorithm() {
        String secret = JwtProperties.SECRET();
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
    private static String sign(JWTCreator.Builder builder) {
        return builder.sign(getAlgorithm());
    }

    public static String generateAccessToken(UserDetails principal) {
        JWTCreator.Builder builder = create();
        builder.withSubject("Access-Token")
                .withClaim("username", principal.getUsername());

        return sign(builder);
    }
    public static String generateRefreshToken(UserDetails principal) {
        JWTCreator.Builder builder = create();
        builder.withSubject("Refresh-Token")
                .withClaim("username", principal.getUsername())
                .withClaim("Refresh-Token-Key", "Refresh-Token-Value");

        return sign(builder);
    }
    public static Map<String, String> generateTokens(UserDetails principal) {
        Map<String, String> tokens = new HashMap<>();
        tokens.put(JwtProperties.ACCESS_KEY(), generateAccessToken(principal));
        tokens.put(JwtProperties.REFRESH_KEY(), generateRefreshToken(principal));
        return tokens;
    }

    public static String preprocess(String token) {
        return token.replace(JwtProperties.PREFIX(), "").strip();
    }
    public static DecodedJWT verifyToken(String token) throws RuntimeException {
        if (!StringUtils.hasText(token))
            throw new RuntimeException("Authentication token is empty.");

        token = preprocess(token);
        return JWT.require(getAlgorithm()).build()
                .verify(token);
    }

    public static String getClaim(DecodedJWT jwt, String claim) {
        return jwt.getClaim(claim).asString();
    }

}
