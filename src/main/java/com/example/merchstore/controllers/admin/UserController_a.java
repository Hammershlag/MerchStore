package com.example.merchstore.controllers.admin;

import com.example.merchstore.components.models.User;
import com.example.merchstore.repositories.CustomUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String viewUsers(@RequestParam(value = "page", defaultValue = "0") int page,
                            @RequestParam(value = "size", defaultValue = "10") int size,
                            @RequestParam(value = "search", required = false) String search,
                            Model model) {
        Page<User> usersPage;

        if (search != null && !search.isEmpty()) {
            usersPage = userRepository.findByUsernameStartingWithIgnoreCase(search, PageRequest.of(page, size));
        } else {
            usersPage = userRepository.findAll(PageRequest.of(page, size));
        }

        model.addAttribute("users", usersPage.getContent());
        model.addAttribute("totalPages", usersPage.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("search", search);
        return "admin/view/viewUsers";
    }
}
