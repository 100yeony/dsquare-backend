package com.ktds.dsquare.member.dto.request;

import lombok.Getter;

@Getter
public class AccountAuthenticationValidationRequest {

    private String email;
    private String code;

}
