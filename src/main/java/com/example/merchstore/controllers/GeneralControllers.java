package com.example.merchstore.controllers;

import com.example.merchstore.Decorators.UserDecorator;
import com.example.merchstore.dto.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 30.05.2024
 */

@Controller
public class GeneralControllers {


    @Autowired
    private HttpSession httpSession;

    @GetMapping("/image")
    public String image(Model model) {
        User user = (User) httpSession.getAttribute("user");

        if (user != null) {
            model.addAttribute("user", user);
        } else {
            return "redirect:/home";
        }
        return "image";
    }

}
