package com.example.merchstore.controllers.functionality;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Locale;

/**
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 27.08.2024
 */
@Controller
public class ConfigController_f {

    @Autowired
    private MessageSource messageSource;

    @GetMapping("/api/config")
    @ResponseBody
    public String getConfigValue(@RequestParam("key") String key) {
        // Get the value from messages.properties
        return messageSource.getMessage(key, null, Locale.getDefault());
    }
}
