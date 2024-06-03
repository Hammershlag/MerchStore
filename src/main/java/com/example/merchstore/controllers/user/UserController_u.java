package com.example.merchstore.controllers.user;

import com.example.merchstore.model.User;
import com.example.merchstore.repositories.CustomUserRepository;
import com.example.merchstore.services.CustomUserDetailsService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.time.LocalDateTime;

/**
 * UserController_a for handling user-related requests.
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

    @GetMapping("/profile")
    public String getCurrentUser(Model model) {
        User user = (User) httpSession.getAttribute("user");

        if (user == null) {
            user = new User();
        } else {
            user = user.displayData();
        }

        model.addAttribute("user", user);
        return "user/profile";
    }

    @GetMapping("/api/update/user")
    public String showEditUserForm(Model model) {
        User user = (User) httpSession.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", user);
        return "user/update";
    }

    @PostMapping("/api/update/user")
    public RedirectView updateUser(@ModelAttribute User user, Model model) {
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
            new RedirectView("/login");
        }


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

        model.addAttribute("user", currentUser);
        return new RedirectView("/profile");
    }
}
