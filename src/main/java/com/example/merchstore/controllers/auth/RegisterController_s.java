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
 * The RegisterController_s class handles the web requests related to user registration in the application.
 *
 * It has two methods:
 * <ul>
 *     <li>registrationForm(String error, Model model): Prepares the model for the registration form, handles any error messages, and returns the view name.</li>
 *     <li>registerUser(User user, MultipartFile image, Model model): Handles the POST request for user registration. It validates the user data, processes the image data, sets the user's properties, registers the user, and redirects to the login form.</li>
 * </ul>
 *
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 28.05.2024
 */

@Controller
@RequestMapping("/api/register")
public class RegisterController_s {

    /**
     * The CustomUserDetailsService that this controller uses to perform CRUD operations on users.
     * @see CustomUserDetailsService
     */
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    /**
     * The PasswordEncoder that this controller uses to encode user passwords.
     * @see PasswordEncoder
     */
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Prepares the model for the registration form, handles any error messages, and returns the view name.
     *
     * @param error The error message to be displayed.
     * @param model The model to be prepared.
     * @return The view name.
     */
    @GetMapping("/form")
    public String registrationForm(@RequestParam(value = "error", required = false) String error, Model model,
                                   @RequestParam(required = false) String lang) {
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

    /**
     * Handles the POST request for user registration. It validates the user data, processes the image data, sets the user's properties, registers the user, and redirects to the login form.
     *
     * @param user The user to be registered.
     * @param image The image data for the user.
     * @param model The model to be prepared.
     * @return The redirect URL.
     */
    @PostMapping
    @ResponseBody
    public RedirectView registerUser(@ModelAttribute User user, @RequestParam("imageData") MultipartFile image, Model model, @RequestParam(required = false) String lang) {
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

