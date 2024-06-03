package com.example.merchstore.repositories;

import com.example.merchstore.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 01.06.2024
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Category findByName(String name);

}
