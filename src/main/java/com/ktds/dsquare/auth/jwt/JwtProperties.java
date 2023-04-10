package com.ktds.dsquare.auth.jwt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class JwtProperties {

    /*** Property ***/
    private static String HEADER;
    private static String ACCESS_KEY;
    private static String REFRESH_KEY;

    private static SignAlgorithm ALGORITHM;
    private static String SECRET;
    private static long EXPIRATION_MILLISECOND;

    private static String PREFIX;
//    private static Map<String, String> CLAIMS;

    /*** Getter ***/
    public static String HEADER() { return HEADER; }
    public static String ACCESS_KEY() { return ACCESS_KEY; }
    public static String REFRESH_KEY() { return REFRESH_KEY; }

    public static SignAlgorithm ALGORITHM() {
        return ALGORITHM;
    }
    public static String SECRET() {
        return SECRET;
    }
    public static long EXPIRATION_MILLISECOND() {
        return EXPIRATION_MILLISECOND;
    }

    public static String PREFIX() {
        return PREFIX;
    }
//    public static Map<String, String> CLAIMS() {
//        return CLAIMS;
//    }

    /*** Setter ***/
    @Value("${jwt.key.header}")
    private void setHeader(String header) {
        HEADER = header;
        log.debug("HEADER is set\n: {}", HEADER);
    }
    @Value("${jwt.key.access-token}")
    public void setAccessKey(String accessKey) {
        ACCESS_KEY = accessKey;
        log.debug("ACCESS_KEY is set\n: {}", ACCESS_KEY);
    }
    @Value("${jwt.key.refresh-token}")
    public void setRefreshKey(String refreshKey) {
        REFRESH_KEY = refreshKey;
        log.debug("REFRESH_KEY is set\n: {}", REFRESH_KEY);
    }

    @Value("${jwt.algorithm}")
    private void setAlgorithm(SignAlgorithm algorithm) {
        ALGORITHM = algorithm;
        log.debug("ALGORITHM is set\n: {}", ALGORITHM);
    }
    @Value("${jwt.secret}")
    private void setSecret(String secret) {
        SECRET = secret;
        log.debug("SECRET is set\n: {}", SECRET);
    }
    @Value("${jwt.expiration-millisecond}")
    private void setExpirationMillisecond(long expirationMillisecond) {
        EXPIRATION_MILLISECOND = expirationMillisecond;
        log.debug("EXPIRATION_MILLISECOND is set\n: {}", EXPIRATION_MILLISECOND);
    }

    @Value("${jwt.prefix}")
    private void setPrefix(String prefix) {
        PREFIX = prefix;
        log.debug("PREFIX is set\n: {}", PREFIX);
    }
//    @Value("${jwt.claims}")
//    private void setClaims(Map<String, String> claims) {
//        CLAIMS = claims;
//    }

}
