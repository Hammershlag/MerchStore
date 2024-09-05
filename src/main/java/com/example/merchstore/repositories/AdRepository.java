package com.example.merchstore.repositories;

import com.example.merchstore.components.models.Ad;
import com.example.merchstore.components.models.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The AdRepository interface is a Spring Data JPA repository for the Ad entity.
 * It provides CRUD operations and a method to find all Ad entities associated with a specific Item entity.
 *
 * It has one method:
 * <ul>
 *     <li>findAllByItem(Item item): This method retrieves all Ad entities associated with the provided Item entity.</li>
 * </ul>
 *
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 26.08.2024
 */

@Repository
public interface AdRepository extends JpaRepository<Ad, Long> {

    /**
     * This method retrieves all Ad entities associated with the provided Item entity.
     *
     * @param item The Item entity to retrieve the Ad entities for.
     * @return A list of Ad entities associated with the provided Item entity.
     */
    public List<Ad> findAllByItem(Item item);


}
