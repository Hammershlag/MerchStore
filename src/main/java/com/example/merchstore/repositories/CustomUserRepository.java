package com.example.merchstore.repositories;

import com.example.merchstore.components.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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

    /**
     * This method retrieves a User entity with the provided username.
     *
     * @param username The username of the User entity to retrieve.
     * @return A User entity with the provided username.
     */
    User findByUsername(String username);

    /**
     * This method retrieves a User entity with the provided email.
     *
     * @param email The email of the User entity to retrieve.
     * @return A User entity with the provided email.
     */
    User findByEmail(String email);

    /**
     * This method retrieves all User entities with pagination.
     *
     * @param pageable The pagination information.
     * @return A page of User entities.
     */
    Page<User> findAll(Pageable pageable);

    /**
     * This method retrieves a page of User entities where the username starts with the provided string, ignoring case.
     *
     * @param username The string to search for at the beginning of the username.
     * @param pageable The pagination information.
     * @return A page of User entities where the username starts with the provided string, ignoring case.
     */
    Page<User> findByUsernameStartingWithIgnoreCase(String username, Pageable pageable);

    /**
     * This method checks if a User entity with the provided username exists.
     *
     * @param username The username to check for.
     * @return A boolean value indicating if a User entity with the provided username exists.
     */
    boolean existsByUsername(String username);

    /**
     * This method checks if a User entity with the provided email exists.
     *
     * @param email The email to check for.
     * @return A boolean value indicating if a User entity with the provided email exists.
     */
    boolean existsByEmail(String email);

    /**
     * This method checks if a User entity with the provided phone number exists.
     *
     * @param phoneNumber The phone number to check for.
     * @return A boolean value indicating if a User entity with the provided phone number exists.
     */
    boolean existsByPhoneNumber(String phoneNumber);

    /**
     * This method updates a User entity with the provided id and user details.
     *
     * @param id The id of the User entity to update.
     * @param user The User entity with the updated details.
     */
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.email = :#{#user.email}, u.firstName = :#{#user.firstName}, u.lastName = :#{#user.lastName}, " +
            "u.phoneNumber = :#{#user.phoneNumber}, u.address = :#{#user.address}, u.gender = :#{#user.gender}, " +
            "u.updatedAt = :#{#user.updatedAt} WHERE u.userId = :id")
    void updateById(@Param("id") Long id, @Param("user") User user);

}
