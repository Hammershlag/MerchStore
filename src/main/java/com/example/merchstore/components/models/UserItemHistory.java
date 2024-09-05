package com.example.merchstore.components.models;

import com.example.merchstore.components.interfaces.DataDisplay;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * The UserItemHistory class represents a user's browsing history of items in the system.
 * It implements the DataDisplay interface to control the display of data.
 *
 * It has four fields:
 * <ul>
 *     <li>id: The ID of the user item history.</li>
 *     <li>user: The user who browsed the item.</li>
 *     <li>item: The item that was browsed.</li>
 *     <li>lastBrowsed: The date and time when the item was last browsed by the user.</li>
 * </ul>
 *
 * It also includes methods to display data and limited display data.
 *
 * This class is also a JPA entity, meaning it is mapped to a corresponding table in the database.
 *
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 03.09.2024
 */


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_item_history")
public class UserItemHistory implements DataDisplay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @Column(name = "last_browsed")
    private LocalDateTime lastBrowsed;

    public UserItemHistory(User user, Item item) {
        this.user = user;
        this.item = item;
        this.lastBrowsed = LocalDateTime.now();
    }

    public UserItemHistory(UserItemHistory userItemHistory) {
        this.user = userItemHistory.getUser();
        this.item = userItemHistory.getItem();
        this.lastBrowsed = userItemHistory.getLastBrowsed();
    }

    @Override
    public DataDisplay displayData() {
        return new UserItemHistory(this);
    }

    @Override
    public DataDisplay limitedDisplayData() {
        return null;
    }
}
