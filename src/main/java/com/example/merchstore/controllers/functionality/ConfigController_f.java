package com.example.merchstore.controllers.functionality;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Locale;

/**
 * The ConfigController_f class handles the web requests related to configuration in the application.
 *
 * It has one method:
 * <ul>
 *     <li>getConfigValue(String key): Handles the GET request for retrieving a configuration value. It retrieves the value associated with the provided key from the messages.properties file and returns it.</li>
 * </ul>
 *
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
