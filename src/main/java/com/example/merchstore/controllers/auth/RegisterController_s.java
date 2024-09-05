package com.example.merchstore.controllers.auth;

import com.example.merchstore.components.enums.Role;
import com.example.merchstore.components.models.User;
import com.example.merchstore.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import static com.example.merchstore.components.utilities.ImageProcessor.*;

/**
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 28.05.2024
 */

@Controller
@RequestMapping("/api/register")
public class RegisterController_s {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/form")
    public String registrationForm(@RequestParam(value = "error", required = false) String error, Model model) {
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
        return "auth/registration";
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

                int angle = bufferedImage.getHeight() > bufferedImage.getWidth() ? 90 : 0;

                bufferedImage = rotateImage(bufferedImage, angle);
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
        user.setRole(Role.USER);
        customUserDetailsService.registerUser(user, passwordEncoder);

        return new RedirectView("/api/login/form");
    }



}

