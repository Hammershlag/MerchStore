package com.example.merchstore.services;

import com.example.merchstore.components.models.CartItem;
import com.example.merchstore.components.models.User;
import com.example.merchstore.repositories.CartItemRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The CheckoutService class is a service component in the Spring framework.
 * It provides a method to get all cart items for a specific user.
 *
 * It has one main method:
 * <ul>
 *     <li>getUserCartItems(User user): This method retrieves all the cart items for the provided user from the CartItemRepository. The operation is transactional, meaning it is part of a single unit of work.</li>
 * </ul>
 *
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 09.06.2024
 */

@Service
public class CheckoutService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Transactional
    public List<CartItem> getUserCartItems(User user) {
        return cartItemRepository.findAllByUser(user);
    }
}
