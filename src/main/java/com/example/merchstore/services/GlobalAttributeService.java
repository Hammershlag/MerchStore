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

    /**
     * The AdRepository dependency is injected by Spring.
     * @see AdRepository
     */
    @Autowired
    AdRepository adRepository;

    /**
     * The list of ads that should be displayed.
     */
    private List<Ad> ads;

    /**
     * The Random object used to generate random numbers.
     */
    private final Random random = new Random();

    /**
     * The map of global attributes.
     */
    private final Map<String, Object> globalAttributes = new ConcurrentHashMap<>();

    /**
     * The constructor initializes the isLoggedIn attribute to false.
     */
    public GlobalAttributeService() {
        globalAttributes.put("isLoggedIn", false);
    }

    /**
     * This method adds a new global attribute with the provided key and value.
     *
     * @param key The key of the attribute.
     * @param value The value of the attribute.
     */
    public void addAttribute(String key, Object value) {
        globalAttributes.put(key, value);
    }

    /**
     * This method returns a copy of the current global attributes.
     *
     * @return A copy of the current global attributes.
     */
    public Map<String, Object> getGlobalAttributes() {
        return new ConcurrentHashMap<>(globalAttributes);
    }

    /**
     * This method removes the global attribute with the provided key.
     *
     * @param key The key of the attribute to remove.
     */
    public void removeAttribute(String key) {
        globalAttributes.remove(key);
    }

    /**
     * This method loads all ads that should be displayed from the AdRepository.
     * It is annotated with @PostConstruct, so it is executed after dependency injection is done.
     */
    @PostConstruct
    public void loadAds() {
        this.ads = adRepository.findAll().stream()
                .filter(Ad::shouldBeDisplayed)
                .toList();
    }

    /**
     * This method returns a random subset of the loaded ads.
     * The number of ads returned is specified by the count parameter.
     *
     * @param count The number of ads to return.
     * @return A list of random ads.
     */
    public List<Ad> getRandomAds(int count) {
        loadAds();
        return ads.stream()
                .skip(Math.abs(random.nextInt(ads.size())))
                .limit(Math.abs(count))
                .toList();
    }
}