package com.ktds.dsquare.member.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Setter @Getter
public class PasswordChangeRequest {

    private String email;
    private String originalPassword;
    private String changedPassword;

    public void encodePassword(PasswordEncoder passwordEncoder) {
        changedPassword = passwordEncoder.encode(changedPassword);
    }

}
