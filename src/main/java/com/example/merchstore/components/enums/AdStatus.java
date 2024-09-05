package com.example.merchstore.components.enums;

/**
 * The AdStatus enum represents the status of an advertisement in the system.
 * It can have one of the following values:
 * <ul>
 *     <li>ACTIVE: The advertisement is currently active and visible to users.</li>
 *     <li>INACTIVE: The advertisement is currently inactive and not visible to users.</li>
 *     <li>DELETED: The advertisement has been deleted and is not visible to users.</li>
 * </ul>
 *
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 25.08.2024
 */
public enum AdStatus {
    /**
     * Represents an active advertisement.
     */
    ACTIVE,

    /**
     * Represents an inactive advertisement.
     */
    INACTIVE,

    /**
     * Represents a deleted advertisement.
     */
    DELETED
}