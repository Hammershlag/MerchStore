package com.example.merchstore.controllers.general;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * The InfoController_g class handles the web requests related to information pages in the application.
 *
 * It has five methods:
 * <ul>
 *     <li>about(): Handles the request for the about information page. It returns the view name for the about information page.</li>
 *     <li>careers(): Handles the request for the careers information page. It returns the view name for the careers information page.</li>
 *     <li>sustainability(): Handles the request for the sustainability information page. It returns the view name for the sustainability information page.</li>
 *     <li>press(): Handles the request for the press information page. It returns the view name for the press information page.</li>
 *     <li>investors(): Handles the request for the investors information page. It returns the view name for the investors information page.</li>
 * </ul>
 *
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 13.06.2024
 */

@Controller
@RequestMapping("/info")
public class InfoController_g {

    /**
     * Handles the request for the about information page. It returns the view name for the about information page.
     *
     * @return The view name for the about information page.
     */
    @RequestMapping("/about")
    public String about() {
        return "general/info/about";
    }

    /**
     * Handles the request for the careers information page. It returns the view name for the careers information page.
     *
     * @return The view name for the careers information page.
     */
    @RequestMapping("/careers")
    public String careers() {
        return "general/info/careers";
    }

    /**
     * Handles the request for the sustainability information page. It returns the view name for the sustainability information page.
     *
     * @return The view name for the sustainability information page.
     */
    @RequestMapping("/sustainability")
    public String sustainability() {
        return "general/info/sustainability";
    }

    /**
     * Handles the request for the press information page. It returns the view name for the press information page.
     *
     * @return The view name for the press information page.
     */
    @RequestMapping("/press")
    public String press() {
        return "general/info/press";
    }

    /**
     * Handles the request for the investors information page. It returns the view name for the investors information page.
     *
     * @return The view name for the investors information page.
     */
    @RequestMapping("/investors")
    public String investors() {
        return "general/info/investors";
    }

}
