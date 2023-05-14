package com.ktds.dsquare.common.notification.dto.response;

import com.ktds.dsquare.common.notification.RegistrationToken;
import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RegistrationTokenRegisterResponse {

    private List<String> subscribingTopics;


    public static RegistrationTokenRegisterResponse toDto(RegistrationToken entity) {
        return RegistrationTokenRegisterResponse.builder()
                .build();
    }

}
