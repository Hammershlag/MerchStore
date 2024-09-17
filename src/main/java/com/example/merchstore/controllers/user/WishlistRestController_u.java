package com.example.merchstore.controllers.user;

import com.example.merchstore.components.models.Item;
import com.example.merchstore.components.models.User;
import com.example.merchstore.components.models.WishlistItem;
import com.example.merchstore.repositories.ItemRepository;
import com.example.merchstore.repositories.WishlistItemRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 17.09.2024
 */

@RestController
@RequestMapping("/user/wishlist")
public class WishlistRestController_u {

    @Autowired
    private WishlistItemRepository wishlistItemRepository;

    @Autowired
    private ItemRepository itemRepository;

    @PostMapping("/change")
    public boolean addToWishlist(@RequestParam(name = "itemId") Long itemId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        Item item = itemRepository.findById(itemId).orElse(null);

        if (item == null || user == null) {
            return false;
        }

        if (wishlistItemRepository.existsByItemAndUser(item, user)) {
            wishlistItemRepository.deleteByItemAndUser(item, user);
            return false;
        } else {
            WishlistItem wishlistItem = new WishlistItem();
            wishlistItem.setItem(item);
            wishlistItem.setUser(user);
            wishlistItemRepository.save(wishlistItem);
            return true;
        }
    }

    @GetMapping("/get")
    public List<WishlistItem> getWishlist(HttpSession session) {
        User user = (User) session.getAttribute("user");
        return wishlistItemRepository.findByUser(user);
    }


}
