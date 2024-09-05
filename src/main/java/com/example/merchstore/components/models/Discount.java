package com.example.merchstore.components.models;

import com.example.merchstore.components.interfaces.DataDisplay;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * The Discount class represents a discount in the system.
 * It implements the DataDisplay interface to control the display of data.
 *
 * It has six fields:
 * <ul>
 *     <li>discountId: The ID of the discount.</li>
 *     <li>code: The unique code of the discount.</li>
 *     <li>description: The description of the discount.</li>
 *     <li>discountPercentage: The percentage of the discount.</li>
 *     <li>validFrom: The start date of the discount's validity.</li>
 *     <li>validUntil: The end date of the discount's validity.</li>
 * </ul>
 *
 * It also includes methods to display data, limited display data, and check if the discount is valid.
 *
 * This class is also a JPA entity, meaning it is mapped to a corresponding table in the database.
 *
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 28.05.2024
 */

@Data @AllArgsConstructor @NoArgsConstructor
@Entity
@Table(name = "discounts")
public class Discount implements DataDisplay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "discount_id")
    private Long discountId;

    @Column(name = "code", unique = true, nullable = false)
    private String code;

    @Column(name = "description")
    private String description;

    @Column(name = "discount_percentage", nullable = false)
    private BigDecimal discountPercentage;

    @Column(name = "valid_from", nullable = false)
    private LocalDate validFrom;

    @Column(name = "valid_until", nullable = false)
    private LocalDate validUntil;

    public Discount(Discount other) {
        this.discountId = other.discountId;
        this.code = other.code;
        this.description = other.description;
        this.discountPercentage = other.discountPercentage;
        this.validFrom = other.validFrom;
        this.validUntil = other.validUntil;
    }

    @Override
    public DataDisplay displayData() {
        return new Discount(this);
    }

    @Override
    public DataDisplay limitedDisplayData() {
        return null;
    }

    public boolean isValid() {
        LocalDate now = LocalDate.now();
        return now.isAfter(validFrom) && now.isBefore(validUntil);
    }
}

