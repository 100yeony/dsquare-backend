package com.ktds.dsquare.member;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Role {

    USER("ROLE_USER"),
    MANAGER("ROLE_MANAGER"),
    OWNER("ROLE_OWNER"),
    ADMIN("ROLE_ADMIN")
    ;

    public final String name;

    Role(String name) {
        this.name = name;
    }


    @JsonCreator
    public static Role from(String name) {
        try {
            return Role.valueOf(name.strip().toUpperCase());
        } catch (IllegalArgumentException e) {
            return Role.USER;
        }
    }

}
