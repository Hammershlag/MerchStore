package com.example.merchstore.controllers.functionality;

import com.example.merchstore.components.models.Currency;
import com.example.merchstore.repositories.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 03.09.2024
 */

@RestController
@RequestMapping("/api/currencies")
public class CurrencyController_f {

    @Autowired
    private CurrencyRepository currencyRepository;

    @GetMapping
    public List<Currency> getAllCurrencies() {
        return currencyRepository.findAll();
    }

}
