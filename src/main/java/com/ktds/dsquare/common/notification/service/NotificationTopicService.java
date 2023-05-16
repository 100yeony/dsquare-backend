package com.ktds.dsquare.common.notification.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.TopicManagementResponse;
import com.ktds.dsquare.common.enums.NotifType;
import com.ktds.dsquare.common.exception.MemberNotFoundException;
import com.ktds.dsquare.common.notification.RegistrationToken;
import com.ktds.dsquare.common.notification.dto.TopicSubscribeRequest;
import com.ktds.dsquare.common.notification.dto.TopicSubscribeResponse;
import com.ktds.dsquare.common.notification.dto.request.RegistrationTokenRegisterRequest;
import com.ktds.dsquare.common.notification.dto.response.RegistrationTokenRegisterResponse;
import com.ktds.dsquare.common.notification.repository.RegistrationTokenRepository;
import com.ktds.dsquare.member.Member;
import com.ktds.dsquare.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.lang.annotation.Target;
import java.util.List;

@RequiredArgsConstructor
@Service
public class NotificationTopicService {

    private final FirebaseMessaging fcm;

    private final RegistrationTokenRepository rtRepository;
    private final MemberRepository memberRepository;


    public RegistrationTokenRegisterResponse addRegistrationToken(RegistrationTokenRegisterRequest request) {
        if (rtRepository.existsByValue(request.getRegistrationToken()))
            throw new RuntimeException();

        Member member = memberRepository.findById(request.getUserId())
                .orElseThrow(() -> new MemberNotFoundException(String.valueOf(request.getUserId())));
        RegistrationToken registrationToken = RegistrationToken.toEntity(request.getRegistrationToken(), member);
        return RegistrationTokenRegisterResponse.toDto(rtRepository.save(registrationToken));
    }

    public TopicSubscribeResponse subscribe(String topic, TopicSubscribeRequest request) throws FirebaseMessagingException {
        TopicManagementResponse response = fcm.subscribeToTopic(List.of(request.getRegistrationToken()), topic);
        return TopicSubscribeResponse.builder()
                .successCount(response.getSuccessCount())
                .failureCount(response.getFailureCount())
                .build();
    }

    public void unsubscribe(String topic, String registrationToken) throws FirebaseMessagingException {
        fcm.unsubscribeFromTopic(List.of(registrationToken), topic);
    }

}
