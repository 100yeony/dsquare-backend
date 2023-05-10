package com.ktds.dsquare.common.enums;

public enum RoleType {
    USER,
    MANAGER,
    OWNER,
    ADMIN;

    public static RoleType findRoleType(String role) {
        switch(role) {
            case "user":
                return RoleType.USER;
            case "manager":
                return RoleType.MANAGER;
            case "owner":
                return RoleType.OWNER;
            case "admin":
                return RoleType.ADMIN;
            default:
                throw new RuntimeException("Invalid Role Type. Using USER || MANAGER || OWNER || ADMIN");
        }
    }
}
