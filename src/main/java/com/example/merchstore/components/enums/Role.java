package com.example.merchstore.components.enums;

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

    Role(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
