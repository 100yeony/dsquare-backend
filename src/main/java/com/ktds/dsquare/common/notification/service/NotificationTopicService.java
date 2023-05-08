package com.ktds.dsquare.common.notification.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.TopicManagementResponse;
import com.ktds.dsquare.common.notification.dto.TopicSubscribeRequest;
import com.ktds.dsquare.common.notification.dto.TopicSubscribeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class NotificationTopicService {

    private final FirebaseMessaging fcm;


    public TopicSubscribeResponse subscribe(String topic, TopicSubscribeRequest request) throws FirebaseMessagingException {
        TopicManagementResponse response = fcm.subscribeToTopic(List.of(request.getRegistrationToken()), topic);
        return TopicSubscribeResponse.builder()
                .successCount(response.getSuccessCount())
                .failureCount(response.getFailureCount())
                .build();
    }

}
