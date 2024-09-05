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
 * The CustomUserDetailsService class is a service component in the Spring framework.
 * It implements the UserDetailsService interface from Spring Security, which is used to retrieve user-related data.
 *
 * It has several methods:
 * <ul>
 *     <li>loadUserByUsername(String username): This method retrieves a User by their username from the CustomUserRepository. If the User is not found, it throws a UsernameNotFoundException. Otherwise, it returns a new CustomUserPrincipal with the User.</li>
 *     <li>existsByUsername(String username), existsByEmail(String email), existsByPhoneNumber(String phoneNumber): These methods check if a User exists in the CustomUserRepository by their username, email, or phone number, respectively.</li>
 *     <li>registerUser(User user, PasswordEncoder passwordEncoder), registerUser(User user, MultipartFile image, PasswordEncoder passwordEncoder): These methods register a new User. They check if the username already exists, set the User's image, encode the User's password, set the User's role, set the creation and update timestamps, and save the User in the CustomUserRepository.</li>
 *     <li>authenticateUser(String username, String password, PasswordEncoder passwordEncoder), authenticateUser(LoginController_s.LoginForm loginForm, PasswordEncoder passwordEncoder): These methods authenticate a User. They retrieve a User by their username or email, and then check if the provided password matches the User's password.</li>
 * </ul>
 *
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 28.05.2024
 */

@Service
public class CustomUserDetailsService implements UserDetailsService {

    /**
     * The CustomUserRepository dependency is injected by Spring.
     * @see CustomUserRepository
     */
    @Autowired
    private CustomUserRepository customUserRepository;

    /**
     * This method retrieves a User by their username from the CustomUserRepository.
     * If the User is not found, it throws a UsernameNotFoundException.
     * Otherwise, it returns a new CustomUserPrincipal with the User.
     *
     * @param username The username of the User to retrieve.
     * @return A new CustomUserPrincipal with the User.
     * @throws UsernameNotFoundException If the User is not found.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = customUserRepository.findByUsername(username);
        if(user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new CustomUserPrincipal(user);
    }

    /**
     * This method checks if a User exists in the CustomUserRepository by their username.
     *
     * @param username The username of the User to check.
     * @return A boolean value indicating if a User exists with the provided username.
     */
    public boolean existsByUsername(String username) {
        return customUserRepository.existsByUsername(username);
    }

    /**
     * This method registers a new User.
     * It checks if the username already exists, sets the User's image, encodes the User's password, sets the User's role, sets the creation and update timestamps, and saves the User in the CustomUserRepository.
     *
     * @param user The User to register.
     * @param passwordEncoder The PasswordEncoder to encode the User's password with.
     * @return The registered User.
     */
    public User registerUser(User user, PasswordEncoder passwordEncoder) {
        if (customUserRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (user.getImage() == null) {
            user.setImage(new byte[0]);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getRole() == null) user.setRole(Role.USER);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        return customUserRepository.save(user);
    }

    /**
     * This method registers a new User.
     * It checks if the username already exists, sets the User's image, encodes the User's password, sets the User's role, sets the creation and update timestamps, and saves the User in the CustomUserRepository.
     *
     * @param user The User to register.
     * @param image The image file for the User.
     * @param passwordEncoder The PasswordEncoder to encode the User's password with.
     * @return The registered User.
     */
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

    /**
     * This method authenticates a User.
     * It retrieves a User by their username or email, and then checks if the provided password matches the User's password.
     *
     * @param username The username or email of the User to authenticate.
     * @param password The password to authenticate the User with.
     * @param passwordEncoder The PasswordEncoder to encode the password with.
     * @return A boolean value indicating if the User is authenticated.
     */
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

    /**
     * This method authenticates a User.
     * It retrieves a User by their username or email, and then checks if the provided password matches the User's password.
     *
     * @param loginForm The LoginForm object containing the username and password to authenticate.
     * @param passwordEncoder The PasswordEncoder to encode the password with.
     * @return A boolean value indicating if the User is authenticated.
     */
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

    /**
     * This method checks if a User exists in the CustomUserRepository by their email.
     *
     * @param email The email of the User to check.
     * @return A boolean value indicating if a User exists with the provided email.
     */
    public boolean existsByEmail(String email) {
        return customUserRepository.existsByEmail(email);
    }

    /**
     * This method checks if a User exists in the CustomUserRepository by their phone number.
     *
     * @param phoneNumber The phone number of the User to check.
     * @return A boolean value indicating if a User exists with the provided phone number.
     */
    public boolean existsByPhoneNumber(String phoneNumber) {
        return customUserRepository.existsByPhoneNumber(phoneNumber);
    }

}
