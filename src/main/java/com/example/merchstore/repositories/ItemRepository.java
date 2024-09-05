package com.example.merchstore.repositories;

import com.example.merchstore.components.models.Category;
import com.example.merchstore.components.models.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The ItemRepository interface is a Spring Data JPA repository for the Item entity.
 * It provides CRUD operations and methods to find Item entities by name, category, or by the starting characters of the name, with or without case sensitivity.
 * It also provides methods to find Item entities by category and name, with pagination.
 *
 * It has eight methods:
 * <ul>
 *     <li>findByName(String name): This method retrieves an Item entity with the provided name.</li>
 *     <li>findByCategory(Category category): This method retrieves a list of Item entities associated with the provided Category entity.</li>
 *     <li>findByCategory(Category category, Pageable pageable): This method retrieves a page of Item entities associated with the provided Category entity, with pagination.</li>
 *     <li>findAll(Pageable pageable): This method retrieves all Item entities with pagination.</li>
 *     <li>findByNameStartingWith(String name, Pageable pageable): This method retrieves a page of Item entities where the name starts with the provided string.</li>
 *     <li>findByCategoryAndNameStartingWith(Category category, String name, Pageable pageable): This method retrieves a page of Item entities associated with the provided Category entity and where the name starts with the provided string.</li>
 *     <li>findByNameStartingWithIgnoreCase(String name, Pageable pageable): This method retrieves a page of Item entities where the name starts with the provided string, ignoring case.</li>
 *     <li>findByCategoryAndNameStartingWithIgnoreCase(Category category, String name, Pageable pageable): This method retrieves a page of Item entities associated with the provided Category entity and where the name starts with the provided string, ignoring case.</li>
 * </ul>
 *
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 02.06.2024
 */

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    Item findByName(String name);
    List<Item> findByCategory(Category category);
    Page<Item> findByCategory(Category category, Pageable pageable);
    Page<Item> findAll(Pageable pageable);
    Page<Item> findByNameStartingWith(String name, Pageable pageable);
    Page<Item> findByCategoryAndNameStartingWith(Category category, String name, Pageable pageable);
    Page<Item> findByNameStartingWithIgnoreCase(String name, Pageable pageable);
    Page<Item> findByCategoryAndNameStartingWithIgnoreCase(Category category, String name, Pageable pageable);


}
