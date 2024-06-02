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
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.example.merchstore.dto.ImageProcessor.*;

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
    public String registrationForm(@RequestParam(value = "error", required = false) String error, Model model) {
        logger.info("Reg get");
        model.addAttribute("user", new User());
        if (error != null) {
            switch (error) {
                case "username":
                    model.addAttribute("errorMessage", "Username already exists.");
                    break;
                case "email":
                    model.addAttribute("errorMessage", "Email already exists.");
                    break;
                case "phone":
                    model.addAttribute("errorMessage", "Phone number already exists.");
                    break;
                case "true":
                    model.addAttribute("errorMessage", "An unexpected error occurred. Please try again.");
                    break;
                default:
                    model.addAttribute("errorMessage", "Unknown error.");
            }
        }
        return "registration";
    }

    @PostMapping
    @ResponseBody
    public RedirectView registerUser(@ModelAttribute User user, @RequestParam("imageData") MultipartFile image, Model model) {
        try {
            if (customUserDetailsService.existsByUsername(user.getUsername())) {
                return new RedirectView("/api/register/form?error=username");
            }
            if(customUserDetailsService.existsByEmail(user.getEmail())) {
                return new RedirectView("/api/register/form?error=email");
            }
            if(customUserDetailsService.existsByPhoneNumber(user.getPhoneNumber())) {
                return new RedirectView("/api/register/form?error=phone");
            }
            if (!image.isEmpty()) {
                BufferedImage bufferedImage = ImageIO.read(image.getInputStream());

                //TODO if image is higher rather than wider, it gets rotated by 90 degrees, FIX IT but now im too lazy

                logger.info("Image height: " + bufferedImage.getHeight());
                logger.info("Image width: " + bufferedImage.getWidth());
                int angle = bufferedImage.getHeight() > bufferedImage.getWidth() ? 90 : 0;

                //bufferedImage = simpleResizeImage(bufferedImage, 200, 200);
                bufferedImage = rotateImage(bufferedImage, 90);
                bufferedImage = resizeAndCropImage(bufferedImage, 200, 200);
                byte[] imageBytes = imageToByteArray(bufferedImage, image.getOriginalFilename().substring(image.getOriginalFilename().lastIndexOf(".") + 1));
                user.setImage(imageBytes);

                model.addAttribute("user", user);
            } else {
                user.setImage(new byte[0]);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new RedirectView( "/api/register/form?error=true");
        }

        customUserDetailsService.registerUser(user, passwordEncoder);

        return new RedirectView("/api/login");
    }



}

