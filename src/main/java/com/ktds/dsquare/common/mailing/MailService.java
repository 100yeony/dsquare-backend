package com.ktds.dsquare.common.mailing;

import com.ktds.dsquare.member.MemberService;
import com.ktds.dsquare.util.RandomUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {

    private final JavaMailSender sender;

    private final MemberService memberService;


    public void findPassword(String to) {
        final String temporaryPassword = RandomUtil.generateRandomString(16);
        try {
            memberService.findPassword(to, temporaryPassword);
            sendMessage(to, "[DSquare] 임시 비밀번호 발급", temporaryPassword);
        } catch (Exception e) {
            throw new RuntimeException("Password finding did not finish normally.");
        }

        log.info("Password finding mail sent to {}", to);
    }

    public void sendMessage(String to, String subject, String text) {
        SimpleMailMessage message = createMessage(to);
        message.setSubject(subject);
        message.setText(text);
        send(message);
    }

    private SimpleMailMessage createMessage(String to) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        return message;
    }

    private void send(SimpleMailMessage message) {
        sender.send(message);
    }

}
