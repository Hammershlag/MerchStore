package com.example.merchstore.controllers.general;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * The HelpController_g class handles the web requests related to help pages in the application.
 *
 * It has four methods:
 * <ul>
 *     <li>contact(): Handles the request for the contact help page. It returns the view name for the contact help page.</li>
 *     <li>faq(): Handles the request for the FAQ help page. It returns the view name for the FAQ help page.</li>
 *     <li>returns(): Handles the request for the returns help page. It returns the view name for the returns help page.</li>
 *     <li>customerService(): Handles the request for the customer service help page. It returns the view name for the customer service help page.</li>
 * </ul>
 *
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 12.06.2024
 */

@Controller
@RequestMapping("/help")
public class HelpController_g {

    /**
     * Handles the request for the contact help page. It returns the view name for the contact help page.
     *
     * @return The view name for the contact help page.
     */
    @RequestMapping("/contact")
    public String contact() {
        return "general/help/contact";
    }

    /**
     * Handles the request for the FAQ help page. It returns the view name for the FAQ help page.
     *
     * @return The view name for the FAQ help page.
     */
    @RequestMapping("/faq")
    public String faq() {
        return "general/help/faq";
    }

    /**
     * Handles the request for the returns help page. It returns the view name for the returns help page.
     *
     * @return The view name for the returns help page.
     */
    @RequestMapping("/returns")
    public String returns() {
        return "general/help/returns";
    }

    /**
     * Handles the request for the customer service help page. It returns the view name for the customer service help page.
     *
     * @return The view name for the customer service help page.
     */
    @RequestMapping("customer-service")
    public String customerService() {
        return "general/help/customer-service";
    }


}
