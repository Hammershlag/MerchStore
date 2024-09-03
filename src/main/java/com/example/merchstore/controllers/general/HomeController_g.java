package com.example.merchstore.controllers.general;

import com.example.merchstore.components.models.*;
import com.example.merchstore.repositories.CategoryRepository;
import com.example.merchstore.repositories.ItemRepository;
import com.example.merchstore.services.BestSellerService;
import com.example.merchstore.services.UserItemHistoryService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

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

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserItemHistoryService userItemHistoryService;

    @GetMapping("/home")
    public String home(HttpServletRequest request, HttpSession session, Model model) {
        Boolean isLoggedIn = (Boolean) session.getAttribute("isLoggedIn");
        if (isLoggedIn == null) {
            isLoggedIn = false;
        }

        List<Item> user_history_items = new ArrayList<>();
        if (isLoggedIn) {

            // Get the user ID from the session
            Long userId = ((User) session.getAttribute("user")).getUserId();

            //TODO After deleting history it should not be loaded again
            user_history_items = userItemHistoryService.getNewestBrowsedItemsForUser(userId);

        } else {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().startsWith("history_")) {
                        user_history_items.add(itemRepository.findById(Long.parseLong(cookie.getValue())).orElse(itemRepository.findById(523L).orElse(null)));
                    }
                }
            } else {
                Logger.getAnonymousLogger().info("No cookies found");
            }
        }



        model.addAttribute("isLoggedIn", isLoggedIn);
        model.addAttribute("categories", categoryRepository.findAll().stream().filter(Category::isMain).toList());
        model.addAttribute("bestsellers", bestSellersService.getBestSellers().keySet().stream().toList());
        model.addAttribute("user_history_items", user_history_items);
        return "general/home";
    }

    @GetMapping("/")
    public String homeRedirect() {
        return "redirect:/home";
    }

}
