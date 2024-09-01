package com.example.merchstore.controllers.general;

import com.example.merchstore.components.models.Item;
import com.example.merchstore.services.BestSellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

/**
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 01.09.2024
 */

@RestController
public class BestSeller {

    @Autowired
    private BestSellerService bestSellerService;

    @GetMapping("/best-seller")
    public String bestSeller() {
        StringBuilder sb = new StringBuilder();
        HashMap<Item, Integer> bestSellers = bestSellerService.getBestSellers();
        bestSellers.forEach((item, sales) -> {
            sb.append(item.getName()).append(" - ").append(sales).append(" sold; ");
        });
        return sb.toString();
    }

}
