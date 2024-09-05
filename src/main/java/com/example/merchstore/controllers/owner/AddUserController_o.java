package com.example.merchstore.controllers.owner;

import com.example.merchstore.components.enums.Role;
import com.example.merchstore.components.models.User;
import com.example.merchstore.services.CustomUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * The AddUserController_o class handles the web requests related to adding a user in the application.
 *
 * It has two methods:
 * <ul>
 *     <li>addUserForm(Model model): Handles the GET request for the add user form. It adds a new User object and the list of roles to the model and returns the view name for the add user form.</li>
 *     <li>addUser(User user, MultipartFile image, String role, Model model): Handles the POST request for adding a user. It checks if a user with the same username, email, or phone number already exists, processes the image, sets the role of the user, adds the user to the model, registers the user, and returns a redirect view to the dashboard.</li>
 * </ul>
 *
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 08.06.2024
 */

@Controller
@RequestMapping("/api/owner/add/user")
public class AddUserController_o {

    /**
     * The CustomUserDetailsService that this controller uses to register a user.
     * @see CustomUserDetailsService
     */
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    /**
     * The PasswordEncoder that this controller uses to encode the password of a user.
     * @see PasswordEncoder
     */
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Handles the GET request for the add user form. It adds a new User object and the list of roles to the model and returns the view name for the add user form.
     *
     * @param model The model to be prepared.
     * @return The view name for the add user form.
     */
    @GetMapping("/form")
    public String addUserForm(Model model) {
        model.addAttribute("newUser", new User());
        model.addAttribute("roles", Role.values());
        return "owner/add/user";
    }

    /**
     * Handles the POST request for adding a user. It checks if a user with the same username, email, or phone number already exists, processes the image, sets the role of the user, adds the user to the model, registers the user, and returns a redirect view to the dashboard.
     *
     * @param user The user to be added.
     * @param image The image of the user.
     * @param role The role of the user.
     * @param model The model to be prepared.
     * @return The redirect view to the dashboard.
     */
    @PostMapping
    @ResponseBody
    public RedirectView addUser(@ModelAttribute User user, @RequestParam("imageData") MultipartFile image,
                                @RequestParam("designatedRole") String role, Model model) {
        try {
            if (customUserDetailsService.existsByUsername(user.getUsername())) {
                return new RedirectView("/api/owner/add/user/form?error=username");
            }
            if (customUserDetailsService.existsByEmail(user.getEmail())) {
                return new RedirectView("/api/owner/add/user/form?error=email");
            }
            if (customUserDetailsService.existsByPhoneNumber(user.getPhoneNumber())) {
                return new RedirectView("/api/owner/add/user/form?error=phone");
            }
            if (!image.isEmpty()) {
                BufferedImage bufferedImage = ImageIO.read(image.getInputStream());

                int angle = bufferedImage.getHeight() > bufferedImage.getWidth() ? 90 : 0;
                bufferedImage = rotateImage(bufferedImage, angle);
                bufferedImage = resizeAndCropImage(bufferedImage, 200, 200);
                byte[] imageBytes = imageToByteArray(bufferedImage, image.getOriginalFilename().substring(image.getOriginalFilename().lastIndexOf(".") + 1));
                user.setImage(imageBytes);

            } else {
                user.setImage(new byte[0]);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new RedirectView("/api/owner/add/user/form?error=true");
        }
        user.setRole(Role.getRole(role));

        model.addAttribute("user", user);

        customUserDetailsService.registerUser(user, passwordEncoder);

        return new RedirectView("/api/owner/dashboard?userAdded=true");
    }

}

