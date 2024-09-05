package com.example.merchstore.services;

import com.example.merchstore.components.models.Item;
import com.example.merchstore.repositories.BestSellerRepository;
import com.example.merchstore.repositories.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The BestSellerService class is a service component in the Spring framework.
 * It provides a method to get the best-selling items.
 *
 * It has one main method:
 * <ul>
 *     <li>getBestSellers(): This method retrieves all the best sellers from the BestSellerRepository, finds the corresponding Item from the ItemRepository, and adds them to a HashMap with the total sales as the value. The HashMap is then returned.</li>
 * </ul>
 *
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 01.09.2024
 */

@Service
public class BestSellerService {

    /**
     * The BestSellerRepository dependency is injected by Spring.
     * @see BestSellerRepository
     */
    @Autowired
    private BestSellerRepository bestSellerRepository;

    /**
     * The ItemRepository dependency is injected by Spring.
     * @see ItemRepository
     */
    @Autowired
    private ItemRepository itemRepository;

    /**
     * This method retrieves all the bestsellers from the BestSellerRepository, finds the corresponding Item from the ItemRepository, and adds them to a HashMap with the total sales as the value. The HashMap is then returned.
     *
     * @return A HashMap containing the best-selling items and their total sales.
     */
    public HashMap<Item, Integer> getBestSellers() {
        HashMap<Item, Integer> bestSellers = new HashMap<>();
        bestSellerRepository.findAll().forEach(bestSeller -> {
            Item item = itemRepository.findById(bestSeller.getItemId()).orElse(null);
            if (item != null) {
                bestSellers.put(item, (int) bestSeller.getTotalSales());
            }
        });
        return bestSellers;
    }

}
