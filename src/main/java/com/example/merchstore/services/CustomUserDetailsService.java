package com.example.merchstore.services;

import com.example.merchstore.dto.User;
import com.example.merchstore.principals.CustomUserPrincipal;
import com.example.merchstore.repositories.CustomUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 28.05.2024
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private CustomUserRepository customUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = customUserRepository.findByUsername(username);
        if(user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new CustomUserPrincipal(user);
    }

    public User registerUser(String username, String password, PasswordEncoder passwordEncoder) {
        if (customUserRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(username + "@default.com");
        user.setFirstName("Default");
        user.setLastName("Default");
        user.setPhoneNumber("1234567890");
        user.setAddress("Default Address");
        user.setRole("USER");
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        return customUserRepository.save(user);
    }

    public boolean authenticateUser(String username, String password, PasswordEncoder passwordEncoder) {
        User user = customUserRepository.findByUsername(username);
        if (user != null) {
            // Compare the provided password with the encoded password stored in the database
            return passwordEncoder.matches(password, user.getPassword());
        }
        return false; // User not found or password incorrect
    }
}
