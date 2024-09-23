package com.example.merchstore.components.models;

import com.example.merchstore.components.enums.Language;
import com.example.merchstore.components.interfaces.DataDisplay;
import com.example.merchstore.components.interfaces.ImageDisplay;
import com.example.merchstore.components.superClasses.Translatable;
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
import java.util.HashMap;
import java.util.function.BiConsumer;
import java.util.function.Function;

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
public class Item extends Translatable implements DataDisplay, ImageDisplay {

    /**
     * The ID of the item.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long itemId;

    /**
     * The name of the item.
     */
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * The description of the item.
     */
    @Column(name = "description", nullable = false)
    private String description;

    /**
     * The price of the item.
     */
    @Column(name = "price", nullable = false)
    private BigDecimal price;

    /**
     * The quantity of the item in stock.
     */
    @Column(name = "stock_quantity", nullable = false)
    private Integer stockQuantity;

    /**
     * The category of the item.
     * @see Category
     */
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    /**
     * The date and time when the item was created.
     */
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    /**
     * The date and time when the item was last updated.
     */
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * The default currency of the item.
     * @see Currency
     */
    @JoinColumn(name = "currency_id", nullable = false)
    @ManyToOne
    private Currency currency;

    /**
     * The image of the item in byte array format.
     */
    @JsonIgnore
    @JdbcTypeCode(Types.BINARY)
    @Column(name = "image", columnDefinition = "BYTEA", nullable = true)
    private byte[] image;

    @Enumerated(EnumType.STRING)
    @Column(name = "language")
    private Language language;

    @JsonIgnore
    @Transient
    final String className = "Item";

    @JsonIgnore
    @Transient
    final HashMap<String, Function<Translatable, String>> textFieldsGetter = new HashMap<>() {{
        put("name", (Translatable t) -> ((Item) t).getName());
        put("description", (Translatable t) -> ((Item) t).getDescription());
    }};

    @JsonIgnore
    @Transient
    final HashMap<String, BiConsumer<Translatable, String>> textFieldsSetter = new HashMap<>() {{
        put("name", (Translatable t, String value) -> ((Item) t).setName(value));
        put("description", (Translatable t, String value) -> ((Item) t).setDescription(value));
    }};

    /**
     * The constructor for the Item class.
     * @param itemId The ID of the item.
     * @param name The name of the item.
     * @param description The description of the item.
     * @param price The price of the item.
     * @param stockQuantity The quantity of the item in stock.
     * @param category The category of the item.
     * @param createdAt The date and time when the item was created.
     * @param updatedAt The date and time when the item was last updated.
     */
    public Item(Long itemId, String name, String description, BigDecimal price, Integer stockQuantity, Category category, LocalDateTime createdAt, LocalDateTime updatedAt, Language language) {
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
        this.language = language;
    }

    /**
     * The constructor for the Item class.
     * @param name The name of the item.
     * @param description The description of the item.
     * @param price The price of the item.
     * @param stockQuantity The quantity of the item in stock.
     * @param category The category of the item.
     * @param createdAt The date and time when the item was created.
     * @param updatedAt The date and time when the item was last updated.
     */
    public Item(String name, String description, BigDecimal price, Integer stockQuantity, Category category, LocalDateTime createdAt, LocalDateTime updatedAt, Language language) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.category = category;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.image = null;
        setDefaultImage();
        this.language = language;
    }

    /**
     * The copy constructor for the Item class.
     * @param item The Item object to copy.
     */
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
        this.language = item.getLanguage();
    }

    /**
     * Get the status of the image. Uploaded if the image is not null, Not uploaded otherwise.
     * @return a String representing the status of the image.
     */
    @JsonProperty("imageStatus")
    public String getImageStatus() {
        return image != null ? "Uploaded" : "Not uploaded";
    }

    /**
     * Display the data of the item.
     *
     * @see DataDisplay
     *
     * @return a DataDisplay object representing the item data.
     */
    @Override
    public DataDisplay displayData() {
        return new Item(this);
    }

    /**
     * Display a limited set of data of the item.
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
     * Set the default image if the image is null.
     */
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
    public String resolveImage() {
        return "";
    }

    /**
     * Convert the image to a base64 string.
     *
     * @return a String representing the image in base64 format.
     */
    @Override
    public String base64Image() {
        setDefaultImage();
        return Base64.getEncoder().encodeToString(image);
    }

    @JsonIgnore
    @Override
    public Long getTranslatableId() {
        return itemId;
    }

    @Override
    public void setTranslatableId(Long id) {
        this.itemId = id;
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