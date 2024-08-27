package com.example.merchstore.services;

import com.example.merchstore.components.models.Ad;
import com.example.merchstore.repositories.AdRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 11.06.2024
 */

@Service
public class GlobalAttributeService {

    @Autowired
    AdRepository adRepository;
    private List<Ad> ads;
    private final Random random = new Random();


    private final Map<String, Object> globalAttributes = new ConcurrentHashMap<>();

    public GlobalAttributeService() {
        globalAttributes.put("isLoggedIn", false);
    }

    public void addAttribute(String key, Object value) {
        globalAttributes.put(key, value);
    }

    public Map<String, Object> getGlobalAttributes() {
        return new ConcurrentHashMap<>(globalAttributes);
    }

    public void removeAttribute(String key) {
        globalAttributes.remove(key);
    }

    @PostConstruct
    public void loadAds() {
        this.ads = adRepository.findAll().stream()
                .filter(Ad::shouldBeDisplayed)
                .toList();
    }

    public List<Ad> getRandomAds(int count) {
        loadAds();
        return ads.stream()
                .skip(random.nextInt(ads.size()))
                .limit(count)
                .toList();
    }
}