package com.example.merchstore.components.models;

import com.example.merchstore.components.interfaces.DataDisplay;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * The Sale class represents a sale in the system.
 * It implements the DataDisplay interface to control the display of data.
 *
 * It has four fields:
 * <ul>
 *     <li>id: The ID of the sale.</li>
 *     <li>item: The item that was sold.</li>
 *     <li>quantity: The quantity of the item that was sold.</li>
 *     <li>saleDate: The date and time when the sale occurred.</li>
 * </ul>
 *
 * It also includes methods to display data and limited display data.
 *
 * This class is also a JPA entity, meaning it is mapped to a corresponding table in the database.
 *
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 01.09.2024
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sales")
public class Sale implements DataDisplay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "sale_date")
    private LocalDateTime saleDate;

    public Sale(Sale sale) {
        this.id = sale.getId();
        this.item = sale.getItem();
        this.quantity = sale.getQuantity();
        this.saleDate = sale.getSaleDate();
    }

    @Override
    public DataDisplay displayData() {
        return new Sale(this);
    }

    @Override
    public DataDisplay limitedDisplayData() {
        return null;
    }
}
