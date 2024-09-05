package com.example.merchstore.controllers.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * The AdminController_a class handles the web requests related to the admin section of the application.
 *
 * It has two methods:
 * <ul>
 *     <li>showDashboard(): Handles the GET request for the admin dashboard and returns the view name.</li>
 *     <li>adminRed(): Handles the GET request for the admin base URL and redirects to the admin dashboard.</li>
 * </ul>
 *
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
