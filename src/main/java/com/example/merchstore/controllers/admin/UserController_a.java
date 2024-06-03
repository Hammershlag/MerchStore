package com.example.merchstore.controllers.admin;

import com.example.merchstore.model.User;
import com.example.merchstore.repositories.CustomUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 03.06.2024
 */

@Controller
@RequestMapping("/api/admin")
public class UserController_a {

    @Autowired
    private CustomUserRepository userRepository;

    @GetMapping("/view/users")
    public String viewUsers(Model model) {
        List<User> users = userRepository.findNonAdminUsers();
        model.addAttribute("users", users);
        return "admin/view/viewUsers";
    }

}
