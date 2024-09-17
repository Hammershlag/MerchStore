package com.example.merchstore.components.models;

import com.example.merchstore.components.interfaces.DataDisplay;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 17.09.2024
 */
@Data
@AllArgsConstructor @NoArgsConstructor
@Entity
@Table(name = "wishlist")
public class WishlistItem implements DataDisplay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wishlist_id")
    private Long wishlistId;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public WishlistItem(WishlistItem wishlistItem) {
        this.wishlistId = wishlistItem.getWishlistId();
        this.item = wishlistItem.getItem();
        this.user = wishlistItem.getUser();
    }

    @Override
    public DataDisplay displayData() {
        return new WishlistItem(this);
    }

    @Override
    public DataDisplay limitedDisplayData() {
        return null;
    }
}
