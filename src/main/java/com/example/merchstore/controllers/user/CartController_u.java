package com.example.merchstore.controllers.user;

import com.example.merchstore.LocaleConfig;
import com.example.merchstore.components.enums.Language;
import com.example.merchstore.components.models.*;
import com.example.merchstore.repositories.CartItemRepository;
import com.example.merchstore.repositories.CurrencyRepository;
import com.example.merchstore.repositories.ItemRepository;
import com.example.merchstore.repositories.PreTranslatedTextRepository;
import com.example.merchstore.services.GlobalAttributeService;
import com.example.merchstore.services.LatestExchangeRateService;
import com.example.merchstore.services.TranslationService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * The CartController_u class handles the web requests related to the user's cart in the application.
 *
 * It has five methods:
 * <ul>
 *     <li>showCart(HttpServletRequest request, Model model, String sortField, String order): Handles the GET request for viewing the cart. It retrieves the cart items, sorts them based on the provided parameters, retrieves the currency from the cookies, retrieves the latest exchange rate for the currency, adds all these attributes to the model, and returns the view name for the cart page.</li>
 *     <li>increaseItemQuantity(Long itemId, String sortField, String order): Handles the POST request for increasing the quantity of an item in the cart. It retrieves the cart item, increases its quantity if it's less than the stock quantity, saves the cart item, and returns a redirect to the cart page.</li>
 *     <li>addItemToCart(Long itemId, int quantity, Model model): Handles the POST request for adding an item to the cart. It retrieves the item, creates a new cart item or updates the existing one, checks the stock quantity, saves the cart item, adds the addedToCart attribute to the model, and returns a redirect to the item page.</li>
 *     <li>decreaseItemQuantity(Long itemId, String sortField, String order): Handles the POST request for decreasing the quantity of an item in the cart. It retrieves the cart item, decreases its quantity if it's more than 1 or deletes it if it's 1, saves the cart item or deletes it, and returns a redirect to the cart page.</li>
 *     <li>removeItemFromCart(Long itemId, String sortField, String order): Handles the POST request for removing an item from the cart. It retrieves the cart item, deletes it, and returns a redirect to the cart page.</li>
 * </ul>
 *
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 04.06.2024
 */

@Controller
@RequestMapping("/user/cart")
public class CartController_u {

    /**
     * The HttpSession that this controller uses to retrieve the user.
     */
    @Autowired
    private HttpSession httpSession;

    /**
     * The CartItemRepository that this controller uses to perform CRUD operations on cart items.
     * @see CartItemRepository
     */
    @Autowired
    private CartItemRepository cartItemRepository;

    /**
     * The ItemRepository that this controller uses to perform CRUD operations on items.
     * @see ItemRepository
     */
    @Autowired
    private ItemRepository itemRepository;

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
    private TranslationService translationService;

    @Autowired
    private PreTranslatedTextRepository preTranslatedTextRepository;

    @Autowired
    private GlobalAttributeService globalAttributeService;

    @Autowired
    private LocaleConfig localeConfig;

    /**
     * Handles the GET request for viewing the cart. It retrieves the cart items, sorts them based on the provided parameters, retrieves the currency from the cookies, retrieves the latest exchange rate for the currency, adds all these attributes to the model, and returns the view name for the cart page.
     *
     * @param request The HttpServletRequest to retrieve the cookies from.
     * @param model The model to be prepared.
     * @param sortField The field to sort the cart items by.
     * @param order The order to sort the cart items in.
     * @return The view name for the cart page.
     */
    @GetMapping()
    public String showCart(HttpServletRequest request, Model model, @RequestParam(value = "sortField", required = false) String sortField,
                           @RequestParam(value = "order", required = false, defaultValue = "asc") String order,
                           @RequestParam(required = false) String lang) {
        User user = (User) httpSession.getAttribute("user");

        Language language = localeConfig.getCurrentLanguage();


        List<CartItem> cartItems = cartItemRepository.findAllByUser(user);

        List<CartItem> translatedCartItems = new ArrayList<>();

        for (CartItem cartItem : cartItems) {
            Item item = cartItem.getItem();
            Item translatedItem = (Item) translationService.translate(item, language);
            CartItem translatedCartItem = new CartItem(cartItem.getUser(),translatedItem, cartItem.getQuantity());
            translatedCartItems.add(translatedCartItem);
        }



        sortField = sortField != null ? sortField : "name";

        boolean isAscending = false;
        if (order != null) {
            isAscending = "asc".equalsIgnoreCase(order);
        }

        String finalSortField = sortField;
        boolean finalIsAscending = isAscending;
        translatedCartItems.sort((item1, item2) -> {
            int result;
            switch (finalSortField) {
                case "name":
                    result = item1.getItem().getName().compareToIgnoreCase(item2.getItem().getName());
                    break;
                case "price":
                    result = item1.getItem().getPrice().compareTo(item2.getItem().getPrice());
                    break;
                case "quantity":
                    result = Integer.compare(item1.getQuantity(), item2.getQuantity());
                    break;
                case "totalPrice":
                    BigDecimal totalPrice1 = item1.getItem().getPrice().multiply(BigDecimal.valueOf(item1.getQuantity()));
                    BigDecimal totalPrice2 = item2.getItem().getPrice().multiply(BigDecimal.valueOf(item2.getQuantity()));
                    result = totalPrice1.compareTo(totalPrice2);
                    break;
                default:
                    result = 0;
            }
            return finalIsAscending ? result : -result;
        });

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
        model.addAttribute("cartItems", translatedCartItems);
        model.addAttribute("sortField", sortField);
        model.addAttribute("order", order);
        return "user/cart";
    }

    /**
     * Handles the POST request for increasing the quantity of an item in the cart. It retrieves the cart item, increases its quantity if it's less than the stock quantity, saves the cart item, and returns a redirect to the cart page.
     *
     * @param itemId The ID of the item to increase the quantity of.
     * @param sortField The field to sort the cart items by.
     * @param order The order to sort the cart items in.
     * @return The redirect to the cart page.
     */
    @PostMapping("/increase")
    public String increaseItemQuantity(@RequestParam("itemId") Long itemId,
                                       @RequestParam(value = "sortField", required = false) String sortField,
                                       @RequestParam(value = "order", required = false, defaultValue = "asc") String order) {
        User user = (User) httpSession.getAttribute("user");
        CartItem cartItem = cartItemRepository.findByUserAndItem(user, itemRepository.findById(itemId).orElseThrow(() -> new IllegalArgumentException("Invalid item id: " + itemId)));
        if (cartItem.getQuantity() < cartItem.getItem().getStockQuantity()) {
            cartItem.setQuantity(cartItem.getQuantity() + 1);
        }
        cartItemRepository.save(cartItem);
        if (sortField != null && order != null) {
            return "redirect:/user/cart?sortField=" + sortField + "&order=" + order;
        } else if (sortField != null) {
            return "redirect:/user/cart?sortField=" + sortField;
        } else if (order != null) {
            return "redirect:/user/cart?order=" + order;
        }
        return "redirect:/user/cart";
    }

    /**
     * Handles the POST request for adding an item to the cart. It retrieves the item, creates a new cart item or updates the existing one, checks the stock quantity, saves the cart item, adds the addedToCart attribute to the model, and returns a redirect to the item page.
     *
     * @param itemId The ID of the item to add to the cart.
     * @param quantity The quantity of the item to add to the cart.
     * @param model The model to be prepared.
     * @return The redirect to the item page.
     */
    @PostMapping("/add")
    public String addItemToCart(@RequestParam("itemId") Long itemId, @RequestParam("quantity") int quantity, Model model) {
        User user = (User) httpSession.getAttribute("user");

        Item item = itemRepository.findById(itemId).orElseThrow(() -> new IllegalArgumentException("Invalid item id: " + itemId));

        CartItem cartItem = new CartItem();
        String addedToCart = "Failed to add item to cart.";



        if(cartItemRepository.existsByUserAndItem(user, item)) {
            cartItem = cartItemRepository.findByUserAndItem(user, item);
            cartItem.setQuantity(cartItem.getQuantity() + quantity);

        } else {
            cartItem = new CartItem(user, item, quantity);
        }

        if (cartItem.getQuantity() > item.getStockQuantity()) {
            addedToCart = "Not enough stock available. Maximum quantity available: " + item.getStockQuantity();
            model.addAttribute("addedToCart", addedToCart);
            return "redirect:/item?id=" + itemId + "&addedToCart=" + addedToCart;
        }

        cartItemRepository.save(cartItem);
        addedToCart = "Item added to cart.";
        model.addAttribute("addedToCart", addedToCart);
        return "redirect:/item?id=" + itemId + "&addedToCart=" + addedToCart;
    }

    /**
     * Handles the POST request for decreasing the quantity of an item in the cart. It retrieves the cart item, decreases its quantity if it's more than 1 or deletes it if it's 1, saves the cart item or deletes it, and returns a redirect to the cart page.
     *
     * @param itemId The ID of the item to decrease the quantity of.
     * @param sortField The field to sort the cart items by.
     * @param order The order to sort the cart items in.
     * @return The redirect to the cart page.
     */
    @PostMapping("/decrease")
    public String decreaseItemQuantity(@RequestParam("itemId") Long itemId,
                                       @RequestParam(value = "sortField", required = false) String sortField,
                                       @RequestParam(value = "order", required = false, defaultValue = "asc") String order) {
        User user = (User) httpSession.getAttribute("user");
        CartItem cartItem = cartItemRepository.findByUserAndItem(user, itemRepository.findById(itemId).orElseThrow(() -> new IllegalArgumentException("Invalid item id: " + itemId)));

        if (cartItem.getQuantity() > 1) {
            cartItem.setQuantity(cartItem.getQuantity() - 1);
            cartItemRepository.save(cartItem);
        } else {
            cartItemRepository.delete(cartItem);
        }

        if (sortField != null && order != null) {
            return "redirect:/user/cart?sortField=" + sortField + "&order=" + order;
        } else if (sortField != null) {
            return "redirect:/user/cart?sortField=" + sortField;
        } else if (order != null) {
            return "redirect:/user/cart?order=" + order;
        }
        return "redirect:/user/cart";
    }

    /**
     * Handles the POST request for removing an item from the cart. It retrieves the cart item, deletes it, and returns a redirect to the cart page.
     *
     * @param itemId The ID of the item to remove from the cart.
     * @param sortField The field to sort the cart items by.
     * @param order The order to sort the cart items in.
     * @return The redirect to the cart page.
     */
    @PostMapping("/remove")
    public String removeItemFromCart(@RequestParam("itemId") Long itemId,
                                     @RequestParam(value = "sortField", required = false) String sortField,
                                     @RequestParam(value = "order", required = false, defaultValue = "asc") String order) {
        User user = (User) httpSession.getAttribute("user");
        CartItem cartItem = cartItemRepository.findByUserAndItem(user, itemRepository.findById(itemId).orElseThrow(() -> new IllegalArgumentException("Invalid item id: " + itemId)));
        cartItemRepository.delete(cartItem);
        if (sortField != null && order != null) {
            return "redirect:/user/cart?sortField=" + sortField + "&order=" + order;
        } else if (sortField != null) {
            return "redirect:/user/cart?sortField=" + sortField;
        } else if (order != null) {
            return "redirect:/user/cart?order=" + order;
        }
        return "redirect:/user/cart";
    }


}
