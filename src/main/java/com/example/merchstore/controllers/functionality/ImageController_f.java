package com.example.merchstore.controllers.functionality;

import com.example.merchstore.components.models.Ad;
import com.example.merchstore.components.models.Category;
import com.example.merchstore.components.models.Item;
import com.example.merchstore.components.models.User;
import com.example.merchstore.repositories.AdRepository;
import com.example.merchstore.repositories.CategoryRepository;
import com.example.merchstore.repositories.CustomUserRepository;
import com.example.merchstore.repositories.ItemRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.ByteBuffer;

import static com.example.merchstore.components.utilities.ImageProcessor.getImageAsByteArray;

/**
 * The ImageController_f class handles the web requests related to images in the application.
 *
 * It has four methods:
 * <ul>
 *     <li>getItemImage(Long id): Handles the GET request for retrieving an item's image. It retrieves the item from the repository, gets its image, and returns it. If the item or its image is not found, it returns a default image.</li>
 *     <li>getCategoryImage(Long id): Handles the GET request for retrieving a category's image. It retrieves the category from the repository, gets its image, and returns it. If the category or its image is not found, it returns a default image.</li>
 *     <li>getUserImage(Long id): Handles the GET request for retrieving a user's image. It retrieves the user from the repository, gets its image, and returns it. If the user or its image is not found, it returns a default image.</li>
 *     <li>getAdImage(Long id): Handles the GET request for retrieving an ad's image. It retrieves the ad from the repository, gets its image, and returns it. If the ad or its image is not found, it returns a default image.</li>
 * </ul>
 *
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 03.09.2024
 */

@RestController
@RequestMapping("/api/image")
public class ImageController_f {

    /**
     * The ItemRepository that this controller uses to perform CRUD operations on items.
     * @see ItemRepository
     */
    @Autowired
    private ItemRepository itemRepository;

    /**
     * The CategoryRepository that this controller uses to perform CRUD operations on categories.
     * @see CategoryRepository
     */
    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * The CustomUserRepository that this controller uses to perform CRUD operations on users.
     * @see CustomUserRepository
     */
    @Autowired
    private CustomUserRepository userRepository;

    /**
     * The AdRepository that this controller uses to perform CRUD operations on ads.
     * @see AdRepository
     */
    @Autowired
    private AdRepository adRepository;

    /**
     * Handles the GET request for retrieving an item's image. It retrieves the item from the repository, gets its image, and returns it. If the item or its image is not found, it returns a default image.
     *
     * @param id The ID of the item.
     * @return The item's image.
     */
    @SneakyThrows
    @GetMapping("/item/{id}")
    public ResponseEntity<byte[]> getItemImage(@PathVariable Long id) {
        Item item = itemRepository.findById(id).orElse(null);
        byte[] image = item != null ? item.getImage() : null;
        if (item == null || image == null || image.length == 0) {
            image = getImageAsByteArray("static/images/avatars/male_avatar_small.jpg");
        }
        return getResponseEntity(image);
    }

    /**
     * Handles the GET request for retrieving a category's image. It retrieves the category from the repository, gets its image, and returns it. If the category or its image is not found, it returns a default image.
     *
     * @param id The ID of the category.
     * @return The category's image.
     */
    @SneakyThrows
    @GetMapping("/category/{id}")
    public ResponseEntity<byte[]> getCategoryImage(@PathVariable Long id) {
        Category category = categoryRepository.findById(id).orElse(null);
        byte[] image = category != null ? category.getImage() : null;
        if (category == null || image == null || image.length == 0) {
            image = getImageAsByteArray("static/images/avatars/male_avatar_small.jpg");
        }
        return getResponseEntity(image);
    }

    /**
     * Handles the GET request for retrieving a user's image. It retrieves the user from the repository, gets its image, and returns it. If the user or its image is not found, it returns a default image.
     *
     * @param id The ID of the user.
     * @return The user's image.
     */
    @SneakyThrows
    @GetMapping("/user/{id}")
    public ResponseEntity<byte[]> getUserImage(@PathVariable Long id) {
        User user = userRepository.findById(id).orElse(null);
        byte[] image = user != null ? user.getImage() : null;
        if (user == null || image == null || image.length == 0) {
            image = getImageAsByteArray("static/images/avatars/male_avatar_small.jpg");
        }
        return getResponseEntity(image);
    }

    /**
     * Handles the GET request for retrieving an ad's image. It retrieves the ad from the repository, gets its image, and returns it. If the ad or its image is not found, it returns a default image.
     *
     * @param id The ID of the ad.
     * @return The ad's image.
     */
    @SneakyThrows
    @GetMapping("/ad/{id}")
    public ResponseEntity<byte[]> getAdImage(@PathVariable Long id) {
        Ad ad = adRepository.findById(id).orElse(null);
        byte[] image = ad != null ? ad.getImage() : null;
        if (ad == null || image == null || image.length == 0) {
            image = getImageAsByteArray("static/images/avatars/male_avatar_small.jpg");
        }
        return getResponseEntity(image);
    }

    /**
     * Creates a ResponseEntity with the provided image data.
     *
     * @param image The image data.
     * @return The ResponseEntity.
     */
    private ResponseEntity<byte[]> getResponseEntity(byte[] image) {
        ByteBuffer imageBuffer = ByteBuffer.wrap(image);
        byte[] imageData = new byte[imageBuffer.remaining()];
        imageBuffer.get(imageData);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
    }
}
