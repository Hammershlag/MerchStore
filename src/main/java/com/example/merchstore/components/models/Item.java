package com.example.merchstore.components.models;

import com.example.merchstore.components.interfaces.DataDisplay;
import com.example.merchstore.components.interfaces.ImageDisplay;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.Base64;

import static com.example.merchstore.components.utilities.Defaults.DEFAULT_ITEM_IMAGE;
import static com.example.merchstore.components.utilities.ImageProcessor.getImageAsByteArray;

/**
 * The Item class represents an item in the system.
 * It implements the DataDisplay and ImageDisplay interfaces to control the display of data and images.
 *
 * It has nine fields:
 * <ul>
 *     <li>itemId: The ID of the item.</li>
 *     <li>name: The name of the item.</li>
 *     <li>description: The description of the item.</li>
 *     <li>price: The price of the item.</li>
 *     <li>stockQuantity: The quantity of the item in stock.</li>
 *     <li>category: The category of the item.</li>
 *     <li>createdAt: The date and time when the item was created.</li>
 *     <li>updatedAt: The date and time when the item was last updated.</li>
 *     <li>image: The image of the item.</li>
 * </ul>
 *
 * It also includes methods to display data, limited display data, set default image, and convert image to base64 string.
 *
 * This class is also a JPA entity, meaning it is mapped to a corresponding table in the database.
 *
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 28.05.2024
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "items")
public class Item implements DataDisplay, ImageDisplay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long itemId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "stock_quantity", nullable = false)
    private Integer stockQuantity;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @JoinColumn(name = "currency_id", nullable = false)
    @ManyToOne
    private Currency currency;

    @JsonIgnore
    @JdbcTypeCode(Types.BINARY)
    @Column(name = "image", columnDefinition = "BYTEA", nullable = true)
    private byte[] image;

    public Item(Long itemId, String name, String description, BigDecimal price, Integer stockQuantity, Category category, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.itemId = itemId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.category = category;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.image = null;
        setDefaultImage();
    }

    public Item(String name, String description, BigDecimal price, Integer stockQuantity, Category category, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.category = category;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.image = null;
        setDefaultImage();
    }

    public Item(Item item) {
        this.itemId = item.getItemId();
        this.name = item.getName();
        this.description = item.getDescription();
        this.price = item.getPrice();
        this.stockQuantity = item.getStockQuantity();
        this.category = item.getCategory();
        this.createdAt = item.getCreatedAt();
        this.updatedAt = item.getUpdatedAt();
        this.image = item.getImage() != null ? item.getImage().clone() : null;
        setDefaultImage();
    }

    @JsonProperty("imageStatus")
    public String getImageStatus() {
        return image != null ? "Uploaded" : "Not uploaded";
    }


    @Override
    public DataDisplay displayData() {
        return new Item(this);
    }

    @Override
    public DataDisplay limitedDisplayData() {
        return null;
    }

    @Override
    public void setDefaultImage() {
        if (image == null || image.length == 0) {
            try {
                image = getImageAsByteArray(DEFAULT_ITEM_IMAGE);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public String base64Image() {
        setDefaultImage();
        return Base64.getEncoder().encodeToString(image);
    }
}