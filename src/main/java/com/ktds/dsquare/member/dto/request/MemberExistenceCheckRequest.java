package com.ktds.dsquare.member.dto.request;

import lombok.Getter;

@Getter
public class MemberExistenceCheckRequest {

    private String type;
    private String value;

}
