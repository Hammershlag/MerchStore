package com.example.merchstore.repositories;

import com.example.merchstore.components.models.User;
import com.example.merchstore.components.models.UserItemHistory;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
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
