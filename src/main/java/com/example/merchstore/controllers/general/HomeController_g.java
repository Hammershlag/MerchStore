package com.example.merchstore.controllers.general;

import com.example.merchstore.components.models.*;
import com.example.merchstore.repositories.CategoryRepository;
import com.example.merchstore.repositories.CurrencyRepository;
import com.example.merchstore.repositories.ItemRepository;
import com.example.merchstore.services.BestSellerService;
import com.example.merchstore.services.LatestExchangeRateService;
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
 * The HomeController_g class handles the web requests related to the home page in the application.
 *
 * It has two methods:
 * <ul>
 *     <li>home(HttpServletRequest request, HttpSession session, Model model): Handles the GET request for the home page. It retrieves the login status, currency, and user history from the session and cookies, retrieves the latest exchange rate for the currency, adds all these attributes to the model, and returns the view name for the home page.</li>
 *     <li>homeRedirect(): Handles the GET request for the root URL. It returns a redirect to the home page.</li>
 * </ul>
 *
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 28.05.2024
 */

@Controller
public class HomeController_g {

    /**
     * The CategoryRepository that this controller uses to perform CRUD operations on categories.
     * @see CategoryRepository
     */
    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * The BestSellerService that this controller uses to retrieve the best sellers.
     * @see BestSellerService
     */
    @Autowired
    private BestSellerService bestSellersService;

    /**
     * The ItemRepository that this controller uses to perform CRUD operations on items.
     * @see ItemRepository
     */
    @Autowired
    private ItemRepository itemRepository;

    /**
     * The UserItemHistoryService that this controller uses to retrieve the newest browsed items for a user.
     * @see UserItemHistoryService
     */
    @Autowired
    private UserItemHistoryService userItemHistoryService;

    /**
     * The CurrencyRepository that this controller uses to perform CRUD operations on currencies.
     * @see CurrencyRepository
     */
    @Autowired
    private CurrencyRepository currencyRepository;

    /**
     * The LatestExchangeRateService that this controller uses to retrieve the latest exchange rate for a currency.
     * @see LatestExchangeRateService
     */
    @Autowired
    private LatestExchangeRateService latestExchangeRateService;

    /**
     * Handles the GET request for the home page. It retrieves the login status, currency, and user history from the session and cookies, retrieves the latest exchange rate for the currency, adds all these attributes to the model, and returns the view name for the home page.
     *
     * @param request The HTTP request.
     * @param session The HTTP session.
     * @param model The model to be prepared.
     * @return The view name for the home page.
     */
    @GetMapping("/home")
    public String home(HttpServletRequest request, HttpSession session, Model model) {
        Boolean isLoggedIn = (Boolean) session.getAttribute("isLoggedIn");
        if (isLoggedIn == null) {
            isLoggedIn = false;
        }

        Currency currency = currencyRepository.findById(1L).orElse(null);


        Cookie[] cookies = request.getCookies();

        List<Item> user_history_items = new ArrayList<>();
        if (isLoggedIn) {

            // Get the user ID from the session
            Long userId = ((User) session.getAttribute("user")).getUserId();

            user_history_items = userItemHistoryService.getNewestBrowsedItemsForUser(userId);

            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("currency")) {
                        currency = currencyRepository.findByShortName(cookie.getValue());
                    }
                }
            }

        } else {
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().startsWith("history_")) {
                        user_history_items.add(itemRepository.findById(Long.parseLong(cookie.getValue())).orElse(itemRepository.findById(523L).orElse(null)));
                    }
                    if (cookie.getName().equals("currency")) {
                        currency = currencyRepository.findByShortName(cookie.getValue());
                    }
                }
            } else {
                Logger.getAnonymousLogger().info("No cookies found");
            }
        }

        ExchangeRate exchangeRate = latestExchangeRateService.getLatestExchangeRateForCurrency(currency.getId());

        model.addAttribute("currency", currency);
        model.addAttribute("exchangeRate", exchangeRate);




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
