package com.example.merchstore.repositories;

import com.example.merchstore.dto.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 28.05.2024
 */

public interface CustomUserRepository extends JpaRepository<User, Long> {

    //Find methods
    User findByUsername(String username);

    //Exist methods
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);
}
