package com.ktds.dsquare.member;

import com.ktds.dsquare.auth.AuthCode;
import com.ktds.dsquare.auth.AuthenticationService;
import com.ktds.dsquare.common.mailing.MailService;
import com.ktds.dsquare.member.dto.request.AccountAuthenticationRequest;
import com.ktds.dsquare.member.dto.request.AccountAuthenticationValidationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AccountService {

    private final AuthenticationService authenticationService;
    private final MailService mailService;


    /**
     * 계정 인증 (회원 가입 등)
     * @param request 계정 인증 요청 정보 (계정 정보)
     */
    public void authenticateAccount(AccountAuthenticationRequest request) {
        AuthCode authCode = authenticationService.authenticateAccount(request.getEmail());
        mailService.sendAccountAuthenticationCode(request.getEmail(), authCode.getCode());
    }

    public void validateAccountAuthentication(AccountAuthenticationValidationRequest request) {
        authenticationService.validateAccountAuthentication(request.getEmail(), request.getCode());
    }

}
