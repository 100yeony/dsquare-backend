package com.ktds.dsquare.common.notification.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Aspect
@Service
public class NotificationSendService {

    private final FirebaseMessaging fcm;


//    @Transactional
    public String send(String to) throws FirebaseMessagingException {
        Message message = makeMessage(to);
        return fcm.send(message);
    }
    public Message makeMessage(String to) {
        return Message.builder()
                .setToken(to)
                .putData("title", "Test Notification Title")
                .putData("body", "Test Notification Body")
                .build();
    }

}
