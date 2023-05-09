package com.ktds.dsquare.auth;

import com.ktds.dsquare.common.exception.AccountAuthenticationFailedException;
import com.ktds.dsquare.util.RandomUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class AuthenticationService {

    private final AuthCodeRepository authCodeRepository;


    @Transactional
    public AuthCode authenticateAccount(String account) {
        if (!StringUtils.hasText(account))
            throw new IllegalArgumentException("Target account cannot be null!");

        final String code = generateUniqueCode();
        return authCodeRepository.save(AuthCode.toEntity(account, code));
    }
    private String generateUniqueCode() {
        String code = "";
        for (int i = 0; i < 10 && authCodeRepository.existsByCode(code); ++i) {
            code = RandomUtil.generateRandomNumber(6);
        }

        return code;
    }

    @Transactional
    public void validateAccountAuthentication(String account, String code) {
        if (!StringUtils.hasText(account) || !StringUtils.hasText(code))
            throw new IllegalArgumentException("Validation information is insufficient!");

        AuthCode authCode = authCodeRepository.findByAccountAndCode(account, code)
                .orElseThrow(AccountAuthenticationFailedException::new);
        authCodeRepository.delete(authCode);
    }

}
