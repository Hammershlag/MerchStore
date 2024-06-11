package com.example.merchstore.services;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 11.06.2024
 */

@Service
public class GlobalAttributeService {

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
}