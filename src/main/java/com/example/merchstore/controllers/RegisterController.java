package com.example.merchstore.controllers;

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
        model.addAttribute("username", "");
        model.addAttribute("password", "");
        return "registration"; // Assuming "registration.html" is in src/main/resources/templates
    }

    @PostMapping
    public String registerUser(@RequestParam String username, @RequestParam String password) {
        customUserDetailsService.registerUser(username, password, passwordEncoder);
        return "redirect:/login";
    }
}
