package com.example.merchstore.components.models;

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
public class Bestseller {

    @Id
    @Column(name = "item_id")
    private Long itemId;

    @Column(name = "total_sales")
    private long totalSales;

}
