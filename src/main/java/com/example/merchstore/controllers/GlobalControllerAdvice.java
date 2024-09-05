package com.example.merchstore.controllers;

import com.example.merchstore.services.GlobalAttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * The GlobalControllerAdvice class is a global controller advice in the application.
 * It provides shared logic that is used across all @Controller classes.
 *
 * It has one method:
 * <ul>
 *     <li>addAttributes(Model model): This method is invoked before any controller method. It adds all global attributes to the model.</li>
 * </ul>
 *
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 11.06.2024
 */

@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private GlobalAttributeService globalAttributeService;

    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAllAttributes(globalAttributeService.getGlobalAttributes());
    }
}
