package com.ktds.dsquare.member.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Setter @Getter
public class SignupRequest {
    
    private String email;
    private String pw;
    private String nickname;
    private String name;
    private String contact;
    private Long tid;
    private String ktMail;


    public void encodePassword(PasswordEncoder passwordEncoder) {
        pw = passwordEncoder.encode(pw);
    }

}
