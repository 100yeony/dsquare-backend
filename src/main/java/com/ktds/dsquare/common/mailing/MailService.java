package com.ktds.dsquare.common.mailing;

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
