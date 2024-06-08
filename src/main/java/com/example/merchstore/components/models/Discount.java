package com.example.merchstore.components.models;

import com.example.merchstore.components.interfaces.DataDisplay;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
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
}

