package com.example.merchstore.services;

import com.example.merchstore.dto.User;
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

    public User registerUser(User user, PasswordEncoder passwordEncoder) {
        if (customUserRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER");
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
        user.setImage(userImage);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER");
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        return customUserRepository.save(user);
    }

    public boolean authenticateUser(String username, String password, PasswordEncoder passwordEncoder) {
        User user = customUserRepository.findByUsername(username);
        if (user != null) {
            return passwordEncoder.matches(password, user.getPassword());
        }
        return false;
    }
}
