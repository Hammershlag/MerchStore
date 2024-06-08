package com.example.merchstore.services;

import com.example.merchstore.controllers.auth.LoginController_s;
import com.example.merchstore.components.enums.Role;
import com.example.merchstore.components.models.User;
import com.example.merchstore.principals.CustomUserPrincipal;
import com.example.merchstore.repositories.CustomUserRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

    public boolean existsByUsername(String username) {
        return customUserRepository.existsByUsername(username);
    }

    public User registerUser(User user, PasswordEncoder passwordEncoder) {
        if (customUserRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (user.getImage() == null) {
            user.setImage(new byte[0]);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        return customUserRepository.save(user);
    }

    @SneakyThrows
    public User registerUser(User user, MultipartFile image, PasswordEncoder passwordEncoder) {
        if (customUserRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        byte[] userImage = image.getBytes();
        if (userImage == null) {
            userImage = new byte[0];
        }
        user.setImage(userImage);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        return customUserRepository.save(user);
    }

    public boolean authenticateUser(String username, String password, PasswordEncoder passwordEncoder) {
        User user = customUserRepository.findByUsername(username);
        if (user == null) {
            user = customUserRepository.findByEmail(username);
        }
        if (user != null) {
            return passwordEncoder.matches(password, user.getPassword());
        }
        return false;
    }

    public boolean authenticateUser(LoginController_s.LoginForm loginForm, PasswordEncoder passwordEncoder) {
        User user = customUserRepository.findByUsername(loginForm.getUsername());
        if (user == null) {
            user = customUserRepository.findByEmail(loginForm.getUsername());
        }
        if (user != null) {
            return passwordEncoder.matches(loginForm.getPassword(), user.getPassword());
        }
        return false;
    }

    public boolean existsByEmail(String email) {
        return customUserRepository.existsByEmail(email);
    }

    public boolean existsByPhoneNumber(String phoneNumber) {
        return customUserRepository.existsByPhoneNumber(phoneNumber);
    }

}
