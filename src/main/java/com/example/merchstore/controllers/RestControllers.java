package com.example.merchstore.controllers;

import com.example.merchstore.Decorators.UserDecorator;
import com.example.merchstore.dto.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.nio.ByteBuffer;

/**
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 30.05.2024
 */

@RestController
public class RestControllers {


    @Autowired
    private HttpSession httpSession;

    @GetMapping("/")
    public RedirectView homeRedirect() {
        return new RedirectView("/home");
    }

    @GetMapping("/currentUser")
    public User home2() {
        User user = (User) httpSession.getAttribute("user");

        if (user != null) {;
            return user.displayData();
        } else {
            return UserDecorator.create("Anonymous").displayData();
        }
    }


    @GetMapping("/image/get")
    public ResponseEntity<byte[]> getImage(HttpSession session) {
        User user = (User) session.getAttribute("user");
        ByteBuffer imageBuffer = ByteBuffer.wrap(user.getImage());
        if (imageBuffer == null) {
            return ResponseEntity.notFound().build();
        }
        byte[] imageData = new byte[imageBuffer.remaining()];
        imageBuffer.get(imageData);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
    }

}
