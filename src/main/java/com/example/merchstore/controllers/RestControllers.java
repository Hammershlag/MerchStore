package com.example.merchstore.controllers;

import com.example.merchstore.Decorators.UserDecorator;
import com.example.merchstore.dto.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

/**
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 30.05.2024
 */

@RestController
public class RestControllers {


    @Autowired
    private HttpSession httpSession;

    @GetMapping("/")
    public RedirectView homeRedirect() {
        return new RedirectView("/home");
    }

    @GetMapping("/currentUser")
    public User home2() {
        User user = (User) httpSession.getAttribute("user");

        if (user != null) {;
            return user.displayData();
        } else {
            return UserDecorator.create("Anonymous").displayData();
        }
    }

}
