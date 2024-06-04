package com.example.merchstore.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 03.06.2024
 */

@Data @AllArgsConstructor @NoArgsConstructor
@Entity
@Table(name = "cart")
public class CartItem implements DataDisplay{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long cartId;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "item_id", referencedColumnName = "item_id")
    private Item item;

    @Column(name = "quantity")
    private int quantity;

    public CartItem(CartItem other) {
        this.cartId = other.cartId;
        this.user = other.user;
        this.item = other.item;
        this.quantity = other.quantity;
    }

    public CartItem(User user, Item item, int quantity) {
        this.user = user;
        this.item = item;
        this.quantity = quantity;
    }

    @Override
    public DataDisplay displayData() {
        return new CartItem(this);
    }

    @Override
    public DataDisplay limitedDisplayData() {
        return null;
    }

}