package com.example.merchstore.components.models;


import com.example.merchstore.components.interfaces.DataDisplay;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The CartItem class represents an item in a user's cart in the system.
 * It implements the DataDisplay interface to control the display of data.
 *
 * It has four fields:
 * <ul>
 *     <li>cartId: The ID of the cart item.</li>
 *     <li>user: The user who owns the cart.</li>
 *     <li>item: The item in the cart.</li>
 *     <li>quantity: The quantity of the item in the cart.</li>
 * </ul>
 *
 * It also includes methods to display data and limited display data.
 *
 * This class is also a JPA entity, meaning it is mapped to a corresponding table in the database.
 *
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 03.06.2024
 */

@Data @AllArgsConstructor @NoArgsConstructor
@Entity
@Table(name = "cart")
public class CartItem implements DataDisplay {

    /**
     * The ID of the cart item.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long cartId;

    /**
     * The user who owns the cart.
     * @see User
     */
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    /**
     * The item in the cart.
     * @see Item
     */
    @ManyToOne
    @JoinColumn(name = "item_id", referencedColumnName = "item_id")
    private Item item;

    /**
     * The quantity of the item in the cart.
     */
    @Column(name = "quantity")
    private int quantity;

    /**
     * The copy constructor for the CartItem class.
     * @param other The CartItem object to copy.
     */
    public CartItem(CartItem other) {
        this.cartId = other.cartId;
        this.user = other.user;
        this.item = other.item;
        this.quantity = other.quantity;
    }

    /**
     * The constructor for the CartItem class.
     * @param user The user who owns the cart.
     * @param item The item in the cart.
     * @param quantity The quantity of the item in the cart.
     */
    public CartItem(User user, Item item, int quantity) {
        this.user = user;
        this.item = item;
        this.quantity = quantity;
    }

    /**
     * Display the data of the cart item.
     *
     * @see DataDisplay
     *
     * @return a DataDisplay object representing the cart item data.
     */
    @Override
    public DataDisplay displayData() {
        return new CartItem(this);
    }

    /**
     * Display the limited data of the cart item.
     *
     * @see DataDisplay
     *
     * @return null, as it is not implemented yet.
     */
    @Override
    public DataDisplay limitedDisplayData() {
        return null;
    }

}