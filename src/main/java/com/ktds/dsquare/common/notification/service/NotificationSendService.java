package com.ktds.dsquare.common.notification.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
import com.ktds.dsquare.common.enums.NotifType;
import com.ktds.dsquare.common.notification.Notification;
import com.ktds.dsquare.common.notification.RegistrationToken;
import com.ktds.dsquare.common.notification.SentNotification;
import com.ktds.dsquare.common.notification.repository.NotificationRepository;
import com.ktds.dsquare.common.notification.repository.RegistrationTokenRepository;
import com.ktds.dsquare.common.notification.repository.SentNotificationRepository;
import com.ktds.dsquare.member.Member;
import com.ktds.dsquare.member.MemberSelectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class NotificationSendService {

    private final FirebaseMessaging fcm;

    private final RegistrationTokenRepository tokenRepository;
    private final NotificationRepository notificationRepository;
    private final SentNotificationRepository sentRepository;
    private final MemberSelectService memberSelectService;


    public void sendNotification(List<Long> receiverList, NotifType type) throws FirebaseMessagingException {
        sendNotification(receiverList, type, Collections.emptyMap());
    }
    public void sendNotification(List<Long> receiverList, NotifType type, Map<String, String> data) throws FirebaseMessagingException {
        if (receiverList == null) {
            throw new IllegalArgumentException("Receiver list cannot be null. (Empty at least)");
        }
        if (type == null) {
            throw new IllegalArgumentException("Notification type cannot be null.");
        }

        List<RegistrationToken> registrationTokens = collectRegistrationToken(receiverList);
        sendNotification(type, registrationTokens, data);
    }
    private List<RegistrationToken> collectRegistrationToken(List<Long> receiverList) {
        List<RegistrationToken> registrationTokens = new ArrayList<>();
        for (long receiverId : receiverList) {
            Member member = memberSelectService.selectWithId(receiverId); // TODO consider efficiency...
            registrationTokens.addAll(tokenRepository.findByOwner(member));
        }
        return registrationTokens;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sendNotification(NotifType type, List<RegistrationToken> registrationTokens, Map<String, String> data) throws FirebaseMessagingException {
        // Build notification data
        if (data == null) {
            log.debug("Notification data is null.");
            return;
        }
        // List up receivers
        List<String> receivers = registrationTokens.stream()
                .map(RegistrationToken::getValue)
                .collect(Collectors.toList());

        // Make message
        MulticastMessage multicastMessage = makeMulticastMessage(data, receivers);
        if (multicastMessage == null) {
            log.debug("Multicast message is null.");
            return;
        }

        // Send notification
        List<SentNotification> sentNotifications = saveNotification(type, data, registrationTokens);
        log.info("The number of sent notifications : {}", sentNotifications.size());
        fcm.sendMulticast(multicastMessage);
    }
    private MulticastMessage makeMulticastMessage(Map<String, String> data, List<String> registrationTokens) {
        if (ObjectUtils.isEmpty(registrationTokens))
            return null;

        return MulticastMessage.builder()
                .putAllData(data)
                .addAllTokens(registrationTokens)
                .build();
    }

    @Transactional
    public List<SentNotification> saveNotification(NotifType type, Map<String, String> data, List<RegistrationToken> registrationTokens) {
        if (registrationTokens == null)
            throw new IllegalArgumentException();

        Notification notification = notificationRepository.save(Notification.toEntity(type, data));
        List<SentNotification> sentNotifications = registrationTokens.stream()
                .map(token -> SentNotification.toEntity(notification, token))
                .collect(Collectors.toList());

        return sentRepository.saveAll(sentNotifications);
    }



    // TODO consider the use below and erase

    public String notifyTopic(String topic) throws FirebaseMessagingException {
        Message message = makeTopicMessage(topic);
        return fcm.send(message);
    }

//    @Transactional
    public String send(String to) throws FirebaseMessagingException {
        Message message = makeMessage(to);
        return fcm.send(message);
    }
    public Message makeTopicMessage(String topic) {
        return Message.builder()
                .setTopic(topic)
                .putData("title", "Topic Notification")
                .putData("body", "New notification :: " + topic)
                .build();
    }
    public Message makeMessage(String to) {
        return Message.builder()
                .setToken(to)
                .putData("title", "Test Notification Title")
                .putData("body", "Test Notification Body")
                .build();
    }

}
