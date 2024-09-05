package com.example.merchstore.controllers.owner;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * The OwnerController_o class handles the web requests related to the owner in the application.
 *
 * It has two methods:
 * <ul>
 *     <li>showDashboard(): Handles the GET request for the owner's dashboard. It returns the view name for the owner's dashboard.</li>
 *     <li>ownerRed(): Handles the GET request for the owner's base URL. It returns a redirect to the owner's dashboard.</li>
 * </ul>
 *
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 08.06.2024
 */
@Controller
@RequestMapping("/api/owner")
public class OwnerController_o {

    /**
     * Handles the GET request for the owner's dashboard. It returns the view name for the owner's dashboard.
     *
     * @return The view name for the owner's dashboard.
     */
    @RequestMapping("/dashboard")
    public String showDashboard() {
        return "owner/dashboard";
    }

    /**
     * Handles the GET request for the owner's base URL. It returns a redirect to the owner's dashboard.
     *
     * @return A redirect to the owner's dashboard.
     */
    @RequestMapping
    public String ownerRed() {
        return "redirect:/api/owner/dashboard";
    }

}
