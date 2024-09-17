package com.example.merchstore.controllers.user;

import com.example.merchstore.components.models.*;
import com.example.merchstore.repositories.CurrencyRepository;
import com.example.merchstore.repositories.ItemRepository;
import com.example.merchstore.repositories.WishlistItemRepository;
import com.example.merchstore.services.LatestExchangeRateService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 17.09.2024
 */

@Controller
@RequestMapping("/user/wishlist")
public class WishlistController_u {

    @Autowired
    private WishlistItemRepository wishlistItemRepository;

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private LatestExchangeRateService latestExchangeRateService;

    @GetMapping("/all")
    public String getAllWishlist(HttpServletRequest request, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        List<WishlistItem> wishlistItems = wishlistItemRepository.findByUser(user);
        model.addAttribute("wishlistItems", wishlistItems);

        Currency currency = currencyRepository.findById(1L).orElse(null);
        
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("currency")) {
                    currency = currencyRepository.findByShortName(cookie.getValue());
                }
            }
        } else {
            Logger.getAnonymousLogger().info("No cookies found");
        }


        ExchangeRate exchangeRate = latestExchangeRateService.getLatestExchangeRateForCurrency(currency.getId());

        model.addAttribute("currency", currency);
        model.addAttribute("exchangeRate", exchangeRate);
        return "/user/wishlist";
    }

}
