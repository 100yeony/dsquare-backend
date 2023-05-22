package com.ktds.dsquare.common.notification;

import lombok.*;

import javax.persistence.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class SentNotification {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Notification notification;
    @ManyToOne
    private RegistrationToken receiver;


    public static SentNotification toEntity(Notification notification, RegistrationToken receiver) {
        return SentNotification.builder()
                .notification(notification)
                .receiver(receiver)
                .build();
    }

}
