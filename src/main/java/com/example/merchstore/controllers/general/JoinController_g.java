package com.example.merchstore.controllers.general;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
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
