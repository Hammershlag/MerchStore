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
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 01.09.2024
 */

@Service
public class BestSellerService {

    @Autowired
    private BestSellerRepository bestSellerRepository;

    @Autowired
    private ItemRepository itemRepository;

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
