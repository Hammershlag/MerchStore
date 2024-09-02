package com.example.merchstore.components.models;

import com.example.merchstore.components.interfaces.DataDisplay;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 01.09.2024
 */

@Data
@AllArgsConstructor @NoArgsConstructor
@Entity
@Table(name = "best_sellers")
public class Bestseller implements DataDisplay {

    @Id
    @Column(name = "item_id")
    private Long itemId;

    @Column(name = "total_sales")
    private long totalSales;

    public Bestseller(Bestseller bestseller) {
        this.itemId = bestseller.getItemId();
        this.totalSales = bestseller.getTotalSales();
    }

    @Override
    public DataDisplay displayData() {
        return new Bestseller(this);
    }

    @Override
    public DataDisplay limitedDisplayData() {
        return null;
    }
}
