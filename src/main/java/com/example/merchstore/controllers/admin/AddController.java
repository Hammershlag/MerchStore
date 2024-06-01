package com.example.merchstore.controllers.admin;

import com.example.merchstore.dto.Category;
import com.example.merchstore.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 01.06.2024
 */

@Controller
@RequestMapping("/api/admin/add")
public class AddController {

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("/category")
    public String addCategory(Model model) {
        model.addAttribute("category", new Category());
        return "addCategory";
    }

    @PostMapping("/category")
    public String addCategory(Category category) {
        if (categoryRepository.findByName(category.getName()) != null) {
            return "redirect:/api/admin/add/category?error=name";
        }
        categoryRepository.save(category);
        return "redirect:/api/admin/dashboard";
    }

}
