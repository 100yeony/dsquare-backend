package com.ktds.dsquare.member.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LoginResponse {

    private String accessToken;
    private String refreshToken;

}
