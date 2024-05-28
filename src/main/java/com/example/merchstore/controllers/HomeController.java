package com.example.merchstore.controllers;

import com.example.merchstore.Decorators.UserDecorator;
import com.example.merchstore.dto.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @Autowired
    private HttpSession httpSession; // Inject HttpSession to access the stored User object

    @GetMapping("/home")
    @ResponseBody
    public String home() {
        return "Welcome to the home page!";
    }

    @GetMapping("/home2")
    public User home2() {

        User user = (User) httpSession.getAttribute("user");

        if (user != null) {
            return user;
        } else {
            return UserDecorator.create("Anonymous");
        }
    }
}
