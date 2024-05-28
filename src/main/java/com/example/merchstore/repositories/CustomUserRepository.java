package com.example.merchstore.repositories;

import com.example.merchstore.dto.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 28.05.2024
 */
public interface CustomUserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);
    boolean existsByUsername(String username);

}
