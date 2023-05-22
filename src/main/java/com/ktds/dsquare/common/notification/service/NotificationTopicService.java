package com.ktds.dsquare.common.notification.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.TopicManagementResponse;
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

import java.util.List;

@RequiredArgsConstructor
@Service
public class NotificationTopicService {

    private final FirebaseMessaging fcm;

    private final RegistrationTokenRepository rtRepository;
    private final MemberRepository memberRepository;


    public RegistrationTokenRegisterResponse addRegistrationToken(RegistrationTokenRegisterRequest request) {
        Member member = memberRepository.findById(request.getUserId())
                .orElseThrow(() -> new MemberNotFoundException(String.valueOf(request.getUserId())));
        String registrationToken = request.getRegistrationToken();
        if (rtRepository.existsByOwnerAndValue(member, registrationToken))
            throw new RuntimeException();

        RegistrationToken registeredRegistrationToken = RegistrationToken.toEntity(registrationToken, member);
        return RegistrationTokenRegisterResponse.toDto(rtRepository.save(registeredRegistrationToken));
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
