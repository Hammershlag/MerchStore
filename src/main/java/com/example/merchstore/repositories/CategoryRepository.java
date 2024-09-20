package com.example.merchstore.repositories;

import com.example.merchstore.components.models.Category;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    /**
     * This method retrieves a Category entity with the provided name.
     *
     * @param name The name of the Category entity to retrieve.
     * @return A Category entity with the provided name.
     */
    Category findByName(String name);

    /**
     * This method retrieves a list of Category entities where the name starts with the provided string, ignoring case.
     *
     * @param name The string to search for at the beginning of the name.
     * @return A list of Category entities where the name starts with the provided string, ignoring case.
     */
    List<Category> findByNameStartingWithIgnoreCase(String name);

    @Query(value = "WITH RECURSIVE subcategories AS (" +
            "SELECT c.category_id, c.name, c.description, c.image, c.main, c.parent_category_id, c.should_display " +
            "FROM categories c WHERE c.category_id = :categoryId " +
            "UNION ALL " +
            "SELECT c2.category_id, c2.name, c2.description, c2.image, c2.main, c2.parent_category_id, c2.should_display " +
            "FROM categories c2 " +
            "INNER JOIN subcategories s ON c2.parent_category_id = s.category_id" +
            ") " +
            "SELECT * FROM subcategories", nativeQuery = true)
    List<Category> findAllSubcategories(@Param("categoryId") Long categoryId);

    @EntityGraph(attributePaths = {"childCategories"})
    List<Category> findByParentCategoryIsNullAndShouldDisplayTrue();

}
