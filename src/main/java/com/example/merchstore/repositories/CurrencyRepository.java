package com.example.merchstore.repositories;

import com.example.merchstore.components.models.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The CurrencyRepository interface is a Spring Data JPA repository for the Currency entity.
 * It provides CRUD operations and a method to find a Currency entity by its short name.
 *
 * It has one method:
 * <ul>
 *     <li>findByShortName(String shortName): This method retrieves a Currency entity with the provided short name.</li>
 * </ul>
 *
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 03.09.2024
 */

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Long> {

    /**
     * This method retrieves a Currency entity with the provided short name.
     *
     * @param shortName The short name of the Currency entity to retrieve.
     * @return A Currency entity with the provided short name.
     */
    Currency findByShortName(String shortName);

}
