package com.example.merchstore.controllers.general;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 12.06.2024
 */

@Controller
@RequestMapping("/help")
public class HelpController_g {

    @RequestMapping("/contact")
    public String contact() {
        return "general/help/contact";
    }

    @RequestMapping("/faq")
    public String faq() {
        return "general/help/faq";
    }

    @RequestMapping("/returns")
    public String returns() {
        return "general/help/returns";
    }

    @RequestMapping("customer-service")
    public String customerService() {
        return "general/help/customer-service";
    }


}
