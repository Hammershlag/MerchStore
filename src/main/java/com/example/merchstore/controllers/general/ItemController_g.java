package com.example.merchstore.controllers.general;

import com.example.merchstore.model.Category;
import com.example.merchstore.model.Item;
import com.example.merchstore.repositories.CategoryRepository;
import com.example.merchstore.repositories.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 03.06.2024
 */

@Controller
@RequestMapping("/item")
public class ItemController_g {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ItemRepository itemRepository;

    @GetMapping("/all")
    public String viewItems(@RequestParam(value = "category", required = false) Long categoryId,
                            @RequestParam(value = "sortField", required = false) String sortField,
                            @RequestParam(value = "order", required = false, defaultValue = "asc") String order,
                            Model model) {
        List<Item> items;
        if (categoryId != null) {
            Category category = categoryRepository.findById(categoryId).orElse(null);
            items = category != null ? itemRepository.findByCategory(category) : List.of();
        } else {
            items = itemRepository.findAll();
        }

        sortField = sortField != null ? sortField : "name";
        boolean isAscending = "asc".equalsIgnoreCase(order);

        String finalSortField = sortField;
        boolean finalIsAscending = isAscending;
        items.sort((item1, item2) -> {
            int result;
            switch (finalSortField) {
                case "name":
                    result = item1.getName().compareToIgnoreCase(item2.getName());
                    break;
                case "price":
                    result = item1.getPrice().compareTo(item2.getPrice());
                    break;
                case "stockQuantity":
                    result = Integer.compare(item1.getStockQuantity(), item2.getStockQuantity());
                    break;
                case "category":
                    result = item1.getCategory().getName().compareToIgnoreCase(item2.getCategory().getName());
                    break;
                default:
                    result = 0;
            }
            return finalIsAscending ? result : -result;
        });

        model.addAttribute("items", items);
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("selectedCategoryId", categoryId);
        model.addAttribute("sortField", sortField);
        model.addAttribute("order", order);
        return "general/viewItems";
    }



    @GetMapping
    public String viewItem(@RequestParam Long id, @RequestParam(required = false) String addedToCart, Model model) {
        Item item = itemRepository.findById(id).orElse(null);
        if (item == null) {
            return "redirect:/item/all";
        }
        model.addAttribute("item", item);
        if (addedToCart != null) {
            model.addAttribute("addedToCart", addedToCart);
        }
        return "general/viewItem";
    }


}
