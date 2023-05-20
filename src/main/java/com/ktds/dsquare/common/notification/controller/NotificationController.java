package com.ktds.dsquare.common.notification.controller;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.ktds.dsquare.common.annotation.AuthUser;
import com.ktds.dsquare.common.notification.dto.TopicSubscribeRequest;
import com.ktds.dsquare.common.notification.dto.request.RegistrationTokenRegisterRequest;
import com.ktds.dsquare.common.notification.service.NotificationReadService;
import com.ktds.dsquare.common.notification.service.NotificationSendService;
import com.ktds.dsquare.common.notification.service.NotificationTopicService;
import com.ktds.dsquare.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/notification")
public class NotificationController {

    private final NotificationTopicService notificationTopicService;
    private final NotificationSendService notificationSendService;
    private final NotificationReadService notificationReadService;


    @PostMapping("/registration-tokens")
    public ResponseEntity<?> addRegistrationToken(@RequestBody RegistrationTokenRegisterRequest request) {
        try {
            return new ResponseEntity<>(notificationTopicService.addRegistrationToken(request), HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Already registered token.", HttpStatus.OK);
        }
    }

    @DeleteMapping("/sent-notifications/{id}")
    public ResponseEntity<Void> readNotification(@PathVariable long id, @AuthUser Member user) {
        try {
            notificationReadService.readNotification(id, user);
        } catch (EntityNotFoundException e) { /* do nothing */ }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
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
