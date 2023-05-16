package com.ktds.dsquare.common.notification.controller;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.ktds.dsquare.common.notification.dto.TopicSubscribeRequest;
import com.ktds.dsquare.common.notification.dto.request.RegistrationTokenRegisterRequest;
import com.ktds.dsquare.common.notification.service.NotificationSendService;
import com.ktds.dsquare.common.notification.service.NotificationTopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/notification")
public class NotificationController {

    private final NotificationTopicService notificationTopicService;
    private final NotificationSendService notificationSendService;


    @PostMapping("/registration-tokens")
    public ResponseEntity<?> addRegistrationToken(@RequestBody RegistrationTokenRegisterRequest request) {
        try {
            return new ResponseEntity<>(notificationTopicService.addRegistrationToken(request), HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Already registered token.", HttpStatus.OK);
        }
    }

    @PostMapping("/topics/{topic}/subscribers")
    public ResponseEntity<?> subscribeTopic(@PathVariable String topic, @RequestBody TopicSubscribeRequest request) throws FirebaseMessagingException {
        return new ResponseEntity<>(notificationTopicService.subscribe(topic, request), HttpStatus.CREATED);
    }

    @PostMapping("/topics/{topic}/notifications")
    public ResponseEntity<?> notifyTopic(@PathVariable String topic) throws FirebaseMessagingException {
        return new ResponseEntity<>(notificationSendService.notifyTopic(topic), HttpStatus.CREATED);
    }

    @PostMapping("/send/{to}")
    public ResponseEntity<String> sendNotification(@PathVariable String to) throws FirebaseMessagingException {
        return new ResponseEntity<>(notificationSendService.send(to), HttpStatus.CREATED);
    }

}
