package com.example.merchstore.controllers.admin;

import com.example.merchstore.dto.Category;
import com.example.merchstore.dto.Discount;
import com.example.merchstore.repositories.CategoryRepository;
import com.example.merchstore.repositories.DiscountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;

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

    @Autowired
    private DiscountRepository discountRepository;

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

    @GetMapping("/discount")
    public String addDiscount(Model model) {
        model.addAttribute("discount", new Discount());
        return "addDiscount";
    }

    @PostMapping("/discount")
    public String addDiscount(Discount discount) {
        if (discountRepository.findByCode(discount.getCode()) != null) {
            return "redirect:/api/admin/add/discount?error=code";
        }
        if (discount.getValidFrom().isBefore(LocalDate.now())) {
            return "redirect:/api/admin/add/discount?error=validFrom";
        }
        if (discount.getValidUntil().isBefore(discount.getValidFrom())) {
            return "redirect:/api/admin/add/discount?error=validUntil";
        }
        discountRepository.save(discount);
        return "redirect:/api/admin/dashboard";
    }

}
