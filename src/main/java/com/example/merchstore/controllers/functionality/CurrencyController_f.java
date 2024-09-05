package com.example.merchstore.controllers.functionality;

import com.example.merchstore.components.models.Currency;
import com.example.merchstore.repositories.CurrencyRepository;
import com.example.merchstore.repositories.ExchangeRateRepository;
import com.example.merchstore.services.LatestExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * The CurrencyController_f class handles the web requests related to currencies in the application.
 *
 * It has two methods:
 * <ul>
 *     <li>getAllCurrencies(): Handles the GET request for retrieving all currencies. It retrieves all currencies from the repository and returns them.</li>
 *     <li>getCurrencyByShortName(String shortName): Handles the GET request for retrieving a currency by its short name. It retrieves the currency from the repository, gets the latest exchange rate for the currency, and returns it.</li>
 * </ul>
 *
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 03.09.2024
 */

@RestController
@RequestMapping("/api/currencies")
public class CurrencyController_f {

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private LatestExchangeRateService latestExchangeRateService;

    @GetMapping
    public List<Currency> getAllCurrencies() {
        return currencyRepository.findAll();
    }

    @GetMapping("/{shortName}")
    public Double getCurrencyByShortName(@PathVariable String shortName) {
        return latestExchangeRateService.getLatestExchangeRateForCurrency(currencyRepository.findByShortName(shortName).getId()).getExchangeRate();
    }

}
