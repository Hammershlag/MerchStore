package com.example.merchstore.controllers.user;

import com.example.merchstore.model.CartItem;
import com.example.merchstore.model.Item;
import com.example.merchstore.model.User;
import com.example.merchstore.repositories.CartItemRepository;
import com.example.merchstore.repositories.ItemRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 04.06.2024
 */

@Controller
@RequestMapping("/user/cart")
public class CartController_u {

    @Autowired
    private HttpSession httpSession;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ItemRepository itemRepository;

    @GetMapping()
    public String showCart(Model model, @RequestParam(value = "sortField", required = false) String sortField,
                           @RequestParam(value = "order", required = false, defaultValue = "asc") String order) {
        User user = (User) httpSession.getAttribute("user");
        List<CartItem> cartItems = cartItemRepository.findAllByUser(user);
        sortField = sortField != null ? sortField : "name";

        boolean isAscending = false;
        if (order != null) {
            isAscending = "asc".equalsIgnoreCase(order);
        }

        String finalSortField = sortField;
        boolean finalIsAscending = isAscending;
        cartItems.sort((item1, item2) -> {
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

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("sortField", sortField);
        model.addAttribute("order", order);
        return "user/cart";
    }

    @PostMapping("/increase")
    public String increaseItemQuantity(@RequestParam("itemId") Long itemId,
                                       @RequestParam(value = "sortField", required = false) String sortField,
                                       @RequestParam(value = "order", required = false, defaultValue = "asc") String order) {
        User user = (User) httpSession.getAttribute("user");
        CartItem cartItem = cartItemRepository.findByUserAndItem(user, itemRepository.findById(itemId).orElseThrow(() -> new IllegalArgumentException("Invalid item id: " + itemId)));
        cartItem.setQuantity(cartItem.getQuantity() + 1);
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

    @PostMapping("/add")
    public String addItemToCart(@RequestParam("itemId") Long itemId, @RequestParam("quantity") int quantity, Model model) {
        User user = (User) httpSession.getAttribute("user");

        Item item = itemRepository.findById(itemId).orElseThrow(() -> new IllegalArgumentException("Invalid item id: " + itemId));

        boolean addedToCart = false;

        if(cartItemRepository.existsByUserAndItem(user, item)) {
            CartItem cartItem = cartItemRepository.findByUserAndItem(user, item);
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartItemRepository.save(cartItem);
            addedToCart = true;
        } else {
            CartItem cartItem = new CartItem(user, item, quantity);
            cartItemRepository.save(cartItem);
            addedToCart = true;
        }

        model.addAttribute("addedToCart", addedToCart);
        return "redirect:/item?id=" + itemId + "&addedToCart=" + addedToCart;
    }

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
