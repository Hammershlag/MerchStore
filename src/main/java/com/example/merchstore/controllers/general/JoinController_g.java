package com.example.merchstore.controllers.general;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * The JoinController_g class handles the web requests related to joining in the application.
 *
 * It has two methods:
 * <ul>
 *     <li>loyalty(): Handles the GET request for the loyalty join page. It returns the view name for the loyalty join page.</li>
 *     <li>newsletter(): Handles the GET request for the newsletter join page. It returns the view name for the newsletter join page.</li>
 * </ul>
 *
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 13.06.2024
 */

@Controller
@RequestMapping("/join")
public class JoinController_g {

    @GetMapping("/loyalty")
    public String loyalty() {
        return "general/join/loyalty";
    }

    @GetMapping("/newsletter")
    public String newsletter() {
        return "general/join/newsletter";
    }

}
