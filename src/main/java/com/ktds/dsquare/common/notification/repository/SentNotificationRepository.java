package com.ktds.dsquare.common.notification.repository;

import com.ktds.dsquare.common.notification.SentNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SentNotificationRepository extends JpaRepository<SentNotification, Long> {
}
