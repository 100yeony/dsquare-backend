package com.ktds.dsquare.common.notification;

import com.ktds.dsquare.common.enums.NotifType;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Notification {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private NotifType type;

    /**
     * Notification data such as "title", "body", etc.
     */
    @ElementCollection
    private Map<String, String> data;

    @CreatedDate
    private LocalDateTime sentAt;


    public static Notification toEntity(NotifType type, Map<String, String> data) {
        return Notification.builder()
                .type(type)
                .data(data)
                .build();
    }

}
