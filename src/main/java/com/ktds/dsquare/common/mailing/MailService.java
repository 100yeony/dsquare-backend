package com.ktds.dsquare.common.mailing;

import com.ktds.dsquare.member.MemberService;
import com.ktds.dsquare.util.RandomUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {

    private final JavaMailSender mailSender;

    private final MemberService memberService;


    /**
     * 계정 인증 번호 전송 (회원 가입 등)
     * @param to 인증할 계정(이메일)
     * @param code 인증 번호
     */
    public void sendAccountAuthenticationCode(String to, String code) {
        sendMessage(
                to,
                "[DSquare] 계정 인증",
                "계정 인증을 위해 아래 인증 번호를 입력해 주세요.\n\n\t[ 인증 번호 : " + code + " ]"
        );
    }

    /**
     * 비밀번호 찾기
     * @param to 비밀번호를 찾을 계정(이메일)
     */
    public void findPassword(String to) {
        final String temporaryPassword = RandomUtil.generateRandomString(16); // TODO 여기서 할 일이 아닌 것 같음
        try {
            memberService.findPassword(to, temporaryPassword);
            sendMessage(to, "[DSquare] 임시 비밀번호 발급", temporaryPassword);
        } catch (Exception e) {
            throw new RuntimeException("Password finding did not finish normally.");
        }

        log.info("Password finding mail sent to {}", to);
    }

    private void sendMessage(String to, String subject, String text) {
        SimpleMailMessage message = createMessage(to);
        message.setSubject(subject);
        message.setText(text);
        send(message);
    }
    private SimpleMailMessage createMessage(String to) {
        if (!StringUtils.hasText(to))
            throw new IllegalArgumentException("Mail receiver must exist!");

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        return message;
    }

    private void send(SimpleMailMessage message) {
        mailSender.send(message);
    }

}
