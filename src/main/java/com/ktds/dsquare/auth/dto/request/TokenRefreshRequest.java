package com.ktds.dsquare.auth.dto.request;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TokenRefreshRequest {

    private String refreshToken;

}
