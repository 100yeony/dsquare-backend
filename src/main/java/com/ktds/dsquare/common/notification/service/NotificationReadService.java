package com.ktds.dsquare.common.notification.service;

import com.ktds.dsquare.common.notification.SentNotification;
import com.ktds.dsquare.common.notification.repository.SentNotificationRepository;
import com.ktds.dsquare.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@RequiredArgsConstructor
@Service
public class NotificationReadService {

    private final SentNotificationRepository sentRepository;


    @Transactional
    public void readNotification(long id, Member user) {
        SentNotification sentNotification = sentRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        sentRepository.delete(sentNotification);
    }

}
