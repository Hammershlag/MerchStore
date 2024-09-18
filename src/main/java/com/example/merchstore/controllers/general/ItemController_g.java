package com.example.merchstore.controllers.general;

import com.example.merchstore.components.models.*;
import com.example.merchstore.repositories.*;
import com.example.merchstore.services.LatestExchangeRateService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * The ItemController_g class handles the web requests related to items in the application.
 *
 * It has two methods:
 * <ul>
 *     <li>viewItems(HttpServletRequest request, Long categoryId, String sortField, String order, int page, int itemsPerPage, String search, String searchItems, Model model): Handles the GET request for viewing items. It retrieves items based on the provided parameters, retrieves the currency from the cookies, retrieves the latest exchange rate for the currency, adds all these attributes to the model, and returns the view name for the items page.</li>
 *     <li>viewItem(HttpServletRequest request, Long id, String addedToCart, Model model): Handles the GET request for viewing a single item. It retrieves the item based on the provided id, retrieves the currency from the cookies, retrieves the latest exchange rate for the currency, retrieves the reviews for the item, checks if the user has submitted a review, calculates the average rating, checks the login status, saves the user item history if the user is logged in, adds all these attributes to the model, and returns the view name for the item page.</li>
 * </ul>
 *
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 03.06.2024
 */

@Controller
@RequestMapping("/item")
public class ItemController_g {

    /**
     * The CategoryRepository that this controller uses to perform CRUD operations on categories.
     * @see CategoryRepository
     */
    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * The ItemRepository that this controller uses to perform CRUD operations on items.
     * @see ItemRepository
     */
    @Autowired
    private ItemRepository itemRepository;

    /**
     * The ReviewRepository that this controller uses to perform CRUD operations on reviews.
     * @see ReviewRepository
     */
    @Autowired
    private ReviewRepository reviewRepository;

    /**
     * The UserItemHistoryRepository that this controller uses to perform CRUD operations on user item history.
     * @see UserItemHistoryRepository
     */
    @Autowired
    private UserItemHistoryRepository userItemHistoryRepository;

    /**
     * The CurrencyRepository that this controller uses to perform CRUD operations on currencies.
     * @see CurrencyRepository
     */
    @Autowired
    private HttpSession session;

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

    @Autowired
    private WishlistItemRepository wishlistItemRepository;

    @Autowired
    private AttributesRepository attributesRepository;

    /**
     * The default number of items per page.
     */
    private static final int DEFAULT_ITEMS_PER_PAGE = 20;

    /**
     * Handles the GET request for viewing items. It retrieves items based on the provided parameters, retrieves the currency from the cookies, retrieves the latest exchange rate for the currency, adds all these attributes to the model, and returns the view name for the items page.
     *
     * @param request The HTTP request.
     * @param categoryId The ID of the category.
     * @param sortField The field to sort by.
     * @param order The order to sort in.
     * @param page The page number.
     * @param itemsPerPage The number of items per page.
     * @param search The search query.
     * @param searchItems The search query for items.
     * @param model The model to be prepared.
     * @return The view name for the items page.
     */
    @GetMapping("/all")
    public String viewItems(HttpServletRequest request, @RequestParam(value = "category", required = false) Long categoryId,
                            @RequestParam(value = "sortField", required = false, defaultValue = "name") String sortField,
                            @RequestParam(value = "order", required = false, defaultValue = "asc") String order,
                            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                            @RequestParam(value = "itemsPerPage", required = false, defaultValue = "" + DEFAULT_ITEMS_PER_PAGE) int itemsPerPage,
                            @RequestParam(value = "search", required = false) String search,
                            @RequestParam(value = "searchItems", required = false) String searchItems,
                            Model model) {
        if (itemsPerPage < 1) {
            itemsPerPage = DEFAULT_ITEMS_PER_PAGE;
        }
        if (search == null) search = searchItems;

        Pageable pageable = PageRequest.of(page - 1, itemsPerPage, Sort.Direction.fromString(order), sortField);

        Page<Item> itemPage;
        if (categoryId != null) {
            Category category = categoryRepository.findById(categoryId).orElse(null);
            if (category != null) {
                itemPage = (search != null && !search.isEmpty()) ?
                        itemRepository.findByCategoryAndNameStartingWithIgnoreCase(category, search, pageable) :
                        itemRepository.findByCategory(category, pageable);
            } else {
                itemPage = Page.empty();
            }
        } else {
            itemPage = (search != null && !search.isEmpty()) ?
                    itemRepository.findByNameStartingWithIgnoreCase(search, pageable) :
                    itemRepository.findAll(pageable);
        }

        List<Item> items = itemPage.getContent();
        int totalPages = itemPage.getTotalPages();

        Currency currency = currencyRepository.findById(1L).orElse(null);
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("currency")) {
                    currency = currencyRepository.findByShortName(cookie.getValue());
                }
            }
        }

        ExchangeRate exchangeRate = latestExchangeRateService.getLatestExchangeRateForCurrency(currency.getId());

        model.addAttribute("currency", currency);
        model.addAttribute("exchangeRate", exchangeRate);


        model.addAttribute("items", items);
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("selectedCategoryId", categoryId);
        model.addAttribute("sortField", sortField);
        model.addAttribute("order", order);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("itemsPerPage", itemsPerPage);
        model.addAttribute("search", search);
        model.addAttribute("searchItems", search);

        return "general/viewItems";
    }

    /**
     * Handles the GET request for viewing a single item. It retrieves the item based on the provided id, retrieves the currency from the cookies, retrieves the latest exchange rate for the currency, retrieves the reviews for the item, checks if the user has submitted a review, calculates the average rating, checks the login status, saves the user item history if the user is logged in, adds all these attributes to the model, and returns the view name for the item page.
     *
     * @param request The HTTP request.
     * @param id The ID of the item.
     * @param addedToCart The message for adding to cart.
     * @param model The model to be prepared.
     * @return The view name for the item page.
     */
    @GetMapping
    public String viewItem(HttpSession session, HttpServletRequest request, @RequestParam Long id, @RequestParam(required = false) String addedToCart, Model model) {
        Item item = itemRepository.findById(id).orElse(null);
        if (item == null) {
            return "redirect:/item/all";
        }



        Currency currency = currencyRepository.findById(1L).orElse(null);
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("currency")) {
                    currency = currencyRepository.findByShortName(cookie.getValue());
                }
            }
        }

        ExchangeRate exchangeRate = latestExchangeRateService.getLatestExchangeRateForCurrency(currency.getId());

        model.addAttribute("currency", currency);
        model.addAttribute("exchangeRate", exchangeRate);

        List<Attribute> attributes = attributesRepository.findAllByItem(item);
        model.addAttribute("attributes", attributes);


        List<Review> reviews = reviewRepository.findAllByItem(item);
        model.addAttribute("item", item);
        model.addAttribute("reviews", reviews);
        model.addAttribute("search", item.getName());
        if (addedToCart != null) {
            model.addAttribute("addedToCart", addedToCart);
        }
        boolean submitted = ((User) session.getAttribute("user")) != null ? reviews.stream()
                .anyMatch(review -> review.getUser().getUserId().equals(((User) session.getAttribute("user")).getUserId())) : false;
        model.addAttribute("submitted", submitted);

        double averageRating = reviews.stream()
                .mapToInt(Review::getStarRating) // Assuming getStarRating() returns an integer
                .average()
                .orElse(0.0); // Use 0.0 if there are no reviews

        Boolean isLoggedIn = (Boolean) session.getAttribute("isLoggedIn");
        if (isLoggedIn == null) {
            isLoggedIn = false;
            model.addAttribute("isWishListed", false);
        }



        if (isLoggedIn) {
            User user = (User) session.getAttribute("user");
            userItemHistoryRepository.save(new UserItemHistory(user, item));

            model.addAttribute("isWishListed", wishlistItemRepository.existsByItemAndUser(item, user));

        }

        model.addAttribute("averageRating", averageRating);


        return "general/viewItem";
    }

    @GetMapping("/search")
    public String searchItems(HttpServletRequest request, @RequestParam String search, Model model) {
        if (search.isEmpty()) {
            return "fragments/itemSearchResults";
        }
        Set<Item> itemsSet = new LinkedHashSet<>(itemRepository.findTop3ByNameStartingWithIgnoreCaseAndStockQuantityGreaterThan(search, 0));
        if (itemsSet.size() < 3) {
            List<Item> itemsContaining = itemRepository.findTop3ByNameContainingIgnoreCaseAndStockQuantityGreaterThan(search, 0);
            for (Item item : itemsContaining) {
                if (!itemsSet.contains(item)) {
                    itemsSet.add(item);
                }
                if (itemsSet.size() == 3) {
                    break;
                }
            }
        }
        List<Item> items = new ArrayList<>(itemsSet);
        model.addAttribute("searchItems", items);

        Currency currency = currencyRepository.findById(1L).orElse(null);
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("currency")) {
                    currency = currencyRepository.findByShortName(cookie.getValue());
                }
            }
        }

        ExchangeRate exchangeRate = latestExchangeRateService.getLatestExchangeRateForCurrency(currency.getId());

        model.addAttribute("currency", currency);
        model.addAttribute("exchangeRate", exchangeRate);

        return "fragments/itemSearchResults";
    }


}
