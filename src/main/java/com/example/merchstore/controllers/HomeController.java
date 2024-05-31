package com.example.merchstore.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 28.05.2024
 */

@Controller
public class HomeController {

    @GetMapping("/home")
    public String home(HttpSession session, Model model) {
        Boolean isLoggedIn = (Boolean) session.getAttribute("isLoggedIn");
        if (isLoggedIn == null) {
            isLoggedIn = false;
        }
        model.addAttribute("isLoggedIn", isLoggedIn);
        return "home";
    }
}
