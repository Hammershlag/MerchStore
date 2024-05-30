package com.example.merchstore.controllers;

import com.example.merchstore.dto.User;
import com.example.merchstore.services.CustomUserDetailsService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

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
                byte[] imageBytes = image.getBytes();
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
}
