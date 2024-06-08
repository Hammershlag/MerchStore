package com.example.merchstore.controllers.owner;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 08.06.2024
 */

@Controller
@RequestMapping("/api/owner")
public class OwnerController_o {

    @RequestMapping("/dashboard")
    public String showDashboard() {
        return "owner/dashboard";
    }

    @RequestMapping
    public String ownerRed() {
        return "redirect:/api/owner/dashboard";
    }

}
