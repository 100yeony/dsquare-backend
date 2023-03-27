package com.ktds.dsquare.common.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatusMsg implements Serializable {
    private static final long serialVersionUID = 1L;

    private String errorCode;
    private String errorMsg;
}
