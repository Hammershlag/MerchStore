package com.example.merchstore.controllers.admin;

import com.example.merchstore.model.Category;
import com.example.merchstore.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 03.06.2024
 */

@Controller
@RequestMapping("/api/admin")
public class CategoryController_a {


    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("/add/category")
    public String addCategory(Model model) {
        model.addAttribute("category", new Category());
        return "admin/add/addCategory";
    }

    @PostMapping("/add/category")
    public String addCategory(Category category) {
        if (categoryRepository.findByName(category.getName()) != null) {
            return "redirect:/api/admin/add/category?error=name";
        }
        categoryRepository.save(category);
        return "redirect:/api/admin/dashboard";
    }

    @GetMapping("/view/categories")
    public String getAllCategories(Model model) {
        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);
        return "admin/view/viewCategories";
    }


}
