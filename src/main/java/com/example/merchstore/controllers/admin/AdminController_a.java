package com.example.merchstore.controllers.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 01.06.2024
 */

@Controller
@RequestMapping("/api/admin")
public class  AdminController_a {

    @RequestMapping("/dashboard")
    public String showDashboard() {
        return "admin/dashboard";
    }

    @RequestMapping
    public String adminRed() {
        return "redirect:/api/admin/dashboard";
    }

}
