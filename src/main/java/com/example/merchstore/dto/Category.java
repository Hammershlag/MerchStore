package com.example.merchstore.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 28.05.2024
 */

@Data @AllArgsConstructor @NoArgsConstructor
@Entity
@Table(name = "categories")
public class Category implements DataDisplay{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    public Category(Category other) {
        this.categoryId = other.categoryId;
        this.name = other.name;
        this.description = other.description;
    }
    @Override
    public DataDisplay displayData() {
        return new Category(this);
    }

    @Override
    public DataDisplay limitedDisplayData() {
        return null;
    }
}
