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
import java.util.ArrayList;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;

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

    /**
     * The ID of the category.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long categoryId;

    /**
     * The name of the category.
     */
    @Column(name = "name", unique = true, nullable = false)
    private String name;

    /**
     * The description of the category.
     */
    @Column(name = "description")
    private String description;

    /**
     * The image of the category in byte array format.
     */
    @JsonIgnore
    @JdbcTypeCode(Types.BINARY)
    @Column(name = "image", columnDefinition = "BYTEA", nullable = true)
    private byte[] image;

    /**
     * A boolean indicating if the category is a main category.
     */
    @Column(name = "main", nullable = false)
    private boolean main = false;

    @ManyToOne
    @JoinColumn(name = "parent_category_id")
    private Category parentCategory;

    @Column(name = "should_display", nullable = false)
    private boolean shouldDisplay;

    @OneToMany(mappedBy = "parentCategory", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Category> childCategories = new ArrayList<>();


    /**
     * The copy constructor for the Category class.
     * @param other The Category object to copy.
     */
    public Category(Category other) {
        this.categoryId = other.categoryId;
        this.name = other.name;
        this.description = other.description;
        this.image = other.image;
        this.main = other.main;
        this.parentCategory = other.parentCategory;
        this.shouldDisplay = other.shouldDisplay;
    }

    /**
     * The constructor for the Category class.
     * @param name The name of the category.
     * @param description The description of the category.
     * @param main A boolean indicating if the category is a main category.
     */
    public Category(String name, String description, boolean main, Category parentCategory) {
        this.name = name;
        this.description = description;
        this.main = main;
        this.parentCategory = parentCategory;
        this.shouldDisplay = true;
        setDefaultImage();
    }

    /**
     * Get the status of the image.
     * @return "Uploaded" if the image is not null, "Not uploaded" otherwise.
     */
    @JsonProperty("imageStatus")
    public String getImageStatus() {
        return image != null ? "Uploaded" : "Not uploaded";
    }

    /**
     * Display the data of the category.
     *
     * @see DataDisplay
     *
     * @return a DataDisplay object representing the category data.
     */
    @Override
    public DataDisplay displayData() {
        return new Category(this);
    }

    /**
     * Display the limited data of the category.
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
     * Set the default image for the category.
     *
     * @see ImageDisplay
     */
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
    public String resolveImage() {
        return "";
    }

    /**
     * Get the Base64 representation of the image.
     *
     * @see ImageDisplay
     *
     * @return a String representing the Base64 image.
     */
    @Override
    public String base64Image() {
        setDefaultImage();
        return Base64.getEncoder().encodeToString(image);
    }

    public String buildCategoryHierarchy(Category category) {
        if (category.getParentCategory() != null) {
            return buildCategoryHierarchy(category.getParentCategory()) + " -> " + category.getName();
        } else {
            return category.getName();
        }
    }



}
