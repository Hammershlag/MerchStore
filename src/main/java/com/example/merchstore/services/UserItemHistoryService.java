package com.example.merchstore.services;

import com.example.merchstore.components.models.Item;
import com.example.merchstore.components.models.User;
import com.example.merchstore.components.models.UserItemHistory;
import com.example.merchstore.repositories.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * The UserItemHistoryService class is a service component in the Spring framework.
 * It provides a method to get the most recently browsed items for a specific user.
 *
 * It has one main method:
 * <ul>
 *     <li>getNewestBrowsedItemsForUser(Long userId): This method retrieves the 10 most recently browsed items for the provided user from the database. It uses a SQL query to get the item IDs of the most recently browsed items, and then fetches the corresponding Item objects from the ItemRepository.</li>
 * </ul>
 *
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 03.09.2024
 */

@Service
public class UserItemHistoryService {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private ItemRepository itemRepository;

    public List<Item> getNewestBrowsedItemsForUser(Long userId) {
        List<Item> items = new ArrayList<>();

        String sql = "WITH RankedItems AS (" +
                "    SELECT user_id, item_id, last_browsed, ROW_NUMBER() OVER (ORDER BY last_browsed DESC) AS rn" +
                "    FROM (" +
                "        SELECT user_id, item_id, MAX(last_browsed) AS last_browsed" +
                "        FROM user_item_history" +
                "        WHERE user_id = ?" +
                "        GROUP BY user_id, item_id" +
                "    ) AS UniqueItems" +
                ") " +
                "SELECT item_id FROM RankedItems WHERE rn <= 10 ORDER BY last_browsed DESC";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Long itemId = rs.getLong("item_id");
                // Fetch the item from the repository
                itemRepository.findById(itemId).ifPresent(items::add);
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception properly
        }

        return items;
    }
}



