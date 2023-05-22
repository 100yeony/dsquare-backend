package com.ktds.dsquare.common.notification.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ktds.dsquare.common.enums.NotifType;
import com.ktds.dsquare.common.notification.Notification;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Map;
import java.util.Objects;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationInfo implements Comparable<NotificationInfo> {

    private NotifType type;
    private Map<String, String> data;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime sentAt;


    public static NotificationInfo toDto(Notification entity) {
        return NotificationInfo.builder()
                .type(entity.getType())
                .data(entity.getData())
                .sentAt(entity.getSentAt())
                .build();
    }


    @Override
    public int compareTo(NotificationInfo o) {
        return Objects.compare(this.sentAt, o.getSentAt(), Comparator.reverseOrder());
    }
}
