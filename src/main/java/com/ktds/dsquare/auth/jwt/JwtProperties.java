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
    private static String SECRET_ACCESS;
    private static String SECRET_REFRESH;
    private static long EXPIRATION_MILLISECOND_ACCESS;
    private static long EXPIRATION_MILLISECOND_REFRESH;

    private static String PREFIX;
//    private static Map<String, String> CLAIMS;

    /*** Getter ***/
    public static String HEADER() { return HEADER; }
    public static String ACCESS_KEY() { return ACCESS_KEY; }
    public static String REFRESH_KEY() { return REFRESH_KEY; }

    public static SignAlgorithm ALGORITHM() {
        return ALGORITHM;
    }
    public static String SECRET_ACCESS() {
        return SECRET_ACCESS;
    }
    public static String SECRET_REFRESH() {
        return SECRET_REFRESH;
    }
    public static long EXPIRATION_MILLISECOND_ACCESS() {
        return EXPIRATION_MILLISECOND_ACCESS;
    }
    public static long EXPIRATION_MILLISECOND_REFRESH() {
        return EXPIRATION_MILLISECOND_REFRESH;
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
    @Value("${jwt.secret.access}")
    private void setSecretAccess(String secretAccess) {
        SECRET_ACCESS = secretAccess;
        log.debug("SECRET_ACCESS is set\n: {}", SECRET_ACCESS);
    }
    @Value("${jwt.secret.refresh}")
    private void setSecretRefresh(String secretRefresh) {
        SECRET_REFRESH = secretRefresh;
        log.debug("SECRET_REFRESH is set\n: {}", SECRET_REFRESH);
    }
    @Value("${jwt.expiration-millisecond.access}")
    private void setExpirationMillisecondAccess(long expirationMillisecondAccess) {
        EXPIRATION_MILLISECOND_ACCESS = expirationMillisecondAccess;
        log.debug("EXPIRATION_MILLISECOND_ACCESS is set\n: {}", EXPIRATION_MILLISECOND_ACCESS);
    }
    @Value("${jwt.expiration-millisecond.refresh}")
    private void setExpirationMillisecondRefresh(long expirationMillisecondRefresh) {
        EXPIRATION_MILLISECOND_REFRESH = expirationMillisecondRefresh;
        log.debug("EXPIRATION_MILLISECOND_REFRESH is set\n: {}", EXPIRATION_MILLISECOND_REFRESH);
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
