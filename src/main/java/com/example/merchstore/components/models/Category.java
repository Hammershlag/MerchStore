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
import java.sql.Types;
import java.util.Base64;

import static com.example.merchstore.components.utilities.Defaults.DEFAULT_CATEGORY_IMAGE;
import static com.example.merchstore.components.utilities.Defaults.DEFAULT_ITEM_IMAGE;
import static com.example.merchstore.components.utilities.ImageProcessor.getImageAsByteArray;

/**
 * The Category class represents a category in the system.
 * It implements the DataDisplay and ImageDisplay interfaces to control the display of data and images.
 *
 * It has five fields:
 * <ul>
 *     <li>categoryId: The ID of the category.</li>
 *     <li>name: The name of the category.</li>
 *     <li>description: The description of the category.</li>
 *     <li>image: The image of the category in byte array format.</li>
 *     <li>main: A boolean indicating if the category is a main category.</li>
 * </ul>
 *
 * It also includes methods to get the status of the image, display data, set the default image, and get the Base64 representation of the image.
 *
 * This class is also a JPA entity, meaning it is mapped to a corresponding table in the database.
 *
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 28.05.2024
 */

@Data @AllArgsConstructor @NoArgsConstructor
@Entity
@Table(name = "categories")
public class Category implements DataDisplay, ImageDisplay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @JsonIgnore
    @JdbcTypeCode(Types.BINARY)
    @Column(name = "image", columnDefinition = "BYTEA", nullable = true)
    private byte[] image;

    @Column(name = "main", nullable = false)
    private boolean main = false;


    public Category(Category other) {
        this.categoryId = other.categoryId;
        this.name = other.name;
        this.description = other.description;
        this.image = other.image;
        this.main = other.main;
    }

    public Category(String name, String description, boolean main) {
        this.name = name;
        this.description = description;
        this.main = main;
        setDefaultImage();
    }

    @JsonProperty("imageStatus")
    public String getImageStatus() {
        return image != null ? "Uploaded" : "Not uploaded";
    }

    @Override
    public DataDisplay displayData() {
        return new Category(this);
    }

    @Override
    public DataDisplay limitedDisplayData() {
        return null;
    }

    @Override
    public void setDefaultImage() {
        if (image == null || image.length == 0) {
            try {
                image = getImageAsByteArray(DEFAULT_CATEGORY_IMAGE);
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
