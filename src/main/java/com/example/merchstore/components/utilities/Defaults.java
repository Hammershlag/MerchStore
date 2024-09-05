package com.example.merchstore.components.utilities;

import org.springframework.stereotype.Component;

/**
 * The Defaults class provides default values for various entities in the system.
 *
 * It has seven static fields:
 * <ul>
 *     <li>DEFAULT_USER_MALE_IMAGE: The default image path for a male user.</li>
 *     <li>DEFAULT_USER_FEMALE_IMAGE: The default image path for a female user.</li>
 *     <li>DEFAULT_USER_IMAGE: The default image path for a user.</li>
 *     <li>DEFAULT_ITEM_IMAGE: The default image path for an item.</li>
 *     <li>DEFAULT_AD_IMAGE: The default image path for an ad.</li>
 *     <li>DEFAULT_CATEGORY_IMAGE: The default image path for a category.</li>
 * </ul>
 *
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 28.08.2024
 */


@Component
public class Defaults {

    /**
     * The default image path for user male image
     */
    public static String DEFAULT_USER_MALE_IMAGE = "static/images/avatars/male_avatar_small.jpg";

    /**
     * The default image path user female image
     */
    public static String DEFAULT_USER_FEMALE_IMAGE = "static/images/avatars/female_avatar_small.jpg";

    /**
     * The default image path for user image
     */
    public static String DEFAULT_USER_IMAGE = "static/images/avatars/default_avatar_small.jpg";

    /**
     * The default image path for item image
     */
    public static String DEFAULT_ITEM_IMAGE = "static/images/avatars/male_avatar_small.jpg";

    /**
     * The default image path for ad image
     */
    public static String DEFAULT_AD_IMAGE = "static/images/avatars/male_avatar_small.jpg";

    /**
     * The default image path for category image
     */
    public static String DEFAULT_CATEGORY_IMAGE = "static/images/avatars/male_avatar_small.jpg";
}
