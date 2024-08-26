package com.example.merchstore.controllers.functionality;

import com.example.merchstore.components.models.Ad;
import com.example.merchstore.components.models.Item;
import com.example.merchstore.components.models.User;
import com.example.merchstore.repositories.AdRepository;
import com.example.merchstore.repositories.CustomUserRepository;
import com.example.merchstore.repositories.ItemRepository;
import jakarta.servlet.http.HttpSession;
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
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 03.06.2024
 */

@RestController
@RequestMapping("/api/image")
public class ImageController_f {

    @Autowired
    private CustomUserRepository customUserRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private AdRepository adRepository;

    @SneakyThrows
    @GetMapping("/user")
    public ResponseEntity<byte[]> getUserImage(HttpSession session) {
        User user = (User) session.getAttribute("user");
        byte[] image = user.getImage();
        if (user.getImage() == null || user.getImage().length == 0) {
            switch (user.getGender()) {
                case MALE -> image = getImageAsByteArray("static/images/avatars/male_avatar.jpg");
                case FEMALE -> image = getImageAsByteArray("static/images/avatars/female_avatar.jpg");
                default -> image = getImageAsByteArray("static/images/avatars/default_avatar.jpg");
            }
        }
        ByteBuffer imageBuffer = ByteBuffer.wrap(image);
        byte[] imageData = new byte[imageBuffer.remaining()];
        imageBuffer.get(imageData);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
    }

    @SneakyThrows
    @GetMapping("/user/{id}")
    public ResponseEntity<byte[]> getUserImage(@PathVariable Long id) {
        User user = customUserRepository.findById(id).orElse(null);
        byte[] image = user != null ? user.getImage() : null;
        if (user == null || image == null || image.length == 0) {
            if (user != null && user.getGender() != null) {
                switch (user.getGender()) {
                    case MALE -> image = getImageAsByteArray("static/images/avatars/male_avatar.jpg");
                    case FEMALE -> image = getImageAsByteArray("static/images/avatars/female_avatar.jpg");
                    default -> image = getImageAsByteArray("static/images/avatars/default_avatar.jpg");
                }
            } else {
                image = getImageAsByteArray("static/images/avatars/default_avatar.jpg");
            }
        }
        ByteBuffer imageBuffer = ByteBuffer.wrap(image);
        byte[] imageData = new byte[imageBuffer.remaining()];
        imageBuffer.get(imageData);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
    }

    @SneakyThrows
    @GetMapping("/item/{id}")
    public ResponseEntity<byte[]> getItemImage(@PathVariable Long id) {
        Item item = itemRepository.findById(id).orElse(null);
        byte[] image = item != null ? item.getImage() : null;
        if (item == null || image == null || image.length == 0) {
            image = getImageAsByteArray("static/images/avatars/male_avatar.jpg");
        }
        ByteBuffer imageBuffer = ByteBuffer.wrap(image);
        byte[] imageData = new byte[imageBuffer.remaining()];
        imageBuffer.get(imageData);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
    }

    @SneakyThrows
    @GetMapping("/ad/{id}")
    public ResponseEntity<byte[]> getAdImage(@PathVariable Long id) {
        Ad ad = adRepository.findById(id).orElse(null);
        byte[] image = ad != null ? ad.getImage() : null;
        if (ad == null || image == null || image.length == 0) {
            image = getImageAsByteArray("static/images/avatars/male_avatar.jpg");
        }
        ByteBuffer imageBuffer = ByteBuffer.wrap(image);
        byte[] imageData = new byte[imageBuffer.remaining()];
        imageBuffer.get(imageData);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
    }

}
