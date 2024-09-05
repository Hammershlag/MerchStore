package com.example.merchstore.components.models;

import com.example.merchstore.components.interfaces.DataDisplay;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The Bestseller class represents a best-selling item in the system. The data is collected from the last 30 days.
 * It implements the DataDisplay interface to control the display of data.
 *
 * It has two fields:
 * <ul>
 *     <li>itemId: The ID of the item.</li>
 *     <li>totalSales: The total sales of the item.</li>
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
@AllArgsConstructor @NoArgsConstructor
@Entity
@Table(name = "best_sellers")
public class Bestseller implements DataDisplay {

    /**
     * The ID of the item.
     */
    @Id
    @Column(name = "item_id")
    private Long itemId;

    /**
     * The total sales of the item.
     */
    @Column(name = "total_sales")
    private long totalSales;

    /**
     * The copy constructor for the Bestseller class.
     * @param bestseller The Bestseller object to copy.
     */
    public Bestseller(Bestseller bestseller) {
        this.itemId = bestseller.getItemId();
        this.totalSales = bestseller.getTotalSales();
    }

    /**
     * Display the data of the bestseller.
     *
     * @see DataDisplay
     *
     * @return a DataDisplay object representing the bestseller data.
     */
    @Override
    public DataDisplay displayData() {
        return new Bestseller(this);
    }

    /**
     * Display the limited data of the bestseller.
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
