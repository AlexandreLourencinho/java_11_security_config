package com.example.springsecurityauthtwo.security.model.enumeration;

/**
 * Roles enumeration types
 *
 * @author Alexandre Lourencinho
 * @version 1.0
 */
public enum ERole {
    ROLE_USER("User"),
    ROLE_MODERATOR("Moderator"),
    ROLE_ACTUATOR("Actuator"),
    ROLE_ADMIN("Admin");

    private final String value;

    ERole(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static ERole fromString(String value) {
        for (ERole role : ERole.values()) {
            if (role.getValue().equalsIgnoreCase(value)) {
                return role;
            }
        }
        return null;
    }
}
