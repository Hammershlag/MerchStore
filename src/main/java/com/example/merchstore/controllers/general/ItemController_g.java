package com.example.merchstore.controllers.general;

import com.example.merchstore.model.Category;
import com.example.merchstore.model.Item;
import com.example.merchstore.repositories.CategoryRepository;
import com.example.merchstore.repositories.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    private static final int DEFAULT_ITEMS_PER_PAGE = 20;

    @GetMapping("/all")
    public String viewItems(@RequestParam(value = "category", required = false) Long categoryId,
                            @RequestParam(value = "sortField", required = false, defaultValue = "name") String sortField,
                            @RequestParam(value = "order", required = false, defaultValue = "asc") String order,
                            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                            @RequestParam(value = "itemsPerPage", required = false, defaultValue = "" + DEFAULT_ITEMS_PER_PAGE) int itemsPerPage,
                            @RequestParam(value = "search", required = false) String search,
                            Model model) {
        if (itemsPerPage < 1) {
            itemsPerPage = DEFAULT_ITEMS_PER_PAGE;
        }

        Pageable pageable = PageRequest.of(page - 1, itemsPerPage, Sort.Direction.fromString(order), sortField);

        Page<Item> itemPage;
        if (categoryId != null) {
            Category category = categoryRepository.findById(categoryId).orElse(null);
            if (category != null) {
                itemPage = (search != null && !search.isEmpty()) ?
                        itemRepository.findByCategoryAndNameStartingWithIgnoreCase(category, search, pageable) :
                        itemRepository.findByCategory(category, pageable);
            } else {
                itemPage = Page.empty();
            }
        } else {
            itemPage = (search != null && !search.isEmpty()) ?
                    itemRepository.findByNameStartingWithIgnoreCase(search, pageable) :
                    itemRepository.findAll(pageable);
        }

        List<Item> items = itemPage.getContent();
        int totalPages = itemPage.getTotalPages();

        model.addAttribute("items", items);
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("selectedCategoryId", categoryId);
        model.addAttribute("sortField", sortField);
        model.addAttribute("order", order);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("itemsPerPage", itemsPerPage);
        model.addAttribute("search", search);
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
