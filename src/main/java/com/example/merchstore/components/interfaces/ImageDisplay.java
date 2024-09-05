package com.example.merchstore.components.interfaces;

/**
 * The ImageDisplay interface provides methods for manipulating the display of images.
 * It is designed to control how images are displayed to users.
 *
 * It has two methods:
 * <ul>
 *     <li>{@link #base64Image()}: This method is used to get the Base64 representation of an image. The implementation of this method should handle the logic of converting the image to Base64.</li>
 *     <li>{@link #setDefaultImage()}: This method is used to set the default image. The implementation of this method should handle the logic of what the default image should be.</li>
 * </ul>
 *
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 27.08.2024
 */
public interface ImageDisplay {
    /**
     * Get the Base64 representation of an image. The implementation of this method should handle the logic of converting the image to Base64.
     *
     * @return a String representing the Base64 image.
     */
    String base64Image();

    /**
     * Set the default image. The implementation of this method should handle the logic of what the default image should be.
     */
    void setDefaultImage();
}
