package com.example.merchstore.repositories;

import com.example.merchstore.components.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * The CustomUserRepository interface is a Spring Data JPA repository for the User entity.
 * It provides CRUD operations and methods to find, check existence, and update User entities by various properties.
 *
 * It has eight methods:
 * <ul>
 *     <li>findByUsername(String username): This method retrieves a User entity with the provided username.</li>
 *     <li>findByEmail(String email): This method retrieves a User entity with the provided email.</li>
 *     <li>findAll(Pageable pageable): This method retrieves all User entities with pagination.</li>
 *     <li>findByUsernameStartingWithIgnoreCase(String username, Pageable pageable): This method retrieves a page of User entities where the username starts with the provided string, ignoring case.</li>
 *     <li>existsByUsername(String username): This method checks if a User entity with the provided username exists.</li>
 *     <li>existsByEmail(String email): This method checks if a User entity with the provided email exists.</li>
 *     <li>existsByPhoneNumber(String phoneNumber): This method checks if a User entity with the provided phone number exists.</li>
 *     <li>updateById(Long id, User user): This method updates a User entity with the provided id and user details.</li>
 * </ul>
 *
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 28.05.2024
 */

public interface CustomUserRepository extends JpaRepository<User, Long> {

    //Find methods
    User findByUsername(String username);
    User findByEmail(String email);
    Page<User> findAll(Pageable pageable);
    Page<User> findByUsernameStartingWithIgnoreCase(String username, Pageable pageable);

    //Exist methods
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.email = :#{#user.email}, u.firstName = :#{#user.firstName}, u.lastName = :#{#user.lastName}, " +
            "u.phoneNumber = :#{#user.phoneNumber}, u.address = :#{#user.address}, u.gender = :#{#user.gender}, " +
            "u.updatedAt = :#{#user.updatedAt} WHERE u.userId = :id")
    void updateById(@Param("id") Long id, @Param("user") User user);

//    @Query("SELECT u FROM User u WHERE u.role <> 'ADMIN'")
//    List<User> findNonAdminUsers();

}
