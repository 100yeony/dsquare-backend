package com.ktds.dsquare.common.notification.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TopicSubscribeResponse {

    private int successCount;
    private int failureCount;

}
