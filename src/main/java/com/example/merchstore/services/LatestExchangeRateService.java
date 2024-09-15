package com.example.merchstore.services;

import com.example.merchstore.components.models.ExchangeRate;
import com.example.merchstore.components.models.LatestExchangeRate;
import com.example.merchstore.repositories.CurrencyRepository;
import com.example.merchstore.repositories.ExchangeRateRepository;
import com.example.merchstore.repositories.LatestExchangeRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The LatestExchangeRateService class is a service component in the Spring framework.
 * It provides methods to manage and update exchange rates.
 *
 * It has several methods:
 * <ul>
 *     <li>updateExchangeRates(): This method retrieves the latest exchange rates from an external API and updates the local repository with these rates.</li>
 *     <li>getLatestExchangeRateForCurrency(Long currencyId): This method retrieves the latest exchange rate for a specific currency. If the latest exchange rate is older than 24 hours, it updates the exchange rates before returning the rate.</li>
 *     <li>getAllLatestExchangeRates(): This method retrieves all the latest exchange rates. If any of the rates are older than 24 hours, it updates the exchange rates before returning them.</li>
 * </ul>
 *
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 04.09.2024
 */

@Service
public class LatestExchangeRateService {

    /**
     * The LatestExchangeRateRepository dependency is injected by Spring.
     * @see LatestExchangeRateRepository
     */
    @Autowired
    private LatestExchangeRateRepository latestExchangeRateRepository;

    /**
     * The CurrencyRepository dependency is injected by Spring.
     * @see CurrencyRepository
     */
    @Autowired
    private CurrencyRepository currencyRepository;

    /**
     * The ExchangeRateRepository dependency is injected by Spring.
     * @see ExchangeRateRepository
     */
    @Autowired
    private ExchangeRateRepository exchangeRateRepository;

    /**
     * The RestTemplate dependency is injected by Spring.
     * @see RestTemplate
     */
    private RestTemplate restTemplate = new RestTemplate();

    /**
     * This method retrieves the latest exchange rates from an external API and updates the local repository with these rates.
     */
    public void updateExchangeRates() {
        String url = "https://api.exchangerate-api.com/v4/latest/PLN";
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        Map<String, Double> rates = (Map<String, Double>) response.get("rates");

        currencyRepository.findAll().forEach(currency -> {
            Double rate = null;

            Object obj = rates.get(currency.getShortName());

            if (obj instanceof Integer) {
                rate = ((Integer) obj).doubleValue();
            } else if (obj instanceof Double) {
                rate = (Double) obj;
            }

            if (rate != null) {
                ExchangeRate exchangeRate = new ExchangeRate();
                exchangeRate.setCurrencyId(currency);
                exchangeRate.setExchangeRate(rate);
                exchangeRate.setLastUpdated(LocalDateTime.now());
                exchangeRateRepository.save(exchangeRate);
            }
        });
    }

    /**
     * This method retrieves the latest exchange rate for a specific currency. If the latest exchange rate is older than 24 hours, it updates the exchange rates before returning the rate.
     *
     * @param currencyId The ID of the currency to retrieve the latest exchange rate for.
     * @return The latest exchange rate for the specified currency.
     */
    public ExchangeRate getLatestExchangeRateForCurrency(Long currencyId) {
        LatestExchangeRate latestExchangeRate = latestExchangeRateRepository.findFirstByCurrencyIdOrderByLastUpdatedDesc(currencyId);
        if (latestExchangeRate == null || Duration.between(latestExchangeRate.getLastUpdated(), LocalDateTime.now()).toHours() > 6) {
            updateExchangeRates();
            latestExchangeRate = latestExchangeRateRepository.findFirstByCurrencyIdOrderByLastUpdatedDesc(currencyId);
        }
        return new ExchangeRate(latestExchangeRate.getId(), currencyRepository.findById(latestExchangeRate.getCurrencyId()).orElse(null), latestExchangeRate.getExchangeRate(), latestExchangeRate.getLastUpdated());
    }

    /**
     * This method retrieves all the latest exchange rates. If any of the rates are older than 24 hours, it updates the exchange rates before returning them.
     *
     * @return A list of all the latest exchange rates.
     */
    public List<ExchangeRate> getAllLatestExchangeRates() {
        List<ExchangeRate> exchangeRates = new ArrayList<>();
        List<LatestExchangeRate> latestExchangeRates = latestExchangeRateRepository.findAll();

        boolean shouldUpdate = false;

        if (latestExchangeRates.isEmpty() || latestExchangeRates.size() == 0) {
            shouldUpdate = true;
        }

        for (LatestExchangeRate latestExchangeRate : latestExchangeRates) {
            if (latestExchangeRate == null ||  Duration.between(latestExchangeRate.getLastUpdated(), LocalDateTime.now()).toHours() > 24) {
                shouldUpdate = true;
            }
            ExchangeRate exchangeRate = new ExchangeRate();
            exchangeRate.setId(-1L);
            exchangeRate.setCurrencyId(currencyRepository.findById(latestExchangeRate.getCurrencyId()).orElse(null));
            exchangeRate.setExchangeRate(latestExchangeRate.getExchangeRate());
            exchangeRate.setLastUpdated(latestExchangeRate.getLastUpdated());
            exchangeRates.add(exchangeRate);
        }

        if (shouldUpdate) {
            updateExchangeRates();
            return getAllLatestExchangeRates();
        }

        return exchangeRates;
    }

}
