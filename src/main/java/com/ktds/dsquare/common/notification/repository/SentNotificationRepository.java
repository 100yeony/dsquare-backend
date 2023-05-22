package com.ktds.dsquare.common.notification.repository;

import com.ktds.dsquare.common.notification.RegistrationToken;
import com.ktds.dsquare.common.notification.SentNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SentNotificationRepository extends JpaRepository<SentNotification, Long> {

    List<SentNotification> findByReceiver(RegistrationToken receiver);

}
