package com.example.merchstore.Decorators;

import com.example.merchstore.model.Gender;
import com.example.merchstore.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 28.05.2024
 */

public class UserDecorator {

    public static User create(String username) {
        User user = new User();
        user.setUserId(Long.MIN_VALUE);
        user.setUsername(username);
        user.setPassword("Default");
        user.setEmail(username + "@default.com");
        user.setFirstName("Default");
        user.setLastName("Default");
        user.setPhoneNumber("1234567890");
        user.setAddress("Default Address");
        user.setRole("USER");
        user.setGender(Gender.DEFAULT);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setImage(null);
        user.setBirthDate(LocalDate.now());
        return user;
    }

}
