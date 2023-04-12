package com.ktds.dsquare.member.dto.request;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class SignupRequest {

    private String email;
    private String pw;
    private String nickname;
    private String name;
    private String contact;
    private Long tid;
    private String ktMail;

}