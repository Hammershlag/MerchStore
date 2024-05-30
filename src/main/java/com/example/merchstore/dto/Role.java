package com.example.merchstore.dto;

/**
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 28.05.2024
 */

public enum Role implements DataDisplay{
    USER,
    ADMIN;

    @Override
    public DataDisplay displayData() {
        return null;
    }

    @Override
    public DataDisplay limitedDisplayData() {
        return null;
    }
}
