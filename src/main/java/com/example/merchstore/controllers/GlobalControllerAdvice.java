package com.example.merchstore.controllers;

import com.example.merchstore.services.GlobalAttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
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
