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
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 04.09.2024
 */

@Service
public class LatestExchangeRateService {

    @Autowired
    private LatestExchangeRateRepository latestExchangeRateRepository;

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private ExchangeRateRepository exchangeRateRepository;

    private RestTemplate restTemplate = new RestTemplate();

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

    public ExchangeRate getLatestExchangeRateForCurrency(Long currencyId) {
        LatestExchangeRate latestExchangeRate = latestExchangeRateRepository.findFirstByCurrencyIdOrderByLastUpdatedDesc(currencyId);
        if (latestExchangeRate == null || Duration.between(latestExchangeRate.getLastUpdated(), LocalDateTime.now()).toHours() > 24) {
            updateExchangeRates();
            latestExchangeRate = latestExchangeRateRepository.findFirstByCurrencyIdOrderByLastUpdatedDesc(currencyId);
        }
        return new ExchangeRate(-1L, currencyRepository.findById(latestExchangeRate.getCurrencyId()).orElse(null), latestExchangeRate.getExchangeRate(), latestExchangeRate.getLastUpdated());
    }

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
