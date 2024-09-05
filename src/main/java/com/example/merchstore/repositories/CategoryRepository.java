package com.example.merchstore.repositories;

import com.example.merchstore.components.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The CategoryRepository interface is a Spring Data JPA repository for the Category entity.
 * It provides CRUD operations and methods to find Category entities by name or by the starting characters of the name, ignoring case.
 *
 * It has two methods:
 * <ul>
 *     <li>findByName(String name): This method retrieves a Category entity with the provided name.</li>
 *     <li>findByNameStartingWithIgnoreCase(String name): This method retrieves a list of Category entities where the name starts with the provided string, ignoring case.</li>
 * </ul>
 *
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 01.06.2024
 */

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Category findByName(String name);
    List<Category> findByNameStartingWithIgnoreCase(String name);

}
