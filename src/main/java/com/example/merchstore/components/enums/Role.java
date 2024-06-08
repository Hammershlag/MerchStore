package com.example.merchstore.components.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 28.05.2024
 */

public enum Role {
    USER(0),
    ADMIN(1),
    OWNER(2);

    private final int value;
    private static final Map<String, Role> ROLE_MAP = new HashMap<>();

    Role(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    static {
        for (Role role : Role.values()) {
            ROLE_MAP.put(role.name(), role);
        }
    }

    public static Role getRole(String role) {
        return ROLE_MAP.get(role);
    }
}
