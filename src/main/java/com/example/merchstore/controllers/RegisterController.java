package com.example.merchstore.controllers;

import com.example.merchstore.dto.User;
import com.example.merchstore.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/register")
public class RegisterController {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/form")
    public String registrationForm(Model model) {
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping
    public String registerUser(@ModelAttribute User user) {
        customUserDetailsService.registerUser(user.getUsername(), user.getPassword(), user.getEmail(), user.getFirstName(), user.getLastName(), user.getPhoneNumber(), user.getAddress(), passwordEncoder);
        return "redirect:/login";
    }
}
