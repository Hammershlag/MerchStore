package com.example.merchstore.repositories;

import com.example.merchstore.components.models.Category;
import com.example.merchstore.components.models.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
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
