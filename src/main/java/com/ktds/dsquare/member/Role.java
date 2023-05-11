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
        name = name.toUpperCase();
        for (Role role : Role.values()) {
            if (role.toString().equals(name))
                return role;
        }

        throw new RuntimeException("Invalid role. Please using [ user || manager || owner || admin ]");
    }

}
