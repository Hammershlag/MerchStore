package com.example.merchstore.components.utilities;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

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
    public static final String DEFAULT_USER_MALE_IMAGE = "static/images/avatars/male_avatar_small.jpg";

    /**
     * The default image path user female image
     */
    public static final String DEFAULT_USER_FEMALE_IMAGE = "static/images/avatars/female_avatar_small.jpg";

    /**
     * The default image path for user image
     */
    public static final String DEFAULT_USER_IMAGE = "static/images/avatars/male_avatar_small.jpg";

    /**
     * The default image path for item image
     */
    public static final String DEFAULT_ITEM_IMAGE = "static/images/avatars/male_avatar_small.jpg";

    /**
     * The default image path for ad image
     */
    public static final String DEFAULT_AD_IMAGE = "static/images/avatars/male_avatar_small.jpg";

    /**
     * The default image path for category image
     */
    public static final String DEFAULT_CATEGORY_IMAGE = "static/images/avatars/male_avatar_small.jpg";

    public static final int BACKUP_INTERVAL_DAYS = 0;
    public static final int BACKUP_INTERVAL_HOURS = 2;
    public static final int BACKUP_INTERVAL_MINUTES = 0;
    public static final int BACKUP_INTERVAL_SECONDS = 0;
    public static final int BACKUP_INTERVAL = (((BACKUP_INTERVAL_DAYS *24) + BACKUP_INTERVAL_HOURS * 60 + BACKUP_INTERVAL_MINUTES) * 60 + BACKUP_INTERVAL_SECONDS) * 1000;

    public static final int TRANSLATION_OVER_DUE_DAYS = 7;
    public static final String TRANSLATION_DEFAULT_LANGUAGE = "en";

    public static final Locale DEFAULT_LOCALE = new Locale("pl", "PL");
    public static final Locale FALLBACK_LOCALE = new Locale("en", "US");
    public static final List<Locale> ALLOWED_LOCALES = Arrays.asList(
            new Locale("pl", "PL"),
            new Locale("en", "US"),
            new Locale("de", "DE")
    );
}
