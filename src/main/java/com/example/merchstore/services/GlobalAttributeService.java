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
 * The GlobalAttributeService class is a service component in the Spring framework.
 * It provides methods to manage global attributes and ads.
 *
 * It has several methods:
 * <ul>
 *     <li>addAttribute(String key, Object value): This method adds a new global attribute with the provided key and value.</li>
 *     <li>getGlobalAttributes(): This method returns a copy of the current global attributes.</li>
 *     <li>removeAttribute(String key): This method removes the global attribute with the provided key.</li>
 *     <li>loadAds(): This method loads all ads that should be displayed from the AdRepository. It is annotated with @PostConstruct, so it is executed after dependency injection is done.</li>
 *     <li>getRandomAds(int count): This method returns a random subset of the loaded ads. The number of ads returned is specified by the count parameter.</li>
 * </ul>
 *
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