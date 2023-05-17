package com.ktds.dsquare.common.notification.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
import com.ktds.dsquare.common.enums.NotifType;
import com.ktds.dsquare.common.notification.Notification;
import com.ktds.dsquare.common.notification.RegistrationToken;
import com.ktds.dsquare.common.notification.SentNotification;
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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class NotificationSendService {

    private final FirebaseMessaging fcm;

    private final RegistrationTokenRepository tokenRepository;
    private final SentNotificationRepository sentRepository;
    private final MemberSelectService memberSelectService;


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sendNotification(long[] receiverList, NotifType type) throws FirebaseMessagingException {
        if (receiverList == null) {
            throw new IllegalArgumentException("Receiver list cannot be null. (Empty at least)");
        }
        if (type == null) {
            throw new IllegalArgumentException("Notification type cannot be null.");
        }

        List<RegistrationToken> registrationTokens = collectRegistrationToken(receiverList);
        sendNotification(type, registrationTokens);
    }
    private List<RegistrationToken> collectRegistrationToken(long[] receiverList) {
        List<RegistrationToken> registrationTokens = new ArrayList<>();
        for (long receiverId : receiverList) {
            Member member = memberSelectService.selectWithId(receiverId); // TODO consider efficiency...
            registrationTokens.addAll(tokenRepository.findByOwner(member));
        }
        return registrationTokens;
    }

    @Transactional
    public void sendNotification(NotifType type, List<RegistrationToken> registrationTokens) throws FirebaseMessagingException {
        // Build notification data
        Map<String, String> data = makeNotificationData(type);
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
        List<SentNotification> sentNotifications = saveNotification(data, registrationTokens);
        log.info("The number of sent notifications : {}", sentNotifications.size());
        fcm.sendMulticast(multicastMessage);
    }
    private Map<String, String> makeNotificationData(NotifType type) {
        Map<String, String> data = null;
        switch (type) {
            case ANSWER_REGISTRATION:
                data = Map.of("title", "답변 등록 알림", "body", "회원님의 질문에 새로운 답변이 등록되었습니다.");
                break;
            case COMMENT_REGISTRATION:
                data = Map.of("title", "댓글 등록 알림", "body", "회원님의 게시글에 새로운 댓글이 등록되었습니다.");
                break;
            case SPECIALITY_QUESTION_REGISTRATION:
                data = Map.of("title", "담당 분야 질문 등록 알림", "body", "담당 분야에 새로운 질문이 등록되었습니다.");
                break;
            case NESTED_COMMENT_REGISTRATION:
            case REQUEST_CHOICE:
            default:
                log.warn("Not supported notification. Type: [{}]", type);
        }
        return data;
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
    public List<SentNotification> saveNotification(Map<String, String> data, List<RegistrationToken> registrationTokens) {
        if (registrationTokens == null)
            throw new IllegalArgumentException();

        Notification notification = Notification.toEntity(data);
        List<SentNotification> sentNotifications = registrationTokens.stream()
                .map(token -> SentNotification.toEntity(notification, token))
                .collect(Collectors.toList());

        return sentRepository.saveAll(sentNotifications);
    }



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
