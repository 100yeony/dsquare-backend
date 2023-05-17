package com.ktds.dsquare.common.notification;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Notification {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Notification data such as "title", "body", etc.
     */
    @ElementCollection
    private Map<String, String> data;

    @CreatedDate
    private LocalDateTime sentAt;


    public static Notification toEntity(Map<String, String> data) {
        return Notification.builder()
                .data(data)
                .build();
    }

}
