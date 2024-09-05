package com.example.merchstore.Decorators;

import com.example.merchstore.components.enums.Gender;
import com.example.merchstore.components.enums.Role;
import com.example.merchstore.components.models.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * The UserDecorator class is a decorator for the User class in the application.
 * It provides a method to create a default User object with predefined values and a specified username.
 *
 * It has one method:
 * <ul>
 *     <li>create(String username): This static method creates a default User object with predefined values and a specified username, and returns it.</li>
 * </ul>
 *
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 28.05.2024
 */
public class UserDecorator {

    /**
     * This static method creates a default User object with predefined values and a specified username, and returns it.
     *
     * @param username The username for the User object.
     * @return A default User object with predefined values and a specified username.
     */
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
        user.setRole(Role.USER);
        user.setGender(Gender.DEFAULT);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setImage(null);
        user.setBirthDate(LocalDate.now());
        return user;
    }

}
