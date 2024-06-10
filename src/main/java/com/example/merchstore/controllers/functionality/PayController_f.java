package com.example.merchstore.controllers.functionality;

import com.example.merchstore.components.enums.OrderStatus;
import com.example.merchstore.components.models.Order;
import com.example.merchstore.components.models.User;
import com.example.merchstore.repositories.OrderRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Objects;

/**
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 09.06.2024
 */

@Controller
@RequestMapping("/api/pay/")
public class PayController_f {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    HttpSession session;

    @GetMapping("order")
    public String order(@RequestParam(value = "id", required = true) Long id, Model model) {
        Order order = orderRepository.findByOrderId(id);
        if (order == null) {
            return "redirect:/home?message=orderNotFound";
        }
        if(!Objects.equals(order.getUser().getUserId(), ((User) session.getAttribute("user")).getUserId())) {
            return "redirect:/home?message=wrongUser";
        }
        if (order.getStatus() != OrderStatus.UNPAID) {
            return "redirect:/home?message=orderAlreadyPaid";
        }
        model.addAttribute("id", id);
        return "orderPay";
    }

    @PostMapping("order")
    public String orderPost(@RequestParam(value = "id", required = true) Long id) {
        Order order = orderRepository.findByOrderId(id);
        order.setStatus(OrderStatus.PENDING);
        orderRepository.save(order);
        return "redirect:/home?message=paymentSuccessful";
    }

}