package com.example.merchstore.repositories;

import com.example.merchstore.components.models.User;
import com.example.merchstore.components.models.UserItemHistory;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The UserItemHistoryRepository interface is a Spring Data JPA repository for the UserItemHistory entity.
 * It provides CRUD operations and methods to find UserItemHistory entities by associated User, and to delete all UserItemHistory entities by associated User.
 *
 * It has two methods:
 * <ul>
 *     <li>findAllByUser(User user): This method retrieves a list of UserItemHistory entities associated with the provided User entity.</li>
 *     <li>deleteAllByUser(User user): This method deletes all UserItemHistory entities associated with the provided User entity. This operation is transactional.</li>
 * </ul>
 *
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 03.09.2024
 */

@Repository
public interface UserItemHistoryRepository extends JpaRepository<UserItemHistory, Long> {

    List<UserItemHistory> findAllByUser(User user);

    @Transactional
    void deleteAllByUser(User user);

}
