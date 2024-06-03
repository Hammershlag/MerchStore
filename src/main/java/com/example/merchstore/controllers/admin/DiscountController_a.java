package com.example.merchstore.controllers.admin;

import com.example.merchstore.model.Discount;
import com.example.merchstore.repositories.DiscountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 03.06.2024
 */

@Controller
@RequestMapping("/api/admin")
public class DiscountController_a {


    @Autowired
    private DiscountRepository discountRepository;

    @GetMapping("/add/discount")
    public String addDiscount(Model model) {
        model.addAttribute("discount", new Discount());
        return "admin/add/addDiscount";
    }

    @PostMapping("/add/discount")
    public String addDiscount(Discount discount) {
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
        return "redirect:/api/admin/dashboard";
    }

    @GetMapping("/view/discounts")
    public String viewDiscounts(@RequestParam(value = "valid", required = false) Boolean valid, Model model) {
        List<Discount> discounts;
        if (Boolean.TRUE.equals(valid)) {
            discounts = discountRepository.findAllValidDiscounts(LocalDate.now());
        } else {
            discounts = discountRepository.findAll();
        }
        model.addAttribute("discounts", discounts);
        return "admin/view/viewDiscounts";
    }

}
