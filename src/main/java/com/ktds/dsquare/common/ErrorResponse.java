package com.ktds.dsquare.common;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ErrorResponse {

    private String code;
    private String message;

}
