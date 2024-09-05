package com.example.merchstore.components.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * The Role enum represents the role of a user in the system.
 * It can have one of the following values:
 * <ul>
 *     <li>USER: The user is a regular user (value 0).</li>
 *     <li>ADMIN: The user is an administrator (value 1).</li>
 *     <li>OWNER: The user is an owner (value 2).</li>
 * </ul>
 * Each role is associated with a specific integer value.
 * The {@link #getValue()} method can be used to get this integer value.
 * The {@link #getRole(String)} method can be used to get the Role constant from its name.
 *
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 28.05.2024
 */
public enum Role {
    /**
     * Represents a regular user.
     */
    USER(0),

    /**
     * Represents an administrator.
     */
    ADMIN(1),

    /**
     * Represents an owner.
     */
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
