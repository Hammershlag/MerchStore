package com.example.merchstore.controllers.general;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 13.06.2024
 */

@Controller
@RequestMapping("/info")
public class InfoController_g {

    @RequestMapping("/about")
    public String about() {
        return "general/info/about";
    }

    @RequestMapping("/careers")
    public String careers() {
        return "general/info/careers";
    }

    @RequestMapping("/sustainability")
    public String sustainability() {
        return "general/info/sustainability";
    }

    @RequestMapping("/press")
    public String press() {
        return "general/info/press";
    }

    @RequestMapping("/investors")
    public String investors() {
        return "general/info/investors";
    }

}
