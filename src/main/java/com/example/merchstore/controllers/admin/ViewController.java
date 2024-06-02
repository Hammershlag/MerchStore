package com.example.merchstore.controllers.admin;

import com.example.merchstore.dto.Category;
import com.example.merchstore.dto.Discount;
import com.example.merchstore.dto.User;
import com.example.merchstore.repositories.CategoryRepository;
import com.example.merchstore.repositories.CustomUserRepository;
import com.example.merchstore.repositories.DiscountRepository;
import com.example.merchstore.repositories.ItemRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 02.06.2024
 */

@Controller
@RequestMapping("/api/admin/view")
public class ViewController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private DiscountRepository discountRepository;

    @Autowired
    private CustomUserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @GetMapping("/categories")
    public String getAllCategories(Model model) {
        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);
        return "viewCategories";
    }

    @GetMapping("/discounts")
    public String getAllDiscounts(Model model) {
        List<Discount> discounts = discountRepository.findAll();
        model.addAttribute("discounts", discounts);
        return "viewDiscounts";
    }

    @GetMapping("/discounts/valid")
    public String getValidDiscounts(Model model) {
        List<Discount> validDiscounts = discountRepository.findAllValidDiscounts(LocalDate.now());
        model.addAttribute("discounts", validDiscounts);
        return "viewDiscounts";
    }

    @GetMapping("/users")
    public String viewUsers(Model model) {
        List<User> users = userRepository.findNonAdminUsers();
        model.addAttribute("users", users);
        return "viewUsers";
    }

    @GetMapping("/items")
    public String viewItems(Model model) {
        model.addAttribute("items", itemRepository.findAll());
        return "viewItems";
    }
}
