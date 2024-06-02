package com.example.merchstore.controllers;

import com.example.merchstore.Decorators.UserDecorator;
import com.example.merchstore.dto.Item;
import com.example.merchstore.dto.User;
import com.example.merchstore.repositories.CustomUserRepository;
import com.example.merchstore.repositories.ItemRepository;
import jakarta.servlet.http.HttpSession;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import static com.example.merchstore.dto.ImageProcessor.getImageAsByteArray;

/**
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 30.05.2024
 */

@RestController
public class RestControllers {


    @Autowired
    private HttpSession httpSession;
    @Autowired
    private CustomUserRepository customUserRepository;

    @Autowired
    private ItemRepository itemRepository;

    @GetMapping("/")
    public RedirectView homeRedirect() {
        return new RedirectView("/home");
    }


    @SneakyThrows
    @GetMapping("/image/user")
    public ResponseEntity<byte[]> getImage(HttpSession session) {
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
    @GetMapping("/image/user/{id}")
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
    @GetMapping("/image/item/{id}")
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





}
