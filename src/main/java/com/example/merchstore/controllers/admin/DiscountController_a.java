package com.example.merchstore.controllers.admin;

import com.example.merchstore.Decorators.ItemDecorator;
import com.example.merchstore.components.models.Discount;
import com.example.merchstore.components.models.Item;
import com.example.merchstore.components.models.ItemDiscount;
import com.example.merchstore.repositories.DiscountRepository;
import com.example.merchstore.repositories.ItemDiscountRepository;
import com.example.merchstore.repositories.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * The DiscountController_a class handles the web requests related to discounts in the admin section of the application.
 *
 * It has three methods:
 * <ul>
 *     <li>addDiscount(Model model): Prepares the model for adding a new discount and returns the view name.</li>
 *     <li>addDiscount(Discount discount, Long itemId): Handles the POST request for adding a new discount. It validates the discount data, saves the discount, optionally associates it with an item, and redirects to the admin dashboard.</li>
 *     <li>viewDiscounts(Boolean valid, String search, Model model): Handles the GET request for viewing discounts. It retrieves the discounts based on the search parameter and validity, adds them to the model along with their associated items, then returns the view name.</li>
 * </ul>
 *
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 03.06.2024
 */

@Controller
@RequestMapping("/api/admin")
public class DiscountController_a {


    @Autowired
    private DiscountRepository discountRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemDiscountRepository itemDiscountRepository;

    @GetMapping("/add/discount")
    public String addDiscount(Model model) {
        model.addAttribute("discount", new Discount());
        return "admin/add/addDiscount";
    }

    @PostMapping("/add/discount")
    public String addDiscount(Discount discount, @RequestParam(value = "itemId", required = false) Long itemId) {
        if (discountRepository.findByCode(discount.getCode()) != null) {
            return "redirect:/api/admin/add/discount?error=code";
        }
        if (discount.getValidFrom().isBefore(LocalDate.now())) {
            return "redirect:/api/admin/add/discount?error=validFrom";
        }
        if (discount.getValidUntil().isBefore(discount.getValidFrom())) {
            return "redirect:/api/admin/add/discount?error=validUntil";
        }
        discountRepository.save(discount);

        Item item = itemRepository.findById(itemId).orElse(null);
        if (item != null) {
            ItemDiscount itemDiscount = new ItemDiscount();
            itemDiscount.setDiscount(discount);
            itemDiscount.setItem(item);
            itemDiscountRepository.save(itemDiscount);
        }




        return "redirect:/api/admin/dashboard";
    }

    @GetMapping("/view/discounts")
    public String viewDiscounts(@RequestParam(value = "valid", required = false) Boolean valid,
                                @RequestParam(value = "searchDis", required = false) String search,
                                Model model) {
        List<Discount> discounts;
        if (search != null && !search.isEmpty()) {
            discounts = discountRepository.findByCodeStartingWithIgnoreCase(search);
        } else if (Boolean.TRUE.equals(valid)) {
            discounts = discountRepository.findAllValidDiscounts(LocalDate.now());
        } else {
            discounts = discountRepository.findAll();
        }

        List<ItemDiscount> itemDiscounts = itemDiscountRepository.findAll();
        Map<Discount, Item> discountItemMap = new HashMap<>();

        for (Discount discount : discounts) {
            ItemDiscount itemDiscount = itemDiscounts.stream()
                    .filter(id -> id.getDiscount().getDiscountId().equals(discount.getDiscountId()))
                    .findFirst()
                    .orElse(null);

            if (itemDiscount != null) {
                discountItemMap.put(discount, itemDiscount.getItem());
            } else {
                discountItemMap.put(discount, ItemDecorator.create());
            }
        }

        model.addAttribute("discounts", discounts);
        model.addAttribute("discountItemMap", discountItemMap);
        model.addAttribute("searchDis", search);
        return "admin/view/viewDiscounts";
    }

}
