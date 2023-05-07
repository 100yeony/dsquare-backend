package com.ktds.dsquare.common.notification.controller;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.ktds.dsquare.common.notification.service.NotificationSendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class NotificationController {

    private final NotificationSendService notificationSendService;


    @PostMapping("/notification/send/{to}")
    public ResponseEntity<String> sendNotification(@PathVariable String to) throws FirebaseMessagingException {
        return new ResponseEntity<>(notificationSendService.send(to), HttpStatus.CREATED);
    }

}
