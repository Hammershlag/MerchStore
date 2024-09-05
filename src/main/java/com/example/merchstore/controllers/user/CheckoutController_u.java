package com.example.merchstore.controllers.user;

import com.example.merchstore.Decorators.ItemDecorator;
import com.example.merchstore.components.enums.OrderStatus;
import com.example.merchstore.components.models.*;
import com.example.merchstore.repositories.*;
import com.example.merchstore.services.CheckoutService;
import com.example.merchstore.services.CustomUserDetailsService;
import com.example.merchstore.services.LatestExchangeRateService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 09.06.2024
 */

@Controller
@RequestMapping("/user/cart/checkout")
public class CheckoutController_u {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private DiscountRepository discountRepository;

    @Autowired
    private ItemDiscountRepository itemDiscountRepository;

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private LatestExchangeRateService latestExchangeRateService;

    @SneakyThrows // Handles DocumentException and IOException
    @GetMapping("/file")
    public void getFile(@RequestParam("orderID") Long orderID, HttpSession session, HttpServletResponse response) {
        User currentUser = (User) session.getAttribute("user");
        Order order = orderRepository.findByOrderId(orderID);

        if (currentUser == null || !currentUser.getUserId().equals(order.getUser().getUserId())) {
            response.sendRedirect("/home?message=wrongUser");
            return;
        }

        List<OrderItem> orderItems = orderItemRepository.findAllByOrder(order);

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=OrderDetails.pdf");

        Document document = new Document();
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
        Font boldFont = FontFactory.getFont(FontFactory.COURIER_BOLD, 16, BaseColor.BLACK);

        // Add Title
        document.add(new Paragraph("Order Details for Order ID: " + orderID, boldFont));
        document.add(new Paragraph("\n"));

        // Add User Details
        document.add(new Paragraph("Ordered by: " + currentUser.getUsername(), font));
        document.add(new Paragraph("\n"));

        // Add Order Items
        for (OrderItem item : orderItems) {
            document.add(new Paragraph("Item: " + item.getItem().getName(), font));
            document.add(new Paragraph("Quantity: " + item.getQuantity(), font));
            document.add(new Paragraph("Price: $" + item.getPrice(), font));
            document.add(new Paragraph("\n"));
        }

        // Check for Discounts
        ItemDiscount itemDiscount = itemDiscountRepository.findItemDiscountByDiscount(order.getDiscount());
        if (itemDiscount != null) {
            document.add(new Paragraph("Discount Applied: " + itemDiscount.getDiscount().getDescription(), font));
            document.add(new Paragraph("Discount Percentage: " + itemDiscount.getDiscount().getDiscountPercentage() + "%", font));
        }

        document.close();
    }

    @PostMapping
    public String performCheckout(HttpServletRequest request, HttpSession session, Model model, @RequestParam(value = "discountCode", required = false) String discountCode) {
        User currentUser = (User) session.getAttribute("user");
        List<CartItem> cartItems = cartItemRepository.findAllByUser(currentUser);
        List<String> insufficientStockItems = new ArrayList<>();
        Discount discount = discountRepository.findByCode(discountCode);

        if (discount == null || !discount.isValid()) {
            discount = discountRepository.findByDiscountId(0L);
        }

        for (CartItem cartItem : cartItems) {
            Item item = cartItem.getItem();
            if (cartItem.getQuantity() > item.getStockQuantity()) {
                insufficientStockItems.add(item.getName() + " only " + item.getStockQuantity() + " available");
                cartItem.setQuantity(item.getStockQuantity());
                if (cartItem.getQuantity() == 0) {
                    cartItemRepository.delete(cartItem);
                } else {
                    cartItemRepository.save(cartItem);
                }
            }
        }

        if (!insufficientStockItems.isEmpty()) {
            model.addAttribute("error", "Some items have insufficient stock: " + String.join(", ", insufficientStockItems) + ". We changed the amount in your cart to the maximum available.");

            return "redirect:/user/cart";
        }

        Currency currency = currencyRepository.findById(1L).orElse(null);
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("currency")) {
                    currency = currencyRepository.findByShortName(cookie.getValue());
                }
            }
        }

        ExchangeRate exchangeRate = latestExchangeRateService.getLatestExchangeRateForCurrency(currency.getId());

        Order order = createOrderFromCartItems(cartItems, currentUser, discount, currency, exchangeRate);

        clearCart(cartItems);

        model.addAttribute("order", order);
        return "redirect:/user/cart/checkout?orderID=" + order.getOrderId();
    }

    @GetMapping
    public String showCheckoutPage(HttpSession session, Model model, @RequestParam(value = "orderID", required = false) Long orderID) {
        if (orderID == null) {
            return "redirect:/user/cart";
        }
        User currentUser = (User) session.getAttribute("user");
        Order order = orderRepository.findByOrderId(orderID);


        ItemDiscount itemDiscount = itemDiscountRepository.findItemDiscountByDiscount(order.getDiscount());
        if(itemDiscount != null) {
            model.addAttribute("itemDiscount", itemDiscount);
        }


        if (currentUser.getUserId() != order.getUser().getUserId()) {
            return "redirect:/home?message=wrongUser";
        }
        model.addAttribute("order", order);
        List<OrderItem> orderItems = orderItemRepository.findAllByOrder(order);
        model.addAttribute("orderItems", orderItems);
        return "user/orderConfirmation";
    }

    @GetMapping("/all")
    public String showAllOrders(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("user");
        List<Order> orders = orderRepository.findAllOrdersByUser(currentUser);
        model.addAttribute("orders", orders.stream().sorted(Comparator.comparing(Order::getOrderDate).reversed()).collect(Collectors.toList()));
        return "user/orders";
    }

    private Order createOrderFromCartItems(List<CartItem> cartItems, User user, Discount discount, Currency currency, ExchangeRate exchangeRate) {
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.UNPAID);
        order.setTotalAmount(BigDecimal.ZERO);
        order.setTotalAfterDiscount(BigDecimal.ZERO);
        order.setDiscount(discount);
        order.setCurrency(currency);
        order.setExchangeRate(exchangeRate);

        orderRepository.save(order);

        for(CartItem cartItem : cartItems) {
            Item item = cartItem.getItem();
            item.setStockQuantity(item.getStockQuantity() - cartItem.getQuantity());

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setItem(item);
            orderItem.setQuantity(cartItem.getQuantity());

            BigDecimal itemPrice = item.getPrice();
            orderItem.setPrice(itemPrice);
            order.setTotalAmount(order.getTotalAmount().add(itemPrice.multiply(BigDecimal.valueOf(cartItem.getQuantity()))));

            BigDecimal itemPriceAfterDiscount = itemPrice;
            List<ItemDiscount> itemDiscounts = itemDiscountRepository.findAllByDiscount(discount);
            if (itemDiscounts.isEmpty() && discount != null) {
                itemPriceAfterDiscount = itemPrice.multiply(BigDecimal.valueOf(100).subtract(discount.getDiscountPercentage())).divide(BigDecimal.valueOf(100));
            } else if (discount != null) {
                itemPriceAfterDiscount = itemPrice.multiply(itemDiscounts.stream().anyMatch(itemDiscount -> Objects.equals(itemDiscount.getItem().getItemId(), item.getItemId())) ? BigDecimal.ONE.subtract(itemDiscounts.getFirst().getDiscount().getDiscountPercentage().divide(BigDecimal.valueOf(100))) : BigDecimal.ONE);
            }
            orderItem.setPriceAfterDiscount(itemPriceAfterDiscount);
            order.setTotalAfterDiscount(order.getTotalAfterDiscount().add(itemPriceAfterDiscount.multiply(BigDecimal.valueOf(cartItem.getQuantity()))));

            Sale sale = new Sale();
            sale.setItem(item);
            sale.setQuantity(cartItem.getQuantity());
            sale.setSaleDate(LocalDateTime.now());
            saleRepository.save(sale);

            itemRepository.save(item);
            orderItemRepository.save(orderItem);
        }

        order.setServiceFee(order.getTotalAfterDiscount().multiply(BigDecimal.valueOf(order.getCurrency().getId() != 1 ? 0.1 : 0.01)));

        orderRepository.save(order);
        return order;
    }

    private void clearCart(List<CartItem> cartItems) {
        for (CartItem item : cartItems) {
            cartItemRepository.delete(item);
        }
    }
}