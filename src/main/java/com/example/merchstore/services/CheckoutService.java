package com.example.merchstore.services;

import com.example.merchstore.components.models.CartItem;
import com.example.merchstore.components.models.User;
import com.example.merchstore.repositories.CartItemRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
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
