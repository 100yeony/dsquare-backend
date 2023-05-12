package com.ktds.dsquare.member;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Role {

    USER,
    MANAGER,
    OWNER,
    ADMIN
    ;


    @JsonCreator
    public static Role from(String name) {
        try {
            return Role.valueOf(name.strip().toUpperCase());
        } catch (IllegalArgumentException e) {
            return Role.USER;
        }
    }

}
