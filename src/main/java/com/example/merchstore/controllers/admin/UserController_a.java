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
 * The UserController_a class handles the web requests related to users in the admin section of the application.
 *
 * It has one method:
 * <ul>
 *     <li>viewUsers(int page, int size, String search, Model model): Handles the GET request for viewing users. It retrieves the users based on the search parameter and pagination, adds them to the model, then returns the view name.</li>
 * </ul>
 *
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 03.06.2024
 */
@Controller
@RequestMapping("/api/admin")
public class UserController_a {

    /**
     * The CustomUserRepository that this controller uses to perform CRUD operations on users.
     * @see CustomUserRepository
     */
    @Autowired
    private CustomUserRepository userRepository;

    /**
     * Handles the GET request for viewing users. It retrieves the users based on the search parameter and pagination, adds them to the model, then returns the view name.
     *
     * @param page The page number.
     * @param size The number of users per page.
     * @param search The search parameter.
     * @param model The model to be prepared.
     * @return The view name.
     */
    @GetMapping("/view/users")
    public String viewUsers(@RequestParam(value = "page", defaultValue = "0") int page,
                            @RequestParam(value = "size", defaultValue = "10") int size,
                            @RequestParam(value = "searchUser", required = false) String search,
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
        model.addAttribute("searchUser", search);
        return "admin/view/viewUsers";
    }
}
