package com.ktds.dsquare.auth.dto.response;

import com.ktds.dsquare.auth.jwt.JwtProperties;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Builder
@Getter
public class LoginResponse {

    private String accessToken;
    private String refreshToken;


    public static LoginResponse toDto(Map<String, String> tokens) {
        return LoginResponse.builder()
                .accessToken(tokens.get(JwtProperties.ACCESS_KEY()))
                .refreshToken(tokens.get(JwtProperties.REFRESH_KEY()))
                .build();
    }

}
