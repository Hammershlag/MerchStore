package com.example.merchstore.controllers.user;

import com.example.merchstore.components.models.User;
import com.example.merchstore.repositories.CustomUserRepository;
import com.example.merchstore.services.CustomUserDetailsService;
import com.example.merchstore.services.GlobalAttributeService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.LocalDateTime;

import static com.example.merchstore.components.utilities.ImageProcessor.*;

/**
 * The UserController_u class handles the web requests related to the user's profile in the application.
 *
 * It has three methods:
 * <ul>
 *     <li>getCurrentUser(Model model): Handles the GET request for viewing the user's profile. It returns the view name for the profile page.</li>
 *     <li>showEditUserForm(Model model): Handles the GET request for showing the edit user form. It returns the view name for the update page.</li>
 *     <li>updateUser(User user, MultipartFile image, String currentPassword, String newPassword, Model model): Handles the POST request for updating the user's profile. It retrieves the current user, checks the username, email, and phone number, checks and updates the image, checks and updates the password, updates the other fields, saves the user, adds the user to the session and global attributes, and returns a redirect to the profile page.</li>
 * </ul>
 *
 * @version 1.0
 * @since 31.05.2024
 */

@Controller
public class UserController_u {

    @Autowired
    private HttpSession httpSession;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private CustomUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private GlobalAttributeService globalAttributeService;

    @GetMapping("/profile")
    public String getCurrentUser(Model model) {
        return "user/profile";
    }

    @GetMapping("/api/update/user")
    public String showEditUserForm(Model model) {
        return "user/update";
    }

    @PostMapping("/api/update/user")
    public RedirectView updateUser(
            @ModelAttribute User user,
            @RequestParam("newImage") MultipartFile image,
            @RequestParam("currentPassword") String currentPassword,
            @RequestParam("newPassword") String newPassword,
            Model model) {
        User currentUser = (User) httpSession.getAttribute("user");

        if (customUserDetailsService.existsByUsername(user.getUsername()) && !user.getUsername().equals(currentUser.getUsername())) {
            return new RedirectView("/api/update/user?error=username");
        }
        if(customUserDetailsService.existsByEmail(user.getEmail()) && !user.getEmail().equals(currentUser.getEmail())) {
            return new RedirectView("/api/update/user?error=email");
        }
        if(customUserDetailsService.existsByPhoneNumber(user.getPhoneNumber()) && !user.getPhoneNumber().equals(currentUser.getPhoneNumber())) {
            return new RedirectView("/api/update/user?error=phone");
        }

        if (currentUser == null) {
            return new RedirectView("/login");
        }

        // Check and update image
        if (!image.isEmpty()) {
            try {
                BufferedImage bufferedImage = ImageIO.read(image.getInputStream());
                bufferedImage = rotateImage(bufferedImage, 90);  // Assuming rotation and cropping functions exist
                bufferedImage = resizeAndCropImage(bufferedImage, 200, 200);
                byte[] imageBytes = imageToByteArray(bufferedImage, image.getOriginalFilename().substring(image.getOriginalFilename().lastIndexOf(".") + 1));
                currentUser.setImage(imageBytes);
            } catch (IOException e) {
                model.addAttribute("errorMessage", "Failed to upload image.");
                return new RedirectView("/api/update/user?error=image");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        // Check and update password
        if (!currentPassword.isEmpty() && !newPassword.isEmpty()) {
            if (!passwordEncoder.matches(currentPassword, currentUser.getPassword())) {
                model.addAttribute("errorMessage", "Current password is incorrect.");
                return new RedirectView("/api/update/user?error=password");
            }
            currentUser.setPassword(passwordEncoder.encode(newPassword));
        }

        // Update other fields
        currentUser.setAddress(user.getAddress());
        currentUser.setEmail(user.getEmail());
        currentUser.setPhoneNumber(user.getPhoneNumber());
        currentUser.setUsername(user.getUsername());
        currentUser.setFirstName(user.getFirstName());
        currentUser.setLastName(user.getLastName());
        currentUser.setGender(user.getGender());
        currentUser.setUpdatedAt(LocalDateTime.now());

        userRepository.updateById(currentUser.getUserId(), currentUser);
        httpSession.setAttribute("user", currentUser);
        globalAttributeService.addAttribute("user", currentUser);

        return new RedirectView("/profile");
    }
}
