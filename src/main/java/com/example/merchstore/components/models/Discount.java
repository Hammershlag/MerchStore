package com.example.merchstore.components.models;

import com.example.merchstore.components.enums.Language;
import com.example.merchstore.components.interfaces.DataDisplay;
import com.example.merchstore.components.superClasses.Translatable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.function.BiConsumer;
import java.util.function.Function;

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
public class Discount extends Translatable implements DataDisplay {

    /**
     * The ID of the discount.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "discount_id")
    private Long discountId;

    /**
     * The unique code of the discount.
     */
    @Column(name = "code", unique = true, nullable = false)
    private String code;

    /**
     * The description of the discount.
     */
    @Column(name = "description")
    private String description;

    /**
     * The percentage of the discount.
     */
    @Column(name = "discount_percentage", nullable = false)
    private BigDecimal discountPercentage;

    /**
     * The start date of the discount's validity.
     */
    @Column(name = "valid_from", nullable = false)
    private LocalDate validFrom;

    /**
     * The end date of the discount's validity.
     */
    @Column(name = "valid_until", nullable = false)
    private LocalDate validUntil;

    @Enumerated(EnumType.STRING)
    @Column(name = "language")
    private Language language;

    @JsonIgnore
    @Transient
    final String className = "Discount";

    @JsonIgnore
    @Transient
    final HashMap<String, Function<Translatable, String>> textFieldsGetter = new HashMap<>() {{
        put("description", (Translatable t) -> ((Discount) t).getDescription());
    }};

    @JsonIgnore
    @Transient
    final HashMap<String, BiConsumer<Translatable, String>> textFieldsSetter = new HashMap<>() {{
        put("description", (Translatable t, String value) -> ((Discount) t).setDescription(value));
    }};

    /**
     * The copy constructor for the Discount class.
     * @param other The Discount object to copy.
     */
    public Discount(Discount other) {
        this.discountId = other.discountId;
        this.code = other.code;
        this.description = other.description;
        this.discountPercentage = other.discountPercentage;
        this.validFrom = other.validFrom;
        this.validUntil = other.validUntil;
    }

    /**
     * Display the data of the discount.
     *
     * @see DataDisplay
     *
     * @return a DataDisplay object representing the discount data.
     */
    @Override
    public DataDisplay displayData() {
        return new Discount(this);
    }

    /**
     * Display the limited data of the discount.
     *
     * @see DataDisplay
     *
     * @return null, as it is not implemented yet.
     */
    @Override
    public DataDisplay limitedDisplayData() {
        return null;
    }

    /**
     * Check if the discount is valid.
     *
     * @return true if the discount is valid, false otherwise.
     */
    public boolean isValid() {
        LocalDate now = LocalDate.now();
        return now.isAfter(validFrom) && now.isBefore(validUntil);
    }

    @JsonIgnore
    @Override
    public Long getTranslatableId() {
        return discountId;
    }

    @Override
    public void setTranslatableId(Long id) {
        this.discountId = id;
    }

    @JsonIgnore
    @Override
    public Language getTranslatableLanguage() {
        return language;
    }


    @Override
    public void setTranslatableLanguage(Language language) {
        this.language = language;
    }
}

