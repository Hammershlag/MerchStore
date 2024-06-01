package com.example.merchstore.controllers;

import com.example.merchstore.dto.User;
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
 * UserController for handling user-related requests.
 * @version 1.0
 * @since 31.05.2024
 */
@Controller
public class UserController {

    @Autowired
    private HttpSession httpSession;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private CustomUserRepository userRepository;

    @GetMapping("/currentUser")
    public String getCurrentUser(Model model) {
        User user = (User) httpSession.getAttribute("user");

        if (user == null) {
            user = new User();
        } else {
            user = user.displayData();
        }

        model.addAttribute("user", user);
        return "userProfile";
    }

    @GetMapping("/api/update/user")
    public String showEditUserForm(Model model) {
        User user = (User) httpSession.getAttribute("user");

        if (user == null) {
            return "redirect:/login"; // Redirect to login if no user is in session
        }

        model.addAttribute("user", user);
        return "updateUser";
    }

    @PostMapping("/api/update/user")
    public RedirectView updateUser(@ModelAttribute User user, Model model) {
        User currentUser = (User) httpSession.getAttribute("user");

        if (customUserDetailsService.existsByUsername(user.getUsername()) && !user.getUsername().equals(currentUser.getUsername())) {
            return new RedirectView("/api/update/user?error=username");
        }
        if(customUserDetailsService.existsByEmail(user.getEmail())) {
            return new RedirectView("/api/update/user?error=email");
        }
        if(customUserDetailsService.existsByPhoneNumber(user.getPhoneNumber())) {
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
        return new RedirectView("/currentUser");
    }
}
