package com.example.merchstore.controllers;

import com.example.merchstore.dto.User;
import com.example.merchstore.services.CustomUserDetailsService;
import org.imgscalr.Scalr;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 28.05.2024
 */

@Controller
@RequestMapping("/api/register")
public class RegisterController {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private org.slf4j.Logger logger = LoggerFactory.getLogger(RegisterController.class);

    @GetMapping("/form")
    public String registrationForm(Model model) {
        logger.info("Reg get");
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping
    @ResponseBody
    public RedirectView registerUser(@ModelAttribute User user, @RequestParam("imageData") MultipartFile image, Model model) {
        try {
            if (!image.isEmpty()) {
                BufferedImage bufferedImage = simpleResizeImage(ImageIO.read(image.getInputStream()), 200);
                byte[] imageBytes = imageToByteArray(bufferedImage, image.getOriginalFilename().substring(image.getOriginalFilename().lastIndexOf(".") + 1));
                user.setImage(imageBytes);

                model.addAttribute("user", user);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new RedirectView( "/api/register/form?error=true");
        }

        customUserDetailsService.registerUser(user, passwordEncoder);

        return new RedirectView("/api/login");
    }

    static byte[] imageToByteArray(BufferedImage image, String formatName) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, formatName, stream);
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
        return stream.toByteArray();
    }

    BufferedImage simpleResizeImage(BufferedImage originalImage, int targetWidth) throws Exception {
        return Scalr.resize(originalImage, targetWidth);
    }


}

