package com.ktds.dsquare.common.notification.service;

import com.ktds.dsquare.common.notification.RegistrationToken;
import com.ktds.dsquare.common.notification.SentNotification;
import com.ktds.dsquare.common.notification.dto.NotificationInfo;
import com.ktds.dsquare.common.notification.repository.RegistrationTokenRepository;
import com.ktds.dsquare.common.notification.repository.SentNotificationRepository;
import com.ktds.dsquare.member.Member;
import com.ktds.dsquare.member.MemberSelectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class NotificationInquiryService {

    private final MemberSelectService memberService;

    private final SentNotificationRepository sentRepository;
    private final RegistrationTokenRepository tokenRepository;


    public List<NotificationInfo> getReceivedNotification(long receiverId) {
        Member member = memberService.selectWithId(receiverId);
        List<RegistrationToken> registrationTokens = tokenRepository.findByOwner(member);

        List<SentNotification> sentNotifications = new ArrayList<>();
//        registrationTokens.parallelStream()
//                .map(sentRepository::findByReceiver)
//                .forEach(sentNotifications::addAll);
        for (RegistrationToken token : registrationTokens)
            sentNotifications.addAll(
                    sentRepository.findByReceiver(token)
            );

        return sentNotifications.parallelStream()
                .map(SentNotification::getNotification)
                .distinct()
                .collect(Collectors.toList()).stream()
                .map(NotificationInfo::toDto)
                .sorted((o1, o2) -> o2.getSentAt().compareTo(o1.getSentAt()))
                .collect(Collectors.toList());
    }

}
