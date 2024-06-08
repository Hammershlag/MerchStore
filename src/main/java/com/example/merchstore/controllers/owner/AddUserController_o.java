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
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 08.06.2024
 */

@Controller
@RequestMapping("/api/owner/add/user")
public class AddUserController_o {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/form")
    public String addUserForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", Role.values());
        return "owner/add/user";
    }

    Logger logger = LoggerFactory.getLogger(AddUserController_o.class);

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
        logger.info("Adding user: " + role.toString());

        user.setRole(Role.getRole(role));

        logger.info("User role: " + user.getRole().toString());

        model.addAttribute("user", user);

        customUserDetailsService.registerUser(user, passwordEncoder);

        return new RedirectView("/api/owner/dashboard?userAdded=true");
    }

}

