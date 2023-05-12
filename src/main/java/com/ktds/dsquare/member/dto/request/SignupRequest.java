package com.ktds.dsquare.member.dto.request;

import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@Builder
@Setter @Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
