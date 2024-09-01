package com.example.merchstore.controllers.general;

import com.example.merchstore.components.models.Category;
import com.example.merchstore.components.models.User;
import com.example.merchstore.repositories.CategoryRepository;
import com.example.merchstore.services.BestSellerService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 28.05.2024
 */

@Controller
public class HomeController_g {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BestSellerService bestSellersService;

    @GetMapping("/home")
    public String home(HttpSession session, Model model) {
        Boolean isLoggedIn = (Boolean) session.getAttribute("isLoggedIn");
        if (isLoggedIn == null) {
            isLoggedIn = false;
        }
        model.addAttribute("isLoggedIn", isLoggedIn);
        model.addAttribute("categories", categoryRepository.findAll().stream().filter(Category::isMain));
        model.addAttribute("bestsellers", bestSellersService.getBestSellers().keySet().stream().toList());
        return "general/home";
    }

    @GetMapping("/")
    public String homeRedirect() {
        return "redirect:/home";
    }

}
