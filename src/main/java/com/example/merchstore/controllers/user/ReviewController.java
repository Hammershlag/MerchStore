package com.example.merchstore.controllers.user;

import com.example.merchstore.components.enums.OrderStatus;
import com.example.merchstore.components.models.*;
import com.example.merchstore.repositories.ItemRepository;
import com.example.merchstore.repositories.OrderItemRepository;
import com.example.merchstore.repositories.OrderRepository;
import com.example.merchstore.repositories.ReviewRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 10.06.2024
 */

@Controller
@RequestMapping("/review")
public class ReviewController {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private HttpSession session;

    @PostMapping("/addReview")
    public String addReview(@RequestParam Long itemId, @RequestParam String description, @RequestParam int star_rating, Model model) {
        Item item = itemRepository.findById(itemId).orElse(null);
        if (item == null) {
            return "redirect:/item/all";
        }

        User user = session.getAttribute("user") instanceof User ? (User) session.getAttribute("user") : null;
        if (user == null) {
            return "redirect:/login";
        }
        if (reviewRepository.existsByItemAndUser(item, user)) {
            return "redirect:/item?id=" + itemId + "&error=reviewExists";
        }
        if (description == null || description.length() < 10) {
            return "redirect:/item?id=" + itemId + "&error=descriptionTooShort";
        }

        // Check if the rating is between 1 and 5
        if (star_rating < 1 || star_rating > 5) {
            return "redirect:/item?id=" + itemId + "&error=invalidRating";
        }

        List<Order> orders = orderRepository.findAllOrdersByUser(user);
        boolean hasOrdered = false;
        for (Order order : orders) {

            if (orderItemRepository.existsByItemAndOrder(item, order) && order.getStatus() != OrderStatus.UNPAID) { //TODO UNPAID for testing purposes
                hasOrdered = true;
                break;
            }

        }
        if(!hasOrdered)
            return "redirect:/item?id=" + itemId + "&error=notOrdered";

        Review review = new Review();
        review.setUser(user);
        review.setItem(item);
        review.setDescription(description);
        review.setStarRating(star_rating);
        review.setCreatedAt(LocalDateTime.now());
        review.setUpdatedAt(LocalDateTime.now());

        reviewRepository.save(review);

        return "redirect:/item?id=" + itemId;
    }

}
